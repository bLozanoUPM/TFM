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
import java.util.*;

public class GetEffectiveSize {

    private final static Logger LOG = LoggerFactory.getLogger(GetEffectiveSize.class);

    private String resources = "./src/main/resources/";

    @Test
    public void effectiveSize() throws IOException {

        List<String> list =
                Collections.synchronizedList(new ArrayList<String>());

        Map<String,Doc> docs = CSVReader.loadCorpus(resources+"corpora/acquis.csv");
        ParallelExecutor pool = new ParallelExecutor();
        double size = docs.size();
        LOG.info("acquis: {}",(int)size);

        int i = 0;
        for (Doc d: docs.values()) {
            String lang = d.getCorpus_id().split("_")[1];
            pool.submit(()->{
                int tokens = LibrairyClient.effectiveSize(d.getText().trim(),lang.toUpperCase());
                if(tokens==0)
                    list.add(d.getId());
                else
                    d.setN_tokens(tokens);
            });
            if(++i%1000==0)LOG.info("{}:{}%",i,(i/size)*100);
        }
        pool.awaitTermination();
        list.forEach(LOG::info);
        list.forEach(docs::remove);
        toFile("acquis_size.csv",docs.values());
    }

    public void toFile(String filename, Collection<Doc> docs) throws IOException {
        File f = new File(resources+"/corpus/"+filename);
        FileWriter fw = new FileWriter(f);
        BufferedWriter bw = new BufferedWriter(fw);

        bw.flush();

        bw.write("id;size_i;ntokens_i\n");

        for (Doc doc: docs){
            String text = doc.getText();
            bw.write(doc.getId() + ";" +
                    text.length() + ";" +
                    doc.getN_tokens() + ";" +
                    "\n"
            );
        }
        bw.close();
        fw.close();
    }
    
}
