library(ggplot2)
library(tibble)

source("acquis/clear.R")

sml <- function(lang,k){
  path = concat("../src/main/resources/acquis/",lang,"/sml",k,"/R/")
  df<-clear(lang)
  
  # Sort data by tokens
  df <- df[order(df$tokens_i),]
  
  # Add index as variable
  row.names(df) <- NULL
  df$index <- as.numeric(row.names(df))
  
  # Divide in k equal sets
  df$cluster <- as.numeric(cut(df$index, k))
  
  # Boxplot of tokens
  ggplot(data=df, aes(x=as.factor(cluster), y=tokens_i, fill=corpus_id)) +
    geom_boxplot()+
    scale_x_discrete(name="group_c")+
    # labs(title = "SML-Tokens distribution",
    #      subtitle= concat("acquis_",lang))
  ggsave(concat(path,"box.png"))
  
  for (i in as.numeric(levels(as.factor(df$cluster)))) {
    c <- subset(df,cluster==i)[,c(1,3)]
    write.csv(c,file = concat(path,"sml",k,"_",i,".csv"),row.names=FALSE,quote = FALSE)
  }
  
  df_summary<-setNames(
    data.frame(matrix(ncol = 8, nrow = 0)),
    c("Set", "Corpus", "NDocs", "Median", "Mean", "Variance", "Min", "Max"))
  
  for (i in 1:k) {
    ndocs=nrow(subset(df, cluster == i))
    var=var(subset(df, cluster == i)$tokens_i)
    sum=summary(subset(df, cluster == i)$tokens_i)
    df_summary<-add_row(df_summary,Set=i, 
                        Corpus="merged",
                        NDocs=ndocs, 
                        Median=sum["Median"],
                        Mean=sum["Mean"],
                        Variance=var,
                        Min=sum["Min."],
                        Max=sum["Max."])
    
    for (corpus in levels(df$corpus_id)) {
      ndocs=nrow(subset(df, corpus_id==corpus & cluster == i))
      var=var(subset(df, corpus_id==corpus & cluster == i)$tokens_i)
      sum=summary(subset(df, corpus_id==corpus & cluster == i)$tokens_i)
      df_summary<-add_row(df_summary,Set=i, 
                          Corpus=corpus,
                          NDocs=ndocs, 
                          Median=sum["Median"],
                          Mean=sum["Mean"],
                          Variance=var,
                          Min=sum["Min."],
                          Max=sum["Max."])
    }
  }
  write.csv2(df_summary,file = concat(path,"summary.csv"),row.names=FALSE)
}

ck <- function(lang,k){
  path = concat("../src/main/resources/acquis/",lang,"/ck",k,"/R/")
  df<-clear(lang)
  
  
  df <- df[order(df$tokens_i),]
  # Add index as variable
  row.names(df) <- NULL
  df$index <- as.numeric(row.names(df))
  
  require(Ckmeans.1d.dp)
  result <- Ckmeans.1d.dp(df$tokens_i,k)
  
  df$cluster <- result$cluster
  
  # Boxplot of tokens
  ggplot(data=df, aes(x=as.factor(cluster), y=tokens_i, fill=corpus_id)) +
    geom_boxplot()+
    scale_x_discrete(name="group_c")+
    # labs(title = concat("CK",k,"-Tokens distribution"),
    #      subtitle= concat("acquis_",lang))
  ggsave(concat(path,"box.png"))
  
  for (i in as.numeric(levels(as.factor(df$cluster)))) {
    c <- subset(df,cluster==i)[,c(1,3)]
    write.csv(c,file = concat(path,"ck",k,"_",i,".csv"),row.names=FALSE,quote = FALSE)
  }
  
  library(tibble)
  df_summary<-setNames(
    data.frame(matrix(ncol = 8, nrow = 0)),
    c("Set", "Corpus", "NDocs", "Median", "Mean", "Variance", "Min", "Max"))
  
  for (i in 1:k) {
    ndocs=nrow(subset(df, cluster == i))
    var=var(subset(df, cluster == i)$tokens_i)
    sum=summary(subset(df, cluster == i)$tokens_i)
    df_summary<-add_row(df_summary,Set=i, 
                        Corpus="merged",
                        NDocs=ndocs, 
                        Median=sum["Median"],
                        Mean=sum["Mean"],
                        Variance=var,
                        Min=sum["Min."],
                        Max=sum["Max."])
    
    for (corpus in levels(df$corpus_id)) {
      ndocs=nrow(subset(df, corpus_id==corpus & cluster == i))
      var=var(subset(df, corpus_id==corpus & cluster == i)$tokens_i)
      sum=summary(subset(df, corpus_id==corpus & cluster == i)$tokens_i)
      df_summary<-add_row(df_summary,Set=i, 
                          Corpus=corpus,
                          NDocs=ndocs, 
                          Median=sum["Median"],
                          Mean=sum["Mean"],
                          Variance=var,
                          Min=sum["Min."],
                          Max=sum["Max."])
    }
  }
  write.csv2(df_summary,file = concat(path,"summary.csv"),row.names=FALSE)
  
  
  
  # Violin of tokens
  ggplot(data=df, aes(x=as.factor(cluster), y=tokens_i, fill=corpus_id)) +
    geom_violin()+
    scale_x_discrete(name="group_c")
  
  # Boxplot+Jitter of tokens
  ggplot(data=df, aes(x=as.factor(cluster), y=tokens_i, fill=corpus_id)) +
    geom_jitter(size=0.1)+
    geom_boxplot(alpha=0.8)+
    scale_x_discrete(name="group_c")
  
  # Boxplot+Jitter of size
  ggplot(data=df, aes(x=as.factor(cluster), y=size_i, fill=corpus_id)) +
    geom_jitter(size=0.1)+
    geom_boxplot(alpha=0.8)+
    scale_x_discrete(name="group_c")
}

for (lang in c("en","es")) {
  for(k in c(3,6,9)){
    sml(lang,k)
    ck(lang,k)
  }
}
