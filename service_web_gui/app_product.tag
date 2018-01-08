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
