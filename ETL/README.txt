Running ETL Lib

* Untar the HW1_ETL.tar.bz2 file

tar -xzvvf HW1_ETL.tar.bz2

Set the parameters in the script file 

* param : headerFilePath : The header file for tsv parsing
* param : encodingFilePath : The encoding file. We use ['utf-8', 'us-ascii', 'ISO-8]
* param : dataFilesPath : The path to TSV data files
* param : outDataFilesPath : The location to produce json files
* param : makeJSON : True / False whether to create json files or not
* param : enableDedup : True/False if deduplication is enabled or not

* run the script file as 

python run_ETL_job.py
