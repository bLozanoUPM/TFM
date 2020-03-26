Q1_d=read.csv("/Users/borjalozanoalvarez/Projects/Library/MetricPerformance/src/main/resources/Q1.csv")
Q2_d=read.csv("/Users/borjalozanoalvarez/Projects/Library/MetricPerformance/src/main/resources/Q2.csv")
Q3_d=read.csv("/Users/borjalozanoalvarez/Projects/Library/MetricPerformance/src/main/resources/Q3.csv")
Q4_d=read.csv("/Users/borjalozanoalvarez/Projects/Library/MetricPerformance/src/main/resources/Q4.csv")

Q1_t=read.csv("/Users/borjalozanoalvarez/Projects/Library/MetricPerformance/src/main/resources/Q1_train.csv")
Q2_t=read.csv("/Users/borjalozanoalvarez/Projects/Library/MetricPerformance/src/main/resources/Q2_train.csv")
Q3_t=read.csv("/Users/borjalozanoalvarez/Projects/Library/MetricPerformance/src/main/resources/Q3_train.csv")
Q4_t=read.csv("/Users/borjalozanoalvarez/Projects/Library/MetricPerformance/src/main/resources/Q4_train.csv")

par(mfrow=c(2,2))

# plot(Q1_d$size_i, xlab = "documents", ylab = "total-size_i",main="Q1")
# plot(Q2_d$size_i, xlab = "documents", ylab = "total-size_i",main="Q2")
# plot(Q3_d$size_i, xlab = "documents", ylab = "total-size_i",main="Q3")
# plot(Q4_d$size_i, xlab = "documents", ylab = "total-size_i",main="Q4")


boxplot(Q1_d$size_i, Q1_t$size_i, horizontal = TRUE,main="Q1",xlab="total-size_i")
boxplot(Q2_d$size_i, Q2_t$size_i, horizontal = TRUE,main="Q2",xlab="total-size_i")
boxplot(Q3_d$size_i, Q3_t$size_i, horizontal = TRUE,main="Q3",xlab="total-size_i")
boxplot(Q4_d$size_i, Q4_t$size_i, horizontal = TRUE,main="Q4",xlab="total-size_i")