/**
 * Created by dscreve on 25/01/2016.
 */
'use strict';

(function() {
    var wsmodule = angular.module("ws-module",[]);

    wsmodule.factory('callWS',['$http',function($http) {
        return function(url,method,headers,content,onSuccess,onFailure) {

            var requestHeaders;
            if (!headers) {
                requestHeaders=[];
            }
            else {
                requestHeaders=headers.slice();

            }
            requestHeaders.push({name:'Access-Control-Allow-Origin',value:'*'});
            var that={url:url,method:method,content:content};

            if (!that.content) {
                that.content='';
            }
            that.processHttpResponse = function(response,callback) {
                var httpResult=response.status;
                var contentResult=response.data;
                var headerResult=response.headers();
                callback(httpResult,contentResult,headerResult);
            }

            var headerContent={};
            for (var i=0;i<requestHeaders.length;i++) {
                var header=requestHeaders[i];
                headerContent[header.name]=header.value;
            }
            $http(
                {
                    transformResponse: [function (data) {
                        // Do whatever you want!
                        return data;
                    }],
                    method: that.method,
                    url:that.url,
                    headers:headerContent,
                    data:that.content}).
            then(function (response) {
                // this callback will be called asynchronously
                // when the response is available
                 that.processHttpResponse(response,onSuccess);
            }, function (response) {
                // called asynchronously if an error occurs
                // or server returns response with an error status.
                that.processHttpResponse(response,onFailure);
            });
        }
    }]);

})();