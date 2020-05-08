package metrics;

import data.DocProjection;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

public class MD implements DocumentMetric {

    private double threshold;

    public MD(){
        this(0.5);
    }

    public MD(double threshold){
        this.threshold=threshold;
    }


    public String name() {
        return "Manhattan Distance";
    }

    @Override
    public String id(){
        return "md";
    }

    @Override
    public Double distance(DocProjection d1, DocProjection d2) {
        List<Double> v1 = d1.getVector();
        List<Double> v2 = d2.getVector();
        return IntStream.range(0, v1.size() - 1)
                    .mapToDouble(i -> Math.abs(v1.get(i)-v2.get(i)))
                    .sum();
    }

    @Override
    public Double similarity(DocProjection d1, DocProjection d2) {
        return 1-distance(d1,d2);
    }

    @Override
    public Double getThreshold() {
        return threshold;
    }

}
