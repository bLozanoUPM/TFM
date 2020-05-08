library(ggplot2)      # Plot 
library(ggpubr)
library(viridis)      # Color
library(RColorBrewer) # Color
library("ggsci")
source("loadResults.R")


#Figure
point=geom_point(size = 4,
                 stroke = 1.3)

#Scale
yscale <- ylim(0,1)
xscale <- xlim(0,1)

#Color
color1=scale_color_brewer(palette = "RdYlBu")
color2=scale_color_brewer(palette = "Dark2")
color3=scale_color_viridis(discrete = TRUE, option = "C")
color4=scale_color_jco()

color = color4


#fill
fill4=scale_fill_jco()

fill=fill4

#Data
lang="en"
eval<-loadResults(lang,"sml3")
# path = concat("../src/main/resources/acquis/",lang,"/")


#Compare metrics
compare_metrics <- function(eval) {
  pAt1 <-ggplot(data=eval,mapping = aes(x = ptm_id,
                                        y = P.1_d,
                                        fill = metric_id,
                                        group=interaction(ptm_id, metric_id)))+
    geom_boxplot()+
    color+
    yscale
  
  
  pAt3 <-ggplot(data=eval,mapping = aes(x = ptm_id,
                                        y = P.3_d,
                                        fill = metric_id,
                                        group=interaction(ptm_id, metric_id)))+
    geom_boxplot()+
    color+
    yscale
  
  pAt5 <-ggplot(data=eval,mapping = aes(x = ptm_id,
                                        y = P.5_d,
                                        fill = metric_id,
                                        group=interaction(ptm_id, metric_id)))+
    geom_boxplot()+
    color+
    yscale
  
  pAt10 <-ggplot(data=eval,mapping = aes(x = ptm_id,
                                         y = P.10_d,
                                         fill = metric_id,
                                         group=interaction(ptm_id, metric_id)))+
    geom_boxplot()+
    color+
    yscale
  
  figure<-ggarrange(pAt1, pAt3, pAt5, pAt10,
                    labels = c("P@1", "P@3", "P@5", "P@10"),
                    font.label = list(size = 16),
                    ncol = 2, nrow = 2, common.legend=TRUE)
  return(figure)
}
for (split in splits) {
  figure<-compare_metrics(loadResults("en",split))
  show(figure)
  # ggsave()
}



plots <- function(eval) {
  pAt1 <-ggplot(data=eval,mapping = aes(x = test_id,
                                        y = P.1_d,
                                        color = ptm_id,
                                        group=interaction(topics_i, ptm_id),
                                        shape=topics_i))+
    point+geom_line(aes(linetype=topics_i))+
    color+
    yscale+
    scale_shape_manual(values = c(1,2,0,5))


  pAt3 <-ggplot(data=eval,mapping = aes(x = test_id,
                                        y = P.3_d,
                                        color = ptm_id,
                                        group=interaction(topics_i, ptm_id),
                                        shape=topics_i))+
    point+geom_line(aes(linetype=topics_i))+
    color+
    yscale+
    scale_shape_manual(values = c(1,2,0,5))

  pAt5 <-ggplot(data=eval,mapping = aes(x = test_id,
                                        y = P.5_d,
                                        color = ptm_id,
                                        group=interaction(topics_i, ptm_id),
                                        shape=topics_i))+
    point+geom_line(aes(linetype=topics_i))+
    color+
    yscale+
    scale_shape_manual(values = c(1,2,0,5))

  pAt10 <-ggplot(data=eval,mapping = aes(x = test_id,
                                         y = P.10_d,
                                         color = ptm_id,
                                         group=interaction(topics_i, ptm_id),
                                         shape=topics_i))+
    point+geom_line(aes(linetype=topics_i))+
    color+
    yscale+
    scale_shape_manual(values = c(1,2,0,5))

  

  figure<-ggarrange(pAt1, pAt3, pAt5, pAt10,
                     labels = c("P@1", "P@3", "P@5", "P@10"),
                     font.label = list(size = 16),
                     ncol = 2, nrow = 2, common.legend=TRUE)
  return(figure)
}

for (metric in levels(eval$metric_id)) {
  assign(metric,plots(loadResults_metric("en","sml3",metric)))
}



  