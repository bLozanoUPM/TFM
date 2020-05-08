package hash;

import data.TopicPoint;
import org.apache.commons.math3.ml.clustering.Cluster;
import org.apache.commons.math3.ml.clustering.DBSCANClusterer;
import org.apache.commons.math3.ml.distance.DistanceMeasure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import statistics.Stats;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TopicLevels {

    private static final Logger LOG = LoggerFactory.getLogger(TopicLevels.class);

    private static final String SEPARATOR = "_";
    private final int levels;

    public TopicLevels(int levels) {
        this.levels = levels;
    }

    public String id() {
        return "density";
    }

    public Map<Integer, List<String>> hash(List<Double> topicDistribution) {
        Map<Integer,List<String>> hashCode = new HashMap<>();
        int minPts = 0;
        Double eps = new Stats(topicDistribution).getVariance();

        DBSCANClusterer<TopicPoint> clusterer = new DBSCANClusterer<>(eps, minPts, new MonoDimensionalDistanceMeasure());

        List<TopicPoint> points = IntStream.range(0, topicDistribution.size()).mapToObj(i -> new TopicPoint("" + i, topicDistribution.get(i))).collect(Collectors.toList());
        List<Cluster<TopicPoint>> clusterList = clusterer.cluster(points);

        List<TopicPoint> groups = new ArrayList<>();
        int totalPoints = 0;
        for (Cluster<TopicPoint> cluster : clusterList) {
            Double score = (cluster.getPoints().stream().map(p -> p.getScore()).reduce((x, y) -> x + y).get()) / (cluster.getPoints().size());
            String label = cluster.getPoints().stream().map(p -> "t" + p.getId()).sorted((x, y) -> -x.compareTo(y)).collect(Collectors.joining(SEPARATOR));
            totalPoints += cluster.getPoints().size();
            groups.add(new TopicPoint(label, score));
        }
        if (totalPoints < topicDistribution.size()) {
            List<TopicPoint> clusterPoints = clusterList.stream().flatMap(l -> l.getPoints().stream()).collect(Collectors.toList());
            List<TopicPoint> isolatedTopics = points.stream().filter(p -> !clusterPoints.contains(p)).collect(Collectors.toList());
            Double score = (isolatedTopics.stream().map(p -> p.getScore()).reduce((x, y) -> x + y).get()) / (isolatedTopics.size());
            String label = isolatedTopics.stream().map(p -> "t" + p.getId()).sorted((x, y) -> -x.compareTo(y)).collect(Collectors.joining(SEPARATOR));
            groups.add(new TopicPoint(label, score));
        }
        Collections.sort(groups, (a, b) -> -a.getScore().compareTo(b.getScore()));

        for(int i=0;i<levels;i++){
            List<String> topics = i >= groups.size()? Arrays.asList(groups.get(groups.size()-1).getId().split(SEPARATOR)) : Arrays.asList(groups.get(i).getId().split(SEPARATOR));
            hashCode.put(i,topics);
        }

        return hashCode;


    }

    public double distance(double[] p1, double[] p2) {
        return Math.abs(p1[0] - p2[0]);
    }

    private static class MonoDimensionalDistanceMeasure implements DistanceMeasure {

        @Override
        public double compute(double[] p1, double[] p2) {
            return Math.abs(p1[0] - p2[0]);
        }
    }

}

