# Web GUI for your microservice - quick and easily

*NOT FINISHED*

It seems that minimalism while building web interfaces for microservices is a quite reasonable idea. We save learning time and development costs, reduce risk of errors due to a smaller code size and fewer libraries used, we make it easier to maintain and reduce the upgrade costs. 
There are also cons - minimalism probably means a smaller number of cool visual elements. However, if our client accepts clear, ergonomic solutions, which is quite likely in the case of business applications, then we can all be winners.

In the further part of this article we will see how to build a web GUI that meets the following assumptions:
* 'mobile first' design in accordance with RWD
* small amount of libraries to download
* small source  code
* easy to learn

Following the API-first development approach, in the [previous  article](https://www.signocom.com/software-development/microservice-with-java-in-a-flash-not-a-problem-at-all/) we have built the store microservice exposing the REST API. We will use now this service as our backend (business logic). 

In order to meet the above requirements, we will use:
* Bootstrap component library
* RIOT UI library
* a small dose of JavaScript

The solution we will build is really small, all files together are XX kB. Of which:
* Bootrstrap
* Riot + Router
* kod webaplikacji

An additional advantage is that apart from running the Store Service, we do not need to install anything else. We only need a text editor and a web browser.

## Web application structure
TODO
SCREENSHOT

## Page view
We will prepare a simplified website template that has a static menu and two pages: home page and product browser. To get a responsive view we use the Boostrap grid system.

SCREENSHOTS

We will start form the standard Bootstrap template `index.html`file.
```
<!doctype html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta.3/css/bootstrap.min.css" integrity="sha384-Zug+QiDoJOrZ5t4lssLdxGhVrurbmBWopoEl+M6BdEfwnCJZtKxi1KgxUyJq13dy" crossorigin="anonymous">
  </head>
  <body>
      <header>
          <!-- Riot components -->
      </header>
      <main>
          <!-- Riot components -->
      </main>
      <footer>
          <!-- Riot components -->
      </footer>
    <!-- jQuery first, then Popper.js, then Bootstrap JS -->
    <script src="https://code.jquery.com/jquery-3.2.1.slim.min.js" integrity="sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN" crossorigin="anonymous"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js" integrity="sha384-ApNbgh9B+Y1QKtv3Rn7W3mgPxhU9K/ScQsAP7hUibX39j7fakFPskvXusvfa0b4Q" crossorigin="anonymous"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta.3/js/bootstrap.min.js" integrity="sha384-a5N7Y/aK3qNeh15eJKGWxsqtnX/wWdSZSKp+81YjTmS15nvnvxKHuzaWwXHDli+4" crossorigin="anonymous"></script>
    <!-- end Bootstrap part -->
    
    <!-- Riot scripts -->
    
  </body>
</html>
```
Then the Riot part: we modify `<!-- Riot components -->` and `<!-- Riot scripts -->` with component tags and script declatations:
```
<body>
      <header>
          <app_nav></app_nav>
      </header>
      <main>
          <app_home></app_home>
          <app_store></app_store>
      </main>
      <footer>
          <app_footer></app_footer>
      </footer>
    <!-- jQuery first, then Popper.js, then Bootstrap JS -->
    ....
    <!-- end of Bootstrap part -->
    
    <!-- Riot scripts -->
    <script src="https://cdnjs.cloudflare.com/ajax/libs/riot/3.8.1/riot+compiler.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/riot-route@3.1.2/dist/route.min.js"></script>
    <!-- Application scripts -->
    <script src="js/routing.js"></script>
    <script src="js/app.js"></script>
    <!-- loading Riot tags -->
    <script data-src="components/app_navigation.tag" type="riot/tag"></script>
    <script data-src="components/app_home.tag" type="riot/tag"></script>
    <script data-src="components/app_store.tag" type="riot/tag"></script>
    <script data-src="components/app_footer.tag" type="riot/tag"></script>
    <script data-src="components/app_product.tag" type="riot/tag"></script>
    <script>
        riot.mount('*');
        route.start(true);
    </script>
  </body>
```

## Application logic
We don't need a lot of code to run our application.  It is enough for us to present components depending on the state of the application and to access data through the service API. The other parts, i. e. the visual side and data presentation, will give us Bootstrap and Riot.

### Navigation
Navigation is very simple with the Riot Router. To do this, we will create a `routing. js` file.
```
route(function(id){
    switch (id){
        case "store":
            app.currentPage = "store";
            break;
        case "":
        case "main":
            app.currentPage = "main";
            break;
    }
    riot.update();
})
```
The `route` method is called every time when reference is made to links defined in the application menu (component `app_nav`). It changes the property `currentPage` of a global object `app`.
```
<app_nav>
    <nav class="nav justify-content-end navbar-dark bg-primary">
        <a class="nav-link text-white" href="#">Home</a>
        <a class="nav-link text-white"" href="#store">Products</a>
    </nav>
</app_nav>
```
The `app` object is defined in the `app. js` file. In the same file there are also methods of accessing REST API: `getData` and `sendData`. We will not explain them here, because they contain the standard use of XMLHttpRequest object, described in many sources.

### Component visibility
Now, when we have `app.currentPage` dependent of the web application context we can show or hide page elements using Riot's `if` condition. Simple and easy.

```
<app_store>
    <div class="container-fluid" if={ app.currentPage === "store" }>
    <!-- the body of the component goes here -->
    </div>
</app_store>
```

### Data access
The most important part - access to backend data. Using XMLHttpRequest and our getData and sendData methods we communicate with the service API.

Thanks to expressions and loops provided by Riot, we can easily illustrate the result of our query.
For example, download and view the product list. Let's look at the fragments of the component source code.
```
<app_store>
...
  <table class="table table-sm" style="margin-top: 10px;">
    <tr each={ productList }>
      <td>{ id }</td><td>{ name }</td><td><a href='' title='show details' onclick={ showDetails }>[...]</a></td>
    </tr>
  </table>
...
<script>
  ...
  self.refresh = function(e){
    // send request to get product list
    getData('http://localhost:8080/api/store',
            null,
            self.updateList, // the callback function called when the data is ready, passing response text
            app.listener,
            null
           )
  }
  self.updateList = function (text) {
    self.productList = JSON.parse(text)
    self.update() // updates the data visualisation using Riot's `each` loop.
  }
  
</script>
...
</app_store>
```
I suggest Riot documentation to get information about data visualization methods.

## Uruchomienie serwisu

Before the next step we need to run our Store Service as described in the [previous  article](https://www.signocom.com/software-development/microservice-with-java-in-a-flash-not-a-problem-at-all/).

Do folderu, w którym utworzyliśmy nasz plik index.html z szablonem strony, pobieramy kod źródłowe serwisu, kompilujemy i uruchamiamy.

We leave the service going to be able to observe effects of our development on the fly.

## Page Components

Komponenty zawierają kod HTML, CSS i JavaScript

## What is left to do

To be fully functional, our application needs some additional elements such as: modification and removal of products and user authentication. However, this is the subject of another article.

## Summary

We saw how quickly and easily we can build responsive web applications with Bootstrap and Riot libraries.

Using the microservice as a backend logic guarantees that both parts of the service: frontend and backend are well separated and can easily be developed independently. Assuming that we maintain the compliance of the API used.

We hope that we have been able to present the usefulness of the "API first" approach and encourage you to try Bootstrap and Riot as a tools for building web applications quickly and efficiently.

---
#### Resources
This article along with complete source codes of the service and the webapplication is available on GitHub: https://github.com/signocom/examples/edit/master/service_web_gui

Article on our backend service design: https://www.signocom.com/software-development/microservice-with-java-in-a-flash-not-a-problem-at-all/

Bootstrap: https://getbootstrap.com/

Riot: http://riotjs.com/

Riot Router: https://github.com/riot/route

XMLHttpRequest: https://developer.mozilla.org/en-US/docs/Web/API/XMLHttpRequest
