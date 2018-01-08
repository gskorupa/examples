route(function(id){
    switch (id){
        case 'store':
            app.currentPage = 'store';
            break;
        case '':
        case 'main':
            app.currentPage = 'main';
            break;
    }
    riot.update();
})