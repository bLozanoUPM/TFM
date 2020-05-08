package data;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class TimeEvaluation {

    private int docs;

    private int topics;

    private Map<String, Duration> evaluations;

    public TimeEvaluation(int docs, int topics){
        this.docs = docs;
        this.topics = topics;
        evaluations = new HashMap<>();
    }

    public void addEval(String metric, Duration time){
        evaluations.put(metric,time);
    }

    public int getDocs() {
        return docs;
    }

    public int getTopics() {
        return topics;
    }

    public Map<String, Duration> getEvaluations() {
        return evaluations;
    }
}
