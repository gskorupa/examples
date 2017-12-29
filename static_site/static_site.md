# Cricket for web development: static website

Java is generally not a language of choice used to build websites when you have limitted resources.
This is partly due to the fact that most popular Java application servers or portal platforms are very complex and often require considerable knowledge in order to be able to use them effectively. For this reason developers of websites choose other tools, such as for example the LAMP platform, to implement simple solutions.

However, if PHP isn't your preferred programming language and the website must be extended with additional functionality, you have to choose between modifying the current solution (with which you don't feel comfortable) or changing the platform to another one.

Fortunately, there is another option we can choose to create solutions quickly and easily from the very beginning, taking advantage of the huge potential of the Java language.

[Cricket MSF](https://cricketmsf.org) allows us to build a variety of backend and web application solutions: from static websites through dynamic webapps to multi-node systems using micro services architecture.

## Preparing the website

To prepare our website we need to:
* select and download the website template
* unpack it to the folder of choice
* create subfolders as required by Cricket MSF
* modify texts inside index.html, change images and CSS files according to our needs

To create our example website we will use "Ballet One Page Free Website Template" offered for free by WebThemez
https://webthemez.com/ballet-one-page-free-website-template/

This website consists of one HTML file (index.html) with supporting CSS and JS files located in subfolders. All the website content is contained in index.html so we can modify texts and images easily.
The one exception is the contact form located at the bottom of the webpage. Originally, PHP must be installed on the server to handle request from this form. Required PHP and JavaScript files are not provided with the free version of the template and it is OK because we also do not want to use PHP in our solution. Let's put this part of the website on hold for a while - we will return to this in the upcoming article, while discussing dynamic websites.

Our procedure consists of several simple steps, as shown in the following example. 

``` 
mkdir mywebsite
wget https://webthemez.com/downloads/free/ballet-one-page-free-web-template.zip
unzip ballet-one-page-free-web-template.zip mywebsite
cd mywebsite
mv ballet-one-page-free-web-templatete www
mkdir data
mkdir logs
```
After editing `index.html` according to our needs, the website is ready.

## Running the website on Docker container

Depending on our preferences and we can run the website with Java SDK or using a Docker container.

The simplest way (assuming that we have Docker installed) is to use dockerized Cricket MSF library which contains embeded HTTP server.

```
cd mywebsite
docker run -it -p 8080:8080 -v $(pwd):/usr/cricket/work gskorupa/cricket:website.1.0
```
When run with `-it` option, we can see the service output on the terminal and can stop the service by pressing `Ctrl-C`.

Our newly created website is visible at http://localhost:8080

## Running the website on Java

It is also easy to run our website without Docker when we have Java 8 or 9 installed. One additional step in this case is downloading the Cricket MSF library.

```
cd mywebsite
wget https://github.com/gskorupa/Cricket/releases/download/1.2.33/cricket-1.2.33.jar
java -jar cricket-1.2.33.jar -r
```
The runtime parameters are preconfigured in the library and the website is available at http://localhost:8080

## Summary
In the article we have built a static website based on the available free website template. We launched it with Java using the Cricket MSF.
The same procedure can be used to implement similar static websites using our favourite programming language.

The next step will be to add dynamically managed elements, i.e. possibility to modify the content and handle the contact form. We will write about it in an upcoming article.
