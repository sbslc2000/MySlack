// server.js
let jsonServer = require('json-server')
let server = jsonServer.create()
let router = jsonServer.router('data.json')
let middlewares = jsonServer.defaults()

server.use(middlewares)

server.use(jsonServer.bodyParser)
server.use(function (req, res, next) {
    if (req.method === 'POST') {
        // Converts POST to GET and move payload to query params
        // This way it will make JSON Server that it's GET request
        req.method = 'GET'
        req.query = req.body
    }

    req.url = req.url.replaceAll("/", "_");
    if(req.url.at(0) === "_") {
        req.url = "/" + req.url.substring(1);
    }
    next();
})

// If you need to scope this behaviour to a particular route, use this

server.use(router)
server.listen(8080, function () {
    console.log('JSON Server is running')
})