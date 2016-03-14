# Continuum

continuum is a JVM library for storing and analyzing large amounts of time based data.

 - Core library for storage, retreival, and analysis of time series and time key value data
 - REST Interface
 - Streaming backup/restore
 - Streaming master/slave and master/master replication

## tl;dr
<p align="center">
![alt tag](/docs/img/spacetime.png)
<p>

## Building

    make
    
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

TODO: Javadocs [available on the web](http://TODO.com)

##Goals
1. Use memory mapped files and log-structured-merge trees (LSM) to efficienty store, retreive, and analyze large amounts of time-based data.
2. Leverage existing underlying core technologies like RocksDB, LevelDB, BerkeleyDB and similar datastores.
3. Employ time-to-live (TTL) or downsampling (RRD) to efficeintly use disk space.
4. A continuous, scalable infintie time-based data stream by incorporating data windowing, downsampling, or both
5. Utilize TWO specialized schema designs for time series data and time key value data.

####TimeSeries Data (space/time): (Analysis and Dashboard/UI)
 - Small number of unique time series with very large data volume
 - Infinite storage using retention policies and data downsampling (RRDTool, Whisper, InfluxDB inspired)
 - Downsampling OK
 - Example: "Realtime" performance metrics, measurements of cpu, or weather temperature readings over time
 - Core only

####TimeKeyValue Data (time/key:value): (User Session and Session metrics)
 - For aggregating small amounts of data into buckets by a unique key
 - Large number of unique keys with small amount of data
 - Age out over time
 - Stream 
  - Fast data: hot, open sets of newest data
  - Slow data: rolling window of data through ColdStorage (S3, NAS)
 - Example: "Realtime" analytics data events grouped by session/user id
 - Core, Repl

###Scaling
 - add disks
 - cold online backup (backup/restore)
 - hot online backup(s) (read-only replication)
 - application level sharding by key or time
 - cluserting (master-master replication)

## Maintainer
Zack Bartel `zack@bartel.com` [zackbartel.com](zackbartel.com)
