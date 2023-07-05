// server.js
var jsonServer = require('json-server')
var server = jsonServer.create()
var router = jsonServer.router('data.json')
var middlewares = jsonServer.defaults()

server.use(middlewares)


const myRoutes = {
    "/api/users/me": "/api_users_me",
    "/api/users/me/workspaces": "/api_users_me_workspaces",
    "/api/workspaces/1/channels": "/api_workspaces_1_channels",
    "/api/workspaces": "/api_workspaces",
    "/api/workspaces/1/channels": "/api_workspaces_1_channels",
    "/api/workspaces/1/users":"/api_workspaces_1_users",
    "/api/workspaces/1":"/api_workspaces_1",
    "/api/workspaces/1/channels/1/messages":"/api_workspaces_1_channels_1_messages"
}

server.use(jsonServer.bodyParser)
server.use(function (req, res, next) {
    if (req.method === 'POST') {
        // Converts POST to GET and move payload to query params
        // This way it will make JSON Server that it's GET request
        req.method = 'GET'
        req.query = req.body
    }

    req.url = myRoutes[req.url] || req.url;

    next()
})

// If you need to scope this behaviour to a particular route, use this

server.use(router)
server.listen(8080, function () {
    console.log('JSON Server is running')
})