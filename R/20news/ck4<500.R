source("R/20news/clear.R")

df<-subset(df,tokens_i<=500)

df <- df[order(df$tokens_i),]
# Add index as variable
row.names(df) <- NULL
df$index <- as.numeric(row.names(df))

nmin = nrow(subset(df, tokens_i >400 & tokens_i<=500))
testmin = nrow(subset(df, tokens_i >400 & tokens_i<=500 & corpus_id == "jrc"))

ggplot(df, aes(x=tokens_i,fill=corpus_id)) + 
  geom_histogram(binwidth = 100, color = "black",  boundary = TRUE) +
  geom_hline(yintercept = nmin, linetype="dashed") +
  geom_hline(yintercept = testmin, linetype="dashed") +
  labs(title="Number of documents in each range (bin=50)")


# Divide in three equal sets
k=4
df$cluster <- as.numeric(cut(df$index, k))


require(Ckmeans.1d.dp)
result <- Ckmeans.1d.dp(df$tokens_i,4)

print(result$size)

df$cluster <- result$cluster



c1 <- subset(df,cluster==1)[,c(1,3)]
c2 <- subset(df,cluster==2)[,c(1,3)]
c3 <- subset(df,cluster==3)[,c(1,3)]
c4 <- subset(df,cluster==4)[,c(1,3)]

write.csv(c1,file = "data/ck4<500_Evaluation/c1.csv",row.names=FALSE,quote = FALSE)
write.csv(c2,file = "data/ck4<500_Evaluation/c2.csv",row.names=FALSE,quote = FALSE)
write.csv(c3,file = "data/ck4<500_Evaluation/c3.csv",row.names=FALSE,quote = FALSE)
write.csv(c4,file = "data/ck4<500_Evaluation/c4.csv",row.names=FALSE,quote = FALSE)



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
write.csv2(df_summary,file = "data/ck4<500/summary.csv",row.names=FALSE)



# Boxplot of tokens
ggplot(data=df, aes(x=as.factor(cluster), y=tokens_i, fill=corpus_id)) +
  geom_boxplot()+
  scale_x_discrete(name="group_c")+
  labs(title = "Tokens distribution")

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

