#Query
 - Best ID bytes to scan with
 - Collectors
 - Filters
 - QueryID implements ID
 - Iterator.prev()

#Compaction
 - Retention Policy

#Load Test
 - Reader thread(s) and writer thread(s)
   - Observable<Datum> fakeDatumGenerator()
    and can .buffer() to test batch
    Metrics dump every x seconds thread

#Microsecond Resolution

#Backup/Restore
 - Streaming {Input,Output} backup and restore via incremental backup/restore
   - snapshots? pruning snapshots?

#Scaling
 - add disks via ShardRockSlab (or ShardedSlab? no)
 - add slaves/read replicas
 - application level sharding by key or time

Performance
 - Copy is BSON Best?
 - Test no tags
 - Remove tags from body (in id?)

Store all session, metric, series data in continuum?
 CAN ROCKS SCAN BEST BY TIMESTAMP OR SESSION ID (sequential vs random)
 Is it possible?
  with one caveat: must know the timestamp or time range of the session
    that other dbs dont have the luxury
    if you can be lenient, duplicate session ids allowed?
