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


//        LibrairyClient.createModel("test","EN","1.0","/librairy/resources/en/sml9/train/s1.csv",100);

        for (int i = 1; i < 4; i++) {
            int version=1;
            for(Integer n: new int[]{50,100,300,500}){
                LibrairyClient.createModel("en_sml3_s"+i,"EN",(version++) +".0","/librairy/resources/en/sml3/train/s"+i+".csv",n);
            }
        }

    }
}
