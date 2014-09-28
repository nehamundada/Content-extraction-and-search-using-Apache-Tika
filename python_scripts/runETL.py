#!/usr/bin/python

import os
import hashlib
import bencode
import glob
import json


hashFile = open('/Volumes/SHRI/etlHash.txt', 'w')
logFile = open('/Volumes/SHRI/etlRun.txt', 'w')
perFileCount = 0

os.chdir("/Volumes/SHRI/Data/")
count = 0
perFileCount = 0
for filename in glob.glob("*.tsv"):
	print filename
	count = count + perFileCount
	perFileCount = 0
	os.system('rm /Volumes/SHRI/output/file.json')
	cmd = "tsvtojson -t /Volumes/SHRI/Data/"+filename+" -j /Volumes/SHRI/output/file.json -c /Users/shri/devel/cs572/sample/headers2.txt -o test -v -e /Users/shri/devel/cs572/sample/etllib/etl/encoding.txt"
	print cmd
	os.system(cmd)	

	f1 = open("/Volumes/SHRI/Data/"+filename)
	data = json.load(f1)

	for a in data['test']:			
		perFileCount = perFileCount + 1
		
		data_md5 = hashlib.md5(bencode.bencode(data['test'][a])).hexdigest()			
		data['test'][a]['hash'] = data_md5			
		
		hashFile.write(data_md5+'\n')
		newFileName = filename[:-4] + '_' + str(perFileCount) + '.json'
		print newFileName
		with open( newFileName, 'w', encoding='utf-8') as outfile:
			json.dump(data['test'][a], outfile)


	