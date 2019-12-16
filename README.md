# Full-Stack Multithreaded Search Engine  

Throughout my Software Development course, I developed a full-stack search engine using Java. This project involved advanced topics such as inheritance and polymorphism, multithreaded programming, networking, and web development. Also, throughout this course, I've learned useful techniques for designing, debugging, refactoring, and reviewing code.

![Search](https://github.com/usf-cs212-fall2019/project-Jenkins1128/blob/master/Project/src/Search.png)
![Results](https://github.com/usf-cs212-fall2019/project-Jenkins1128/blob/master/Project/src/Results.png)


## Run

-url : Seed URL my web crawler should initially crawl to build the inverted index.

-port : The port the web server should use to accept socket connections. 8080 is the default.

-limit : The total number of URLs to crawl (including the seed URL) when building the index. 50 is the default. 

-threads : The number of worker threads to use. 5 is the default.

### On Eclipse

```
$ java Driver -url https://www.cs.usfca.edu/~cs212/docs/jdk-12.0.2_doc-all/api/allclasses-index.html
-port 8080 -limit 50 -threads 3
```
