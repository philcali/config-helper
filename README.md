# Configuration Provisioning

This simple library enables config injection through proxies from any
data source. As long as the provider can provide parameters, this library 
handles the value provisioning. 

## How to use it

__Step 1__: Define your configuration pojo

For example, these OAuth client creds

``` java
public interface ClientConfig {
    String getClientId();
    String getClientSecret();
    String getRedirectUrl();
    List<String> getScopes();
``` 

__Step 2__: Define your config proxy

``` java
IConfigFactory factory = new ConfigProxyFactory(new EnvConfigProvider());

ClientConfig config = factory.create(ClientConfig.class);
config.getClientId() // CLIENT_CONFIG env variable
```

__Step 3__: Define your source

As you can see `IConfigProvider`s are capable of loading sources fdrom anything.

``` java
IConfigFactory factory = new ConfigProxyFactory(new SystemManagerConfigProvider());

ClientConfig config = factory.create(ClientConfig.class, "/Prod/Application/OAuthCreds");
config.getClientId() // "/Prod/Application/OAuthCreds/ClientId"
```
