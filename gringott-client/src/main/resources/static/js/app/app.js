
var app = angular.module("enchere", ['ngMaterial', 'ngMessages', 'ngRoute']);
app.config(function($routeProvider) {
    $routeProvider
        .when("/", {
            templateUrl : "views/main.htm"
        })
        .when("/enchere/add", {
            templateUrl : "views/addenchere.htm"
        })
        .when("/enchere", {
            templateUrl : "views/encherelist.htm",
            controller : "lesencheres"
        })
        .when("/enchere/my", {
            templateUrl : "views/myenchere.htm",
            controller : "mesEncheres"
        }).when("/sell", {
            templateUrl : "views/sell.htm",
            controller : "sellCtrl"
        }).when("/login",{
            templateUrl : "views/login.htm",
            controller : "Signin"
        });
});

app.controller('homeCtrl', function($scope){

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
                $location.path("/enchere/my");
            },
            function(err){
                console.log(err);
            }
        );

    };

});

app.factory('AuthService', function($http,$location){
    return {
        authenticate : function(name){

            var req = {
                method: 'POST',
                url: '/login',
                headers: {
                    'Content-Type': 'application/json'
                },
                data: name
            };

            $http(req).then(
                function(response){
                    console.log(response);
                    localStorage.setItem("name",name);
                    console.log(localStorage.getItem("name"));
                    $location.path("/sell");
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

app.controller("Signin",function ($scope, AuthService) {
    $scope.login = {
        pseudo : ''
    };

    $scope.addPseudo = function () {
        AuthService.authenticate($scope.login.pseudo)
    }
});

app.controller('mesEncheres', function ($scope,$http) {
    $scope.listEnchers = [];
    $scope.name = localStorage.getItem("name");

    $scope.getMesEncheres = function(name){
        var req = {
            method: 'GET',
            url:`/users/${name}/encheres`,
        };

        $http(req).then(
            function(response){
                $scope.listEnchers = response.data;
            },
            function(err){
                console.log(err);
            }
        );
    };

    var init = function () {
        $scope.getMesEncheres($scope.name);
    }

    init();
});

app.controller("lesencheres",function ($scope,$http) {

    $scope.lesEncheres = [];


    $scope.getEncheres = function () {
        var req = {
            method: 'GET',
            url: 'encheres',
            headers: {
                'Content-Type': 'application/json'
            }
        };

        $http(req).then(
            function(response){
                $scope.lesEncheres = response.data;
            },
            function(err){
                console.log(err);
            }
        );


    };


    var init =function() {
        $scope.getEncheres();
    };

    init();

});

