/**
 * Created by dscreve on 18/09/15.
 */
'use strict';

(function() {
    var loginmodule = angular.module("login-module",['ngMessages','ui.bootstrap','ws-module','shared-module']);

    loginmodule.factory('callLoginWS',['callWS','sharedService',function(callWS,sharedService) {
        return function(login,password,controller) {

            var url = sharedService.baseWS+"/token";
            var headers=[
                            {name:'login',value:login},
                            {name:'password',value:password}
                        ];

            callWS(url,'GET',headers,null,
                function(httpResult,contentResult,headerResult) {
                // on Success
                    controller.onLoginSucceeded(httpResult,contentResult,headerResult);
                },
                function(httpResult,contentResult,headerResult) {
                // onFailure
                    controller.onLoginFailed(httpResult,contentResult,headerResult);
                 });

         };
    }]);

    loginmodule.controller('LoginCtrl',['callLoginWS','$scope','$translate','sharedService',
        function(callLoginWS,$scope,$translate,sharedService) {
        this.message="";
        this.loading=false;
        this.onLogin = function() {
            this.loading=true;
            callLoginWS(this.login,this.password,this);
        }

        this.onLoginSucceeded = function(httpResult,contentResult,headerResult) {
            var resultObj = JSON.parse(contentResult);

            sharedService.setToken(resultObj.token);
            this.loading=false;
            $scope.$emit("loggedIn",true);
        }

        this.onLoginFailed = function(httpResult,contentResult,headerResult) {
            var resultObj = JSON.parse(contentResult);
            if (resultObj) {
                this.message = $translate.instant(resultObj.message);
            }
            else {
                this.message = $translate.instant("SERVER_CONN_ERROR");
            }
            this.loading=false;
            $scope.$emit("loggedIn",false);
        }

    }]);

})();