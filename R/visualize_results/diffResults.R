
diff <- function(lang,split,metric="wjl") {
  df <- loadResults_metric(lang,split,metric)
  # df <- loadResults(lang,split)
  # df<-aggregate(df[,5:8],by = list(ptm_id=df$ptm_id,test_id=df$test_id,metric=df$metric_id), mean)
  # df<-melt(df)
  
  summary=subset(read.csv(concat("../src/main/resources/acquis/",lang,"/",split,"/R/summary.csv"), sep = ";"), Corpus=="merged")[,c(1,3:6)]
  summary$Mean <- as.double(gsub(",", ".", as.character(summary$Mean)))
  summary$Variance <- as.double(gsub(",", ".", as.character(summary$Variance)))
  summary$Median <- as.numeric(as.character(summary$Median))
  summary$Set <- factor(summary$Set,labels = levels(df$ptm_id))
  colnames(summary)[1]<-"ptm_id"
  df<-merge(df,summary)
  
  df$diff_LogVar <- mapply(function(ptm,test) log(subset(summary,ptm_id==ptm)$Variance)-log(subset(summary,ptm_id==concat(split,"-",test))$Variance),
                    df$ptm_id,
                    df$test_id)
  df$diff_Mean <- mapply(function(ptm,test) subset(summary,ptm_id==ptm)$Mean-subset(summary,ptm_id==concat(split,"-",test))$Mean,
                          df$ptm_id,
                          df$test_id)
  df$diff_Median <- mapply(function(ptm,test) subset(summary,ptm_id==ptm)$Median-subset(summary,ptm_id==concat(split,"-",test))$Median,
                          df$ptm_id,
                          df$test_id)
  return(df)
}


plotAllResults <- function(lang,split="sml"){
  datalist = list()
  for (i in c(3,6,9)) {
    datalist[[i]]<-diff(lang,concat(split,i))
  }
  return(do.call(rbind, datalist))
}

# plotAllResults <- function(lang){
#   datalist = list()
#   for (i in 1:length(splits)) {
#     datalist[[i]]<-diff(lang,splits[i])
#   }
#   
#   return(do.call(rbind, datalist))
# }




