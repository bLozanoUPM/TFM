df=read.csv("/Users/borjalozanoalvarez/Projects/Library/MetricPerformance/src/main/resources/size/20news_size.csv")

# Remove outliers in terms of size (some documents are way to big)
outliers <- boxplot(df$size_i, plot=FALSE)$out
df <- df[-which(df$size_i %in% outliers),]
