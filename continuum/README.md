#Continuum core

#TODO

###Query
 - Collectors
 - Filters
 - QueryID implements ID (Best ID bytes to scan with)
 - Iterator.prev()

###Compaction
 - Retention Policy

###Load Test
 - Reader thread(s) and writer thread(s)
   - Observable<Datum> fakeDatumGenerator()
     - and can .buffer() to test batch
   - Metrics dump every x seconds thread

###Microsecond Resolution

###Performance
 - Copy is BSON Best?
 - Test no tags
 - Remove tags from body (in id?)

###Backup/Restore
 - Streaming {Input,Output} backup and restore via incremental backup/restore
   - snapshots? pruning snapshots?

###Store all session, metric, series data in continuum?
 - Is it possible?
  - CAN ROCKS SCAN BEST BY TIMESTAMP OR SESSION ID (sequential vs random)
  - with one caveat: must know the timestamp or time range of the session
   - that other dbs dont have the luxury
   - if you can be lenient, duplicate session ids allowed?