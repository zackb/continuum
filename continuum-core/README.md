#Continuum core
###Premise
Using log-structured-merge trees, memory mapped files, and two sepcialized schema designs, continuum can efficiently store, retrieve, and analyze data for Statistical analysis, Dashboard / UI presentation, User session and metric data storage and retrieval 

###Concepts

#####TimeSeries
    Key: <name>:<tag1>:<tag2>:<value1>:<value2>:<timestamp>

    Value: <blob>:<double>

#####TimeKeyValue
    Key: <timestamp>:<name>:<tag1>:<tag2>:<value1>:<value2>

    Value: <blob>:<object>

####Space
    Compaction based on retention policies
 
####Time
    Streaming cold and hot data through cold and hot storage 

#TODO

###Compaction
 - Retention Policy

###Document and Enforce Reserved Words
 - fields
 - start
 - end
 - interval
 - fn
 - group

###Microsecond Resolution

###Javadocs
 - Generate and host

###Performance
 - Copy is BSON Best?
 - Test no tags
 - Remove tags from body (in id?)

###Store all session, metric, series data in continuum?
 - Is it possible?
  - CAN ROCKS SCAN BEST BY TIMESTAMP OR SESSION ID (sequential vs random)
  - with one caveat: must know the timestamp or time range of the session
   - that other dbs dont have the luxury
   - if you can be lenient, duplicate session ids allowed?
