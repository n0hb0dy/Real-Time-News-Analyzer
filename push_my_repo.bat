@ECHO off
cd "C:\Users\98jjm\OneDrive\Documents\OctBatch"
git add .
git commit -m "`%date% %time%`"
git push -u origin main
PAUSE