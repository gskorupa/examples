var eventListener = riot.observable()
var app = {
    "currentPage": "main",
    "debug": false,
    "log": function(message){if(app.debug){console.log(message)}},
    "listener": eventListener,
    "apiURL": 'http://localhost:8080/api/store'
}

function getData(url, query, callback, eventListener, errorEventName) {

    var oReq = new XMLHttpRequest();
    var defaultErrorEventName = "err:";
    oReq.onerror = function (oEvent) {
        app.log("onerror " + this.status + " " + oEvent.toString())
        eventListener.trigger("auth"+this.status);
    }
    oReq.onreadystatechange = function () {
        if (this.readyState == 4) {
            if (this.status == 200) {
                app.log(JSON.parse(this.responseText));
                callback(this.responseText);
            } else {
                var tmpErrName
                if (errorEventName == null) {
                    tmpErrName=defaultErrorEventName + this.status
                } else {
                    tmpErrName=errorEventName
                }
                eventListener.trigger(tmpErrName);
            }
        }
    };
    oReq.open("get", url, true);
    oReq.send(query);
    return false;
}

function sendData(data, method, url, callback, eventListener, errorEventName) {
    app.log("sendData ...")
    var urlEncodedData = "";
    var urlEncodedDataPairs = [];
    var name;
    var oReq = new XMLHttpRequest();
    var defaultErrorEventName = "err:";
    // Turn the data object into an array of URL-encoded key/value pairs.
    for (name in data) {
        urlEncodedDataPairs.push(encodeURIComponent(name) + '=' + encodeURIComponent(data[name]));
    }
    // Combine the pairs into a single string and replace all %-encoded spaces to 
    // the '+' character; matches the behaviour of browser form submissions.
    urlEncodedData = urlEncodedDataPairs.join('&').replace(/%20/g, '+');
    oReq.onerror = function (oEvent) {
        app.log("onerror " + this.status + " " + oEvent.toString())
        callback(oEvent.toString());
    }
    oReq.onreadystatechange = function () {
        if (this.readyState == 4) {
            if (this.status > 199 && this.status < 203) {
                app.log(JSON.parse(this.responseText));
                callback(this.responseText);
            } else {
                app.log("onreadystatechange")
                if (errorEventName == null) {
                    eventListener.trigger(defaultErrorEventName + this.status);
                } else {
                    eventListener.trigger(errorEventName);
                }
            }
        }
    }
    oReq.open(method, url);
    oReq.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
    oReq.send(urlEncodedData);
    return false;
}
