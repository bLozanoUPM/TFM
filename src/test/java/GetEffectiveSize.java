import data.Doc;
import io.CSVReader;
import io.LibrairyClient;
import io.ParallelExecutor;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;

public class GetEffectiveSize {

    private final static Logger LOG = LoggerFactory.getLogger(GetEffectiveSize.class);

    private String resources = "./src/main/resources/";

    @Test
    public void effectiveSize(){
        String[] corpora = {"jrc","dgt"};
        String[] languages = {"es","en"};

        Map<String,Doc> docs;
        ParallelExecutor pool = new ParallelExecutor();
        for (String corpus: corpora) {
            for (String lang: languages){
                docs = CSVReader.loadCorpus(resources+"corpora/" +
                        corpus.toUpperCase()+"_"+
                        lang.toUpperCase()+".csv");
                double size = docs.size();
                LOG.info("{}_{}: {}",corpus,lang,size);

                int i = 0;
                for (Doc d: docs.values()) {
                    pool.submit(()->d.setN_tokens(LibrairyClient.effectiveSize(d.getText(),lang.toUpperCase())));
                    if(i++%1000==0)LOG.info("{}:{}",i,i/size);
                }
                pool.awaitTermination();

            }
        }
    }

    public void toFile(String filename, Collection<Doc> docs) throws IOException {
        File f = new File(resources+"/coprus/"+filename);
        FileWriter fw = new FileWriter(f);
        BufferedWriter bw = new BufferedWriter(fw);

        bw.flush();

        bw.write("id;lables_s;size_i;ntokens_i;text_s\n");

        for (Doc doc: docs){
            String text = doc.getText();
            bw.write(doc.getId() + ";" +
                    doc.getLabels().toString() + ";" +
                    text.length() + ";" +
                    doc.getN_tokens() + ";" +
                    "\"" + doc.getText().replace("\n","").replace("\"","") +"\"\n"
            );
        }
        bw.close();
        fw.close();
    }
}
