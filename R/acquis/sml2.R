library(ggplot2)

source("R/acquis/clear.R")
lang = "es"
path = concat("data/acquis/",lang,"/ssmmll/")
df<-clear(lang)

# Sort data by tokens
df <- df[order(df$tokens_i),]

# Add index as variable
row.names(df) <- NULL
df$index <- as.numeric(row.names(df))

# Divide in three equal sets
k=6
df$cluster <- as.numeric(cut(df$index, k))

# Boxplot of tokens
ggplot(data=df, aes(x=as.factor(cluster), y=tokens_i, fill=corpus_id)) +
  geom_boxplot()+
  scale_x_discrete(name="group_c")+
  labs(title = "SSMMLL-Tokens distribution",
       subtitle= concat("acquis_",lang))
ggsave(concat(path,"box.png"))

s <- subset(df,cluster==1)[,c(1,4)]
S <- subset(df,cluster==2)[,c(1,4)]
m <- subset(df,cluster==3)[,c(1,4)]
M <- subset(df,cluster==4)[,c(1,4)]
g <- subset(df,cluster==5)[,c(1,4)]
G <- subset(df,cluster==6)[,c(1,4)]

write.csv(s,file = concat(path,"smallest.csv"),row.names=FALSE,quote = FALSE)
write.csv(S,file = concat(path,"small.csv"),row.names=FALSE,quote = FALSE)
write.csv(m,file = concat(path,"mediumLow.csv"),row.names=FALSE,quote = FALSE)
write.csv(M,file = concat(path,"mediumHigh.csv"),row.names=FALSE,quote = FALSE)
write.csv(g,file = concat(path,"large.csv"),row.names=FALSE,quote = FALSE)
write.csv(G,file = concat(path,"largest.csv"),row.names=FALSE,quote = FALSE)



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



# Boxplot of tokens
ggplot(data=df, aes(x=as.factor(cluster), y=tokens_i, fill=corpus_id)) +
  geom_boxplot()+
  scale_x_discrete(name="group_c")+
  labs(title = "Tokens distribution in sets with equal number of documents")

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


