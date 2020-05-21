import cc.mallet.types.Dirichlet;
import data.DocProjection;
import data.TimeEvaluation;
import hash.TopicLevels;
import io.ParallelExecutor;
import metrics.*;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;

public class MetricComparison {

    private static final Logger LOG = LoggerFactory.getLogger(MetricComparison.class);

    private static final int N_MIN = 100000;
    private static final int N_MAX = 100000;
    private static final int[] K = {10,25,50,100,150,300};
    private static final int I = 1;

    @Test
    public void timeComparison(){
        List<TimeEvaluation> evaluations = new ArrayList<>();

        List<DocumentMetric> metrics = Arrays.asList(
                new JSD(0.5d),
                new HE(0.5d),
                new WJL(),
                new JL()
        );

        TopicLevels tl = new TopicLevels(3);

        for (int n = N_MIN; n <= N_MAX ; n*=10) {

            for (int k = 5; k < 300; k+=5){
                Dirichlet distribution = new Dirichlet(k);
                for (int i = 0; i < I; i++) {
                    LOG.info("\n\n\n{} documents -- {} K -- iteration {}",n,k,i);

                    TimeEvaluation eval = new TimeEvaluation(n,k);
                    List<DocProjection> docs = new ArrayList<>();


                    /*
                        Inverse indexing of topic levels
                        For each topic k a list of integers is defined such as
                            index_k -> list of documents who has k in any level
                     */
                    Map<String,Set<Integer>> index = new HashMap<>();
                    for (int j = 0; j < k; j++) {
                        index.put("t"+j,new HashSet<>());
                    }

                    /*
                        Generate N samples of size k
                    */
                    for (int j = 0; j < n; j++) {
                        double[] sample = distribution.nextDistribution();
                        List<Double> vector = (DoubleStream.of(sample).boxed().collect(Collectors.toList()));
                        List<List<String>> topics = new ArrayList<>(tl.hash(vector).values());

                        int finalI = j;
                        topics.forEach(level ->
                                level.forEach(id ->
                                        index.get(id).add(finalI)));

                        docs.add(new DocProjection(vector,topics));
                    }

                    for(DocumentMetric metric: metrics) {
                        Instant start = Instant.now();
                        for (DocProjection d1 : docs) {
                            for (DocProjection d2 : docs) {
                                metric.similarity(d1, d2);
                            }
                        }
                        Instant end = Instant.now();
                        Duration timeElapsed = Duration.between(start, end);

                        eval.addEval(metric.id().equals("wjl") ? "wjl_pw":metric.id() ,timeElapsed);
                        LOG.info("{}\t:\t{}",timeElapsed,metric.id().equals("wjl") ? "wjl_pw":metric.id());
                    }

                    DocumentMetric metric = new WJL();

                    Instant start = Instant.now();
                    for (DocProjection d1 : docs) {
                        Set<DocProjection> relevant = new HashSet<>();
                        for (List<String> level : d1.getTopics()) {
                            for (String id : level) {
                                for (Integer d2 : index.get(id)) {
                                    relevant.add(docs.get(d2));
                                }
                            }
                        }

                        for (DocProjection d2 : relevant) {
                            metric.similarity(d1, d2);
                        }

                    }
                    Instant end = Instant.now();

                    Duration timeElapsed = Duration.between(start, end);
                    eval.addEval("wjl_ii",timeElapsed);
                    LOG.info("{}\t:\twjl_ii",timeElapsed);

                    evaluations.add(eval);
                }
            }
        }

        try {
            evaluationToFile(evaluations);
        }catch (Exception ignored){}
    }

    public void evaluationToFile(List<TimeEvaluation> evaluations) throws IOException {


        String filename = "time_comp_"+N_MAX+"_"+I+".csv";
        File f = new File("./R/"+filename);
        FileWriter file = new FileWriter(f);
        file.flush();
        BufferedWriter bw = new BufferedWriter(file);

        bw.write("docs_i,topics_i,metric_id,time_ns,time_ms,time_m,time_h");
        bw.newLine();

        evaluations.forEach(eval->{
            String topics = String.valueOf(eval.getTopics());
            String docs = String.valueOf(eval.getDocs());
            eval.getEvaluations().forEach((metric, time) -> {
                try {
                    bw.write(docs);bw.write(",");
                    bw.write(topics);bw.write(",");
                    bw.write(metric);bw.write(",");
                    bw.write(String.valueOf(time.toNanos()));bw.write(",");
                    bw.write(String.valueOf(time.toMillis()));bw.write(",");
                    bw.write(String.valueOf(time.toMinutes()));bw.write(",");
                    bw.write(String.valueOf(time.toHours()));bw.newLine();
                } catch (IOException e) {
                e.printStackTrace();
            }
            });
        });

        bw.close();
    }

    public static void main(String[] args) {
        Dirichlet distribution = new Dirichlet(4);

        for (int i = 0; i < 10; i++) {
            double[] sample = distribution.nextDistribution();
            List<Double> vector = (DoubleStream.of(sample).boxed().collect(Collectors.toList()));
            System.out.println(vector);
        }
    }
}
