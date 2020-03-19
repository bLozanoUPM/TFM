import io.LibrairyClient;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class CreateModels {

    private final static Logger LOG = LoggerFactory.getLogger(CreateModels.class);

    private final static String resources = "./src/main/resources/acquis/";

    @Test
    public void createModels() throws IOException {
        for(String lang: new String[]{/*"en",*/"es"})
            Files.list(Paths.get(resources+"/"+lang+"/"))
                    .filter(Files::isDirectory)
                    .forEach(d->{
                        try {
                            Files.list(Paths.get(d.toString()+"/train/"))
                            .forEach(t->{
                                LOG.info(t.toString());
                                String model = d.getFileName().toString();
                                int version = 1;
                                for(Integer n: new int[]{50,100,300,500}){
                                    String model_name = t.getFileName().toString();
                                    LibrairyClient.createModel(lang+"_"+model+"_"+model_name.substring(0, model_name.lastIndexOf('.')),
                                            lang.toUpperCase(),
                                            (version++) +".0",
                                            "/librairy/resources/"+lang+"/"+model+"/train/" + model_name,
                                            n);
                                }
                            });
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
    }

    @Test
    public void createModel() throws IOException {
        String lang = "en";
        String model = "ck6";
        String split = "c1";

        int version = 1;
        for(Integer n: new int[]{50,100,300,500}){
            LibrairyClient.createModel(lang+"_"+model+"_"+split,
                    lang.toUpperCase(),
                    (version++) +".0",
                    "/librairy/resources/en/"+model+"/train/" + split+".csv",
                    n);
        }

    }
}
