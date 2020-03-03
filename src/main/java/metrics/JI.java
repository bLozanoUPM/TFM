package metrics;

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

public class JI {

    public double jaccardIndex(List<String> l1, List<String> l2){
        double intersectionSize = l1.stream()
                .distinct()
                .filter(l2::contains)
                .count();

        double unionSize = Stream.of(l1,l2)
                .flatMap(Collection::stream)
                .distinct()
                .count();

        return (intersectionSize/unionSize);
    }

    public String id() {
        return "Jaccard Similarity Coefficient";
    }
}
