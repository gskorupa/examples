# How to build minimalistic web GUI for your microservice

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

## Page view

Przygotujemy uproszczony template strony że stałym menu oraz trzema kartami prezentującym: strona startową,  przeglądarkę produktów oraz formularz dodawania produktu. W tym celu wykorzystamy grid system  bootstrapa.

KOD

## Application logic

Do zaimplementowania nawigacji wykorzystamy bibliotekę Router. Odpowiada ona za ...

KOD

## Uruchomienie serwisu

Before the next step we need to run our Store Service as described in the [previous  article](https://www.signocom.com/software-development/microservice-with-java-in-a-flash-not-a-problem-at-all/).

Do folderu, w którym utworzyliśmy nasz plik index.html z szablonem strony, pobieramy kod źródłowe serwisu, kompilujemy i uruchamiamy.

We leave the service going to be able to observe effects of our development on the fly.

## JavaScript - routing
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
### JavaScript - data access
```
```

## Page Components

Komponenty zawierają kod HTML, CSS i JavaScript

### Page Header
```
<app_nav>
    <nav class="nav justify-content-end navbar-dark bg-primary">
        <a class="nav-link text-white" href="#">Home</a>
        <a class="nav-link text-white"" href="#store">Products</a>
    </nav>
</app_nav>
```
### Page Footer
```
<app_footer>
    <div class="bg-primary text-white fixed-bottom">&nbsp;&COPY; 2018 SIGNOCOM Sp. z o.o.</div>
</app_footer>
```
### Home Page
```
<app_home>
    <div class="container-fluid" if={ app.currentPage === "main" }>
        <div class="row">
            <div class="col text-center">
                <h1 style="margin-top: 10px;">Store Service</h1>
                <img style="width: 300px" src="https://upload.wikimedia.org/wikipedia/commons/2/2a/Flower_of_life_91-circles.svg">
            </div>
        </div>
        <div class="row">
            <div class="col">
                <p style="margin-top: 10px;">This is an example Single Page Application using the REST API of the background microservice.</p>
                <p>The source code and the documentation is available on <a href="https://github.com/signocom/examples/tree/master/service_web_gui">GitHub</a></p>
            </div>
        </div>
    </div>
</app_home>
```
### Product Browser
```
<app_store>
    <div class="container-fluid" if={ app.currentPage === "store" }>
        <div class="modal"  id="poductModalWindow" tabindex="-1" role="dialog">
            <div class="modal-dialog modal-dialog-centered" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title">{ formTitle }</h5>
                            <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                            </button>
                    </div>
                    <div class="modal-body">
                        <app_product></app_product>
                    </div>
                </div>
            </div>
        </div>
        <div class="row">
            <div class="col"><h2 style="margin-top: 10px;">Product list</h2></div>
        </div>
        <div class="row">
            <div class="col">
                <button type="button" class="btn btn-outline-primary" onclick={ refresh }>Refresh</button> 
                <button type="button" class="btn btn-outline-primary" onclick={ addNew }>Add new</button>
            </div>
        </div>
        <div class="row" if={ alertToShow }>
             <div class="col">
                <div class="alert alert-success" role="alert">
                    This is a success alert—check it out!
                </div>
            </div>
        </div>
        <div class="row">
            <div class="col">
                <table class="table table-sm" style="margin-top: 10px;">
                    <thead>
                        <tr><th>ID</th><th>NAME</th><th>&nbsp;</th></tr>
                    </thead>
                    <tbody>
                        <tr each={ productList }>
                            <td>{ id }</td><td>{ name }</td><td><a href='' title='show details' onclick={ showDetails }>[...]</a></td>
                        </tr>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
    
    <script>
        var self = this
        this.editing = false
        this.alertToShow = false
        this.productList = []
        this.formTitle = ''
        
        self.showDetails = function(e){
            e.preventDefault()
            this.formTitle = 'Product card'
            riot.mount('app_product',{
                product: e.item,
                editMode: false,
                callback: this.hideDetails
            })
            $('#poductModalWindow').modal('show')
        }
        
        self.hideDetails = function(e){
            console.log('CLOSING FORM')
            $('#poductModalWindow').modal('hide')
            self.refresh(null)
        }
        
        self.addNew = function(e){
            this.formTitle = 'New product'
            var newProduct = {id:'',name:'',sku:'',unit:'',price:'',stock:''}
            riot.mount('app_product',{
                product: newProduct,
                editMode: true,
                callback: this.hideDetails
            })
            $('#poductModalWindow').modal('show')
        }
        
        self.updateList = function (text, message) {
            app.log('RESULT: ' + text)
            self.productList = JSON.parse(text)
            self.update()
        }
        
        self.refresh = function(e){
            getData(app.apiURL,
                    null,
                    self.updateList,
                    app.listener,
                    'OK',
                    null
                    )
        }

    </script>
</app_store>

```

### Product View
```
<app_product>
    <form onsubmit={ submit }>
        <div class="form-row">
            <div class="form-group col-md-6">
                <label for="id">ID</label>
                <input type="text" class="form-control" id="id" placeholder="Product ID" required="true" value={ product.id } readonly={ !editMode }>
            </div>
            <div class="form-group col-md-6">
                <label for="sku">SKU</label>
                <input type="text" class="form-control" id="sku" placeholder="Product SKU" required="true" value={ product.sku } readonly={ !editMode }>
            </div>
        </div>
        <div class="form-row">
            <div class="form-group col-md-12">
                <label for="name">Name</label>
                <input type="text" class="form-control" id="name" placeholder="Product name" required="true" value={ product.name } readonly={ !editMode }>
            </div>
        </div>
        <div class="form-row">
            <div class="form-group col-md-6">
                <label for="unit">Unit</label>
                <input type="text" class="form-control" id="unit" placeholder="Unit name" required="true" value={ product.unit } readonly={ !editMode }>
            </div>
            <div class="form-group col-md-6">
                <label for="price">Unit price</label>
                <input type="text" class="form-control" id="price" placeholder="Price per unit" 
                       pattern="[0-9]*[.]?[0-9]+"
                       required="true" value={ product.unitPrice } readonly={ !editMode }>
            </div>
        </div>
        <div class="form-row">
            <div class="form-group col-md-12">
                <label for="stock">Available in stock</label>
                <input type="text" class="form-control" id="stock" placeholder="Value" 
                       pattern="[0-9]*"
                       required="true" value={ product.stock } readonly={ !editMode }>
            </div>
        </div>
        <button type="submit" class="btn btn-primary" if={ editMode }>Save</button>
        <button type="button" class="btn btn-secondary" onclick={ callback }>Cancel</button>
    </form>
    <script>
        
        if(! opts.product){
            console.log('OPTS NOT AVAILABLE')
            this.product = {id:'',name:''}
        }else{
            this.product = opts.product
        }
        this.callback = opts.callback
        this.editMode = opts.editMode

        this.on('mount', function() {
            // access to child tag
            console.log('MOUNTED')
        })
        this.on('unmount', function() {
            // access to child tag
            console.log('UNMOUNTED')
        })
        
        submit = function(e){
            e.preventDefault()
            //submit only if editMode
            this.product['id'] = e.target.elements['id'].value
            this.product['sku'] = e.target.elements['sku'].value
            this.product['name'] = e.target.elements['name'].value
            this.product['unit'] = e.target.elements['unit'].value
            this.product['price'] = e.target.elements['price'].value
            this.product['stock'] = e.target.elements['stock'].value
            sendData(this.product, 'POST', app.apiURL, this.callback, app.listener, 'OK', null)
            //this.callback(e)
        }
    </script>
</app_product>

```

## The final effect

To be fully functional, our application needs some additional elements such as: modification and removal of products and user authentication. However, this is the subject of another article.

## Summary

We saw how quickly and easily we can build responsive webapplications using Bootstrap and Riot libraries.

Using the microservice as a backend logic causes that both parts of the service: frontend and backend are well separated and can easily be developed independently. Assuming that we maintain the compliance of the API used.

We hope that we have been able to present the usefulness of the "API first" approach and encourage you to try Bootstrap and Riot as a tools for building web applications quickly and efficiently.

---
#### Resources
This article along with complete source codes of the service and the webapplication is available on GitHub: https://github.com/signocom/examples/edit/master/service_web_gui

Bootstrap: https://getbootstrap.com/

Riot: http://riotjs.com/

Riot Router: https://github.com/riot/route

