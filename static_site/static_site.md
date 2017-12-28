# Cricket for web development: static website

Java is generally not a language of choice used to build websites when you have limitted resources.
This is partly due to the fact that most popular Java application servers or portal platforms are very complex and often require considerable knowledge in order to be able to use them effectively. For this reason developers of websites choose other tools, such as for example the LAMP platform, to implement simple solutions.

However, if PHP isn't your preferred programming language and the website must be extended with additional functionality, you have to choose between modifying the current solution (with which you don't feel comfortable) or changing the platform to another one.

Fortunately, there is another option that allows you to create solutions quickly and easily from the very beginning, taking advantage of the huge potential of Java.

Cricket MSF allows you to quickly start building a variety of backend and web application solutions: from static websites through dynamic webapps to multi-node systems using micro services architecture.

## Let's start with preparing a static website

To create our example website we will use "Ballet One Page Free Website Template" offered for free by WebThemez
https://webthemez.com/ballet-one-page-free-website-template/

This website consists of one HTML file (index.html) with supporting CSS and JS files located in subfolders. All the website content is inside index.html so we can modify texts and images easily.
The one exception is the contact form which is located at the bottom of the webpage. Originally PHP must be installed on the server to handle request from this form. It is not provided with the free version of the template and we also do not want to use PHP in our solution. We will put this part of the website on hold for a while - we will return to this in the upcoming article, while discussing dynamic websites.

To prepare customized version of the website we need to:
* download the template
* unpack it to the folder of choice
* create folder structure as required by Cricket MSF
* modify texts inside index.html, change images and CSS files according to our needs

``` 
wget https://webthemez.com/downloads/free/ballet-one-page-free-web-template.zip
unzip ballet-one-page-free-web-template.zip mywebsite
cd mywebsite
mv ballet-one-page-free-web-templatete www
```

# Running the website

Depending on our preferences and we can run the website with Java SDK or using a Docker container.

The simplest way, if you have Docker installed, is to use dockerized 
```
docker run -it -p 8080:8080 -v /home/greg/workspace/Cricket/test:/usr/cricket/work cricket:test.1
```
