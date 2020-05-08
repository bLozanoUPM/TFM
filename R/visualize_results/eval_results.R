library(reshape2)

eval_box_test <- function(lang,split,metric) {
  eval<-loadResults_metric(lang,split,metric)
  
  plotlist<-list()
  
  i<-1
  for (test in levels(eval$test_id)) {
    aux<-subset(eval, test_id==test)
    aux<-melt(aux)
    
    plotlist[[i]]<-ggplot(aux,aes(x=variable,y=value,fill=ptm_id))+
      geom_boxplot()+yscale+labs(x='',y='')+
      stat_summary(fun=mean, geom="errorbar",aes(ymax = ..y.., ymin = ..y.., group=variable),
                   width = 1, linetype = "dashed", color="red")+
      # stat_summary(fun=median, geom="errorbar",aes(ymax = ..y.., ymin = ..y.., group=variable),
      #              width = 1, linetype = "dashed", color="green")+
      fill
    
    i<-i+1
  }
  
  return(ggarrange(plotlist = plotlist, ncol = 3, nrow = length(levels(eval$test_id))/3,common.legend = TRUE,labels = levels(eval$test_id)))
  
}

eval_point_test <- function(lang,split,metric) {
  title_size=20
  
  eval<-loadResults_metric(lang,split,metric)
  
  pAt1 <-ggplot(data=eval,mapping = aes(x = test_id,
                                       y = P.1_d,
                                       color = ptm_id,
                                       group=interaction(topics_i, ptm_id),
                                       shape=topics_i))+
    
    point+geom_line(aes(linetype=topics_i))+
    color+
    yscale+
    scale_shape_manual(values = c(1,2,0,5))+
    ggtitle("P@1")+
    theme(plot.title = element_text(size = title_size, face = "bold"))+
    xlab('')+
    ylab('')
  
  
  pAt3 <-ggplot(data=eval,mapping = aes(x = test_id,
                                       y = P.3_d,
                                       color = ptm_id,
                                       group=interaction(topics_i, ptm_id),
                                       shape=topics_i))+
    point+geom_line(aes(linetype=topics_i))+
    color+
    yscale+
    scale_shape_manual(values = c(1,2,0,5))+
    ggtitle("P@3")+
    theme(plot.title = element_text(size = title_size, face = "bold"))+
    xlab('')+
    ylab('')
  
  pAt5 <-ggplot(data=eval,mapping = aes(x = test_id,
                                       y = P.5_d,
                                       color = ptm_id,
                                       group=interaction(topics_i, ptm_id),
                                       shape=topics_i))+
    point+geom_line(aes(linetype=topics_i))+
    color+
    yscale+
    scale_shape_manual(values = c(1,2,0,5))+ggtitle("P@5")+
    theme(plot.title = element_text(size = title_size, face = "bold"))+
    xlab('')+
    ylab('')
  
  
  pAt10 <-ggplot(data=eval,mapping = aes(x = test_id,
                                        y = P.10_d,
                                        color = ptm_id,
                                        group=interaction(topics_i, ptm_id),
                                        shape=topics_i))+
    point+geom_line(aes(linetype=topics_i))+
    color+
    yscale+
    scale_shape_manual(values = c(1,2,0,5))+
    ggtitle("P@10")+
    theme(plot.title = element_text(size = title_size, face = "bold"))+
    xlab('')+
    ylab('')
  
  figure<-ggarrange(pAt1, pAt3, pAt5, pAt10,
                    ncol = 2, nrow = 2, common.legend=TRUE)
  return(figure)
}


eval_box_model <- function(lang,split,metric) {
  eval<-loadResults_metric(lang,split,metric)
  
  plotlist<-list()
  
  i<-1
  for (ptm in levels(eval$ptm_id)) {
    aux<-subset(eval, ptm_id==ptm)
    aux<-melt(aux)
    
    plotlist[[i]]<-ggplot(aux,aes(x=variable,y=value,fill=test_id))+
      geom_boxplot()+yscale+labs(x='',y='')+
      stat_summary(fun=mean, geom="errorbar",aes(ymax = ..y.., ymin = ..y.., group=variable),
                   width = 1, linetype = "dashed", color="red")+
      # stat_summary(fun=median, geom="errorbar",aes(ymax = ..y.., ymin = ..y.., group=variable),
      #              width = 1, linetype = "dashed", color="green")+
      fill
    
    i<-i+1
  }
  
  return(ggarrange(plotlist = plotlist, ncol = 3, nrow = length(levels(eval$ptm_id))/3,common.legend = TRUE,labels = levels(eval$ptm_id)))
  
}
