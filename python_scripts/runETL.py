#!/usr/bin/python

import os
import hashlib
import bencode
import glob
import json
import threading
import subprocess


logFile = open('etlRun.txt', 'wb')


def process_json(filename, pathName, tarName):

	perFileCount = 0	
	f1 = open(filename)
	data = json.load(f1)

	for a in data['test']:			
		perFileCount = perFileCount + 1
		
		# data_md5 = hashlib.md5(bencode.bencode(data['test'][a])).hexdigest()			
		# data['test'][a]['hash'] = data_md5			
		

		# hashFile.write(data_md5+'\n')
		# newFileName = filename[:-4] + '_' + str(perFileCount) + '.json'
		try:
			newFileName = pathName + '/' + str(a['id'])+'.json'
			# print perFileCount,' ', newFileName
			f2 = open( newFileName, 'wb')
			json.dump(a, f2)
			f2.close()
		except err:
			logFile.write('\nError in writing json file in : ' + tarName )
			logFile.write('\n'+str(err))

	print filename,'\t',perFileCount

	os.system('rm file.json')
	os.system('tar -cjvf '+tarName+'.tar.bz2 '+pathName)
	os.system('rm -r '+pathName)

	logFile.write(tarName+'\t'+str(perFileCount)+'\n')


def run_cmd(cmd):
	os.system(cmd)
	



headerFilePath = 'headers2.txt'
encodingFilePath = '/Users/shri/devel/cs572/sample/etllib/etl/encoding.txt'
dataFilesPath = '/Volumes/Storage/employment/'
# dataFilesPath = '/Users/shri/Desktop/computrabajo-co-20121204/'
outDataFilesPath = '/Users/shri/Desktop/'




perFileCount = 0
urls = {}

# os.chdir(dataFilesPath)
count = 0

for filename in os.listdir(dataFilesPath):

	# if filename[:4] != '.tsv':
	# 	continue
	try:

		outPath = outDataFilesPath + filename[:-4]
		os.system('mkdir ' + outPath)
		logFile.write('\n'+filename)
		count = count + perFileCount
		perFileCount = 0
		
		cmd = "tsvtojson -t "+dataFilesPath+filename+" -j file.json -c "+ headerFilePath +" -o test -v -e " + encodingFilePath
		# cmd = "sh command.sh "+dataFilesPath+filename+" file.json "+ headerFilePath +"  test  " + encodingFilePath
		print cmd
		# subprocess.call(cmd)

		t1 = threading.Thread(target=run_cmd, args=[cmd])
		t1.start()
		t1.join()
		# while t1.is_alive():
		#  	print '...processing'

		t1 = threading.Thread(target=process_json, args=['file.json', outPath, filename[:-4]])
		t1.start()
		t1.join()

	except err:
		logFile.write('\nError in writing json file in : ' + tarName )
		logFile.write('\n'+str(err))
		print "Unexpected error:", sys.exc_info()[0]

logFile.close()

	

	

