library(ggplot2)
library("ggsci")

source("R/20news/clear.R")

#Color
colors = c("#B2182B","#D6604D","#F4A582","#FDDBC7","#D1E5F0","#92C5DE","#4393C3","#2166AC")

color ="papayawhip"

ggplot(df,aes(y=tokens_i,x=size_i))+
  geom_point(alpha=0.7, size=0.4)+
  geom_smooth(method='lm', color="red")

ggplot(df, aes(x=size_i)) + 
  geom_density(alpha=0.7, fill=color)+
  labs(title="Density of the size in each corpus")

ggplot(df, aes(x=tokens_i)) + 
  geom_density(alpha=0.7, fill=color)+
  labs(title="Density of the tokens in each corpus")

ggplot(df, aes(x=tokens_i)) + 
  geom_histogram(binwidth = 50, color = "black", fill = color,  boundary = TRUE) +
  geom_hline(yintercept = 5000, linetype="dashed") +
  geom_hline(yintercept = 1000, linetype="dashed") +
  labs(title="Number of documents in each range (bin=50)")

ggplot(df, aes(x=tokens_i)) + 
  geom_histogram(binwidth = 100, color = "black", , fill = color, boundary = TRUE) +
  geom_hline(yintercept = 5000, linetype="dashed") +
  geom_hline(yintercept = 1000, linetype="dashed") +
  labs(title="Number of documents in each range (bin=100)")

ggplot(df, aes(x=tokens_i)) + 
  geom_histogram(binwidth = 150, color = "black", , fill = color, boundary = TRUE) +
  geom_hline(yintercept = 5000, linetype="dashed") +
  geom_hline(yintercept = 1000, linetype="dashed") +
  labs(title="Number of documents in each range (bin=150)")


ggplot(df, aes(x=tokens_i,fill=color)) + 
  geom_histogram(binwidth = 200, color = "black", , fill = color, boundary = TRUE) +
  geom_hline(yintercept = 5000, linetype="dashed") +
  geom_hline(yintercept = 1000, linetype="dashed") +
  labs(title="Number of documents in each range (bin=200)")

ggplot(df,aes(x="", y=size_i))+
  geom_violin(alpha=0.4, fill ="black")+
  geom_boxplot(fill=color)+
  labs(title="Distribution of size by corpus")


ggplot(df,aes(x="", y=tokens_i))+
  geom_violin(alpha=0.4, fill ="black")+
  geom_boxplot(fill=color)+
  labs(title="Distribution of tokens by corpus")

ggplot(df,aes(x="",y=tokens_i))+
  geom_violin(alpha=0.4, fill ="black")+
  geom_boxplot(fill=color)+
  labs(title="Distribution of tokens by corpus")+
  xlab("corpus")




