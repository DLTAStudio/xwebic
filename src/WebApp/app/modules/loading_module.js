/**
 * Created by dscreve on 29/01/2016.
 */
'use strict';

(function() {
    var loadingmodule = angular.module("loading-module",['ngMessages','ui.bootstrap','ws-module','shared-module']);

    loadingmodule.factory('callCheckTokenWS',['callWS','sharedService',function(callWS,sharedService) {
        return function(controller) {

            var url = sharedService.baseWS+"/token/check";
            var token = sharedService.getToken();

            if (token) {
                url+="/"+token;
            }

            callWS(url,'GET',[],null,
                function(httpResult,contentResult,headerResult) {
                    // on Success
                    controller.displayBots();
                },
                function(httpResult,contentResult,headerResult) {
                    // onFailure
                    controller.displayLogin();
                });

        };
    }]);


    loadingmodule.controller('LoadingCtrl',['$rootScope','$location','sharedService','callCheckTokenWS',
        function($rootScope,$location,sharedService,callCheckTokenWS) {

        this.displayLogin = function() {
            $location.path('/login');
        }

        this.displayBots = function() {
            $location.path('/bots');
        }

        callCheckTokenWS(this);

    }]);


})();