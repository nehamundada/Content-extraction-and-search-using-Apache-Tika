#!/usr/bin/python

import csv
import glob
import os

contents = {}


os.chdir("/Volumes/SHRI/Data/")
for filename in glob.glob("*.tsv"):
    f1 = open(filename)

    rows = csv.reader(f1, delimiter='\t')
    index = 16
    
    count = 0
    for r in rows:
    	count = count + 1
    	# a = len(r)
    	# print filename,'\t',a
    	# if a != 20:
    	# 	print '*'*30	
    	# break
    	if len(r) < index:
    		print '\t'.join(r)
    		continue

    	if r[index] not in contents:
    		contents[r[index]] = 0

    	contents[r[index]] = contents[r[index]] + 1

    print filename, ' - ', count

for a in contents:
	print a,'\t',contents[a]



