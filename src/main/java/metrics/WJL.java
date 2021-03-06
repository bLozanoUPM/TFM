package metrics;

import data.DocProjection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class WJL implements DocumentMetric {

    private static final Logger LOG = LoggerFactory.getLogger(WJL.class);

    private List<Double> weights = new ArrayList<>();
    private int max;
    private Double threshold;

    public WJL() {
        this(new double[]{3d, 2d, 1d}, 0d);
    }

    public WJL(double threshold) {
        this(new double[]{3d, 2d, 1d}, threshold);
    }

    public WJL(double[] weights, double threshold) {
        this.threshold = threshold;
        for (int i = 0; i < weights.length; i++) {
            this.weights.add(weights[i]);
            max += weights[i] * weights[i];
        }
    }

    public String name() {
        return "Weighted Jaccard Levels";
    }

    public String id(){
        return "wjl";
    }

    // For now, Similarity is measured just by checking if topic levels are disjoint
    public Double similarity(DocProjection d1, DocProjection d2) {

        List<List<String>> t1 = d1.getTopics();
        List<List<String>> t2 = d2.getTopics();
        double d = 0;
        for (int i = 0; i < t1.size(); i++) {
            for (int j = 0; j < t2.size(); j++) {
                List<String> lvlA = t1.get(i);
                List<String> lvlB = t2.get(j);
                d += Collections.disjoint(lvlA, lvlB) ? 0 : (weights.get(i) * weights.get(j))*(new JI().jaccardIndex(lvlA,lvlB));
            }
        }

        return d / max;
    }

    @Override
    public Double getThreshold() {
        return threshold;
    }


    public Double distance(DocProjection d1, DocProjection d2) {
        return 1 - similarity(d1, d2);
    }

}
