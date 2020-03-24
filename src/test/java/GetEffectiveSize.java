import data.Doc;
import io.CSVReader;
import io.LibrairyClient;
import io.ParallelExecutor;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
}
