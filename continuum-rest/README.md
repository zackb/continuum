---
title: Continuum REST API
brand: dlvr
version: 0.1.0
---

# Continuum REST API

<pre class="base">
https://continuum.dlvr.com/api/1.0
</pre>
[DOCS](https://is.gd/2qRT3y)
### Path

For this documentation, we will assume every request begins with the above path.

### Format

All calls accept and return data in **JSON**.

### Authentication and Authorization

All calls require https AND either BASIC authentication OR Token based [OAuth2.0 Client Credentials](https://tools.ietf.org/html/rfc6749#section-1.3.4).

### Status Codes

- **200** Successful GET.
- **201** Successful POST.
- **202** Successful Provision queued.
- **401** Unauthenticated.
- **403** Unauthorized.
- **409** Unsuccessful POST or DELETE (Will return an errors object)



# Read

## GET /read

Query time series or time key value data. Required fields are start, end, name. Optional values are tagname=tagvalue query parameters, interval for histogram responses, function aggregate to apply to the data.

#### example request
Get ten days worth of average temperature values in LAX in one day intervals.

    $ curl -k -u dlvr:dlvr https://continuum.dlvr.com/api/1.0/read/temperature?location=lax&start=0&end=10d&function=avg&interval=1d

#### response

    {
      "data": [
        {1455355748946: 90.1},
        {1455269348946: 89.354678},
        {1455182948946: 92.9},
        {1455096548946: 90.0},
        {1455010148946: 90.0004},
        {1454923748946: 87.123},
        {1454837348946: 94.555},
        {1454750948946: 92.5},
        {1454664548946: 81.0},
        {1454578148946: 80.999},
      ],
      "execution_time": 2,
    }



## POST /write

Creates a new atom. Api will respond with status 201 the atom has been created, 202 the atom has been queued, or 403 the authentication credentials lack the required privileges.

#### example request

    $ curl https://continuum.dlvr.com/api/1.0/write \
      -F "name=temperature" \
      -F "value=90.1" \
      -F "location=lax" \
      -F "timestamp=12348567"

