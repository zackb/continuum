# Continuum

continuum is a JVM library for storing and analyzing large amounts of time based data.

 - Core library for storage, retreival, and analysis of time series and time key data
 - REST Interface
 - Streaming master/slave replication
 - Limited "database" server

TODO: Javadocs [available on the web](http://url.com/to/docs).

## Building

    make intall
    
 or

    ./gradlew install

### Maven
```
		<dependency>
			<groupId>com.dlvr</groupId>
			<artifactId>continuum-core</artifactId>
			<version>0.1</version>
		</dependency>
```

##Goals
 Leverage LSM (Log Structured Merge) disk and memory (mmap) based trees like RocksDB, LevelDB, BerkeleyDB and similar flat file datastores, to efficiently store and retrieve data.

####TimeSeries Data (space/time): (ScoreCard and Portal)
 - Small number of unique time series with very large data volume
 - Infinite storage using retention policies and data downsampling (RRDTool, Whisper, InfluxDB inspired)
 - Downsampling OK
 - Example: "Realtime" performance metrics, measurements of cpu, or weather temperature readings over time
 - Core only

####TimeKey Data (time/key:key:value): (Session and Metrics)
 - For aggregating small amounts of data into buckets by a unique key
 - Large number of unique keys with small amount of data
 - Age out over time
 - Stream 
  - Fast data: hot, open sets of newest data
  - Slow data: rolling window of data through ColdStorage (S3, NAS)
 - Example: "Realtime" analytics data events grouped by session/user id
 - Core, Repl

###Scaling
 - add disks via ShardRockSlab (or ShardedSlab? no)
 - add slaves/read replicas
 - application level sharding by key or time
 - will never be support for clustering


## Maintainer
 
Zack @ DLVR `zack@dlvr.com`
