
app.factory('enchereService', function($q, $timeout) {

    var service = {}, listenerItems = $q.defer(), listenerMyItems = $q.defer(), socket = {
        client: null,
        stomp: null
    }, messageIds = [];

    service.RECONNECT_TIMEOUT = 30000;
    service.SOCKET_URL = "/enchere";
    service.ITEMS_TOPIC = "/topic/items";
    service.MY_ITEMS_TOPIC = "/topic/items/my";

    service.receiveItems = function() {
        return listenerItems.promise;
    };

    service.receiveMyItems = function() {
        return listenerMyItems.promise;
    };


    var reconnect = function() {
        $timeout(function() {
            initialize();
        }, this.RECONNECT_TIMEOUT);
    };

    var startListener = function() {
        socket.stomp.subscribe(service.ITEMS_TOPIC, function(data) {
            console.log("ITEMS_TOPIC", data)
            listenerItems.notify(data.body);
        });

        socket.stomp.subscribe(service.MY_ITEMS_TOPIC, function(data) {
            listenerMyItems.notify(data.body);
        });
    };

    var initialize = function() {
        socket.client = new SockJS(service.SOCKET_URL);
        socket.stomp = Stomp.over(socket.client);
        socket.stomp.connect({}, startListener);
        socket.stomp.onclose = reconnect;
    };

    initialize();
    return service;
});