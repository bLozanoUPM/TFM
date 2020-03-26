library(ggplot2)
library(dendextend)

# jrc=read.csv("/Users/borjalozanoalvarez/Projects/Library/MetricPerformance/src/main/resources/size/total_size.csv")
# jrc=read.csv("/Users/borjalozanoalvarez/Projects/Library/MetricPerformance/src/main/resources/size/homogenized_size.csv")
# 
# 
# dend <- jrc$size  %>% 
#   dist(method = "euclidean") %>%
#   hclust(method = "average")  %>%
#   as.dendrogram %>% 
#   set("branches_k_color", k = 6)
# 
# ggd1 <- as.ggdend(dend)

load("/Users/borjalozanoalvarez/Projects/TFM/denogram.RData")
ggplot(ggd1,labels = FALSE) 
ggplot(ggd1,labels = FALSE, theme = theme_minimal()) 

