package io;

import data.Doc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class CSVReader {

    private static Logger LOG = LoggerFactory.getLogger(CSVReader.class);

    /*
        Update Corpus
            filename:       {mode-name}_ptm.csv
            column names:   id;   ptm_id;   test_id;   labels_t;   topics_t;   vector_d
            separator:      ;
     */
    public static  Map<String,Doc> loadProjections(String path) throws IOException {
        Map<String,Doc> corpora = new HashMap<>();
        LOG.info(path);
        Files.list(Paths.get(path))
                .filter(f->f.getFileName().toString().endsWith("csv"))
                .forEach(f->{
                    String nTopics = f.getFileName().toString().split("\\.")[0];
                    try {
                        FileReader fr = new FileReader(f.toFile());
                        BufferedReader csvReader = new BufferedReader(fr);
                        String row = null;
                        csvReader.readLine();     // column names
                        while ( (row = csvReader.readLine()) != null )
                        {
                            String[] document = row.replaceAll("\\s+","").split(";");

                            String id = document[0];
                            String ptm_id = document[1];
                            String test_id = document[2];


                            //labels
                            List<String> labels = Arrays.asList(document[3].replaceAll("[\\[\\]]","").split(","));

                            // topics
                            String topics =  document[4];
                            String[] topicArray = topics.substring(1,topics.lastIndexOf("]")).split("[\\[\\]],");
                            List<List<String>> topicLevels = new ArrayList<>();
                            for (String topicLevel : topicArray) {
                                List<String> level = Arrays.asList(topicLevel.replaceAll("[\\[\\]]","").split(","));
                                topicLevels.add(level);
                            }

                            //vector
                            double[] vectorArray = Arrays.stream(document[5].replaceAll("[\\[\\]]", "").split(","))
                                    .mapToDouble(Double::parseDouble).toArray();
                            List<Double> vector = Arrays.stream(vectorArray).boxed().collect(Collectors.toList());

                            if(!corpora.containsKey(id)){
                                corpora.put(id,new Doc(id,labels,test_id));
                            }

                            corpora.get(id).addProjection(ptm_id+"_"+nTopics,vector,topicLevels);

                        }

                    } catch (IOException e) {
                        e.printStackTrace();
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
            column names:   id;corpus_id;labels_t;text_s
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
                String[] document = row.split(";",4);

                String text = document[3].replace("\"","");
                if(text.isEmpty())continue;
                String id = document[0];
                String corpus_id = document[1];
                List<String> lables = Arrays.asList(document[2].replaceAll("[\\[\\]]","").split(","));

                corpus.put(id,new Doc(id,corpus_id,lables,text));
            }
            csvReader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return corpus;
    }

    /*
        Load list of documents from CSV
            column names:   id;     lables_s;   text_s
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

    public static List<String> ids(String path){
        List<String> ids = new ArrayList<>();
        try {
            File file = new File(path);
            FileReader fileReader = new FileReader(file);

            BufferedReader csvReader = new BufferedReader(fileReader);
            String row = null;
            csvReader.readLine();     // column names
            while ( (row = csvReader.readLine()) != null )
            {
                String[] document = row.split(",");

               ids.add(document[0]);
            }
            csvReader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return ids;
    }

}
