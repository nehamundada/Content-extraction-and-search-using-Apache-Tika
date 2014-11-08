#!/usr/bin/python

import json
import urllib2
import sqlite3
import csv
import time
import sys
import codecs

start = int(sys.argv[1])
end = int(sys.argv[2])
positionFile = sys.argv[3]
f1 = open(positionFile)
contents = csv.reader(f1, delimiter='|')
first = True
fout = codecs.open('logfile','w','ISO-8859-1')


count =  0
print start,' to ',end

for row in contents:
	
	count = count + 1
	if count <= start:
		print count, ' : ',start
		continue
	
	if count > end:
		break
		
	#if first == True:
	#	first = False
	#	continue


	# req_url = 'https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20geo.placefinder%20where%20text%3D%22'+str(row[0]).strip()+'%2C'+str(row[1]).strip()+'%22%20and%20gflags%3D%22R%22&format=json'
	req_url = 'https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20geo.placefinder%20where%20text%3D%22'+str(row[0]).strip()+'%2C'+str(row[1]).strip()+'%22%20and%20gflags%3D%22R%22&format=json&diagnostics=true&callback='
	#print req_url
	data = urllib2.urlopen(req_url).read()

	jData = json.loads(data)
	
	if jData == None:
		continue
	if 'query' not in jData:
		continue
	if jData['query'] == None or 'results' not in jData['query']:
		continue
	if jData['query']['results'] == None or 'Result' not in jData['query']['results']:
		continue

	results = jData['query']['results']['Result']
	obj = {}
	if 'street' in results and results['street'] != None:
		obj['street'] = "'"+results['street'].replace("'"," ").strip()+"'"
	else:
		obj['street'] = ''

	if 'city' in results and results['city'] != None:
		obj['city'] = "'"+results['city'].replace("'"," ").strip()+"'"
	else:
		obj['city'] = ''
	if 'county' in results and results['county'] != None:
		obj['county'] = "'"+results['county'].replace("'"," ").strip()+"'"
	else:
		obj['county'] = ''
	if 'state' in results and results['state'] != None:
		obj['state'] = "'"+results['state'].replace("'"," ")+"'"
	else:
		obj['state'] = ''
	if 'country' in results and results['country'] != None:
		obj['country'] = "'"+results['country'].replace("'"," ")+"'"
	else:
		obj['country'] = ''
	if 'postal' in results and results['postal'] != None:
		obj['postal'] = "'"+results['postal'].replace("'"," ")+"'"
	else:
		obj['postal'] = ''
	if 'countrycode' in results and results['countrycode'] != None:
		obj['countrycode'] = "'"+results['countrycode'].replace("'"," ")+"'"
	else:
		obj['countrycode'] = ''
	

	sql = "insert into address_info values ("+ str(row[0])+", "+ str(row[1])+", '"+ obj['street'].replace("'"," ").strip()+"', '"+ obj['city'].replace("'"," ").strip()+"', '"+ obj['state'].replace("'"," ").strip()+"', '"+ obj['county'].replace("'"," ").strip()+"', '"+ obj['country'].replace("'"," ").strip()+"', '"+ obj['postal'].replace("'"," ").strip()+"', '"+ obj['countrycode'].replace("'"," ").strip()+"', null); "
	# print sql
	fout.write('\n'+sql)
	#cur.execute(sql)

	time.sleep(5)

#con.commit()
#con.close()
fout.close()



#https://query.yahooapis.com/v1/public/yql?q=select * from geo.placefinder where text="48.4542292,-123.5404696" and gflags="R"&format=json
