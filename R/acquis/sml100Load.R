n50 = read.csv("/Users/borjalozanoalvarez/Projects/Library/MetricPerformance/src/main/resources/acquis_en/evaluation/sml_50/JCTL(0.0).csv")
n100 = read.csv("/Users/borjalozanoalvarez/Projects/Library/MetricPerformance/src/main/resources/acquis_en/evaluation/sml_100/JCTL(0.0).csv")
n300 = read.csv("/Users/borjalozanoalvarez/Projects/Library/MetricPerformance/src/main/resources/acquis_en/evaluation/sml_300/JCTL(0.0).csv")
n500 = read.csv("/Users/borjalozanoalvarez/Projects/Library/MetricPerformance/src/main/resources/acquis_en/evaluation/sml_500/JCTL(0.0).csv")

n50$n_topics <- 50
n100$n_topics <- 100
n300$n_topics <- 300
n500$n_topics <- 500

eval <- rbind(n50,n100,n300,n500)
eval$ptmModel_id <- factor(eval$ptmModel_id,levels(eval$ptmModel_i)[c(3,2,1)])
eval$testSet_id <- factor(eval$testSet_id,levels(eval$testSet_id)[c(3,2,1)])
eval$n_topics <- as.factor(eval$n_topics)
