import os

initial_port = 8081

lang = "es"
split = "ck"
k = 9

f = open("docker-compose.topics.yml", "w")

f.write("version: '3'\n")
f.write("services:\n")

for i in range(1,k+1):
    name=lang+"_"+split+str(k)+"_"+str(i)
    f.write("  "+name+":\n")
    f.write("  "*2+"image: blozanoalvarez/"+name+":1.0\n")
    f.write("  "*2+"ports:\n")
    f.write("  "*3+"- \""+str(initial_port+i-1)+":7777\"\n")


#    environment:
#      - "JAVA_OPTS=-Xms128m -Xmx128m -Des.enforce.bootstrap.checks=true"