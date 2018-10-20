
app.factory('enchereService', function($q, $timeout) {

    var service = {}, listenerItems = $q.defer(),listenerMyItems = $q.defer(),listenerWonItems = $q.defer(),listenerMyName = $q.defer(), socket = {
        client: null,
        stomp: null
    }, messageIds = [];

    service.RECONNECT_TIMEOUT = 30000;
    service.SOCKET_URL = "/enchere";
    service.ITEMS_TOPIC = "/topic/items";
    service.MY_ITEMS_TOPIC = "/topic/items/my";
    service.MY_WON_TOPICS = "/topic/items/won";
    service.MY_NAME = "/topic/name";

    service.receiveItems = function() {
        return listenerItems.promise;
    };

    service.receiveMyItems = function() {
        return listenerMyItems.promise;
    };

    service.receiveWonItems = function() {
        return listenerWonItems.promise;
    };

    service.receiveMyName = function() {
        return listenerMyName.promise;
    };


    var reconnect = function() {
        $timeout(function() {
            initialize();
        }, this.RECONNECT_TIMEOUT);
    };

    var startListener = function() {
        socket.stomp.subscribe(service.ITEMS_TOPIC, function(data) {
            listenerItems.notify(data.body);
        });

        socket.stomp.subscribe(service.MY_ITEMS_TOPIC, function(data) {
            listenerMyItems.notify(data.body);
        });

        socket.stomp.subscribe(service.MY_WON_TOPICS, function(data) {
            console.log(data.body);
            listenerWonItems.notify(data.body);
        });

        socket.stomp.subscribe(service.MY_NAME, function(data) {
            listenerMyName.notify(data.body);
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