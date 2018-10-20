
var app = angular.module("enchere", ['ngMaterial', 'ngMessages', 'ngRoute']);

app.run(function ($rootScope,$location,$route,AuthService) {
    var name = localStorage.getItem("name");
    if (name){
        AuthService.authenticate(name);
    } else {
        $location.path("/login");
    }
});

app.config(function($routeProvider) {
    $routeProvider
        .when("/", {
            templateUrl : "views/main.htm"
        })
        .when("/enchere/add", {
            templateUrl : "views/addenchere.htm",
            controller : "addEnchere",
            resolve : {
                'auth' : function(AuthGuard){
                    return AuthGuard.authenticate();
                }
            }
        })
        .when("/enchere", {
            templateUrl : "views/encherelist.htm",
            controller : "lesencheres",
            resolve : {
                'auth' : function(AuthGuard){
                    return AuthGuard.authenticate();
                }
            }
        })
        .when("/enchere/my", {
            templateUrl : "views/myenchere.htm",
            controller : "mesEncheres",
            resolve : {
                'auth' : function(AuthGuard){
                    return AuthGuard.authenticate();
                }
            }
        }).when("/enchere/win", {
            templateUrl : "views/enchereWin.htm",
            controller : "enchereWin",
            resolve : {
               'auth' : function(AuthGuard){
                    return AuthGuard.authenticate();
                }
            }
        }).when("/login",{
            templateUrl : "views/login.htm",
            controller : "Signin"
        });
});

app.controller('homeCtrl', function($scope){

});


app.controller('enchereWin', function($scope,$http,enchereService){
    $scope.message = "Mes victoires";

    $scope.mesVictoires = [];
    $scope.name = localStorage.getItem("name");
    var req = {
        method: 'get',
        url: '/users/'+$scope.name+'/victoires',
        headers: {
            'Content-Type': 'application/json'
        }
    };

    $http(req).then(
        function(response){
            console.log(response);
            $scope.mesVictoires = response.data;
        },
        function(err){
            console.log(err);
        }
    );

    enchereService.receiveWonItems().then(function(greeting) {
        console.log('Success: ' + greeting);
    }, function(reason) {
        console.log('Failed: ' + reason);
    }, function(update) {
        console.log('Got notification: ' + update);
        $scope.mesVictoires = JSON.parse(update);
    });


});

app.controller('addEnchere', function($scope, $http,$location) {
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
        };

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


app.controller("Signin",function ($scope,$http, AuthService) {
    $scope.login = {
        pseudo : ''
    };

    var name = localStorage.getItem("name");
    if (name) {
        var req = {
            method: 'POST',
            url: `/logout`,
            headers: {
                'Content-Type': 'application/json'
            },
            data:  name

        };

        $http(req).then(
            function (response) {
                localStorage.removeItem("name");
            },
            function (err) {
                console.log(err);
            }
        );
    }

    $scope.addPseudo = function () {
        AuthService.authenticate($scope.login.pseudo)
    }
});

app.controller('mesEncheres', function ($scope,$http,enchereService) {
    $scope.listEnchers = [];
    $scope.name = localStorage.getItem("name");

    $scope.getMesEncheres = function(name){
        var req = {
            method: 'GET',
            url:`/users/${name}/encheres`
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
        enchereService.receiveMyItems().then(function(greeting) {
            console.log('Success: ' + greeting);
        }, function(reason) {
            console.log('Failed: ' + reason);
        }, function(update) {
            console.log('Got notification: ' + update);
            $scope.listEnchers = JSON.parse(update);
        });
    };

    init();
});

app.controller("lesencheres",function ($scope,$http, enchereService) {

    $scope.lesEncheres = [];
    $scope.name = localStorage.getItem("name");

    $scope.getRandomSpan =  function (max){
        // console.log(max);
        return Math.floor((Math.random()*max)+1);
    };


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

    $scope.enrichir= function(item,nombre){
        $scope.message  = null;
        console.log(nombre);
        if (nombre <= item.currentPrice) {
            $scope.message = "Montant doit etre superieur ou egal au prix de courant";
            return;
        }

        var localItem = deepCopy(item);
        localItem.currentPrice = nombre;

        var req = {
            method : "POST",
            url:'/users/'+$scope.name+'/bid',
            headers: {
                'Content-Type': 'application/json'
            },
            data: localItem

        };

        $http(req).then(
            function (response) {
                console.log(response);
                $scope.message = "Bid envoye";
            },
            function (err) {
                console.log(err)
            }
        )
    };

    var init =function() {
        $scope.getEncheres();
        console.log("Dans getEncheres", enchereService.RECONNECT_TIMEOUT);
        enchereService.receiveItems().then(function(greeting) {
            console.log('Success: ' + greeting);
        }, function(reason) {
            console.log('Failed: ' + reason);
        }, function(update) {
            console.log('Got notification: ' + update);
            $scope.lesEncheres = JSON.parse(update);
            $scope.message = null;
        });
    };

    init();

});

app.controller("menuCtrl", function ($scope,enchereService) {
   $scope.name = localStorage.getItem("name");

    enchereService.receiveMyName().then(function(greeting) {
        console.log('Success: ' + greeting);
    }, function(reason) {
        console.log('Failed: ' + reason);
    }, function(update) {
        console.log('Got notification: ' + update);
        $scope.name = update;
    });

});

//---------------------- Directive ---------------------
app.directive("enchereDetail",function () {
    return {
        scope: {
            item : "="
        },
        templateUrl:"views/enchereDetail.htm"
    }
});

app.directive("menuApp",function () {
   return{
       templateUrl:"views/menu.htm",
       link: function ($scope) {
           $scope.name = localStorage.getItem("name");
       }
   }
});

//---------------------- Factory ---------------------

app.factory('AuthService', function($http,$route,$location){
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
                   // console.log(response);
                    localStorage.setItem("name",name);
                    $location.path("/enchere/add");
                },
                function(err){
                    console.log(err);
                }
            );

        }
    }
});

app.factory('AuthGuard',function ($q,$location) {
    return {
        authenticate : function(){
            //Authentication logic here
            var name = localStorage.getItem("name");
            if(!name){
                $location.path("/login");
                return $q.reject('Not Authenticated');
            }
        }
    }
});

//---------------------- Functions ---------------------
var deepCopy = function (toCopy) {
    return JSON.parse(JSON.stringify(toCopy));

}