#!/usr/bin/python

import os
import hashlib
import bencode
import glob
import json
import threading
import sys
import time

timestamp1 = str(int(time.time()))
logFile = open('etlRun_'+timestamp1+'.txt', 'wb')
uniqFile = open('uniq_hash_file.txt', 'wb')
uniq_data = {}

makeJSON = False

headerFilePath = 'headers2.txt'
encodingFilePath = '/Users/shri/devel/cs572/sample/etllib/etl/encoding.txt'
dataFilesPath = '/Volumes/Storage/employment/'
outDataFilesPath = '/Users/shri/Desktop/'


perFileCount = 0

count = 0
fileCount = 0 

temp_json_file = 'file_'+ timestamp1 + '.json'


def process_json(filename, pathName, tarName):	

	perFileCount = 0	
	duplicates = 0
	f1 = open(filename)
	data = json.load(f1)

	for a in data['test']:			
		
		try:

			if 'Title' not in a:
				a['Title'] = ''
			if 'jobtype' not in a:
				a['jobtype'] = ''
			if 'company' not in a:
				a['company'] = ''
			if 'duration' not in a:
				a['duration'] = ''
			txt = a['Location'].strip().lower() + a['department'].strip().lower()  + a['Title'].lower() + a['jobtype'].lower() + a['company'].lower() + a['duration'].lower()
			hsh = hashlib.sha1(txt.encode('utf-8')).hexdigest()
			
			if hsh in uniq_data:				
				duplicates = duplicates + 1
			else:
				uniq_data[hsh] = 0
				uniqFile.write('\n'+str(hsh))
				perFileCount = perFileCount + 1

			if makeJSON == True:
				newFileName = pathName + '/' + str(a['id'])+'.json'
				f2 = open( newFileName, 'wb')
				a['hash'] = hsh
				json.dump(a, f2)
				f2.close()

		except:
			err = sys.exc_info()[0]
			print ('^'*30),str(err)
			logFile.write('\nError in writing json file in : ' + tarName )
			logFile.write('\n'+str(err))

	print ('*'*10),filename,'\tUniq: ',perFileCount,'\t Duplicates: ',duplicates


	os.system('rm ' + temp_json_file)

	if makeJSON == True:		
		os.system('tar -cjvf '+tarName+'.tar.bz2 '+pathName)
		os.system('rm -r '+pathName)

	logFile.write(tarName+'\tUniq: '+str(perFileCount)+'\tDup: '+str(duplicates)+'\n')


def run_cmd(cmd):
	os.system(cmd)
	






for filename in os.listdir(dataFilesPath):

	fileCount = fileCount + 1

	# if filename[:4] != '.tsv':
	# 	continue
	try:

		outPath = outDataFilesPath + filename[:-4]

		if makeJSON == True:
			os.system('mkdir ' + outPath)

		# logFile.write('\n'+filename)
		count = count + perFileCount
		perFileCount = 0
		
		cmd = "tsvtojson -t "+dataFilesPath+filename+" -j "+temp_json_file+" -c "+ headerFilePath +" -o test -v -e " + encodingFilePath
		
		# cmd = "sh command.sh "+dataFilesPath+filename+" file.json "+ headerFilePath +"  test  " + encodingFilePath
		
		print cmd
		
		t1 = threading.Thread(target=run_cmd, args=[cmd])
		t1.start()
		t1.join()
		
		t1 = threading.Thread(target=process_json, args=[temp_json_file, outPath, filename[:-4] ])
		t1.start()
		t1.join()

	except:
		err = sys.exc_info()[0]
		logFile.write('\nError in writing json file in : ' + tarName )
		logFile.write('\n'+str(err))
		print "Unexpected error:", sys.exc_info()[0]

	if fileCount == 4:
		break;

logFile.close()
uniqFile.close()

	

	

