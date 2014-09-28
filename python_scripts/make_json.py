#!/usr/bin/python

import sys
import csv
import json
import glob
import os
import codecs

def make_json():
	filename = sys.argv[1]
	headerFile = sys.argv[2]
	headers = []

	f1 = open(headerFile)
	for h in f1:
		headers.append(h.strip())


	f1 = codecs.open(filename, encoding='utf-8')

	# content = csv.reader(f1, delimiter='\t')
	# try:
	for a1 in f1:
		try:
			print a1
			line = a1.strip().split('\t')

			obj = {}
			for i in range(0,len(headers)):
				obj[headers[i]] = line[i]
		except UnicodeDecodeError:
			print("Error")

		# print json.dumps(obj)


def count_json() :
	filename = sys.argv[1]
	f1 = open(filename)
	data = json.load(f1)
	print len(data['test'])
	print data['test'][len(data['test'])-1]


def find_total_lines():
	os.chdir("/Volumes/SHRI/Data/")
	count = 0
	for filename in glob.glob("*.tsv"):
		f1 = open(filename)
		print filename
		for a in f1:
			count = count + 1

	print count


# make_json()
# find_total_lines()
count_json()