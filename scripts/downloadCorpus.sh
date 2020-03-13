wget --save-cookies cookies.txt 'https://docs.google.com/uc?export=download&id=1g6LotttRN89FicpwlWdJCVjAV5JzTnXm' -O- \
     | sed -rn 's/.*confirm=([0-9A-Za-z_]+).*/\1/p' > confirm.txt

wget --load-cookies cookies.txt -O  corpora.zip\
     'https://docs.google.com/uc?export=download&id=1g6LotttRN89FicpwlWdJCVjAV5JzTnXm&confirm='$(<confirm.txt)

unzip corpora.zip
rm corpora.zip
