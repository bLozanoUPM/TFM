import data.Doc;
import io.CSVReader;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class HoldOutSplit {

    private final static Logger LOG = LoggerFactory.getLogger(HoldOutSplit.class);

    private final double split = 0.05;
    private final String lang = "es";
    private String resources_R = "/Users/borjalozanoalvarez/Projects/TFM/data/";
    private String resources = "/Users/borjalozanoalvarez/Projects/Library/TFM/src/main/resources/";


//    @Test
//    public void acquis() throws IOException {
//
//        Random generator = new Random(1);
//
//        Map<String,Doc> jrc, dgt;
//        jrc = CSVReader.loadCorpus(resources+"corpora/JRC_"+lang.toUpperCase()+".csv");
//        dgt = CSVReader.loadCorpus(resources+"corpora/DGT_"+lang.toUpperCase()+".csv");
//
//        List<String> jrc_ids,dgt_ids;
//
//        jrc_ids = new ArrayList<>(jrc.keySet());
//
//
//        resources_R+="acquis/"+lang+"/";
//        resources+="acquis/"+lang+"/";
//
//        Files.list(Paths.get(resources_R))
//                .filter(Files::isDirectory)
//                .forEach(d->{   // For each split set
//                    try {
//                        String spl = d.getFileName().toString();
//                        LOG.info("\n\n\n" +
//                                "########################### " +
//                                spl +
//                                " ###########################");
//
//                        Files.list(d.toAbsolutePath())
//                                .forEach(f->{   // For each individual split
//                                    String file_name = spl+"/"+f.getFileName().toString();
//
//                                    if(f.getFileName().toString().equals("summary.csv")
//                                            || !file_name.endsWith(".csv"))
//                                        return;
//
//                                    String file_path = resources_R+file_name;
//                                    List<String> ids = new ArrayList<>(
//                                            CSVReader.loadSize(file_path)
//                                                    .keySet()
//                                    );
//                                    int test_size = (int) Math.floor(ids.size() * split);
//
//                                    List<String> test = new ArrayList<>(jrc_ids);
//                                    test.retainAll(ids);
//
//                                    LOG.info("{} -> {}::{}>{}",file_name,ids.size(),test_size,test.size()>=test_size ? test.size()>=test_size : test.size());
//
//                                    if(test_size<=test.size()){
//                                        Collections.shuffle(test,generator);
//                                        test.subList(0,test_size);
//                                    }
//
//                                    ids.removeAll(test);
//
//                                    // Train/Test Files
//                                    try {
//
//                                        File testFile = new File(resources+spl+"/test/"+f.getFileName().toString());
//                                        File trainFile = new File(resources+spl+"/train/"+f.getFileName().toString());
//
//                                        FileWriter testFW = new FileWriter(testFile);
//                                        FileWriter trainFW = new FileWriter(trainFile);
//
//                                        BufferedWriter testBW = new BufferedWriter(testFW);
//                                        BufferedWriter trainBW = new BufferedWriter(trainFW);
//
//                                        testBW.write("id,root-labels_t,text_t\n");
//                                        trainBW.write("id,text_t\n");
//
//                                        test.forEach(q->{
//                                            try {
//                                                testBW.write(q+",\""+jrc.get(q).getLabels()+"\",\""+jrc.get(q).getText()+"\"\n");
//                                            } catch (IOException e) {
//                                                e.printStackTrace();
//                                            }
//                                        });
//
//                                        ids.forEach(q->{
//                                            try {
//                                                trainBW.write(q+",\""+dgt.get(q).getText()+"\"\n");
//                                            } catch (IOException e) {
//                                                e.printStackTrace();
//                                            }
//                                        });
//
//
//                                        testBW.close();
//                                        trainBW.close();
//                                    } catch (IOException e) {
//                                        e.printStackTrace();
//                                    }
//
//                                });
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                });
//
//    }


}
