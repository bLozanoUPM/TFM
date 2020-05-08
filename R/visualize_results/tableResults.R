library(data.table)
library(dplyr)
library(formattable)
library(tidyr)
library("htmltools")
library("webshot")    

export_formattable <- function(f, file, width = "100%", height = NULL, 
                               background = "white", delay = 0.2){
  w <- as.htmlwidget(f, width = width, height = height)
  path <- html_print(w, background = background, viewer = NULL)
  url <- paste0("file:///", gsub("\\\\", "/", normalizePath(path)))
  webshot(url,
          file = file,
          selector = ".formattable_widget",
          delay = delay)
}

lang="en"
split="sml9"

pptable <- function(lang,split) {
  customGreen = "#F93943"
  
  customGreen0 = "#71CA97"
  
  customRed = "#ff7f7f"
  
  df<-loadResults(lang,split)
  df<-aggregate(df[,5:8],by = list(ptm_id=df$ptm_id), mean)
  # df<-melt(df)
  
  summary=subset(read.csv(concat("../src/main/resources/acquis/",lang,"/",split,"/R/summary.csv"), sep = ";"), Corpus=="merged")[,c(1,3:6)]
  summary$Mean <- as.double(gsub(",", ".", as.character(summary$Mean)))
  summary$Variance <- as.double(gsub(",", ".", as.character(summary$Variance)))
  summary$Median <- as.numeric(as.character(summary$Median))
  summary$Set <- factor(summary$Set,labels = levels(df$ptm_id))
  colnames(summary)[1]<-"ptm_id"
  df<-merge(df,summary)[,c(1,7,8,2,3,4,5,6,9)]
  
  
  figure<-formattable(df, align =c("l","c","c","c","c", "c", "c", "r", "r"), list(
    `Indicator Name` = formatter("span", style = ~ style(color = "grey",font.weight = "bold")), 
    `P.1_d`= color_tile(customGreen, customGreen0),
    `P.3_d`= color_tile(customGreen, customGreen0),
    `P.5_d`= color_tile(customGreen, customGreen0),
    `P.10_d`= color_tile(customGreen, customGreen0),
    `Variance` = color_bar(customRed),
    `NDocs` = color_bar("lightblue")
  ))
  return(figure)
}

for (split in splits) {
  show(pptable("en",split))
  # export_formattable(figure,concat("visualize_results/model_comp/",lang,"_",split,"_table",".png"))

}


