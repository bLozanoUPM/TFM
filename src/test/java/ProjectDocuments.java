import data.Doc;
import io.CSVReader;
import io.LibrairyClient;
import io.ParallelExecutor;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class ProjectDocuments {

    private static final Logger LOG = LoggerFactory.getLogger(ProjectDocuments.class);

    private static  String resources = "./src/main/resources/acquis/";

    // Change this with the eval name
    private static final String model = "sml";

    // Change this with the training subsets
    private static final int split = 9;

    // Change this with the number of topics
    private static final int ntopics = 500;

    // Change this with the language
    private static final String lang = "es";

    @Before
    public void before() {
        resources = resources+
                lang+"/"+
                model+
                split;
        LOG.info(resources);

        for (int i = 0; i < split ; i++) {
            LOG.info("{}{}_{} log:{}",model,split,i+1,LibrairyClient.modelLog(8081+i));
        }
    }

    @Test
    public void projectDocuments() throws IOException {

        LOG.info("\n\n\n" +
                "########################### Starting document projections to the " +
                model+split+" with "+ntopics+" topics"+
                " ###########################");

        Map<String,Doc> corpus  = new HashMap<>();

        // Load the documents and add test_id
        for (int i = 1; i <= split ; i++) {
            Map<String,Doc> test = CSVReader.loadSplits(resources
                    +"/test/"+
                    lang+"_"+model+split+"_"+i+".csv");
            for (Doc d : test.values()) {
                d.setCorpus_id(""+i);
            }
            corpus.putAll(test);
        }

        double size = corpus.size();

        // Project documents in the model
        ParallelExecutor pool = new ParallelExecutor();

        int n = 0;
        for (Doc d : corpus.values()) {
            for (int i = 0; i < split; i++) {
                int finalI = i;
                pool.submit(() -> {
                    d.addProjection(model + (finalI + 1),
                            "http://localhost:808" + (finalI + 1) + "/inferences"
                    );
                });
            }
            if ((++n % 1000) == 0) LOG.info("{}:{}",n, n/size);
        }

        LOG.info("Awaiting termination");
        pool.awaitTermination();

        corporaToFile(corpus.values());
    }

    /*
        Writes the projection of the documents
        Format:
            filename:       {model-name}/{ntopics}.csv
            column names:   ptm_id;    test_id;  labels_t;  topics_t;   vector_d
            separator:      ;
     */
    public void corporaToFile(Collection<Doc> docs) throws IOException {

        /*
            To avoid having all 4 models running at the same time (which consume a lot of resources) models are run one at
            time. Change this variable to match the model running
         */

        String dir = resources+ "/topic/";

        if (Files.notExists(Paths.get(dir))) {
            new File(dir).mkdir();
        }

        //  Filename: {model-name}_ptm.csv
        String filename = ntopics+".csv";
        File f = new File(dir+"/"+filename);
        FileWriter file = new FileWriter(f);
        file.flush();                                           // Truncate file
        BufferedWriter bw = new BufferedWriter(file);

        //  csv columns
        bw.write("id;ptm_id;test_id;labels_t;topics_t;vector_d\n");

        docs.forEach(d->{
            d.getModels().forEach(ptm->{
                try{
                    bw.write(d.getId());bw.write(";");
                    bw.write(ptm);bw.write(";");
                    bw.write(d.getCorpus_id());bw.write(";");
                    bw.write(d.getLabels().toString().replace(" ",""));bw.write(";");
                    bw.write(d.getModelProjection(ptm).getTopics().toString().replace(" ",""));bw.write(";");
                    bw.write(d.getModelProjection(ptm).getVector().toString().replace(" ",""));bw.write("\n");
                } catch (IOException e){
                    e.printStackTrace();
                }
            });
        });
        bw.close();
    }
}
