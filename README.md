# Full-Stack Multithreaded Search Engine  

Throughout my Software Development course, I developed a full-stack search engine using Java. This project involved advanced topics such as inheritance and polymorphism, multithreaded programming, networking, and web development. Also, throughout this course, I've learned useful techniques for designing, debugging, refactoring, and reviewing code.

![Search](https://github.com/usf-cs212-fall2019/project-Jenkins1128/blob/master/Project/src/Search.png)
![Results](https://github.com/usf-cs212-fall2019/project-Jenkins1128/blob/master/Project/src/Results.png)

## Add the Jar files in Eclipse
[Jar files](https://github.com/Jenkins1128/SearchEngine/tree/master/SearchEngineJars)

0. Eclipse -> Preferences -> Java -> Build Path -> User Libraries 
1. Click New...
2. Add User library name. Ex: opennlp-tools
3. Click on the newly created user library and click Add External JARs...
4. Find the jar file(s) in your system explorer and add them to your library.

 Name: opennlp-tools 
 
 Jar file: opennlp-tools-1.9.1.jar

 Name : lang 
 
 Jar file: commons-lang3-3.9.jar

 Name: text
 
 Jar file: commons-text-1.8.jar

 Name: jetty
 
 Jar files: jetty-all-9.4.9.v20180320-uber.jar, javax.servlet-api-4.0.1.jar
 
 
 If the above libraries are not in your project build path...
 
 1. Right click on your project in Project Explorer
 2. Click Build Path -> Configure Build Path...
 3. Go to Java Build Path -> Libraries
 4. Click on Class Path to highlight.
 5. Click on Add Library...
 6. Double click User Libary and check jetty, lang, text, and opennlp-tools
 7. Click Finish
 8. Click Apply and Close.

## Run

-url: Seed URL my web crawler should initially crawl to build the inverted index.

-port: The port the web server should use to accept socket connections. 8080 is the default.

-limit: The total number of URLs to crawl (including the seed URL) when building the index. 50 is the default. 

-threads: The number of worker threads to use. 5 is the default.

### On Eclipse

```
$ java Driver -url https://www.cs.usfca.edu/~cs212/docs/jdk-12.0.2_doc-all/api/allclasses-index.html
-port 8080 -limit 50 -threads 3
```

You should see similar output below in your console within 10 to 30 seconds to show that the localhost is running.
```
2019-12-16 14:42:54.878:INFO::main: Logging initialized @13021ms to org.eclipse.jetty.util.log.StdErrLog
2019-12-16 14:42:55.477:INFO:oejs.Server:main: jetty-9.4.z-SNAPSHOT; built: 2018-03-20T05:21:10-07:00; git: 1f8159b1e4a42d3f79997021ea1609f2fbac6de5; jvm 12.0.2+10
2019-12-16 14:42:55.771:INFO:oejs.AbstractConnector:main: Started ServerConnector@36d64342{HTTP/1.1,[http/1.1]}{0.0.0.0:8080}
2019-12-16 14:42:55.797:INFO:oejs.AbstractConnector:main: Started ServerConnector@7ef99ea1{HTTP/1.1,[http/1.1]}{localhost:8080}
2019-12-16 14:42:55.798:INFO:oejs.Server:main: Started @13941ms
```

In your browser url, type 
```
localhost:<port number>

Ex: localhost:8080
```

Happy querying!

