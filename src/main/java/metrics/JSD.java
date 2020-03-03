package metrics;

import data.DocProjection;

import java.util.ArrayList;
import java.util.List;

public class JSD implements VectorMetrics{

    Double threshold;

    public JSD(){
        this(0.5);
    }

    public JSD(double threshold){
        this.threshold=threshold;
    }

    public void setThreshold(Double threshold) {
        this.threshold = threshold;
    }

    public Double getThreshold(){
        return threshold;
    }

    public String name() {
        return "Jensen Shannon Divergence";
    }

    public String id(){
        return "jsd";
    }

    public Double distance(DocProjection d1, DocProjection d2) {

        List<Double> v1 = d1.getVector();
        List<Double> v2 = d2.getVector();

        assert (v1.size() == v2.size());

        List<Double> avg = new ArrayList<>();
        for (int i = 0; i < v1.size(); i++) {
            double pq = v1.get(i) + v2.get(i);
            pq /= 2;
            avg.add(pq);
        }

        return (0.5 * new KL().distance(v1,avg)) + (0.5 * new KL().distance(v2, avg));
    }

    public Double similarity(DocProjection d1, DocProjection d2) {
        return 1 - distance(d1,d2);
    }

}
