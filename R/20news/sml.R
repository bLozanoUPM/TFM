library(ggplot2)
library(ggpubr)
library(viridis)      # Color
library(RColorBrewer) # Color

source("R/20news/clear.R")

#Color
color1=scale_color_brewer(palette = "RdYlBu")
color2=scale_color_brewer(palette = "Dark2")
color3=scale_color_viridis(discrete = TRUE, option = "C")

color = color3

# Sort data by tokens
df <- df[order(df$tokens_i),]

# Add index as variable
row.names(df) <- NULL
df$index <- as.numeric(row.names(df))

# Divide in three equal sets
k=3
df$cluster <- as.numeric(cut(df$index, k))

small <- subset(df,cluster==1)[,c(1,3)]
medium <- subset(df,cluster==2)[,c(1,3)]
large <- subset(df,cluster==3)[,c(1,3)]

write.csv(small,file = "data/20News/sml_Evaluation/small.csv",row.names=FALSE,quote = FALSE)
write.csv(medium,file = "data/20News/sml_Evaluation/medium.csv",row.names=FALSE,quote = FALSE)
write.csv(large,file = "data/20News/sml_Evaluation/large.csv",row.names=FALSE,quote = FALSE)

df_summary<-setNames(
  data.frame(matrix(ncol = 8, nrow = 0)),
  c("Set", "Corpus", "NDocs", "Median", "Mean", "Variance", "Min", "Max"))

library(tibble)
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
write.csv2(df_summary,file = "data/20News/sml_Evaluation/summary.csv",row.names=FALSE)



# Boxplot of tokens
ggplot(data=df, aes(x=as.factor(cluster), y=tokens_i, fill=as.factor(cluster))) +
  geom_boxplot()+
  scale_x_discrete(name="group_c")+
  labs(title = "Tokens distribution in sets with equal number of documents")

# Violin of tokens
ggplot(data=df, aes(x=as.factor(cluster), y=tokens_i, fill=as.factor(cluster))) +
  geom_violin()+
  color1

# Boxplot+Jitter of tokens
ggplot(data=df, aes(x=as.factor(cluster), y=tokens_i, fill=as.factor(cluster))) +
  geom_jitter(size=0.1)+
  geom_boxplot(alpha=0.8)+
  color1

# Boxplot+Jitter of size
ggplot(data=df, aes(x=as.factor(cluster), y=size_i, fill=as.factor(cluster))) +
  geom_jitter(size=0.1)+
  geom_boxplot(alpha=0.8)


