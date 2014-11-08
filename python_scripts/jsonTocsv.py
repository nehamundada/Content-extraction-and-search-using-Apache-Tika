#!/usr/bin/python

import os
import hashlib
import glob
import json
import csv
import sys
import time
import datetime
import codecs
import sqlite3


count = 0 
data_dir = '/Users/shri/devel/cs572/code/Content-extraction-and-search-using-Apache-Tika/python_scripts/dedupJson/'
con = sqlite3.connect('/Users/shri/devel/cs572/code/Content-extraction-and-search-using-Apache-Tika/python_scripts/jobs.sqlite')
# data_dir = '/home/shri/devel/Content-extraction-and-search-using-Apache-Tika/python_scripts/sample_processing/'
# logFile = open('solr_indexing_'+timestamp1+'.txt', 'wb')
# fout = codecs.open('test.csv','w', 'ISO-8859-1')
# fout.write('|'.join([ "id", "Title" ,"faxNumber", "Location","duration","lastSeenDate","postedDate","Location2" ,"start","phoneNumber","department","hash","company","contactPerson","applications","firstSeenDate","salary","latitude","url","longitude","jobtype"]))
headers = [ "id", "Title" ,"faxNumber", "Location","duration","lastSeenDate","postedDate","Location2" ,"start","phoneNumber","department","hash","company","contactPerson","applications","firstSeenDate","salary","latitude","url","longitude","jobtype"]

cur = con.cursor()
cur.execute("PRAGMA synchronous=OFF")

for filename in os.listdir(data_dir):
	if '.tar.bz2' not in filename:
		continue
		
	
	dir_name = filename[:-8]
	os.system('tar -xjf ' + data_dir+filename + ' -C ' + data_dir)
	#cmd = 'mv '+data_dir+'home/shri/devel/employment_data/'+dir_name+'/* ' + data_dir + 'final_data'
	# os.system(cmd)


	new_dir_path = data_dir + 'home/shri/devel/employment_data/'+dir_name + '/'
	for jsonFile in os.listdir(new_dir_path):

		count = count + 1
		f1 = open(new_dir_path+jsonFile)
		jData = json.load(f1)
		
		# processing data formats
		jData['postedDate'] = jData['postedDate'] + 'T00:00:00Z'
		jData['firstSeenDate'] = jData['firstSeenDate'] + 'T00:00:00Z'
		jData['lastSeenDate'] = jData['lastSeenDate'] + 'T00:00:00Z'

		# remove unknown key
		del jData['unknown']
		# f1.close()
		# f1 = open(data_dir+'processed_json/'+jsonFile, 'w')
		# f1.write(json.dumps(jData))
		# print json.dump(jData)
		# f1.close()	

		
		vals = []
		cols = []
		for h in headers:
			cols.append(h)
			vals.append("'"+jData[h].strip().replace("'",'')+"'")
		query = 'insert into jobs (' + (','.join(cols)) + ') values (' + (','.join(vals)) + '); '
		# print query

		cur.execute(query)
		print count

		# fout.write('\n'+'\t'.join([jData["id"], jData["Title"], jData["faxNumber"], jData["Location"], jData["duration"], jData["lastSeenDate"], jData["postedDate"], jData["Location2"], jData["start"], jData["phoneNumber"], jData["department"], jData["hash"], jData["company"], jData["contactPerson"], jData["applications"], jData["firstSeenDate"], jData["salary"], jData["latitude"], jData["url"], jData["longitude"], jData["jobtype"]]))
		    

	# if count == 2:
	# 	break
	cmd = 'rm -r ' + data_dir + 'home/'
	os.system(cmd)
	# os.system('rm -r ' + data_dir + 'final_data/*')

	con.commit()

con.close()

# logFile.close()
fout.close()