package data;

import java.util.List;

public class DocProjection {

    private List<Double> vector;

    private List<List<String>> topics;

    public DocProjection(List<Double> vector, List<List<String>> topics){
        this.vector=vector;
        this.topics=topics;
    }

    public List<Double> getVector() {
        return vector;
    }

    public  List<List<String>> getTopics() {
        return topics;
    }
}
