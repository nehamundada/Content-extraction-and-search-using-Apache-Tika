#!/usr/bin/python

import sys
import csv
import json
import glob
import os
import codecs


def count_json() :
	if len(sys.argv) < 2:
		print 'Usage : count_json <json_file>'
		return 1
	filename = sys.argv[1]
	f1 = open(filename)
	data = json.load(f1)
	print len(data['test'])
	print data['test'][len(data['test'])-1]


count_json()