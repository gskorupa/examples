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
        var self = this
        
        if(! opts.product){
            app.log('OPTS NOT AVAILABLE')
            self.product = {id:'',name:''}
        }else{
            self.product = opts.product
        }
        self.callback = opts.callback
        self.editMode = opts.editMode
        
        submit = function(e){
            e.preventDefault()
            //submit only if editMode
            self.product['id'] = e.target.elements['id'].value
            self.product['sku'] = e.target.elements['sku'].value
            self.product['name'] = e.target.elements['name'].value
            self.product['unit'] = e.target.elements['unit'].value
            self.product['price'] = e.target.elements['price'].value
            self.product['stock'] = e.target.elements['stock'].value
            sendData(self.product, 'POST', app.apiURL, self.callback, app.listener, null)
        }
    </script>
</app_product>
