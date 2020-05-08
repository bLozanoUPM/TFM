concat <- function( ..., sep="" ) paste( ..., sep = sep )

#Splits
splits=c("sml3","sml6","sml9",
         "ck3","ck6","ck9")

loadResults <- function(lang,split){
  df=read.csv(concat("../src/main/resources/acquis/",lang,"/",split,"/evaluation/",split,"_evaluation.csv"), sep = ",")[,c(1:4,12:15)]
  df$test_id=as.factor(df$test_id)
  df$topics_i=as.factor(df$topics_i)
  return(df)
}


loadResults_metric <- function(lang,split,metric){
  df=subset(loadResults(lang,split),metric_id==metric)
  # df$metric_id<-NULL
  return(df)
}

loadAllResults <- function(lang){
  datalist = list()
  for (i in 1:length(splits)) {
    datalist[[i]]<-loadResults(lang,splits[i])
  }
  return(do.call(rbind, datalist))
}

