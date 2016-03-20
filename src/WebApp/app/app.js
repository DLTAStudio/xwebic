'use strict';

// Declare app level module which depends on views, and components
var app = angular.module("app-module",['ngRoute','loading-module','login-module','shared-module',
    'bot-module','integration-module','angular-loading-bar','pascalprecht.translate']);


app.config(['$routeProvider','$translateProvider',function($routeProvider,$translateProvider) {
    $routeProvider.when('/', {templateUrl:'./views/loading.html'})
        .when('/login', {templateUrl:'./views/login.html'})
        .when('/bots', {templateUrl:'./views/bots.html'})
        .otherwise({redirectTo:'/'});


    $translateProvider.useSanitizeValueStrategy('escape');
    $translateProvider.useStaticFilesLoader({
        prefix: 'locales/locale-',
        suffix: '.json'
    });

    $translateProvider.registerAvailableLanguageKeys(['en','fr'], {
        'en_*': 'en',
        'fr_*': 'fr'
    });

    $translateProvider.determinePreferredLanguage();
    $translateProvider.fallbackLanguage('en');
}]);



app.controller('ApplicationCtrl',['$rootScope','$location','sharedService','$scope',function($rootScope,$location,sharedService,$scope) {

    $rootScope.sharedService = sharedService;

    this.onDisconnectButton = function() {
        sharedService.setToken(null);
        $rootScope.loggedIn=false;
        $location.path('/login');
    };

    $rootScope.loggedIn=(sharedService.getToken()!=null);

    var that=this;
    $scope.$on("loggedIn",function(event,data) {
        $rootScope.loggedIn=data;
        if ($rootScope.loggedIn) {
            $location.path('/bots');
        }
        else {
            $location.path('/login');

        }
    });
}]);

app.controller('MainBotCtrl',['$rootScope','$location','sharedService',function($rootScope,$location,sharedService) {

    if (!$rootScope.loggedIn) {
        $location.path('/');
        return;
    }
}]);


