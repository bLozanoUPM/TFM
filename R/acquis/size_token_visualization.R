library(ggplot2)

source("R/acquis/clear.R")
lang = "es"
path = concat("data/acquis/",lang,"/")
df<-clear(lang)

ggplot(df,aes(y=tokens_i,x=size_i))+
  geom_point(alpha=0.7, size=0.4, aes(color=df$corpus_id))+
  geom_smooth(method='lm', color="black") +
  labs(title= "Relation between size in number of characters and number of tokens for each document in the corpus",
       colour = "corpus_id",
       subtitle= concat("acquis_",lang))
ggsave(concat(path,"scatter_size-tokens.png"))

ggplot(df, aes(x=size_i, fill=corpus_id)) + 
  geom_density(alpha=0.7)+
  labs(title="Density of the size in each corpus",
       subtitle= concat("acquis_",lang))
ggsave(concat(path,"dens_size.png"))

ggplot(df, aes(x=tokens_i, fill=corpus_id)) + 
  geom_density(alpha=0.7)+
  labs(title="Density of the tokens in each corpus",
       subtitle= concat("acquis_",lang))
ggsave(concat(path,"dens_tokens.png"))

# ggplot(df, aes(x=tokens_i,fill=corpus_id)) + 
#   geom_histogram(binwidth = 50, color = "black",  boundary = TRUE) +
#   geom_hline(yintercept = 5000, linetype="dashed") +
#   geom_hline(yintercept = 1000, linetype="dashed") +
#   labs(title="Number of documents in each range (bin=50)")

ggplot(df, aes(x=tokens_i,fill=corpus_id)) + 
  geom_histogram(binwidth = 100, color = "black",  boundary = TRUE, alpha=0.7) +
  labs(title="Number of documents in each range (bin=100)",
       subtitle= concat("acquis_",lang))
ggsave(concat(path,"hist_100.png"))

# ggplot(df, aes(x=tokens_i,fill=corpus_id)) + 
#   geom_histogram(binwidth = 150, color = "black",  boundary = TRUE) +
#   geom_hline(yintercept = 5000, linetype="dashed") +
#   geom_hline(yintercept = 1000, linetype="dashed") +
#   labs(title="Number of documents in each range (bin=150)")
# 
# 
# ggplot(df, aes(x=tokens_i,fill=corpus_id)) + 
#   geom_histogram(binwidth = 200, color = "black",  boundary = TRUE) +
#   geom_hline(yintercept = 5000, linetype="dashed") +
#   geom_hline(yintercept = 1000, linetype="dashed") +
#   labs(title="Number of documents in each range (bin=200)")

ggplot(df,aes(x=corpus_id, y=size_i, fill=corpus_id))+
  geom_violin(alpha=0.4, fill ="black")+
  geom_boxplot()+
  labs(title="Distribution of size by corpus",
       subtitle= concat("acquis_",lang))
ggsave(concat(path,"box_size-corpus.png"))



ggplot(df,aes(x=corpus_id, y=tokens_i, fill=corpus_id))+
  geom_violin(alpha=0.4, fill ="black")+
  geom_boxplot()+
  labs(title="Distribution of size by corpus",
       subtitle= concat("acquis_",lang))
ggsave(concat(path,"box_tokens-corpus.png"))


ggplot(df,aes(x="",y=tokens_i))+
  geom_violin(alpha=0.4, fill ="black")+
  geom_boxplot(fill="#FFFFEA")+
  labs(title="Distribution of tokens by corpus")+
  xlab("corpus")




