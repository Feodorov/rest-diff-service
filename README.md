REST Diff service
=================

Usage
-----
Start server on port 8888 (configurable in application.conf):
```
sbt run
```
Upload diff data:
```
curl -X POST -d $(echo hello1 | base64) localhost:8888/v1/diff/1/left
curl -X POST -d $(echo hellno | base64) localhost:8888/v1/diff/1/right
```
Get diff:
```
curl --compress -X POST -d $(echo hello1 | base64) localhost:8888/v1/diff/1
{
  "outcome": "UNEQUAL",
  "diffs": [{
    "offset": 4,
    "length": 2
  }]
}
```

Limitations
-----------
* uploaded content size is limited to 10Mb
* response is expected to be small - so no response streaming or compression is used
* same format for response and diff results for simplicity
* separate thread pools for spray (max 2 threads) and business logic methods
* neither response time is measured, nor any other monitoring features are implemented 
* no persistence for user data, but current RAM storage can be easily switched to persistent version by providing another implementation of Storage trait
