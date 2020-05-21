package data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class RetrievalEvaluation {

    private static final Logger LOG = LoggerFactory.getLogger(RetrievalEvaluation.class);

    private String description;

    private Precision ap;

    private long N;

    private long TP;

    private long FP;

    private long FN;

    private Double precision;

    private Double recall;

    private Double fmeasure;


    /************************  Constructors ************************/

    public RetrievalEvaluation(){
        this("Evaluation");
    }

    public RetrievalEvaluation(String description){
        this.description=description;
        ap = new Precision();
    }


    /************************  Getters/Setters ************************/

    public String getDescription() {
        return description;
    }

    public long getN() {
        return N;
    }

    public long getTP() {
        return TP;
    }

    public void setTP(long TP) {
        this.TP = TP;
    }

    public Double getMAP() {
        return ap.meanAveragePrecision();
    }

    private Double getMAPatK(int k) {
        return ap.meanAveragePrecisionAtK(k);
    }

    public Double getPK(int k){
        return ap.precisionAtK(k);
    }

    public Double getPrecision() {
        if(precision==null) setPrecision();
        return precision;
    }

    private void setPrecision() {
        double positive = TP + FP;
        precision = (positive == 0.0) ? 0.0 : TP/positive;
    }

    public Double getRecall() {
        if (recall == null)  setRecall();
        return recall;
    }

    private void setRecall() {
        double positive = (double) TP + (double) FN;

        recall = (positive == 0.0) ? 0.0 : (double) TP / positive;
    }

    public Double getFMeasure() {
        if(fmeasure == null) setFMeasure();
        return fmeasure;
    }

    private void setFMeasure() {
        Double precision = getPrecision();
        Double recall = getRecall();

        fmeasure = ((precision == 0) && (recall == 0)) ? 0.0 : 2 * (precision*recall) / (precision+recall);
    }

    public void setN(long n) {
        N = n;
    }


    /************************  Other Methods ************************/

    public void addResult(List<String> relevant, List<String> retrieved){
        if (relevant.isEmpty()){
            recall = 1.0;
            if (retrieved.isEmpty()){
                precision = 1.0;
            }
        }

        N   += retrieved.size();

        TP   += retrieved.stream().filter(relevant::contains).count();

        FP   += retrieved.stream().filter( e -> !relevant.contains(e)).count();

        FN   += relevant.stream().filter( e -> !retrieved.contains(e)).count();

        ap.addAveragePrecision(relevant,retrieved);
    }

    public String summary() {
        return description +
                "\n  N="   + N +
                ",\n TP="   + TP +
                ",\n FP="  + FP +
                ",\n FN="  + FN +
                ",\n precision="      + getPrecision()+
                ",\n recall="         + getRecall() +
                ",\n fMeasure="       + getFMeasure() +
                ",\n  P@1="+ getPK(1) +
                ",\n P@3="+ getPK(3) +
                ",\n P@5="+ getPK(5) +
                ",\n P@10="+ getPK(10)+
                ",\n P@15="+ getPK(15)+
                ",\n MAP="      + getMAP()+
                '\n';
    }


    /*
        Returns a csv format of the evaluation
        Current format:
        N_i,TP_i,FP_i,FN_i,precision_d,recall_d,fMeasure_d,P@1,P@3,P@5,P@10,P@15,MAP_d
    */
    @Override
    public String toString(){
        return N + "," +
                TP + "," +
                FP + "," +
                FN + "," +
                getPrecision() + "," +
                getRecall() + "," +
                getFMeasure() + "," +
                getPK(1) +"," +
                getPK(3) +"," +
                getPK(5) +"," +
                getPK(10) +"," +
                getPK(15) +"," +
                getMAP() +"," +
                getMAPatK(10);
    }

}
