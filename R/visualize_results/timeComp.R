eval<-read.csv("./time_comp_10000_1.csv")


eval$docs_i <- as.factor(eval$docs_i )
eval$topics_i <- as.factor(eval$topics_i )

ggplot(subset(eval, docs_i=="10000"),aes(x=topics_i,y=time_ms/1000,color=metric_id, group=metric_id))+
  geom_point(size=3)+geom_line()+color4+xlab("number of topics")+ylab("time in seconds")



+title(xlab = "seconds", ylab = "number of topics")ggplot(subset(eval, docs_i==1000),aes(x=topics_i,y=log(time_ms),color=metric_id, linetype=docs_i, group=interaction(metric_id,docs_i)))+
  geom_point()+geom_line()+color4

