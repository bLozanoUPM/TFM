library(ggplot2)      # Plot 
library(ggpubr)
library(viridis)      # Color
library(RColorBrewer) # Color


#Figure
point=geom_point(size = 4,
                 stroke = 1.3,
                 alpha = 0.9)

#Scale
yscale <- ylim(0, 1)
xscale <- xlim(0,1)

#Color
color1=scale_color_brewer(palette = "RdYlBu")
color2=scale_color_brewer(palette = "Dark2")
color3=scale_color_viridis(discrete = TRUE, option = "C")

color = color2




pAt1 <-ggplot(data=eval,mapping = aes(x = testSet_id,
                                      y = P.1_d,
                                      color = ptmModel_id,
                                      group=interaction(n_topics, ptmModel_id),
                                      shape=n_topics))+
  point+geom_line(aes(linetype=n_topics))+
  color+
  yscale+
  scale_shape_manual(values = c(1,2,0,5))
  

pAt3 <-ggplot(data=eval,mapping = aes(x = testSet_id,
                                      y = P.3_d,
                                      color = ptmModel_id,
                                      group=interaction(n_topics, ptmModel_id),
                                      shape=n_topics))+
  point+geom_line(aes(linetype=n_topics))+
  color+
  yscale+
  scale_shape_manual(values = c(1,2,0,5))

pAt5 <-ggplot(data=eval,mapping = aes(x = testSet_id,
                                      y = P.5_d,
                                      color = ptmModel_id,
                                      group=interaction(n_topics, ptmModel_id),
                                      shape=n_topics))+
  point+geom_line(aes(linetype=n_topics))+
  color+
  yscale+
  scale_shape_manual(values = c(1,2,0,5))

pAt10 <-ggplot(data=eval,mapping = aes(x = testSet_id,
                                       y = P.10_d,
                                       color = ptmModel_id,
                                       group=interaction(n_topics, ptmModel_id),
                                       shape=n_topics))+
  point+geom_line(aes(linetype=n_topics))+
  color+
  yscale+
  scale_shape_manual(values = c(1,2,0,5))

# pAt15 <-ggplot(data=eval,mapping = aes(x = testSet_id,
#                                        y = P.10_d,
#                                        color = ptmModel_id,
#                                        group=interaction(n_topics, ptmModel_id),
#                                        shape=n_topics))+
#   point+geom_line(aes(linetype=n_topics))+
#   color+
#   yscale+
#   scale_shape_manual(values = c(1,2,0,5))

figure <-ggarrange(pAt1, pAt3, pAt5, pAt10,
                   labels = c("P@1", "P@3", "P@5", "P@10"),
                   font.label = list(size = 16),
                   ncol = 2, nrow = 2, common.legend=TRUE)

annotate_figure(figure,top = text_grob("CK4 HE(0.5)", color = "black", face = "bold", size = 14))

contrast <- ggplot(data=eval, mapping = aes(x=P.1_d,
                                            y=P.3_d,
                                            color=testSet_id,
                                            shape=ptmModel_id
                                            ))+
  geom_point(aes(size=n_topics))+
  color2+
  xscale+
  yscale
annotate_figure(contrast,top = text_grob("SML JCTL(0.0).csv", color = "black", face = "bold", size = 14))

  