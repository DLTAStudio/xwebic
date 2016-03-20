/**
 * Module that manage Bots
 */
'use strict';


function Bot(id,name)
{
    this.id=id;
    this.name=name;
}


(function() {
    var botmodule = angular.module("bot-module",['ngMessages','ui.bootstrap','ws-module','shared-module']);

    botmodule.factory('callLoadAllBotWS',['callWS','sharedService',function(callWS,sharedService) {
        return function(controller) {

            var url = sharedService.baseWS+"/bots";
            var headers=[
                {name:'token',value:sharedService.getToken()},
            ];

            callWS(url,'GET',headers,null,
                function(httpResult,contentResult,headerResult) {
                    // on Success
                    var contentObject=JSON.parse(contentResult);
                    controller.bots=[];
                    contentObject.results.forEach(function(botObject) {
                        var newBot = new Bot(botObject._id,botObject.name);
                        controller.bots.push(newBot);
                    });
                    if ((controller.bots.length>0) && (-1==controller.currentIndex)) {
                        controller.currentIndex = 0;
                    }
                        else {
                        controller.currentIndex=-1;

                    }
                },
                function(httpResult,contentResult,headerResult) {
                    // onFailure
                    controller.bots=[];
                    controller.currentIndex=-1;
                    sharedService.setToken(null);

                });

        };
    }]);

    botmodule.controller('ListBotsCtrl',['callLoadAllBotWS','sharedService','$scope',function(callLoadAllBotWS,sharedService,$scope) {
        this.bots=[];
        this.currentIndex=-1;
        var that=this;
        if (sharedService.getToken()) {
            callLoadAllBotWS(that);
        }

        $scope.$watch(angular.bind(this, function () {
            return sharedService.getToken();
        }), function(newVal,oldVal) {
            if (newVal) {
                callLoadAllBotWS(that);
            }
            else {
                that.bots=[];
            }
        });

        this.onBotClicked = function(index) {
            this.currentIndex=index;
            var bot = this.bots[this.currentIndex];
            $scope.$parent.$broadcast("botselected",bot);
        }
    }]);

})();