# Scala - Hive: Real time news analyzer

## Project Description

This project aims to search the latest information on TV shows, including release times, seasons, episodes, and cast. 

## Technologies Used

* Hive
* Scala
* HDFS
* Hadoop

## Features

List of features ready and TODOs for future development
* Lookup TV Shows with keywords
* Store searches within Hive tables

To-do list:
* Include option to save JSON files to HDFS
* Add query table cleanup for temporary Hive tables

## Getting Started
   
git clone https://github.com/n0hb0dy/Realt-Time-News-Analyzer.git
Tested for HortonWorks 2.6.5 VM with user maria_dev 

> scp -P 2222 .\newp1_2.11-v1SNAP.jar maria_dev@sandbox-hdp.hortonworks.com:/home/maria_dev/hive/jar
> To copy to Linux VM
> spark-submit ./hive/jar/newp1_2.11-v1SNAP.jar --class example.p1
> To run the program

## Usage

> Login with username and password
> Type a single character when prompted to select an option or to change menus
> Look up TV shows with key words to get their ID's
> View recently released shows
> View the latest seasons, episodes, and cast of a TV show
> Store the searches into a Hive Table
> Change usernames, passwords, and access levels
