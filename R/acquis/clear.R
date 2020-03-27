clear <- function(lang){
  df=read.csv("../src/main/resources/corpora/acquis_size.csv", sep = ";")
  
  if(lang=="en"){
    df=subset(df, corpus_id=="jrc_en" | corpus_id=="dgt_en")
    jrc=subset(df, corpus_id=="jrc_en")
  }
  else if (lang=="es"){
    df=subset(df, corpus_id=="jrc_es" | corpus_id=="dgt_es")
    jrc=subset(df, corpus_id=="jrc_es")
  }
  else {
    print("language not recognized")
    exit()
  }
  
  
  # Define a min size and ntokens (dgt corpus is not clean so the lower bound will be defined by jrc)
  jrc_minToken = min(jrc$tokens_i, na.rm = TRUE)
  
  df <- df[df$tokens_i>=jrc_minToken,]
  
  # Remove outliers in terms of size (some documents are way to big)
  outliers <- boxplot(df$size_i, plot=FALSE)$out
  df <- df[-which(df$size_i %in% outliers),]
  
  # Some entries could not not be tokenized. 
  df <- na.omit(df)
  
  return(df)
}

concat <- function( ..., sep="" ) paste( ..., sep = sep )

