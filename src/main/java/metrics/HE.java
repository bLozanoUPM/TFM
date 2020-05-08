package metrics;

import data.DocProjection;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class HE implements VectorMetrics {


    Double threshold;

    public HE(){
        this(0.5);
    }

    public HE(double threshold){
        this.threshold=threshold;
    }

    public void setThreshold(Double threshold) {
        this.threshold = threshold;
    }

    public String name(){
        return "Hellinger";
    }

    @Override
    public String id() {
        return "he";
    }

    @Override
    public Double distance(DocProjection d1, DocProjection d2) {
        List<Double> v1 = d1.getVector();
        List<Double> v2 = d2.getVector();

        assert (v1.size() == v2.size());

        double sum = 0;
        for(int i=0; i<v1.size(); i++){

            double sqrtv1 = Math.sqrt(v1.get(i));
            double sqrtv2 = Math.sqrt(v2.get(i));

            double pow2 = Math.pow(sqrtv1 - sqrtv2, 2.0);
            sum += pow2;
        }

        double sqrtSum = Math.sqrt(sum);
        double multiplier = 1.0 / Math.sqrt(2.0);
        return multiplier*sqrtSum;
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
