package data;

import io.LibrairyClient;
import metrics.DocumentMetric;
import metrics.JSD;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class Doc {

    private static final Logger LOG = LoggerFactory.getLogger(Doc.class);

    private String id;

    private String corpus_id;

    private String text;

    private List<String> labels;

    private List<String> relevant;

    private Map<String, DocProjection> modelProjection = new HashMap<>();

    private int n_tokens = 0;


    /************************  Constructors ************************/

    /*
        Constructor
        Create a document from its source and labels. Usually used before projection in a PTM
     */
    public Doc(String id, List<String> labels, String text, int n_tokens){
        this.id = id;
        this.text = text;
        this.labels = labels;
        this.n_tokens = n_tokens;
        relevant = new ArrayList<>();
    }


    /*
        Constructor
        Create a document without source
     */
    public Doc(String id, List<String> labels, String corpus_id){
        this.id = id;
        this.corpus_id=corpus_id;
        this.labels = labels;
        relevant = new ArrayList<>();
    }


    /*
        Constructor
        Create a document with source
     */
    public Doc(String id, String corpus_id, String text){
        this.id = id;
        this.corpus_id = corpus_id;
        this.text = text.replace("\n","");
    }

    /************************  Generic Getters ************************/

    public String getId(){
        return id;
    }

    public List<String> getLabels(){
        return labels;
    }

    public String getText() {
        return text;
    }

    public String getCorpus_id() {
        return corpus_id;
    }

    public int getN_tokens(){
        return n_tokens;
    }

    /************************  Generic Setters ************************/

    public void setN_tokens(int n){
        this.n_tokens = n;
    }

    public void setCorpus_id(String corpus){
        this.corpus_id = corpus;
    }

    /************************  Relationships Methods ************************/

    /*
        Set a list of relevant documents by means of their shared labels,
        which are the root categories of the Eurovoc Ontology.
        The list contains the IDs of each document with at least one common label
     */
    public void setRelevant(List<Doc> corpus){
        relevant = new ArrayList<>();
        corpus.forEach(d->{
            if(!(d.getId().equals(this.id)) && !(Collections.disjoint(labels,d.getLabels())))
                relevant.add(d.getId());
        });
    }

    public List<String> getRelevant() {
        return relevant;
    }

    /*
        Get documents retrieved by the ptm $model the JSD (>0.5) metric sorted by value
     */
    public List<String> getRetrieved(String model, List<Doc> corpus){
         return getRetrieved(model,corpus,new JSD());
    }

    /*
        Returns the list of documents retrieved by the $model from the ones in the $corpus
        comparing them by $metric
     */
    public List<String> getRetrieved(String model, List<Doc> corpus, DocumentMetric metric){

        Map<String,Double> retrievedMap = new HashMap<>();
        List<String> retrieved;

        DocProjection doc = modelProjection.get(model);
        corpus.forEach(d ->{
            Double similarity = metric.similarity(doc,d.modelProjection.get(model));
            if(!(d.getId().equals(this.id)) && (similarity>metric.getThreshold()))
                retrievedMap.put(d.getId(),similarity);
        });

        // Sort by value
        retrieved = retrievedMap
                .entrySet()
                .stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());


        return retrieved;
    }




    /************************  PTM Methods ************************/

    /*
        Infer the document projection using the Librairy
     */
    public void addProjection(String model ) {
        modelProjection.put(model,LibrairyClient.infer(this));
    }

    /*
        Infer the document projection using the Librairy
     */
    public void addProjection(String model, String endpoint ){
        modelProjection.put(model,LibrairyClient.infer(this,endpoint));
    }


    /*
        Add a new model projection from:
            DocProjection
     */
    public void addProjection(String model, DocProjection docProjection){
        modelProjection.put(model,docProjection);
    }

    /*
        Add a new model projection from:
            Vector
            Topics
     */
    public void addProjection(String model, List<Double> vector, List<List<String>> topics){
        modelProjection.put(model,new DocProjection(vector, topics));
    }

    public DocProjection getModelProjection(String model){
        return modelProjection.get(model);
    }

    /*
        Set of ptm names in which the document has been projected
     */
    public Set<String> getModels(){
        return modelProjection.keySet();
    }


    /************************  Other Methods ************************/

    @Override
    public String toString(){
        return id   +   "\n"+
                "Number of relevant documents: "    +   relevant.size()+
                "Models: "  + modelProjection.toString();
    }
}
