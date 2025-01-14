## Redis Benchmark

### 2020/11

*1Fork 1Thread*

*Hashes*
```
Benchmark                                             Mode  Cnt     Score      Error  Units
RedisHashesBenchmark.hashAllReader                   thrpt    5  1552.488 ±  443.569  ops/s
RedisHashesBenchmark.hashFieldValueReader            thrpt    5  1732.734 ±  323.702  ops/s
RedisHashesBenchmark.hashWriter                      thrpt    5  1788.148 ±  465.638  ops/s
RedisHashesBenchmark.laserdiscHashAllReader          thrpt    5   191.136 ±   35.789  ops/s
RedisHashesBenchmark.laserdiscHashFieldValueReader   thrpt    5   196.131 ±   54.382  ops/s
RedisHashesBenchmark.laserdiscHashWriter             thrpt    5   198.798 ±   66.568  ops/s
RedisHashesBenchmark.redicolousHashAllReader         thrpt    5   177.576 ±   30.305  ops/s
RedisHashesBenchmark.redicolousHashFieldValueReader  thrpt    5   178.976 ±   42.134  ops/s
RedisHashesBenchmark.redicolousHashWriter            thrpt    5   174.271 ±   51.027  ops/s
RedisHashesBenchmark.redis4catsHashAllReader         thrpt    5   192.695 ±   78.915  ops/s
RedisHashesBenchmark.redis4catsHashFieldValueReader  thrpt    5   207.522 ±   50.858  ops/s
RedisHashesBenchmark.redis4catsHashWriter            thrpt    5   235.784 ±  154.717  ops/s
```

*Keys*
```
Benchmark                                      Mode  Cnt     Score     Error  Units
RedisKeysBenchmark.keyExistsReader            thrpt    5  1776.811 ± 356.406  ops/s
RedisKeysBenchmark.keyPttlReader              thrpt    5  1724.097 ± 279.740  ops/s
RedisKeysBenchmark.laserDiscKeyExistsReader   thrpt    5   193.703 ±  65.749  ops/s
RedisKeysBenchmark.laserDiscKeyPttlReader     thrpt    5   193.458 ±  50.529  ops/s
RedisKeysBenchmark.redicolousKeyExistsReader  thrpt    5   163.529 ±  76.439  ops/s
RedisKeysBenchmark.redicolousKeyPttlReader    thrpt    5   174.327 ±  50.959  ops/s
RedisKeysBenchmark.redis4catsKeyExistsReader  thrpt    5   226.404 ± 109.230  ops/s
RedisKeysBenchmark.redis4catsKeyPttlReader    thrpt    5   243.537 ± 130.926  ops/s
```

*Lists*
```
Benchmark                                         Mode  Cnt     Score     Error  Units
RedisListsBenchmark.laserdiscListByIndexReader   thrpt    5   157.208 ±  45.700  ops/s
RedisListsBenchmark.laserdiscListLengthReader    thrpt    5   136.873 ±  38.442  ops/s
RedisListsBenchmark.laserdiscListRangeReader     thrpt    5   165.093 ± 237.914  ops/s
RedisListsBenchmark.laserdiscListWriter          thrpt    5   165.620 ± 112.546  ops/s
RedisListsBenchmark.listByIndexReader            thrpt    5  1504.266 ± 596.492  ops/s
RedisListsBenchmark.listLengthReader             thrpt    5  1561.305 ± 528.915  ops/s
RedisListsBenchmark.listRangeReader              thrpt    5  1456.099 ± 909.016  ops/s
RedisListsBenchmark.listWriter                   thrpt    5  1771.367 ± 652.887  ops/s
RedisListsBenchmark.redicolousListByIndexReader  thrpt    5   135.511 ±  33.624  ops/s
RedisListsBenchmark.redicolousListLengthReader   thrpt    5   117.951 ±  66.588  ops/s
RedisListsBenchmark.redicolousListRangeReader    thrpt    5   126.667 ± 330.676  ops/s
RedisListsBenchmark.redicolousListWriter         thrpt    5   150.982 ±  94.001  ops/s
RedisListsBenchmark.redis4catsListByIndexReader  thrpt    5   214.701 ±  52.466  ops/s
RedisListsBenchmark.redis4catsListLengthReader   thrpt    5   195.073 ± 408.724  ops/s
RedisListsBenchmark.redis4catsListRangeReader    thrpt    5   216.921 ± 357.256  ops/s
RedisListsBenchmark.redis4catsListWriter         thrpt    5   206.256 ± 555.952  ops/s
```

*Sets*
```
Benchmark                                       Mode  Cnt     Score     Error  Units
RedisSetsBenchmark.laserdiscSetCardReader      thrpt    5   196.554 ±  36.414  ops/s
RedisSetsBenchmark.laserdiscSetDiffWriter      thrpt    5   183.157 ±  63.127  ops/s
RedisSetsBenchmark.laserdiscSetMembersReader   thrpt    5   181.293 ±  30.221  ops/s
RedisSetsBenchmark.laserdiscSetWriter          thrpt    5   183.056 ±  37.632  ops/s
RedisSetsBenchmark.redicolousSetCardReader     thrpt    5   173.508 ±  48.081  ops/s
RedisSetsBenchmark.redicolousSetDiffWriter     thrpt    5   172.539 ±  27.593  ops/s
RedisSetsBenchmark.redicolousSetMembersReader  thrpt    5   177.507 ±  24.302  ops/s
RedisSetsBenchmark.redicolousSetWriter         thrpt    5   178.137 ±  45.988  ops/s
RedisSetsBenchmark.redis4catsSetCardReader     thrpt    5   257.881 ±  68.513  ops/s
RedisSetsBenchmark.redis4catsSetDiffWriter     thrpt    5   252.935 ± 119.800  ops/s
RedisSetsBenchmark.redis4catsSetMembersReader  thrpt    5   254.934 ± 133.798  ops/s
RedisSetsBenchmark.redis4catsSetWriter         thrpt    5   249.977 ± 136.127  ops/s
RedisSetsBenchmark.setCardReader               thrpt    5  1735.714 ± 358.915  ops/s
RedisSetsBenchmark.setDiffWriter               thrpt    5  1719.237 ± 224.646  ops/s
RedisSetsBenchmark.setMembersReader            thrpt    5  1768.035 ± 249.180  ops/s
RedisSetsBenchmark.setWriter                   thrpt    5  1707.503 ± 407.231  ops/s
```

*SortedSets*
```
Benchmark                                                 Mode  Cnt     Score     Error  Units
RedisSortedSetsBenchmark.laserdiscSortedSetCardReader    thrpt    5   170.267 ±  89.732  ops/s
RedisSortedSetsBenchmark.laserdiscSortedSetCountReader   thrpt    5   179.003 ±  40.080  ops/s
RedisSortedSetsBenchmark.laserdiscSortedSetRangeReader   thrpt    5   178.721 ±  50.206  ops/s
RedisSortedSetsBenchmark.laserdiscSortedSetWriter        thrpt    5   172.339 ±  30.774  ops/s
RedisSortedSetsBenchmark.redicolousSortedSetCardReader   thrpt    5   158.050 ±  28.218  ops/s
RedisSortedSetsBenchmark.redicolousSortedSetCountReader  thrpt    5   154.691 ±  28.389  ops/s
RedisSortedSetsBenchmark.redicolousSortedSetRangeReader  thrpt    5   156.128 ±  22.739  ops/s
RedisSortedSetsBenchmark.redicolousSortedSetWriter       thrpt    5   154.230 ±  38.051  ops/s
RedisSortedSetsBenchmark.redis4catsSortedSetCardReader   thrpt    5   243.307 ±  60.217  ops/s
RedisSortedSetsBenchmark.redis4catsSortedSetCountReader  thrpt    5   232.416 ±  66.536  ops/s
RedisSortedSetsBenchmark.redis4catsSortedSetRangeReader  thrpt    5   236.565 ±  63.771  ops/s
RedisSortedSetsBenchmark.redis4catsSortedSetWriter       thrpt    5   232.165 ±  54.573  ops/s
RedisSortedSetsBenchmark.sortedSetCardReader             thrpt    5  1742.036 ± 234.697  ops/s
RedisSortedSetsBenchmark.sortedSetCountReader            thrpt    5  1595.086 ± 343.268  ops/s
RedisSortedSetsBenchmark.sortedSetRangeReader            thrpt    5  1678.910 ± 628.130  ops/s
RedisSortedSetsBenchmark.sortedSetWriter                 thrpt    5  1546.634 ± 416.838  ops/s
```

*Strings*
```
Benchmark                                        Mode  Cnt     Score     Error  Units
RedisStringsBenchmark.laserdiscStringAppender   thrpt    5   195.582 ±  50.816  ops/s
RedisStringsBenchmark.laserdiscStringReader     thrpt    5   200.390 ±  74.596  ops/s
RedisStringsBenchmark.laserdiscStringWriter     thrpt    5   202.781 ±  53.051  ops/s
RedisStringsBenchmark.redicolousStringAppender  thrpt    5   178.787 ±  46.825  ops/s
RedisStringsBenchmark.redicolousStringReader    thrpt    5   180.344 ±  47.700  ops/s
RedisStringsBenchmark.redicolousStringWriter    thrpt    5   178.654 ±  38.993  ops/s
RedisStringsBenchmark.redis4catsStringAppender  thrpt    5   206.677 ± 300.552  ops/s
RedisStringsBenchmark.redis4catsStringReader    thrpt    5   248.249 ±  84.419  ops/s
RedisStringsBenchmark.redis4catsStringWriter    thrpt    5   224.979 ± 187.463  ops/s
RedisStringsBenchmark.stringAppender            thrpt    5  1509.828 ± 432.539  ops/s
RedisStringsBenchmark.stringReader              thrpt    5  1395.982 ± 566.448  ops/s
RedisStringsBenchmark.stringWriter              thrpt    5  1314.077 ± 784.545  ops/s
```
```
sbt 'benchmarks/jmh:run -i 5 -wi 1 -f1 -t1 monix.connect.benchmarks.redis.*Benchmark.*'
```