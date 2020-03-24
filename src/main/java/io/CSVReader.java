package io;

import data.Doc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class CSVReader {

    private static Logger LOG = LoggerFactory.getLogger(CSVReader.class);

    /*
        Read all files containing models in $Models and return a corpus separated in the different testSets
        The expected format of the files is:
            filename:       {mode-name}_ptm.csv
            column names:   id;    corpus_id;  labels_t;  topics_t;   vector_d
            separator:      ;
     */
    public static Map<String,List<Doc>> loadCorpora(String path, List<String> models){
        Map<String,Doc> corpus = new HashMap<>();

        for(String model: models){
            try
            {
                File file = new File(path+model+"_ptm.csv");
                FileReader fileReader = new FileReader(file);
                BufferedReader csvReader = new BufferedReader(fileReader);
                String row = null;
                csvReader.readLine();     // column names
                while ( (row = csvReader.readLine()) != null )
                {

                      /*    Remove whitespaces and divide line in:
                            id;    corpus_id;  labels_t;  topics_t;   vector_d  */
                    String[] document = row.replaceAll("\\s+","").split(";");

                    String id = document[0]; // id
                    String q = document[1];  // corpus_id

                    //labels
                    List<String> labels = Arrays.asList(document[2].replaceAll("[\\[\\]]","").split(","));

                    // topics
                    String topics =  document[3];
                    String[] topicArray = topics.substring(1,topics.lastIndexOf("]")).split("[\\[\\]],");
                    List<List<String>> topicLevels = new ArrayList<>();
                    for (String topicLevel : topicArray) {
                        List<String> level = Arrays.asList(topicLevel.replaceAll("[\\[\\]]","").split(","));
                        topicLevels.add(level);
                    }

                    //vector
                    double[] vectorArray = Arrays.stream(document[4].replaceAll("[\\[\\]]", "").split(","))
                            .mapToDouble(Double::parseDouble).toArray();
                    List<Double> vector = Arrays.stream(vectorArray).boxed().collect(Collectors.toList());

                    if(!corpus.containsKey(id)){
                        Doc doc = new Doc(id,labels,q);
                        doc.addProjection(model,vector,topicLevels);
                        corpus.put(id,doc);

                    }
                    else{
                        corpus.get(id).addProjection(model,vector,topicLevels);
                    }
                }
                csvReader.close();
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }

        Map<String,List<Doc>> corpora = new HashMap<>();

        //  Divide the corpus in the testIds
        corpus.values().forEach(doc -> {
            if(corpora.containsKey(doc.getCorpus_id())){
                corpora.get(doc.getCorpus_id()).add(doc);
            }
            else{
                List<Doc> testSet = new ArrayList<>();
                testSet.add(doc);
                corpora.put(doc.getCorpus_id(),testSet);
            }
        });

        return corpora;
    }

    public static LinkedHashMap<String,Integer> loadSize(String path){

        LinkedHashMap<String, Integer> corpus_size =  new LinkedHashMap<>();

        try {
            File file = new File(path);
            FileReader fileReader = new FileReader(file);
            BufferedReader csvReader = new BufferedReader(fileReader);
            String row = null;
            csvReader.readLine();     // column names

            while ( (row = csvReader.readLine()) != null )
            {
                String[] key_value = row.split(",");
                corpus_size.put(key_value[0],Integer.valueOf(key_value[1]));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return corpus_size;
    }

    /*
        Load list of documents from CSV
            column names:   id;     lables_s;      size_i;      ntokens_i;      text_s
            separator:      ;
     */
    public static Map<String,Doc> loadCorpus(String path){
        Map<String,Doc> corpus = new HashMap<>();
        try {
            File file = new File(path);
            FileReader fileReader = new FileReader(file);

            BufferedReader csvReader = new BufferedReader(fileReader);
            String row = null;
            csvReader.readLine();     // column names
            while ( (row = csvReader.readLine()) != null )
            {
                String[] document = row.split(";",5);

                String id = document[0];
                List<String> lables = Arrays.asList(document[1].replaceAll("[\\[\\]]","").split(","));
                int tokens = Integer.parseInt(document[3]);
                String text = document[4].replace("\"","");
                corpus.put(id,new Doc(id,lables,text,tokens));
            }
            csvReader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return corpus;
    }

    /*
        Load list of documents from CSV
            column names:   id;     lables_s;      size_i;      ntokens_i;      text_s
            separator:      ;
     */
    public static Map<String,Doc> loadSplits(String path){
        Map<String,Doc> corpus = new HashMap<>();
        try {
            File file = new File(path);
            FileReader fileReader = new FileReader(file);

            BufferedReader csvReader = new BufferedReader(fileReader);
            String row = null;
            csvReader.readLine();     // column names
            while ( (row = csvReader.readLine()) != null )
            {
                String[] document = row.split(",\"",3);

                String id = document[0];
                List<String> labels = Arrays.asList(document[1].replaceAll("[\\[\\]]","").
                        replaceAll("\"","").split(","));
                String text = document[2].replace("\"","");
                corpus.put(id,new Doc(id,labels,text,0));
            }
            csvReader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return corpus;
    }

}
