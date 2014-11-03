#!/usr/bin/python

import os
import hashlib
#import bencode
import glob
import json
import threading
import sys
import time
import sqlite3
import datetime


count = 0 
data_dir = '/home/shri/devel/Content-extraction-and-search-using-Apache-Tika/python_scripts/dedup_data/'
con = sqlite3.connect('data.sqlite')




for filename in os.listdir(data_dir):
	if '.tar.bz2' not in filename:
		continue

	count = count + 1
	dir_name = filename[:-8]
	os.system('tar -xjf ' + data_dir+filename + ' -C ' + data_dir)
	#cmd = 'mv '+data_dir+'home/shri/devel/employment_data/'+dir_name+'/* ' + data_dir + 'final_data'
	# os.system(cmd)

	cur = con.cursor()

	new_dir_path = data_dir + 'home/shri/devel/employment_data/'+dir_name + '/'
	for jsonFile in os.listdir(new_dir_path):

		f1 = open(new_dir_path+jsonFile)
		jData = json.load(f1)
				
		# open the cursor
		cur = con.cursor()

		sql = "insert into locations (id, lat, long) values ('"+ jData['id']+"', "
		if jData['latitude'] == '':
			sql = sql + 'NULL, '
		else:
			sql = sql + jData['latitude'] + ", "
		
		if jData['longitude'] == '':
			sql = sql + 'NULL '
		else:
			sql = sql + jData['longitude'] 

		sql = sql + '); '
		# print sql
		cur.execute(sql)
		
		    
	# if count == 2:
	# 	break
	cmd = 'rm -r ' + data_dir + 'home/'
	os.system(cmd)
	# os.system('rm -r ' + data_dir + 'final_data/*')

	con.commit()
	print count

if con:
	con.close()
