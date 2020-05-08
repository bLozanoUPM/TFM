library(ggplot2)      # Plot 
library(ggpubr)
library(viridis)      # Color
library(RColorBrewer) # Color
library("ggsci")
source("visualize_results/loadResults.R")


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

color = color2


#fill
fill=scale_fill_jco()


  