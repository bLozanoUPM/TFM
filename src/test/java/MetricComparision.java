import data.Doc;
import data.Evaluation;
import io.CSVReader;
import io.ParallelExecutor;
import metrics.*;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class MetricComparision {

    private static final Logger LOG = LoggerFactory.getLogger(MetricComparision.class);

    private static final String path = "/Users/borjalozanoalvarez/Projects/Library/MetricPerformance/src/main/resources/20News/";

    private static final List<DocumentMetric> metrics = Arrays.asList(
            new JSD(0.5d),
            new HE(0.5d),
            new JCTL(),
            new DTL()
    );

    boolean fromFile = true;

    private static final Map<String, List<String>> evaluations = new HashMap<String,List<String>>(){
        {
            put("sml",Arrays.asList("small", "medium", "large"));
        }
    };


    String evaluation = "sml";
    private int[] ntopics = {50,100,300,500};

    List<String> models = evaluations.get(evaluation);

    Map<String,List<Doc>> corpora;

    /*
        Test all $models using $metric
     */
    @Test
    public void testAllModels(){

        for(int n: ntopics){
            LOG.info("\n\n\n" +
                        "########################### " +
                        n +
                        " ###########################");
            corpora = CSVReader.loadCorpora(path+ "topic/" +evaluation+"_"+n+"/",models);

            for(DocumentMetric metric: metrics){
                LOG.info("\n\n\n" +
                        "########################### " +
                        metric.id() +
                        " ###########################");
                Map<String,Map<String,Evaluation>> evalMap = new HashMap<>();

                // TODO parallelize
                for (String model: models) {
                    LOG.info("\n\n\n" +
                            "########################### " +
                            model +
                            " ###########################");

                    Map<String, Evaluation> evaluations = new HashMap<>();  // <testSet_id,JSD_Evaluation>

                    // TODO parallelize
                    for(String testSet: models){

                        List<Doc> corpus = corpora.get(testSet);

                        Evaluation eval = new Evaluation("Evaluating Librairy model "+model+" on testSet "+testSet);

                        ParallelExecutor pool = new ParallelExecutor();

                        corpus.forEach(d->{
                            pool.submit(()->{
                                d.setRelevant(corpus);
                            });
                        });

                        pool.awaitTermination();


                        // Add result comparing the list of relevant with the retrieved by the $model from the $corpus
                        corpus.forEach(d -> eval.addResult(d.getRelevant(),d.getRetrieved(model,corpus,metric)));

                        LOG.info(eval.summary());

                        evaluations.put(testSet,eval);
                    }

                    evalMap.put(model,evaluations);
                }

                try {
                    evaluationToFile(evalMap, metric,n);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }


    }

    @Test
    public void singleModelTest() throws IOException {

        for (String testSet: models) {

            LOG.info("\n\n\n" +
                    "###########################" +
                    testSet +
                    "###########################");

            List<Doc> corpus = corpora.get(testSet);

            Evaluation eval = new Evaluation();

            ParallelExecutor pool = new ParallelExecutor();

            corpus.forEach(d->{
                pool.submit(()->{
                    d.setRelevant(corpus);
                    d.addProjection(testSet);
                });
            });

            pool.awaitTermination();

            corpus.forEach(d -> eval.addResult(d.getRelevant(),d.getRetrieved(testSet,corpus)));

            LOG.info(eval.summary());
        }
        corporaToFile(corpora);

    }


    public void corporaToFile(Map<String,List<Doc>> corpora) throws IOException {

        /*
            To avoid having all 4 models running at the same time (which consume a lot of resources) models are run one at
            time. Change this variable to match the model running
         */
        String ptm = "Q4";

        //  Filename: {model-name}_ptm.csv
        String filename = ptm+"_ptm.csv";
        File f = new File(path+ "topic/" +filename);
        FileWriter file = new FileWriter(f);
        file.flush();                                           // Truncate file
        BufferedWriter bw = new BufferedWriter(file);

        //  csv columns
        bw.write("id;corpus_id;tables_t;topics_t;vector_d\n");

        corpora.forEach((testSet_id,v)->{
            v.forEach(doc -> {
                try {
                    bw.write(doc.getId());bw.write(";");
                    bw.write(testSet_id);bw.write(";");
                    bw.write(doc.getLabels().toString());bw.write(";");
                    bw.write(doc.getModelProjection(testSet_id).getTopics().toString());bw.write(";");
                    bw.write(doc.getModelProjection(testSet_id).getVector().toString());bw.write("\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        });
        bw.close();

    }

    /*
        Save evaluation output to a file.
        Format:
            filename:       $metric-name($metric-th).csv
            column names:   ptmModel_id,testSet_id,metric_id,N_i,TP_i,FP_i,FN_i,precision_d,recall_d,fMeasure_d,P@1,P@3,P@5,P@10,P@15,MAP_d
            separator:      ,
     */
    public void evaluationToFile(Map<String,Map<String,Evaluation>> evaluations, DocumentMetric metric, int ntopics) throws IOException {

        LOG.info("\n\n\n" +
                            "########################### " +
                            path+ "20News/evaluation/" +evaluation+"_"+ntopics +
                            " ###########################");

        if (Files.notExists(Paths.get(path+ "evaluation/" +evaluation))) {
            new File(path+ "evaluation/" +evaluation+"_"+ntopics).mkdir();
        }

        String metric_id = metric.id();

        String filename = metric_id.toUpperCase()+"("+metric.getThreshold()+").csv";
        File f = new File(path+ "evaluation/" +evaluation+"_"+ntopics+"/"+filename);
        FileWriter file = new FileWriter(f);
        file.flush();
        BufferedWriter bw = new BufferedWriter(file);

        bw.write("ptmModel_id,testSet_id,metric_id,N_i,TP_i,FP_i,FN_i,precision_d,recall_d,fMeasure_d,P@1_d,P@3_d,P@5_d,P@10_d,P@15_d,MAP_d");
        bw.newLine();

        evaluations.forEach((ptm_id,modelEvaluations)->
                modelEvaluations.forEach((testSEt_id, eval)->{
                    try {
                        bw.write(ptm_id);bw.write(",");
                        bw.write(testSEt_id);bw.write(",");
                        bw.write(metric.id());bw.write(",");
                        bw.write(eval.toString());bw.newLine();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }));

        bw.close();
    }

}
