L1_distance <- function(p,q){
  if (length(p)!=length(q)) return(-1)
  d=sum(abs(p-q))/2
  if(d>1)d=1
  return(d)
}

L2_distance <- function(p,q){
  if (length(p)!=length(q)) return(-1)
  d=sqrt(sum(p^2 + q^2))
  if(d>sqrt(2)) d=sqrt(2)
  return(d/sqrt(2))
}

KL_distance <- function(p,q){
  if (length(p)!=length(q)) return(-1)
  d=sum(p*(log2(p)-log2(q)))
  return(d)
}

JSD_distance <- function(p,q){
  if (length(p)!=length(q)) return(-1)
  m=(p+q)/2
  d=(KL_distance(p,m)+KL_distance(q,m))/2
  return(d)
}

HE_distance <- function(p,q){
  if (length(p)!=length(q)) return(-1)
  d=sum((sqrt(p)-sqrt(q))^2)
  return(d/sqrt(2))
}