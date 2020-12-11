# Camel 3 CDI - Error Handling


This example expose some REST interfaces and show some error handling scenarios



## Testing 



NoErrorRoute Happy path.

```
$ curl -d "{\"value\": \"value without error\"}" -H "Content-Type: application/json" -X POST http://0.0.0.0:8080/eh/noerror
```

NoErrorRoute Happy path with Thread sleep.

```
$ curl -d "{\"value\": \"value without error\"}" -H "Content-Type: application/json" -H "x-sleep: 3000" -X POST http://0.0.0.0:8080/eh/noerror
```

NumberRoute - Happy path -  without error handling.

```
$ curl -d "{\"value\": \"1\"}" -H "Content-Type: application/json" -X POST http://0.0.0.0:8080/eh/in
```

NumberRoute - Value NumberFormatException - NumberRoute without error handling.

```
$ curl -d "{\"value\": \"value with error\"}" -H "Content-Type: application/json" -X POST http://0.0.0.0:8080/eh/in
```

InvalidNumberOnExceptionToRoute call a route that raise an exception, onException does not catch the error raise in the called route.

```
$ curl -d "{\"value\": \"value without error\"}" -H "Content-Type: application/json" -X POST http://0.0.0.0:8080/eh/inoeto
```

InvalidNumberRouteOnExceptionSelfContainedRoute has onException in the route, it catch the error.

```
$ curl -d "{\"value\": \"value without error\"}" -H "Content-Type: application/json" -X POST http://0.0.0.0:8080/eh/inoesc
```

InvalidNumberTryCatchRoute call another route that does not have onException but try catch block.

```
$ curl -d "{\"value\": \"value without error\"}" -H "Content-Type: application/json" -X POST http://0.0.0.0:8080/eh/intc
```

ErrorRoute always raise an exception.

```
$ curl -d "{\"value\": \"1\"}" -H "Content-Type: application/json" -X POST http://0.0.0.0:8080/eh/te
```

ErrorOnExceptionRoute Implementation Throw a Exception and is handled by onException

```
$ curl -d "{\"value\": \"1\"}" -H "Content-Type: application/json" -X POST http://0.0.0.0:8080/eh/teoe
```

ErrorOnExceptionToRoute Call a route that throw an exception and handle it with caller onException, in this case onException is ignored.

```
$ curl -d "{\"value\": \"1\"}" -H "Content-Type: application/json" -X POST http://0.0.0.0:8080/eh/teoet
```

ErrorOnExceptionToTryCatchRoute Call a route that throw an exception and handle it with caller try/catch, in this case try/catch works."

```
$ curl -d "{\"value\": \"1\"}" -H "Content-Type: application/json"  -X POST http://0.0.0.0:8080/eh/teoetc
```


CallHttpBackendUndertowRoute Call backend HTTP with thread sleep

```
$ curl -d "{\"value\": \"1\"}" -H "Content-Type: application/json" -H "x-sleep: 3000"  -X POST http://0.0.0.0:8080/eh/chbu
```


curl -d "{\"value\": \"1\"}" -H "Content-Type: application/json" -H "x-sleep: 4000"  -X POST http://0.0.0.0:8080/eh/chbu



curl -d "{\"value\": \"wwww1\"}" -H "Content-Type: application/json" -H "x-sleep: 1000"  -X POST http://0.0.0.0:8080/eh/chbu







CallHttpBackendHttpRoute Call backend HTTP with thread sleep - Happy path

```
$ curl -d "{\"value\": \"1\"}" -H "Content-Type: application/json" -H "x-sleep: 1000"  -X POST http://0.0.0.0:8080/eh/chbh
```

CallHttpBackendHttpRoute Call backend HTTP with thread sleep - Timeout exception handled by second onException block.

```
$ curl -d "{\"value\": \"1\"}" -H "Content-Type: application/json" -H "x-sleep: 3000"  -X POST http://0.0.0.0:8080/eh/chbh
```

CallHttpBackendHttpRoute Call backend HTTP with an invalid number - The first onException block will catch the error.

```
curl -d "{\"value\": \"efdw1\"}" -H "Content-Type: application/json" -H "x-sleep: 1000"  -X POST http://0.0.0.0:8080/eh/chbh
```

