
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

    var getMessage = function(data) {
        var message = JSON.parse(data), out = {};
        out.message = message.message;
        out.time = new Date(message.time);
        if (_.contains(messageIds, message.id)) {
            out.self = true;
            messageIds = _.remove(messageIds, message.id);
        }
        return out;
    };

    var startListener = function() {
        socket.stomp.subscribe(service.ITEMS_TOPIC, function(data) {
            listenerItems.notify(getMessage(data.body));
        });

        socket.stomp.subscribe(service.MY_ITEMS_TOPIC, function(data) {
            listenerMyItems.notify(getMessage(data.body));
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