package data;



import org.apache.commons.math3.stat.StatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class Precision {

    private static final Logger LOG = LoggerFactory.getLogger(Precision.class);

    private List<Double> averagePrecision;
    private List<Integer> precisionAtK;
    private int k;

    Precision(){
        this(0);
    }

    Precision(int k){
        averagePrecision = new ArrayList<>();
        precisionAtK = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            precisionAtK.add(0);
        }
        this.k=k;
    }

    /*
        Add a new averagePrecision
     */
    void addAveragePrecision(List<String> relevant, List<String> retrieved){

        if(retrieved.isEmpty()) {
            averagePrecision.add(0.0);
            return;
        }

        int found                   = 0;
        List<Double> precisionList  = new ArrayList<>();
        int k_l = ((k==0) || (k>retrieved.size())) ? retrieved.size() : k;

        for (int i = 0; i < k_l; i++) {
            boolean isRelevant = relevant.contains(retrieved.get(i));
            found += (isRelevant) ? 1 : 0;
            if(i<15)
                precisionAtK.set(i,precisionAtK.get(i)+found);
            double precision = (found == 0) ? 0.0 : ((double) found / (double) (i+1));
            precisionList.add(precision);
        }
        double[] ap =  precisionList.stream().mapToDouble(Double::doubleValue).toArray();
        Double mean = StatUtils.mean(ap);
        averagePrecision.add(mean);

    }

    double meanAveragePrecision(){
        double[] ap =  averagePrecision.stream().mapToDouble(Double::doubleValue).toArray();
        return StatUtils.mean(ap);
    }

    double precisionAtK(int k){

        double relevantAtK = (double) precisionAtK.get(k-1);
        double totalAtK = (double) k*averagePrecision.size();

        return relevantAtK / totalAtK;
    }


}
