package metrics;

import data.DocProjection;

public interface DocumentMetric {

    String id();

    Double distance(DocProjection d1, DocProjection d2);

    Double similarity(DocProjection d1, DocProjection d2);

    Double getThreshold();

}
