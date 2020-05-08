import data.Doc;
import data.RetrievalEvaluation;
import io.CSVReader;
import io.ParallelExecutor;
import metrics.*;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class Evaluation {

    private static final Logger LOG = LoggerFactory.getLogger(Evaluation.class);

    private static final String path = "./src/main/resources/acquis/";

    private static String resources;

    private static final List<DocumentMetric> metrics = Arrays.asList(
            new JSD(0.5d),
            new HE(0.5d),
            new WJL(),
            new JL()
    );

    private static final String[] language = {/*"en",*/"es"};

    private static final String[] splits = {"ck"/*,"sml"*/};

    private static final int[] groups = {3/*,6,9*/};

    private int[] ntopics = {50,100,300,500};

    private Map<String,Doc> corpora;


    /*
        Test all $models using $metric
     */
    @Test
    public void testAllModels() throws IOException {



        for(String lang: language){
            resources = path+lang+"/";

            // For each spliting criteria, i.e ck_ or sml_
            for(String s: splits){

                // For each different sets of groups in the splitting criteria; example ck3, sml6
                for(int k:groups){
                    String model = s+k;
                    Map<String, RetrievalEvaluation> evaluations = new ConcurrentHashMap<>();
                    LOG.info("\n\n\n" +
                            "###################################################### " +
                            model +
                            " ###################################################### ");

                    corpora = CSVReader.loadProjections(resources+model+"/topic");






//                    corpora.values().forEach(doc -> {
//                        if(doc.getModels().size()/(double)k!=4) {
//                            Set<String> aux = new HashSet<>();
//                            for (int ntopic : ntopics) for (int i = 1; i <=k ; i++) aux.add(s+i+"_"+ntopic);
//                            aux.removeAll(doc.getModels());
//
//                            LOG.info("{}:{}", doc.getId(), aux);
//                        }
//                    });
                    LOG.info(String.valueOf(corpora.size()));

                    // TestSet
                    for (int i = 1; i<=k; i++) {
                        int finalI = i;
                        List<Doc> testSet = corpora.values().stream()
                                .filter(d->Integer.parseInt(d.getCorpus_id())==finalI)
                                .collect(Collectors.toList());

                        ParallelExecutor pool_1 = new ParallelExecutor();
                        testSet.forEach(d -> pool_1.submit(() -> {d.setRelevant(testSet);}));
                        pool_1.awaitTermination();

                        ParallelExecutor pool_2 = new ParallelExecutor();
                        LOG.info("{}",testSet.size());

                        // Models
                        for (int j = 1; j<=k; j++) {
                            int finalJ = j;
                            // Topics
                            for(int topics: ntopics){
                                for (DocumentMetric metric : metrics) {
                                    String evalName = model+"-"+ finalJ+"_"+finalI+"_"+topics+"_"+metric.id();
                                    pool_2.submit(()->{
                                        RetrievalEvaluation eval = new RetrievalEvaluation(evalName);
                                        testSet.forEach(d ->
                                                eval.addResult(d.getRelevant(), d.getRetrieved(s+ finalJ+"_"+topics, testSet, metric))
                                        );
                                        LOG.info(eval.summary());
                                        evaluations.put(eval.getDescription(),eval);
                                    });
                                }
                            }
                        }
                        pool_2.awaitTermination();
                    }
                    evaluationToFile(model,evaluations);
                }
            }
        }

    }

    /*
        Save evaluation output to a file.
        Format:
            filename:       $model_evaluation.csv
            column names:   ptmModel_id,testSet_id,metric_id,N_i,TP_i,FP_i,FN_i,precision_d,recall_d,fMeasure_d,P@1,P@3,P@5,P@10,P@15,MAP_d
            separator:      ,
     */
    public void evaluationToFile(String model, Map<String, RetrievalEvaluation> evaluations) throws IOException {


        String filename = model+"_evaluation.csv";
        File f = new File(resources+"/"+model+"/evaluation/"+filename);
        FileWriter file = new FileWriter(f);
        file.flush();
        BufferedWriter bw = new BufferedWriter(file);

        bw.write("ptm_id,test_id,topics_i,metric_id,N_i,TP_i,FP_i,FN_i,precision_d,recall_d,fMeasure_d,P@1_d,P@3_d,P@5_d,P@10_d,P@15_d,MAP_d");
        bw.newLine();

        evaluations.forEach((data,eval)->{
                String[] info = data.split("_");
                String ptm_id = info[0];
                String test_id = info[1];
                String topics = info[2];
                String metric = info[3];
                    try {
                        bw.write(ptm_id);bw.write(",");
                        bw.write(test_id);bw.write(",");
                        bw.write(topics);bw.write(",");
                        bw.write(metric);bw.write(",");
                        bw.write(eval.toString());bw.newLine();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });

        bw.close();
    }

}
