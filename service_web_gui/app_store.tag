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
