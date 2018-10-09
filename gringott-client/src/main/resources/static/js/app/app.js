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
        });
});


