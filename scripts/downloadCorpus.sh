wget --save-cookies cookies.txt 'https://docs.google.com/uc?export=download&id=13hc1-FBRyTk7bwI48mYUB6LbJHQgTTCb' -O- \
     | sed -rn 's/.*confirm=([0-9A-Za-z_]+).*/\1/p' > confirm.txt

wget --load-cookies cookies.txt -O  corpora.zip\
     'https://docs.google.com/uc?export=download&id=13hc1-FBRyTk7bwI48mYUB6LbJHQgTTCb&confirm='$(<confirm.txt)

unzip corpora.zip
rm corpora.zip cookies.txt confirm.txt
