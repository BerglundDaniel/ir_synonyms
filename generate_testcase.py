#!usr/bin/python
import sys
import math
import os

#Python script to generate test files

#Variables
shortSymsDocs=[1,2,3,6]
longSymsDocs=[1,2,5,6]
queryDocs=[1,6,8]
numDocs=10
numLongCommon=10
numShortCommon=10

#Command line arguments
if(len(sys.argv)>2):
  for i in range(1,len(sys.argv)):
    arg=sys.argv[i]
    if(arg=="-O"):
      fileRoot=sys.argv[i+1] #Fileroot name
else:
  sys.exit("To few arguments")

#Create and move in to a directory to store the text files in
if not os.path.exists(fileRoot):
  os.makedirs(fileRoot)
  os.chdir(fileRoot)
else:
  sys.exit("File already exists")

#Create and write document files
for i in range(1,numDocs+1):
  fileOut=open(str(i)+".txt",'w')

  #Common words
  for n in range(numLongCommon+1):
    fileOut.write("longCommonWord ")
  for n in range(numShortCommon+1):
    fileOut.write("common ")

  #Query word
  if i in queryDocs:
    for n in range(2+i):
      fileOut.write("query ")

  #Short synonyms
  if i in shortSymsDocs:
    for n in range(4):
      fileOut.write("synonyms ")

  #Long synomyms
  if i in longSymsDocs:
    for n in range(4):
      fileOut.write("longSynonymsWord ")

  fileOut.close()

#Write info file TODO useful for larger tests
#Word - number of occuranses - 
#infoFileOut=open(fileRoot+"_info.txt",'w')

#infoFileOut.close()
