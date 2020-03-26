# Ploting results

library(ggplot2)      # Plot 
library(ggpubr)
library(viridis)      # Color
library(RColorBrewer) # Color


#Figure
point=geom_point(alpha = 0.8,
           size = 4)

#Scale
yscale <- ylim(0, 1)

#Color
color1=scale_color_brewer(palette = "RdYlBu")
color2=scale_color_brewer(palette = "Dark2")
color3=scale_color_viridis(discrete = TRUE, option = "C")

color = color1

#Precision
precision <- ggplot(data=eval,mapping = aes(x = ptmModel_id,
                                 y = precision_d,
                                 color = testSet_id,
                                 group = testSet_id))+point+geom_line()+color+yscale

#Recall
recall <- ggplot(data=eval,mapping = aes(x = ptmModel_id,
                                 y = recall_d,
                                 color = testSet_id,
                                 group = testSet_id))+point+geom_line()+color+yscale

#MAP
MAP <- ggplot(data=eval,mapping = aes(x = ptmModel_id,
                                 y = MAP_d,
                                 color = testSet_id,
                                 group = testSet_id))+point+geom_line()+color+yscale
#FMeasure
fMeasure <- ggplot(data=eval,mapping = aes(x = ptmModel_id,
                                 y = fMeasure_d,
                                 color = testSet_id,
                                 group = testSet_id))+point+geom_line()+color+yscale

#P@
pAt1 <-ggplot(data=eval,mapping = aes(x = testSet_id,
                                  y = P.1_d,
                                  color = ptmModel_id,
                                  group = ptmModel_id))+point+geom_line()+color+yscale


pAt3 <-ggplot(data=eval,mapping = aes(x = testSet_id,
                                      y = P.3_d,
                                      color = ptmModel_id,
                                      group = ptmModel_id))+point+geom_line()+color+yscale

pAt5 <-ggplot(data=eval,mapping = aes(x = testSet_id,
                                      y = P.5_d,
                                      color = ptmModel_id,
                                      group = ptmModel_id))+point+geom_line()+color+yscale

pAt10 <-ggplot(data=eval,mapping = aes(x = testSet_id,
                                       y = P.10_d,
                                       color = ptmModel_id,
                                       group = ptmModel_id))+point+geom_line()+color+yscale

pAt15 <-ggplot(data=eval,mapping = aes(x = testSet_id,
                                       y = P.15_d,
                                       color = ptmModel_id,
                                       group = ptmModel_id))+point+geom_line()+color+yscale


#Figures
figure <-ggarrange(pAt1, pAt3, pAt5, pAt10,
                    labels = c("P@1", "P@3", "P@5", "P@10"),
                    font.label = list(size = 16),
                    ncol = 2, nrow = 2, common.legend=TRUE)

annotate_figure(figure,
                top = text_grob("ck4<500_500", color = "red", face = "bold", size = 14))

figure <- ggarrange(pAt3, pAt5, pAt15, MAP,
                    labels = c("P@3", "P@5", "P@15", "MAP"),
                    font.label = list(size = 16),
                    ncol = 2, nrow = 2, common.legend=TRUE)
print(figure)


figure <- ggarrange(precision, recall, MAP, fMeasure,
                    labels = c("P", "R", "MAP", "FM"),
                    font.label = list(size = 16),
                    ncol = 2, nrow = 2, common.legend=TRUE)
print(figure)


# figure <- annotate_figure(figure,
#                 top = text_grob("Jensen Shanon Divergence", color = "red", face = "bold", size = 14),
#                 bottom = text_grob("Threshold: \n0.5", color = "blue",
#                                    hjust = 1, x = 1, face = "italic", size = 10))
# print(figure)
