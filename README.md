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

* Name: opennlp-tools 
* Jar file: opennlp-tools-1.9.1.jar

* Name : lang 
* Jar file: commons-lang3-3.9.jar

* Name: text
* Jar file: commons-text-1.8.jar

* Name: jetty
* Jar files: jetty-all-9.4.9.v20180320-uber.jar, javax.servlet-api-4.0.1.jar

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
