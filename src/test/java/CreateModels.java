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
                    .filter(d->!d.getFileName().toString().contains("3")) //TODO
                    .forEach(d->{
                        String model = d.getFileName().toString();
                        LOG.info(model);
                        try {
                            Files.list(Paths.get(d.toString()+"/train/"))
                                    .forEach(t->{
                                        int version = 1;
                                        for(Integer n: new int[]{50,100,300,500}){
                                            String model_name = t.getFileName().toString();
                                            LibrairyClient.createModel(model_name.substring(0, model_name.lastIndexOf('.')),
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
    public void createModel() {
        String lang = "es";
        String model = "sml9";
        String split = "1";

        int version = 1;
        for(Integer n: new int[]{50/*,100,300,500*/}){
            String name = lang+"_"+model+"_"+split;
            LibrairyClient.createModel(lang+"_"+model+"_"+split,
                    lang.toUpperCase(),
                    (version++) +".0",
                    "/librairy/resources/"+lang+"/"+model+"/train/" + name + ".csv",
                    n);
        }

    }
}
