source("acquis/clear.R")

es<-clear("es")
nrow(subset(es,corpus_id=="dgt_es"))
nrow(subset(es,corpus_id=="jrc_es"))
nrow(es)

median(subset(es,corpus_id=="dgt_es")$tokens_i)
median(subset(es,corpus_id=="jrc_es")$tokens_i)
median(es$tokens_i)

mean(subset(es,corpus_id=="dgt_es")$tokens_i)
mean(subset(es,corpus_id=="jrc_es")$tokens_i)
mean(es$tokens_i)

var(subset(es,corpus_id=="dgt_es")$tokens_i)
var(subset(es,corpus_id=="jrc_es")$tokens_i)
var(es$tokens_i)

min(subset(es,corpus_id=="dgt_es")$tokens_i)
min(subset(es,corpus_id=="jrc_es")$tokens_i)
min(es$tokens_i)

max(subset(es,corpus_id=="dgt_es")$tokens_i)
max(subset(es,corpus_id=="jrc_es")$tokens_i)
max(es$tokens_i)



en<-clear("en")
nrow(subset(en,corpus_id=="dgt_en"))
nrow(subset(en,corpus_id=="jrc_en"))
nrow(en)

median(subset(en,corpus_id=="dgt_en")$tokens_i)
median(subset(en,corpus_id=="jrc_en")$tokens_i)
median(en$tokens_i)

mean(subset(en,corpus_id=="dgt_en")$tokens_i)
mean(subset(en,corpus_id=="jrc_en")$tokens_i)
mean(en$tokens_i)

var(subset(en,corpus_id=="dgt_en")$tokens_i)
var(subset(en,corpus_id=="jrc_en")$tokens_i)
var(en$tokens_i)

min(subset(en,corpus_id=="dgt_en")$tokens_i)
min(subset(en,corpus_id=="jrc_en")$tokens_i)
min(en$tokens_i)

max(subset(en,corpus_id=="dgt_en")$tokens_i)
max(subset(en,corpus_id=="jrc_en")$tokens_i)
max(en$tokens_i)
