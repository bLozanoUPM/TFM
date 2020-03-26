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
}
