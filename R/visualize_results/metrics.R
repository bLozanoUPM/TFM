#Compare metrics
title_size = 20

compare_metrics <- function(lang){
  
  eval<-loadAllResults(lang)

  pAt1 <-ggplot(data=eval,mapping = aes(x = metric_id,
                                        y = P.1_d,
                                        fill = metric_id))+
    geom_boxplot()+
    fill+
    yscale+
    ggtitle("P@1")+
    theme(plot.title = element_text(size = title_size, face = "bold"))+
    xlab('')+
    ylab('')
  
  
  pAt3 <-ggplot(data=eval,mapping = aes(x = metric_id,
                                        y = P.3_d,
                                        fill = metric_id))+
    geom_boxplot()+
    fill+
    yscale+
    ggtitle("P@3")+
    theme(plot.title = element_text(size = title_size, face = "bold"))+
    xlab('')+
    ylab('')
  
  
  pAt5 <-ggplot(data=eval,mapping = aes(x = metric_id,
                                        y = P.5_d,
                                        fill = metric_id))+
    geom_boxplot()+
    fill+
    yscale+
    ggtitle("P@5")+
    theme(plot.title = element_text(size = title_size, face = "bold"))+
    xlab('')+
    ylab('')
  
  pAt10 <-ggplot(data=eval,mapping = aes(x = metric_id,
                                         y = P.10_d,
                                         fill = metric_id))+
    geom_boxplot()+
    fill+
    yscale+
    ggtitle("P@10")+
    theme(plot.title = element_text(size = title_size, face = "bold"))+
    xlab('')+
    ylab('')
  
  
  
  figure<-ggarrange(pAt1, pAt3, pAt5, pAt10,
                    # labels = c("P@1", "P@3", "P@5", "P@10"),
                    # font.label = list(size = 16),
                    ncol = 2, nrow = 2, common.legend=TRUE)
  
  return(figure)
}

compare_metrics_eval <- function(lang,split) {
  eval=loadResults(lang,split)
  pAt1 <-ggplot(data=eval,mapping = aes(x = ptm_id,
                                        y = P.1_d,
                                        fill = metric_id,
                                        group=interaction(ptm_id, metric_id)))+
    geom_boxplot()+
    fill+
    yscale+
    ggtitle("P@1")+
    theme(plot.title = element_text(size = title_size, face = "bold"))+
    xlab('')+
    ylab('')


  pAt3 <-ggplot(data=eval,mapping = aes(x = ptm_id,
                                        y = P.3_d,
                                        fill = metric_id,
                                        group=interaction(ptm_id, metric_id)))+
    geom_boxplot()+
    fill+
    yscale+
    ggtitle("P@3")+
    theme(plot.title = element_text(size = title_size, face = "bold"))+
    xlab('')+
    ylab('')

  pAt5 <-ggplot(data=eval,mapping = aes(x = ptm_id,
                                        y = P.5_d,
                                        fill = metric_id,
                                        group=interaction(ptm_id, metric_id)))+
    geom_boxplot()+
    fill+
    yscale+
    ggtitle("P@5")+
    theme(plot.title = element_text(size = title_size, face = "bold"))+
    xlab('')+
    ylab('')

  pAt10 <-ggplot(data=eval,mapping = aes(x = ptm_id,
                                         y = P.10_d,
                                         fill = metric_id,
                                         group=interaction(ptm_id, metric_id)))+
    geom_boxplot()+
    fill+
    yscale+
    ggtitle("P@10")+
    theme(plot.title = element_text(size = title_size, face = "bold"))+
    xlab('')+
    ylab('')

  figure<-ggarrange(pAt1, pAt3, pAt5, pAt10,
                    ncol = 2, nrow = 2, common.legend=TRUE)
  return(figure)
}

