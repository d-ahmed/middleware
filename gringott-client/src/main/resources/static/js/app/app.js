var app = angular.module("enchere", ["ngRoute"]);
app.config(function($routeProvider) {
    $routeProvider
        .when("/", {
            templateUrl : "views/main.htm"
        })
        .when("/enchere/add", {
            templateUrl : "views/addenchere.htm"
        })
        .when("/enchere", {
            templateUrl : "views/encherelist.htm"
        })
        .when("/enchere/my", {
            templateUrl : "views/myenchere.htm"
        }).when("/sell", {
            templateUrl : "views/sell.htm"
        });
});


app.controller('sellCtrl', function($scope, $http) {
    $scope.item = {
        name : '',
        description : '',
        price: null,
        time: null
    };

    $scope.sell = function() {
        var req = {
            method: 'POST',
            url: '/sell',
            headers: {
                'Content-Type': 'application/json'
            },
            data: $scope.item
        }

        $http(req).then(
            function(response){
                console.log(response);
            },
            function(err){
                console.log(err);
            }
        );

    };
});