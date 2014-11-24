#!/usr/bin/python

import os
import hashlib
import bencode
import glob
import json
import threading
import sys
import time

'''
INIT PARAMS
Set the below params befor running the script

param : headerFilePath : The header file for tsv parsing
param : encodingFilePath : The encoding file. We use ['utf-8', 'us-ascii', 'ISO-8]
param : dataFilesPath : The path to TSV data files
param : outDataFilesPath : The location to produce json files
param : makeJSON : True / False whether to create json files or not
param : enableDedup : True/False if deduplication is enabled or not
'''
 
makeJSON = True

enableDedup = True

headerFilePath = 'headers.txt'
encodingFilePath = '/Users/shri/devel/cs572/sample/etllib/etl/encoding.txt'
dataFilesPath = '/Volumes/SHRI/Data/'
outDataFilesPath = '/Users/shri/Desktop/'

timestamp1 = str(int(time.time()))
logFile = open('etlRun_'+timestamp1+'.txt', 'wb')
#uniqFile = open('uniq_hash_file.txt', 'wb')
uniq_data = {}


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

			isUnique = True
			if enableDedup == True:
				if 'Title' not in a:
					a['Title'] = ''
					print 'Title empty'
				if 'jobtype' not in a:
					a['jobtype'] = ''
					print 'jobtype empty'
				if 'company' not in a:
					a['company'] = ''
					print 'company empty'
				if 'duration' not in a:
					a['duration'] = ''
					print 'duration empty'

				# We calcualte a digent using 6 fields for each row and find absolute duplicate records

				txt = a['Location'].strip().lower() + a['department'].strip().lower()  + a['Title'].lower() + a['jobtype'].lower() + a['company'].lower() + a['duration'].lower()
				hsh = hashlib.sha1(txt.encode('utf-8')).hexdigest()
				
				if hsh in uniq_data:				
					duplicates = duplicates + 1
					isUnique = False
				else:
					uniq_data[hsh] = 0

			if isUnique == True:
			
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


	'''
	After generating all json files for a single TSV file, we archive the directory ( .bz2)  to save space.
	We also delete the json file generated by the tsvtojson parser. We delete this file because the tsvtojson library does not run if a file exists with the same name
	'''
	os.system('rm ' + temp_json_file)

	if makeJSON == True:		
		os.system('tar -cjvf '+outDataFilesPath+tarName+'.tar.bz2 '+pathName)
		os.system('rm -r '+pathName)

	logFile.write(tarName+'\tUniq: '+str(perFileCount)+'\tDup: '+str(duplicates)+'\n')


def run_cmd(cmd):
	os.system(cmd)
	






for filename in os.listdir(dataFilesPath):

	fileCount = fileCount + 1

	try:

		outPath = outDataFilesPath + filename[:-4]

		if makeJSON == True:
			os.system('mkdir ' + outPath)


		count = count + perFileCount
		perFileCount = 0
		
		cmd = "tsvtojson -t "+dataFilesPath+filename+" -j "+temp_json_file+" -c "+ headerFilePath +" -o test -v -e " + encodingFilePath
				
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


logFile.close()
#uniqFile.close()

	

	

