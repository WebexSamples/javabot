# javaBot

## Description
Used to listen to events in Webex Teams on a localhost port. The only command it can understand is **hello**,
replies "hello there".

## Requirements

* **Webex Teams access token**: this will be used to communicate with Webex Teams. Ideally you would use the access token of the same bot here.
* **open port on localhost**: this is where the app will listen for incoming messages
* **Java 1.8**
* **Maven 3.6.1**

## Installation
* clone the repo
* ```mvn install```
* ````mvn exec:java -Dexec.mainClass=Main```

## Usage

```
+---------------------------------------------+
|                                             |
|        _                  _           _     |
|       | | __ ___   ____ _| |__   ___ | |_   |
|    _  | |/ _` \ \ / / _` | '_ \ / _ \| __|  |
|   | |_| | (_| |\ V / (_| | |_) | (_) | |_   |
|    \___/ \__,_| \_/ \__,_|_.__/ \___/ \__|  |
|                                             |
|                                             |
+---------------------------------------------+

Please enter an access token
AaBbCcDdEeFfGgHhIiJjAaBbCcDdEeFfGgHhIiJjAaBbCcDdEeFfGgHhIiJj_ABCD_1a2b3c4d-1234-abcd-9876-abcdefghijkl
INFO Verifying account
INFO Account verified as Droid
Please enter port you want to listen on:
5000
INFO Service listening on localhost:5000
```

## Dependencies

```xml
<dependencies>
 <dependency>
   <groupId>org.glassfish</groupId>
   <artifactId>javax.json</artifactId>
   <version>1.0.4</version>
 </dependency>
 
 <dependency>
   <groupId>com.ciscospark</groupId>
   <artifactId>ciscospark-client</artifactId>
   <version>1.0-SNAPSHOT</version>
  </dependency>

  <dependency>
   <groupId>org.json</groupId>
   <artifactId>json</artifactId>
   <version>20180130</version>
  </dependency>
 </dependencies>
```

