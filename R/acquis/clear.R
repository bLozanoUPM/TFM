clear <- function(lang){
  path<-concat("/Users/borjalozanoalvarez/Projects/Library/MetricPerformance/src/main/resources/size/acquis_",lang,"_size.csv")
  print(path)
  df=read.csv(path)
  
  # Define a min size and ntokens (dgt corpus is not clean so the lower bound will be defined by jrc)
  jrc_minToken = range(subset(df, corpus_id=="jrc")$tokens_i, na.rm = TRUE)[1]
  
  df <- df[df$tokens_i>=jrc_minToken,]
  
  # Remove outliers in terms of size (some documents are way to big)
  outliers <- boxplot(df$size_i, plot=FALSE)$out
  df <- df[-which(df$size_i %in% outliers),]
  
  # Some entries could not not be tokenized. 
  df <- na.omit(df)
  
  return(df)
}

concat <- function( ..., sep="" ) paste( ..., sep = sep )

