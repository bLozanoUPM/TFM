n50 = read.csv("/Users/borjalozanoalvarez/Projects/Library/MetricPerformance/src/main/resources/evaluation/sml2_50/JCTL(0.0).csv")
n100 = read.csv("/Users/borjalozanoalvarez/Projects/Library/MetricPerformance/src/main/resources/evaluation/sml2_100/JCTL(0.0).csv")
n300 = read.csv("/Users/borjalozanoalvarez/Projects/Library/MetricPerformance/src/main/resources/evaluation/sml2_300/JCTL(0.0).csv")
n500 = read.csv("/Users/borjalozanoalvarez/Projects/Library/MetricPerformance/src/main/resources/evaluation/sml2_500/JCTL(0.0).csv")

n50$n_topics <- 50
n100$n_topics <- 100
n300$n_topics <- 300
n500$n_topics <- 500

eval <- rbind(n50,n100,n300,n500)
eval$ptmModel_id <- factor(eval$ptmModel_id,levels(eval$ptmModel_i)[c(6,5,4,3,1,2)])
eval$testSet_id <- factor(eval$testSet_id,levels(eval$testSet_id)[c(6,5,4,3,1,2)])
eval$n_topics <- as.factor(eval$n_topics)
