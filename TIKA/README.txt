### Running Tika Parser ###

Untar the HW1.tar.bz2 file
```
tar -xzvvf HW1.tar.bz2
```

* Copy the “tika-app-1.6.jar” file in the lib directory. Make sure you name the tike jar as “tika-app-1.6.jar” 

* App.properties files:
 * Set the ‘inputDataDir’ field to the directory containing the data set. Do not include the trailing / (slash)
 * Set the ‘outputDataDir’ field to the directory where the json files will be created. Do not include the trailing / (slash)
 * If you want to enable the deduplication feature - set the enableDeDup=True else enableDeDup=False

* run the script file as 
```
sh run.sh
```