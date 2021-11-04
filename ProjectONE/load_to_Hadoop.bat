@ECHO off
START cmd /k "scp -P 2222 C:\Users\98jjm\OneDrive\Documents\newp1\target\scala-2.11\newp1_2.11-v1SNAP.jar maria_dev@sandbox-hdp.hortonworks.com:/home/maria_dev/hive/jar"
PAUSE