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

public class JL implements DocumentMetric {

    private static final Logger LOG = LoggerFactory.getLogger(JL.class);


    private Double threshold;

    public JL() {
        this(0d);
    }

    public JL(double threshold) {
        this.threshold = threshold;
    }

    public String name() {
        return "Jaccard Levels";
    }

    public String id(){
        return "jl";
    }


    public Double similarity(DocProjection d1, DocProjection d2) {
        
        List<List<String>> t1 = d1.getTopics();
        List<List<String>> t2 = d2.getTopics();

        double d = 0;
        for (int i = 0; i < t1.size() ; i++) {
            d += Collections.disjoint(t1.get(i), t2.get(i)) ? 0 : (new JI().jaccardIndex(t1.get(i), t2.get(i)));
        }
        
        return d / 3;
    }

    @Override
    public Double getThreshold() {
        return threshold;
    }

    public Double distance(DocProjection d1, DocProjection d2) {
        return 1 - similarity(d1, d2);
    }

}
