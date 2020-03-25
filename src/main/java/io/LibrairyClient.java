package io;

import data.Doc;
import data.DocProjection;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

public class LibrairyClient {

    private static final Logger LOG = LoggerFactory.getLogger(LibrairyClient.class);

    private static final JSONArray POS = new JSONArray("[     \"NOUN\",     \"VERB\",     \"PROPER_NOUN\",     \"ADJECTIVE\" ]");

    /*
        Update this with your information
     */
    private static final String basic_auth = "TFM:2020";
    private static final String email = "borja.lozano.alvarez@alumnos.upm.es";
    private static final String docker_pwd = "S96jPp5TS4JzsKy";
    private static final String docker_user = "blozanoalvarez";


    static public DocProjection infer(Doc document) {
        String endpoint = "http://localhost:8080/inferences";
        return infer(document, endpoint);
    }

    public static DocProjection infer(Doc document, String endpoint) {

        DocProjection projection = null;
        List<Double> vector;
        List<List<String>> topics = new ArrayList<>();

        topics.add(0,new ArrayList<>());
        topics.add(1,new ArrayList<>());
        topics.add(2,new ArrayList<>());

        JSONObject body = new JSONObject();
        body.put("text",document.getText());
        body.put("topics",true);

        JSONObject response = new JSONObject(post(body,endpoint));

        JSONArray jTopics = (JSONArray) response.get("topics");
        for(Object topic:jTopics){
            topics.get((((JSONObject) topic).getInt("id"))).add((((JSONObject) topic).getString("name")));
        }

        double[] vectorArray = Arrays.stream(response.get("vector").toString().replaceAll("[\\[\\]]","")
                .split(",")).mapToDouble(Double::parseDouble)
                .toArray();
        vector = Arrays.stream(vectorArray).boxed().collect(Collectors.toList());

        projection = new DocProjection(vector,topics);

        return projection;
    }

    /*
        Get the number of tokens in a string
     */
    static public Integer effectiveSize(String text, String lang){
        return tokens(text,lang).size();
    }

    /*
        Get the list of tokens in a string
     */
    static public List<String> tokens(String text, String lang){

        String endpoint = "http://librairy.linkeddata.es/nlp/tokens";
        JSONObject body = new JSONObject();
        body.put("filter",POS);
        body.put("form","RAW");
        body.put("lang",lang);
        body.put("multigrams",true);
        body.put("text",text);

        String response = post(body,endpoint);
        if(response==null) return Collections.emptyList();
        JSONObject tokens = new JSONObject(response);

        return new ArrayList<>(Arrays.asList(((String) tokens.get("tokens")).split(" ")));
    }

    /*
        Create a new Librairy model
     */
    static public void createModel(String name, String lang, String version, String doc_url, int topics){
        String endpoint = "http://localhost:8081/topics";
        JSONObject body = new JSONObject();
        body.put("name",name);
        body.put("description","topic model "+name);
        body.put("version",version);
        body.put("contactEmail", email);
        body.put("parameters", new JSONObject(){{
            put("seed", "1066");
            put("lowercase", "true");
            put("topics", topics);
            put("autolabels", "false");
            put("language", lang);
            put("iterations", "1000");
            put("autowords", "true");
            put("entities", "false");
            put("max-doc-ratio", "0.9");
            put("alpha", "0.1");
            put("beta", "0.01");
            put("part-of-speech", "VERB NOUN PROPER_NOUN ADJECTIVE");
            put("top-words", "10");
        }});
        body.put("docker", new JSONObject(){{
            put("email",email);
            put("password", docker_pwd);
            put("repository", "blozanoalvarez/"+name);
            put("user", docker_user);
        }});
        body.put("dataSource", new JSONObject(){{
            put("dataFields", new JSONObject(){{
                put("id", "0");
                put("text", new JSONArray(){{
                    put(1);
                }});
            }});
            put("filter", ",");
            put("format", "CSV");
            put("offset", 1);
            put("size", -1);
            put("url", doc_url);
        }});
        LOG.info(body.toString());
        JSONObject response = new JSONObject(post(body,endpoint));

        LOG.info("\n"
                + "######################################################\n"
                + "Name: " + name + "\n"
                + "Version: " + version + "\n"
                + "N_Topics: " + topics
                +"\n\n\n"

        );

    }


    /*
        Get Stats from a model
     */
    static public Map<String,Object> modelStats(int port) {
        String response = get("http://localhost:"+port+"/settings");
        JSONObject settings = new JSONObject(response);
        JSONObject stats = settings.getJSONObject("stats");
        return stats.toMap();

    }

    static public double modelLog(int port) {
        return (double) modelStats(port).get("loglikelihood");

    }
    /************************  Other Methods ************************/

    /*
        POST request from library
     */
    private static String post(JSONObject body, String endpoint){
        StringBuilder content = new StringBuilder();

        try {
            URL url = new URL(endpoint);
            HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();
            httpConnection.setDoOutput(true);
            httpConnection.setRequestMethod("POST");
            httpConnection.setRequestProperty("Content-Type", "application/json");
            httpConnection.setRequestProperty("Accept", "application/json");
            if(endpoint.endsWith("/topics")) {
                String encoded = Base64.getEncoder().encodeToString((basic_auth).getBytes(StandardCharsets.UTF_8));  //Java 8
                httpConnection.setRequestProperty("Authorization", "Basic "+encoded);
            }

            DataOutputStream wr = new DataOutputStream(httpConnection.getOutputStream());
            wr.write(body.toString().getBytes());
            int responseCode = httpConnection.getResponseCode();

            BufferedReader bufferedReader;

            // Creates a reader buffer
            if (responseCode > 199 && responseCode < 300) {
                bufferedReader = new BufferedReader(new InputStreamReader(httpConnection.getInputStream()));
            } else {
                LOG.error("ERROR {} in Librairy connection. Trace:",responseCode);
                LOG.info("{}",body);
                bufferedReader = new BufferedReader(new InputStreamReader(httpConnection.getErrorStream()));
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    content.append(line).append("\n");
                }
                bufferedReader.close();
                LOG.error(content.toString());
                return null;
            }

            // To receive the response

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                content.append(line).append("\n");
            }
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return content.toString();
    }


    /*
        GET
     */
    private static String get(String endpoint){
        StringBuilder content = new StringBuilder();
        try {

            URL url = new URL(endpoint);
            HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();
            httpConnection.setDoOutput(true);
            httpConnection.setRequestMethod("GET");
            httpConnection.setRequestProperty("Accept", "application/json");

            int responseCode = httpConnection.getResponseCode();

            BufferedReader bufferedReader;

            // Creates a reader buffer
            if (responseCode > 199 && responseCode < 300) {
                bufferedReader = new BufferedReader(new InputStreamReader(httpConnection.getInputStream()));
            } else {
                LOG.error("ERROR {} in Librairy connection. Trace:",responseCode);
                bufferedReader = new BufferedReader(new InputStreamReader(httpConnection.getErrorStream()));
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    content.append(line).append("\n");
                }
                bufferedReader.close();
                LOG.error(content.toString());
                return null;
            }

            // To receive the response

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                content.append(line).append("\n");
            }
            bufferedReader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return content.toString();
    }

    public static void main(String[] args){
        System.out.println(effectiveSize("Council Decisionof 5 June 2008on the application of the provisions of the Schengen acquis relating to the Schengen Information System in the Swiss Confederation(2008/421/EC)THE COUNCIL OF THE EUROPEAN UNION,Having regard to the Agreement between the European Union, the European Community and the Swiss Confederation concerning the Swiss Confederation's association with the implementation, application, and development of the Schengen acquis (hereinafter referred to as the Agreement), which was signed on 26 October 2004 and entered into force on 1 March 2008, and in particular Article 15(1) thereof,Whereas:Article 15(1) of the Agreement provides that the provisions of the Schengen acquis shall apply in the Swiss Confederation only pursuant to a Council Decision to that effect after verification that the necessary conditions for the application of that acquis have been met;The Council has verified that the Swiss Confederation ensures satisfactory levels of data protection by taking the following steps:A full questionnaire was forwarded to the Swiss Confederation, whose replies were recorded, and verification and evaluation visits were made to the Swiss Confederation, in accordance with the applicable Schengen evaluation procedures as set out in the Decision of the Executive Committee setting up a Standing Committee on the evaluation and implementation of Schengen (hereinafter referred to as SCH/Com-ex (98) 26 def.), in the area of Data Protection.On 5 June 2008, the Council concluded that the Swiss Confederation had fulfilled the conditions in this area. It is therefore possible to set a date from which the Schengen acquis relating to the Schengen Information System (hereinafter referred to as the SIS) may apply to it.The entry into force of this Decision should allow for real SIS data to be transferred to the Swiss Confederation. The concrete use of that data should allow the Council, through the applicable Schengen evaluation procedures as set out in SCH/Com-ex (98) 26 def., to verify the correct application of the provisions of the Schengen acquis relating to the SIS in the Swiss Confederation. Once those evaluations have been carried out, the Council should decide on the lifting of checks at internal borders with the Swiss Confederation.The Agreement between the Swiss Confederation, the Republic of Iceland and the Kingdom of Norway concerning the implementation, application and development of the Schengen acquis and concerning the criteria and mechanisms for establishing the State responsible for examining a request for asylum lodged in Switzerland, Iceland or Norway stipulates that it shall be put into effect in respect of the implementation, application and development of the Schengen acquis on the same date as the Agreement is put into effect.A separate Council Decision should be taken setting a date for the lifting of checks on persons at internal borders. Until the date of the lifting of checks set out in that Decision, certain restrictions on the use of the SIS should be imposed,HAS DECIDED AS FOLLOWS:Article 1The provisions of the Schengen acquis relating to the SIS, as referred to in Annex I, shall apply to the Swiss Confederation in its relations with the Kingdom of Belgium, the Czech Republic, the Kingdom of Denmark, the Federal Republic of Germany, the Republic of Estonia, the Hellenic Republic, the Kingdom of Spain, the French Republic, the Italian Republic, the Republic of Latvia, the Republic of Lithuania, the Grand Duchy of Luxembourg, the Republic of Hungary, the Republic of Malta, the Kingdom of the Netherlands, the Republic of Austria, the Republic of Poland, the Portuguese Republic, the Republic of Slovenia, the Slovak Republic, the Republic of Finland, and the Kingdom of Sweden from 14 August 2008.The provisions of the Schengen acquis relating to the SIS, as referred to in Annex II, shall apply to the Swiss Confederation in its relations with the Kingdom of Belgium, the Czech Republic, the Kingdom of Denmark, the Federal Republic of Germany, the Republic of Estonia, the Hellenic Republic, the Kingdom of Spain, the French Republic, the Italian Republic, the Republic of Latvia, the Republic of Lithuania, the Grand Duchy of Luxembourg, the Republic of Hungary, the Republic of Malta, the Kingdom of the Netherlands, the Republic of Austria, the Republic of Poland, the Portuguese Republic, the Republic of Slovenia, the Slovak Republic, the Republic of Finland, and the Kingdom of Sweden from the date laid down in those provisions.From 9 June 2008 real SIS data may be transferred to the Swiss Confederation.From 14 August 2008, the Swiss Confederation will be able to enter data into the SIS and use SIS data, subject to paragraph 4.Until the date of the lifting of checks at internal borders with the Swiss Confederation, the Swiss Confederation:shall not be obliged to refuse entry to its territory to or to expel nationals of third States for whom an SIS alert has been issued by a Member State for the purpose of refusing entry;shall refrain from entering the data covered by Article 96 of the Convention of 19 June 1990 implementing the Schengen Agreement of 14 June 1985 between the Governments of the States of Benelux Economic Union, the Federal Republic of Germany and the French Republic on the gradual abolition of checks at their common borders (hereinafter referred to as the Schengen Convention).Article 2This Decision shall enter into force on the day of its publication in the Official Journal of the European Union.Done at Luxembourg, 5 June 2008.For the CouncilThe PresidentD. Mate","EN"));
    }
}
