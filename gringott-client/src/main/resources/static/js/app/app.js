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
        }).when("/login",{
            templateUrl : "views/login.htm"
        });
});



app.factory('AuthService', function($http){
    return {
        authenticate : function(name){

            var req = {
                method: 'POST',
                url: '/login',
                headers: {
                    'Content-Type': 'application/json'
                },
                data: name
            }

            $http(req).then(
                function(response){
                    console.log(response);
                },
                function(err){
                    console.log(err);
                }
            );

            /*if(isAuthenticated){
                //If authenticated, return anything you want, probably a user object
                // return true;
            } else {
                //Else send a rejection
                // return $q.reject('Not Authenticated');
            }*/
        }
    }
});


app.controller('sellCtrl', function($scope, $http,$location) {
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
                $location.url("/#!/enchere");
            },
            function(err){
                console.log(err);
            }
        );

    };

});

app.controller("Signin",function ($scope, AuthService) {
    $scope.login = {
        pseudo : ''
    };

    $scope.addPseudo = function () {
        AuthService.authenticate($scope.login.pseudo)
    }
});

