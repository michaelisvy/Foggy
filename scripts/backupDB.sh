#!/bin/sh

DATESTAMP=`date +"%Y-%m-%d"`
mysqldump -u root beijingair > ~/backupdb/foggybeijing.$DATESTAMP.sql
mysqldump -u root wp_foggy > ~/backupdb/wp_foggy.$DATESTAMP.sql