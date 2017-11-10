
echo 'checking and pushing with comment ' $1

git add .
git commit -m '$1'
git push
