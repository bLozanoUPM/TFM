library(ggplot2)
library(ggpubr)

par(mfrow=c(2,2))

jrc=read.csv("/Users/borjalozanoalvarez/Projects/Library/MetricPerformance/src/main/resources/size/homogenized_size.csv")
jrc$index <- as.numeric(row.names(jrc))

Q1=jrc[jrc[,"q_i"]==1,]
Q1$index <- as.numeric(row.names(Q1))

Q2=jrc[jrc[,"q_i"]==2,]
Q2$index <- as.numeric(row.names(Q2))

Q3=jrc[jrc[,"q_i"]==3,]
Q3$index <- as.numeric(row.names(Q3))

Q4=jrc[jrc[,"q_i"]==4,]
Q4$index <- as.numeric(row.names(Q4))


box <- geom_boxplot(outlier.colour="black", outlier.shape=16,
             outlier.size=2, notch=FALSE)
scatter <- geom_point(alpha = .7,
                      size = 3)

figure1<-ggplot(jrc, aes(x=q_i, y = size_i,
                group = q_i)) +
  box+ coord_flip()

# Q1_plot<-ggplot(Q1,aes(y=size_i))+box
# 
# Q2_plot<-ggplot(Q2,aes(y=size_i))+box
# 
# Q3_plot<-ggplot(Q3,aes(y=size_i))+box
# 
# Q4_plot<-ggplot(Q4,aes(y=size_i))+box
# 
# 
# figure <- ggarrange(Q1_plot, Q2_plot, Q3_plot, Q4_plot,
#                     labels = c("Q1", "Q2", "Q3", "Q4"),
#                     font.label = list(size = 16),
#                     ncol = 2, nrow = 2, common.legend=TRUE)
# figure1 <- annotate_figure(figure,
#                            top = text_grob("Ordered size of all the quatriles", color = "red", face = "bold", size = 14),
#                            bottom = text_grob("Reduction: \n", color = "blue",
#                                               hjust = 1, x = 1, face = "italic", size = 10))

Q1_plot<-ggplot(Q1,aes(x=index,y=size_i))+scatter

Q2_plot<-ggplot(Q2,aes(x=index,y=size_i))+scatter

Q3_plot<-ggplot(Q3,aes(x=index,y=size_i))+scatter

Q4_plot<-ggplot(Q4,aes(x=index,y=size_i))+scatter


figure <- ggarrange(Q1_plot, Q2_plot, Q3_plot, Q4_plot,
                    labels = c("Q1", "Q2", "Q3", "Q4"),
                    font.label = list(size = 16),
                    ncol = 2, nrow = 2, common.legend=TRUE)
figure2 <- annotate_figure(figure,
                          top = text_grob("Ordered size of all the quatriles", color = "red", face = "bold", size = 14),
                          bottom = text_grob("Reduction: \n", color = "blue",
                                             hjust = 1, x = 1, face = "italic", size = 10))

print(figure1)
# print(figure2)
