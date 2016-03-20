/**
 * Module for shared data
 */

(function() {
    var sharedmodule = angular.module("shared-module",[]);

    sharedmodule.factory('sharedService',['$location',function($location) {
        var attr = {};

        attr.getToken = function() {
            var token=localStorage.getItem("token");
            //console.log(token);
            return token;
        }

        attr.setToken = function(token) {
            if (!token) {
                localStorage.removeItem("token");
            }
            else {
                localStorage.setItem("token", token);
            }

        }

        // test en local
        attr.baseWS = "http://localhost:8080/xwebic/api"

        //console.log("app.baseWS = "+attr.baseWS)
        return attr;
    }]);

})();