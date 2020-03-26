require(Ckmeans.1d.dp)

source("R/clear.R")

df <- df[order(df$tokens_i),]
# Add index as variable
row.names(df) <- NULL
df$index <- as.numeric(row.names(df))

k=3


# Optimal k 1D cluster


result <- Ckmeans.1d.dp(df$tokens_i,k)

print(result$size)

df$cluster <- result$cluster


# Cut data in k equal subsets

df$cluster <- as.numeric(cut(df$index, k))






