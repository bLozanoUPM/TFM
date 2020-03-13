wget --save-cookies cookies.txt 'https://docs.google.com/uc?export=download&id=192ieQmiNR1v67_0UUYZInDJcs1GvHnL_' -O- \
     | sed -rn 's/.*confirm=([0-9A-Za-z_]+).*/\1/p' > confirm.txt

wget --load-cookies cookies.txt -O  acquis.zip\
     'https://docs.google.com/uc?export=download&id=192ieQmiNR1v67_0UUYZInDJcs1GvHnL_&confirm='$(<confirm.txt)


unzip acquis.zip
rm acquis.zip
