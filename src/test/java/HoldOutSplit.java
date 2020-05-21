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
    private final String lang = "en";
    private String resources = "./src/main/resources/";


    @Test
    public void acquis_Splits() throws IOException {

        Map<String,Doc> docs;
        docs = CSVReader.loadCorpus(resources+"corpora/acquis.csv")
                .entrySet()
                .stream()
                .filter(d->d.getValue().getCorpus_id().endsWith(lang))
                .collect(Collectors.toMap(Map.Entry::getKey,Map.Entry::getValue));

        List<String> jrc_ids = docs
                .entrySet()
                .stream()
                .filter(d->d.getValue().getCorpus_id().startsWith("jrc"))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        resources+="acquis/"+lang+"/";

        Files.list(Paths.get(resources))
                .filter(Files::isDirectory)
                .forEach(d->{   // For each split set
                    try {
                        String spl = d.getFileName().toString();
                        LOG.info("\n\n\n" +
                                "########################### " +
                                spl +
                                " ###########################");

                        Files.list(Paths.get(d.toAbsolutePath() + "/R"))
                                .forEach(f->{   // For each individual split
                                    String filename = f.getFileName().toString();

                                    if(filename.equals("summary.csv")
                                            || !filename.endsWith(".csv"))
                                        return;

                                    LOG.info(spl+"/"+filename);

                                    List<String> train = new ArrayList<>(
                                            CSVReader.loadSize(f.toAbsolutePath().toString())
                                                    .keySet()
                                    );
                                    int test_size = (int) Math.floor(train.size() * split);

                                    List<String> test = new ArrayList<>(jrc_ids);
                                    test.retainAll(train);

                                    if(test_size<=test.size()){
                                        Collections.shuffle(test);
                                        test = test.subList(0,test_size+1);
                                    }

                                    train.removeAll(test);
                                    Collections.shuffle(train);

                                    LOG.info("{} -> {}::{}>{}",spl+"/"+filename,
                                            train.size(),
                                            test_size,
                                            test.size()>=test_size ? test.size()>=test_size : test.size());

                                    // Train/Test Files
                                    try {

                                        File testFile = new File(resources+spl+"/test/"+lang+"_"+filename);
                                        File trainFile = new File(resources+spl+"/train/"+lang+"_"+filename);

                                        FileWriter testFW = new FileWriter(testFile);
                                        FileWriter trainFW = new FileWriter(trainFile);

                                        BufferedWriter testBW = new BufferedWriter(testFW);
                                        BufferedWriter trainBW = new BufferedWriter(trainFW);

                                        testBW.write("id,root-labels_t,text_t\n");
                                        trainBW.write("id,text_t\n");

                                        test.forEach(q->{
                                            try {
                                                testBW.write(q+",\""+docs.get(q).getLabels()+"\",\""
                                                        +docs.get(q).getText().replaceAll(",","")+
                                                        "\"\n");
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                        });

                                        train.forEach(q->{
                                            try {
                                                trainBW.write(q+",\""+docs.get(q).getText()+"\"\n");
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                        });


                                        testBW.close();
                                        trainBW.close();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }

                                });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });

    }

    @Test
    public void acquis() throws  IOException {
        Map<String,Doc> docs;
        docs = CSVReader.loadCorpus(resources+"corpora/acquis.csv")
                .entrySet()
                .stream()
                .filter(d->d.getValue().getCorpus_id().endsWith(lang))
                .collect(Collectors.toMap(Map.Entry::getKey,Map.Entry::getValue));

        List<String> jrc_ids = docs
                .entrySet()
                .stream()
                .filter(d->d.getValue().getCorpus_id().startsWith("jrc"))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());


        resources+="acquis/"+lang+"/";

        List<String> train = CSVReader.ids("src/main/resources/corpora/acquis_"+lang+"_clean.csv");
        List<String> test = jrc_ids.stream().filter(train::contains).collect(Collectors.toList());

        int test_size = (int) Math.floor(docs.size() * split);

        if(test_size<=jrc_ids.size()){
            Collections.shuffle(jrc_ids);
            test = jrc_ids.subList(0,test_size+1);
        }

        train.removeAll(test);
        Collections.shuffle(train);

        LOG.info("\nTrain:{}\nTest:{}\nTotal:{}",train.size(),test.size(),train.size()+test.size());

        // Train/Test Files
        try {

            File testFile = new File(resources+"test.csv");
            File trainFile = new File(resources+"train.csv");

            FileWriter testFW = new FileWriter(testFile);
            FileWriter trainFW = new FileWriter(trainFile);

            BufferedWriter testBW = new BufferedWriter(testFW);
            BufferedWriter trainBW = new BufferedWriter(trainFW);

            testBW.write("id,root-labels_t,text_t\n");
            trainBW.write("id,text_t\n");

            test.forEach(q->{
                try {
                    testBW.write(q+",\""+docs.get(q).getLabels()+"\",\""
                            +docs.get(q).getText().replaceAll(",","")+
                            "\"\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            train.forEach(q->{
                try {
                    trainBW.write(q+",\""+docs.get(q).getText()+"\"\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });


            testBW.close();
            trainBW.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}
