# Ploting results

library(ggplot2)      # Plot 
library(ggpubr)
library(viridis)      # Color
library(RColorBrewer) # Color


jrc_en=read.csv("/Users/borjalozanoalvarez/Projects/Library/MetricPerformance/src/main/resources/evaluation/JCTL(0.2).csv")

#Figure
point=geom_point(alpha = 1,
                 size = 3)

#Scale
yscale <- ylim(0, 1)

#Color
color1=scale_color_brewer(palette = "RdYlBu")
colr2=scale_color_viridis(discrete = TRUE, option = "C")

color = color1

#Precision
precision <- ggplot(data=jrc_en,mapping = aes(x = ptmModel_id,
                                              y = precision_d,
                                              color = testSet_id,
                                              group = testSet_id))+point+geom_line()+color+yscale

#Recall
recall <- ggplot(data=jrc_en,mapping = aes(x = ptmModel_id,
                                           y = recall_d,
                                           color = testSet_id,
                                           group = testSet_id))+point+geom_line()+color+yscale

#MAP
MAP <- ggplot(data=jrc_en,mapping = aes(x = ptmModel_id,
                                        y = MAP_d,
                                        color = testSet_id,
                                        group = testSet_id))+point+geom_line()+color+yscale
#FMeasure
fMeasure <- ggplot(data=jrc_en,mapping = aes(x = ptmModel_id,
                                             y = fMeasure_d,
                                             color = testSet_id,
                                             group = testSet_id))+point+geom_line()+color+yscale



figure <- ggarrange(precision, recall, MAP, fMeasure,
                    labels = c("P", "R", "MAP", "FM"),
                    font.label = list(size = 16),
                    ncol = 2, nrow = 2, common.legend=TRUE)
figure <- annotate_figure(figure,
                top = text_grob("Jaccard Index Topic Levels", color = "red", face = "bold", size = 14),
                bottom = text_grob("Threshold: \n0.2", color = "blue",
                                   hjust = 1, x = 1, face = "italic", size = 10))

                