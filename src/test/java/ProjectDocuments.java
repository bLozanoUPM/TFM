import data.Doc;
import io.ParallelExecutor;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class ProjectDocuments {

    private static final Logger LOG = LoggerFactory.getLogger(ProjectDocuments.class);

    private static final String resources = "/Users/borjalozanoalvarez/Projects/Library/MetricPerformance/src/main/resources/20News/";

    // Change this with the training subsets
    private static final String[] testCorpora = {"small", "medium", "large"};

    // Change this with the eval name
    private static final String model = "sml";

    // Change this with the number of topics
    private static final int ntopics = 50;

    // PTM trained with one the correspondent train set
    /*
        To avoid having all 4 models running at the same time (which consume a lot of resources) models are run one at
        time. Change this variable to match the model running
    */
    private static final String ptm = testCorpora[2];



    @Test
    public void projectDocuments() throws IOException {

        LOG.info("\n\n\n" +
                        "########################### " +
                        ptm +
                        " ###########################");

        Map<String, List<Doc>> corpora = new HashMap<>();

        for(String testCorpus: testCorpora){
            // Load documents from file
            List<Doc> corpus = new ArrayList<>();

            BufferedReader csvReader = new BufferedReader(new FileReader(resources+model+"Eval/"+testCorpus+"_test.csv"));

            String row;
            csvReader.readLine();
            while ((row = csvReader.readLine()) != null) {
                String[] document = row.replace("\"","").split(",",3);
                List<String> roots = Arrays.asList(document[1].split(" "));
//                corpus.add(new Doc(document[0],document[2],roots));
            }
            csvReader.close();


            // Project documents in the model
            ParallelExecutor pool = new ParallelExecutor();
            corpus.forEach(d->{
                pool.submit(()->{
                    d.addProjection(ptm);
                });
            });

            pool.awaitTermination();
            LOG.info("Test set {} completed", testCorpus);

            corpora.put(testCorpus, corpus);
        }
        corporaToFile(corpora);
    }

    public void corporaToFile(Map<String, List<Doc>> corpora) throws IOException {

        /*
            To avoid having all 4 models running at the same time (which consume a lot of resources) models are run one at
            time. Change this variable to match the model running
         */

        String dir = resources+ "topic/" +model+"_"+ntopics;

        if (Files.notExists(Paths.get(dir))) {
            new File(resources+ "topic/" +model+"_"+ntopics).mkdir();
        }

        //  Filename: {model-name}_ptm.csv
        String filename = ptm+"_ptm.csv";
        File f = new File(dir+"/"+filename);
        FileWriter file = new FileWriter(f);
        file.flush();                                           // Truncate file
        BufferedWriter bw = new BufferedWriter(file);

        //  csv columns
        bw.write("id;corpus_id;tables_t;topics_t;vector_d\n");

        corpora.forEach((testSet_id,v)-> v.forEach(doc -> {
            try {
                bw.write(doc.getId());bw.write(";");
                bw.write(testSet_id);bw.write(";");
                bw.write(doc.getLabels().toString());bw.write(";");
                bw.write(doc.getModelProjection(ptm).getTopics().toString());bw.write(";");
                bw.write(doc.getModelProjection(ptm).getVector().toString());bw.write("\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }));
        bw.close();

    }
}
