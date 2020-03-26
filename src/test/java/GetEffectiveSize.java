import data.Doc;
import io.CSVReader;
import io.LibrairyClient;
import io.ParallelExecutor;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.*;

public class GetEffectiveSize {

    private final static Logger LOG = LoggerFactory.getLogger(GetEffectiveSize.class);

    private String resources = "./src/main/resources/";

    Map<String,Doc> docs;

    @Before
    public void before() throws IOException{
        docs = CSVReader.loadCorpus(resources+"corpora/acquis.csv");
        File f = new File(resources+"/missing.txt");
        if(f.exists()){
            List<String> miss = new ArrayList<>();
            FileReader fileReader = new FileReader(f);

            BufferedReader reader = new BufferedReader(fileReader);
            String row = null;
            while ( (row = reader.readLine()) != null )
            {
                miss.add(row);
            }
            reader.close();

            docs.keySet().retainAll(miss);
        }

    }

    @Test
    public void effectiveSize() throws IOException {

        List<String> miss =
                Collections.synchronizedList(new ArrayList<String>());

        ParallelExecutor pool = new ParallelExecutor();
        double size = docs.size();
        LOG.info("acquis: {}",(int)size);

        int i = 0;
        for (Doc d: docs.values()) {
            String lang = d.getCorpus_id().split("_")[1];
            pool.submit(()->{
                int tokens = LibrairyClient.effectiveSize(d.getText().trim(),lang.toUpperCase());
                if(tokens==0)
                    miss.add(d.getId());
                else
                    d.setN_tokens(tokens);
            });
            if(++i%1000==0)LOG.info("{}:{}%",i,(i/size)*100);
        }
        pool.awaitTermination();
        LOG.info("{}",miss.size());
        miss.forEach(docs::remove);
        toFile("acquis_size.csv",docs.values());
        missing(miss);
    }

    public void toFile(String filename, Collection<Doc> docs) throws IOException {
        File f = new File(resources+"/corpora/"+filename);
        FileWriter fw = new FileWriter(f,true);
        BufferedWriter bw = new BufferedWriter(fw);

        if(f.length()==0)
            bw.write("id;corpus_id;size_i;ntokens_i\n");

        for (Doc doc: docs){
            String text = doc.getText();
            bw.write(doc.getId() + ";" +
                    doc.getCorpus_id() + ";" +
                    text.length() + ";" +
                    doc.getN_tokens() + ";" +
                    "\n"
            );
        }
        bw.close();
        fw.close();
    }

    /*
        Due to LibrAIry api having some problem ingesting all documents errors have to be safe and process in another batch
     */
    public void missing(List<String> missing) throws IOException {
        File f = new File(resources+"/missing.txt");
        FileWriter fw = new FileWriter(f);
        BufferedWriter bw = new BufferedWriter(fw);

        bw.flush();
        for (String miss: missing ) {
        bw.write(miss);bw.newLine();
        }
        bw.close();
        fw.close();
    }

}
