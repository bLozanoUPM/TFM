source("visualize_results/commons.R")
source("visualize_results/metrics.R")
source("visualize_results/eval_results.R")
source("visualize_results/diffResults.R")


es<-plotAllResults("es","sml")
es$language <- "spanish"
en<-plotAllResults("en","sml")
en$language <- "english"

df<-rbind(en,es)

pAt1<-ggplot(df, aes(x=diff_Median,y=P.1_d, color=language))+
  geom_point(alpha=0.4, size=1)+geom_smooth()+
  color+yscale+
  ggtitle("P@1", subtitle = concat("EN correlation = ",sprintf("%.4f",cor(en$diff_Median,en$P.1_d)),"\n","ES correlation = ",sprintf("%.4f",cor(es$diff_Median,es$P.1_d))))+
  theme(plot.title = element_text(size = title_size, face = "bold"))+
  xlab('')+
  ylab('')

pAt3<-ggplot(df, aes(x=diff_Median,y=P.3_d,color=language))+
  geom_point(alpha=0.4, size=1)+geom_smooth()+
  color+yscale+
  ggtitle("P@3", subtitle = concat("EN correlation = ",sprintf("%.4f",cor(en$diff_Median,en$P.3_d)),"\n","ES correlation = ",sprintf("%.4f",cor(es$diff_Median,es$P.3_d))))+
  theme(plot.title = element_text(size = title_size, face = "bold"))+
  xlab('')+
  ylab('')

pAt5<-ggplot(df, aes(x=diff_Median,y=P.5_d,color=language))+
  geom_point(alpha=0.4, size=1)+geom_smooth()+
  color+yscale+
  ggtitle("P@5", subtitle = concat("EN correlation = ",sprintf("%.4f",cor(en$diff_Median,en$P.5_d)),"\n","ES correlation = ",sprintf("%.4f",cor(es$diff_Median,es$P.5_d))))+
  theme(plot.title = element_text(size = title_size, face = "bold"))+
  xlab('')+
  ylab('')


pAt10<-ggplot(df, aes(x=diff_Median,y=P.10_d,color=language))+
  geom_point(alpha=0.4, size=1)+geom_smooth()+
  color+yscale+
  ggtitle("P@10", subtitle = concat("EN correlation = ",sprintf("%.4f",cor(en$diff_Median,en$P.10_d)),"\n","ES correlation = ",sprintf("%.4f",cor(es$diff_Median,es$P.10_d))))+
  theme(plot.title = element_text(size = title_size, face = "bold"))+
  xlab('')+
  ylab('')

figure<-ggarrange(pAt1, pAt3, pAt5, pAt10,
                  # labels = c("P@1", "P@3", "P@5", "P@10"),
                  # font.label = list(size = 16),
                  ncol = 2, nrow = 2, common.legend=TRUE)

figure

