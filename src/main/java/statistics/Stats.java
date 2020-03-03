package statistics;

import org.apache.commons.math3.stat.StatUtils;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Stats {


    private static final Logger LOG = LoggerFactory.getLogger(Stats.class);

    private int size;

    private Double popVariance;

    private Double min;

    private Double max;

    private Double dev;

    private Double mode;

    private Double mean;

    private Double median;

    private Double variance;

    public Stats() {
    }

    public Stats(List<Double> values) {
        this(values.stream().mapToDouble(Double::doubleValue).toArray());

    }

    public Stats(double[] stats){
        StandardDeviation stdDev = new StandardDeviation();
        min = StatUtils.min(stats);
        max = StatUtils.max(stats);
        dev = stdDev.evaluate(stats);
        mode = StatUtils.mode(stats)[0];
        mean = StatUtils.mean(stats);
        median = StatUtils.geometricMean(stats);
        variance = StatUtils.variance(stats);
        popVariance = StatUtils.populationVariance(stats);
        size = stats.length;
    }




    public Double getMin() {
        return min;
    }

    public Double getMax() {
        return max;
    }

    public Double getDev() {
        return dev;
    }

    public Double getMode() {
        return mode;
    }

    public Double getMean() {
        return mean;
    }

    public Double getMedian() {
        return median;
    }

    public Double getVariance() {
        return variance;
    }

    public Double getPopVariance() {
        return popVariance;
    }

    public int getSize() {
        return size;
    }


    public static List<Double> intToDouble(Collection<Integer> values){
        List<Double> doubleValues = new ArrayList<>();
        values.forEach(v->doubleValues.add(v.doubleValue()));
        return doubleValues;
    }


    @Override
    public String toString() {
        return toString("Stats:");
    }

    public String toString(String header){
        return header+ "\n" +
                "total=\t\t" + size + "\n" +
//                "popVariance=\t" + popVariance + "\n" +
                "min=\t\t" + min + "\n" +
                "max=\t\t" + max + "\n" +
                "dev=\t\t" + dev + "\n" +
                "mode=\t\t" + mode + "\n" +
                "mean=\t\t" + mean + "\n" +
                "median=\t\t" + median + "\n" +
                "variance=\t" + variance + "\n\n";
    }
}
