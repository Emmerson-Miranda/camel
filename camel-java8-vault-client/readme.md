Camel Java8 Router Project
==========================

Camel Java8 Router Project to access secrets storage into Hashicorp Vault. 

This is a Maven project with a simple example how to call Vault Rest API and get secrets using Apache Camel and Undertow component.


Environment Parameters
------------------
You must have two environment variables in order to connect with Vault, one is for URL and another the access Token.

Mind the URL and Token values in this document are examples.

```
export PARAM_VAULT_URL=http://127.0.0.1:8200
export PARAM_VAULT_TOKEN=s.vvMr4cJCBT8rccg6Z5DfCuZK
```

Setting up secrets 
------------------
For this example I setup some secrets in Vault as per below image.
![cubbyhole](vault_cubbyhole.png "Secrets in Vault") 


Testing 
------------------

This example expose an HTTP endpoint to be called


Example

```
http://127.0.0.1:8080/proxy/resolve/key?keyname=v1/cubbyhole/tres
```
![example](vault_camel_client.png "Testing from browser") 



More info
===========

*   https://learn.hashicorp.com/vault/getting-started/apis


