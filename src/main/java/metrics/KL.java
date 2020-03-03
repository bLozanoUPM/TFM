package metrics;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


public class KL{

    private static final Logger LOG = LoggerFactory.getLogger(KL.class);

    public String id() {
        return "Kullback-Leibler";
    }

    public Double distance(List<Double> v1, List<Double> v2) {

        assert (v1.size() == v2.size());

        double klDiv = 0.0;
        for (int i = 0; i < v1.size(); ++i) {
            if (v1.get(i) == 0) {
                continue;
            }
            if (v2.get(i) == 0) {
                return Double.POSITIVE_INFINITY;
            }
            klDiv += v1.get(i) * (Math.log(v1.get(i)/ v2.get(i))/Math.log(2)); // log2, bits
        }
        return klDiv;

    }

    public Double similarity(List<Double> v1, List<Double> v2) {
        return 1-distance(v1,v2);
    }

    public Double getThreshold() {
        return null;
    }

//    public static void main(String[] args) {
//        double v1[] = {0.3,0.3,0.4};
//        double v2[] = {0.1,0.1,0.8};
//        LOG.info(String.valueOf(new KL().distance(v1,v2)));
//        LOG.info(String.valueOf(new KL().similarity(v1,v2)));
//
//    }
}
