library(MCMCpack)
library(ggplot2)
library(ggtern)
library(philentropy)
library(viridis)      # Color
library(RColorBrewer) # Color

source("R/distances.R")

color1=scale_fill_brewer(palette = "RdYlBu")
color2=scale_fill_brewer(palette = "Dark2")
color3=scale_color_viridis()
color4=scale_color_viridis(option = "C" )

color=color4

samples = 100000
draws <- rdirichlet(samples,c(1,1,1))
q2=c(0.9,0.05,0.05)
q1=c(1/3,1/3,1/3)
vector1 <- c()
vector2 <- c()
vector3 <- c()
vector1B <- c()
vector2B <- c()
vector3B <- c()

for (sample in 1:nrow(draws)) {
  draw <- draws[sample,]
  vector1[sample]<-JSD_distance(draw,q1)
  vector2[sample]<-HE_distance(draw,q1)
  vector3[sample]<-L1_distance(draw,q1)
  vector1B[sample]<-JSD_distance(draw,q2)
  vector2B[sample]<-HE_distance(draw,q2)
  vector3B[sample]<-L1_distance(draw,q2)
}

df_A <-as.data.frame(draws)
df_B <-as.data.frame(draws)
df_A$JSD <- vector1
df_A$HE<- vector2
df_A$L1<- vector3
df_B$JSD <- vector1B
df_B$HE<- vector2B
df_B$L1<- vector3B

# df[samples,]<-c(q,1,1,1)

# df[df$HE>1,]$HE=1
# df[df$JSD>1,]$JSD=1

JSD<-ggtern(df_A,aes(V1,V2,V3)) +
  geom_point(aes(color=JSD))+
  color

HE<-ggtern(df_A,aes(V1,V2,V3)) +
  geom_point(aes(color=HE))+
  color

L1<-ggtern(df_A,aes(V1,V2,V3)) + 
  geom_point(aes(color=L1))+
  color
# 
JSD<-JSD+geom_point(data =  subset(df_A,JSD>0.499 & JSD<0.501)
                    ,mapping = aes(V1,V2,V3))+
  geom_point(data=subset(df_A,JSD>0.249 & JSD<0.251),mapping = aes(V1,V2,V3))+
  geom_point(aes(q1[1],q1[2],q1[3]),color="#F1F572")


#
HE<-HE+geom_point(data =  subset(df_A,HE>0.499 & HE<0.501)
                  ,mapping = aes(V1,V2,V3))+
  geom_point(data=subset(df_A,HE>0.249 & HE<0.251),mapping = aes(V1,V2,V3))+
  geom_point(aes(q1[1],q1[2],q1[3]),color="#F1F572")

L1<-L1+geom_point(subset(df_A,L1>0.249 & L1<0.251),mapping = aes(V1,V2,V3))+
  geom_point(subset(df_A,L1>0.499 & L1<0.501),mapping = aes(V1,V2,V3))+
  geom_point(aes(q1[1],q1[2],q1[3]),color="#F1F572")

JSD_aux<-ggtern(df_B,aes(V1,V2,V3)) +
  geom_point(aes(color=JSD))+
  color

HE_aux<-ggtern(df_B,aes(V1,V2,V3)) +
  geom_point(aes(color=HE))+
  color

L1_aux<-ggtern(df_B,aes(V1,V2,V3)) + 
  geom_point(aes(color=L1))+
  color
# 
JSD_aux<-JSD_aux+geom_point(data =  subset(df_B,JSD>0.499 & JSD<0.501)
                            ,mapping = aes(V1,V2,V3))+
  geom_point(data=subset(df_B,JSD>0.249 & JSD<0.251),mapping = aes(V1,V2,V3))+
  geom_point(aes(q2[1],q2[2],q2[3]),color="#F1F572")


#
HE_aux<-HE_aux+geom_point(data =  subset(df_B,HE>0.499 & HE<0.501)
                          ,mapping = aes(V1,V2,V3))+
  geom_point(data=subset(df_B,HE>0.249 & HE<0.251),mapping = aes(V1,V2,V3))+
  geom_point(aes(q2[1],q2[2],q2[3]),color="#F1F572")

L1_aux<-L1_aux+geom_point(subset(df_B,L1>0.249 & L1<0.251),mapping = aes(V1,V2,V3))+
  geom_point(subset(df_B,L1>0.499 & L1<0.501),mapping = aes(V1,V2,V3))+
  geom_point(aes(q2[1],q2[2],q2[3]),color="#F1F572")


grid.arrange(JSD,HE,L1, JSD_aux, HE_aux,L1_aux, nrow=2)
ggsave("Figures/heatmap.jpg")
