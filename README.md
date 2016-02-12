# Continuum

continuum is a JVM library for storing and analyzing large amounts of time series data.
Additional tools include
 - REST Interface
 - Streaming master/slave replication
 - Limited "database" server

TODO: Javadocs [is available on the web](http://url.com/to/docs).

## Building

    make intall
    or
    gradlew install

TODO: Maven

## Goals
Take advatage of RocksDB, LevelDB, BerkeleyDB or similar flat file formats to efficiently store and retrieve data.

Key Data:
 - For aggregating small amounts of data into buckets by a unique key
 - Large number of unique keys with small amount of data
 - Example: Analytics data events grouped by user id

Series Data:
 - Small number of unique time series with very large data volume
 - Infinite storage using retention policies and data downsampling (RRD, Whisper, InfluxDB inspired)
 - Example: Measurements of cpu or weather temperature readings over time

###Scaling
 - add disks via ShardRockSlab (or ShardedSlab? no)
 - add slaves/read replicas
 - application level sharding by key or time
 - will never be support for clustering


## Maintainer
 
Maintained by Zack @ DLVR `zack@dlvr.com`
