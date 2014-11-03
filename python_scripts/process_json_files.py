#!/usr/bin/python

import os
import hashlib
#import bencode
import glob
import json
import threading
import sys
import time
import datetime


count = 0 
# data_dir = '/home/shri/devel/Content-extraction-and-search-using-Apache-Tika/python_scripts/dedup_data/'
data_dir = '/home/shri/devel/Content-extraction-and-search-using-Apache-Tika/python_scripts/sample_processing/'
# logFile = open('solr_indexing_'+timestamp1+'.txt', 'wb')




for filename in os.listdir(data_dir):
	if '.tar.bz2' not in filename:
		continue
		
	count = count + 1
	dir_name = filename[:-8]
	os.system('tar -xjf ' + data_dir+filename + ' -C ' + data_dir)
	#cmd = 'mv '+data_dir+'home/shri/devel/employment_data/'+dir_name+'/* ' + data_dir + 'final_data'
	# os.system(cmd)


	new_dir_path = data_dir + 'home/shri/devel/employment_data/'+dir_name + '/'
	for jsonFile in os.listdir(new_dir_path):

		f1 = open(new_dir_path+jsonFile)
		jData = json.load(f1)
		    

		
		# processing data formats
		jData['postedDate'] = jData['postedDate'] + 'T00:00:00Z'
		jData['firstSeenDate'] = jData['firstSeenDate'] + 'T00:00:00Z'
		jData['lastSeenDate'] = jData['lastSeenDate'] + 'T00:00:00Z'

		# remove unknown key
		del jData['unknown']
		f1.close()
		f1 = open(data_dir+'processed_json/'+jsonFile, 'w')
		f1.write(json.dumps(jData))
		# print json.dump(jData)
		f1.close()	
		    

	# if count == 2:
	# 	break
	cmd = 'rm -r ' + data_dir + 'home/'
	os.system(cmd)
	# os.system('rm -r ' + data_dir + 'final_data/*')


# logFile.close()
