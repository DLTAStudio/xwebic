/**
 * Created by dscreve on 27/01/2016.
 */

function Integration(id,botName,number,startedTime,duration,currentStep,result,xcodebuildOutput,buildServiceLog,
                     xcodebuildLog,sourceControlLog) {
    this.id=id;
    this.botName=botName;
    this.number=number;
    this.startedTime=startedTime;
    this.duration=duration;
    this.currentStep=currentStep;
    this.result=result;
    /** Pas supporté par Xcode 7.2
    this.xcodebuildOutput=buildServiceLog;
    this.buildServiceLog=buildServiceLog;
    this.xcodebuildLog=xcodebuildLog;
    this.sourceControlLog=sourceControlLog;
**/
    this.xcodebuildOutput='';
    this.buildServiceLog='';
    this.xcodebuildLog='';
    this.sourceControlLog='';

    this.getFormattedStartTime = function() {
        return this.startedTime.format('DD/MM/YYYY - h:mm:ss');
    }

    this.getFormattedDuration = function() {
        var durationMoment = moment.duration(this.duration,'seconds');
        return durationMoment.format("m [min] s [sec]");
    }


}

(function() {
    var integrationmodule = angular.module("integration-module",['ngMessages','ui.bootstrap','ws-module','shared-module','chart.js']);



    integrationmodule.factory('callLoadBotIntegrationWS',['callWS','sharedService',function(callWS,sharedService) {
        return function(controller,botId) {

            var url = sharedService.baseWS+"/bots/"+botId+"/integrations?summary_only"; //
            var headers=[
                {name:'token',value:sharedService.getToken()},
            ];

            callWS(url,'GET',headers,null,
                function(httpResult,contentResult,headerResult) {
                    // on Success
                    var contentObject=JSON.parse(contentResult);
                    contentObject.results.forEach(function(integrationObject) {
                        startMoment = moment(integrationObject.startedTime);

                        var xcodebuildOutput='';
                        var buildServiceLog='';
                        var xcodebuildLog='';
                        var sourceControlLog='';

                        if (integrationObject.assets!=undefined) {
                            if (undefined != integrationObject.assets.xcodebuildOutput) {
                                xcodebuildOutput = integrationObject.assets.xcodebuildOutput.relativePath;
                            }

                            if (undefined != integrationObject.assets.buildServiceLog) {
                                buildServiceLog = integrationObject.assets.buildServiceLog.relativePath;
                            }

                            if (undefined != integrationObject.assets.xcodebuildLog) {
                                xcodebuildLog = integrationObject.assets.xcodebuildLog.relativePath;
                            }

                            if (undefined != integrationObject.assets.sourceControlLog) {
                                sourceControlLog = integrationObject.assets.sourceControlLog.relativePath;
                            }
                        }
                         var newIntegration = new Integration(integrationObject._id,integrationObject.bot.name,integrationObject.number,startMoment,
                             integrationObject.duration,integrationObject.currentStep,integrationObject.result,
                             xcodebuildOutput,buildServiceLog,xcodebuildLog,sourceControlLog);
                        controller.integrations.push(newIntegration);

                        if (integrationObject.buildResultSummary!=undefined) {
                            var chartLabel;
                            if (contentObject.results.length>10) {
                                chartLabel='';
                            }
                            else {
                                chartLabel=integrationObject.number;
                            }
                            var coverage = integrationObject.buildResultSummary.codeCoveragePercentage;
                            controller.coverage_labels.unshift(chartLabel);
                             controller.coverage_data[0].unshift(coverage);

                            var testCount = integrationObject.buildResultSummary.testsCount;
                            var testFailureCount = integrationObject.buildResultSummary.testFailureCount;
                            controller.tests_labels.unshift(chartLabel);
                            controller.tests_data[0].unshift(testCount);
                            controller.tests_data[1].unshift(testFailureCount);

                            var errorCount = integrationObject.buildResultSummary.errorCount;
                            var warningCount = integrationObject.buildResultSummary.warningCount;
                            controller.build_labels.unshift(chartLabel);
                            controller.build_data[0].unshift(errorCount);
                            controller.build_data[1].unshift(warningCount);
                        }
                        if (controller.integrations.length>0)
                            controller.lastIntegrationResult=controller.integrations[0].result;
                    }
                    );
                },
                function(httpResult,contentResult,headerResult) {
                    // onFailure
                    controller.reset();
                    sharedService.setToken(null);
                });

        };
    }]);

    integrationmodule.factory('callPerformBotIntegrationWS',['callWS','sharedService','callLoadBotIntegrationWS',
        function(callWS,sharedService,callLoadBotIntegrationWS) {
        return function(controller,botId) {

            var url = sharedService.baseWS+"/bots/"+botId+"/integrations"; //
            var headers=[
                {name:'token',value:sharedService.getToken()},
            ];

            callWS(url,'POST',headers,null,
                function(httpResult,contentResult,headerResult) {
                    // on Success
                    controller.onRefreshButton();
                },
                function(httpResult,contentResult,headerResult) {
                    // onFailure
                    controller.refresh();
                });

        };
    }]);

    /** Code temporaire parce que l'API xCode ne fonctionne pas **/

    integrationmodule.factory('callLoadIntegrationAssetWS',['callWS','sharedService','callLoadBotIntegrationWS',
        function(callWS,sharedService,callLoadBotIntegrationWS) {
            return function(controller,integrationId) {

                var url = sharedService.baseWS+"/integrations/"+integrationId+"/assets"; //
                var headers=[
                    {name:'token',value:sharedService.getToken()}
                ];

                callWS(url,'GET',headers,null,
                    function(httpResult,contentResult,headerResult) {
                        // on Success
                        controller.downloadAsset(contentResult,headerResult,integrationId);
                    },
                    function(httpResult,contentResult,headerResult) {
                        // onFailure
                        controller.refresh();

                    });

            };
        }]);
    integrationmodule.controller('ListIntegrationCtrl',['callPerformBotIntegrationWS','callLoadBotIntegrationWS',
        'callLoadIntegrationAssetWS','sharedService', '$scope','$translate',
        function(callPerformBotIntegrationWS,callLoadBotIntegrationWS,callLoadIntegrationAssetWS,sharedService,$scope,$translate) {

        this.reset = function() {
            this.lastIntegrationResult="N/A";
            this.integrations=[];
            this.botId="";
            this.coverage_labels = [];
            this.coverage_series = [$translate.instant('Coverage')];
            this.coverage_data = [[]];

            this.tests_labels = [];
            this.tests_series = [$translate.instant('Tests Count'),$translate.instant('Test Failure count')];
            this.tests_data = [[],[]];

            this.build_labels = [];
            this.build_series = [$translate.instant('Build errors'),$translate.instant('Build warnings')];
            this.build_data = [[],[]];

        }

        this.reset();
        var that=this;
        $scope.$on("botselected", function(event, bot) {
            that.reset();
            that.botId=bot.id;
                callLoadBotIntegrationWS(that,that.botId);
            }
        );

        this.refresh = function() {
            this.integrations=[];
            callLoadBotIntegrationWS(this,this.botId);
        }

        this.onIntegrateButton = function() {
            callPerformBotIntegrationWS(that,that.botId);
        }

        this.onRefreshButton = function() {
            this.refresh();
         }

        this.getIntegrationURL = function(integration) {
            return sharedService.baseWS+"/integrations/"+integration.id+"/assets/"+sharedService.getToken(); //

        }

        this.statusColorStyle=function(resultString) {

            if (undefined==resultString) {
                if (that.integrations.length==0) {
                    return "";
                }
                else {
                    resultString=that.integrations[0].result;
                }
            }
            var vert="rgba(135,255,99, 0.8)";
            var rouge="rgba(255,66,70, 0.8)";
            var orange="rgba(255, 215, 37, 0.8)";

            switch(resultString) {
                case 'succeeded':
                    return { 'color':vert,'font-weight': 'bold' };
                    break;

                case 'test-failures':
                    return { 'color':orange,'font-weight': 'bold' };
                    break;

                case 'build-errors':
                    return { 'color':rouge,'font-weight': 'bold'};
                    break;

                case 'warnings':
                    return { 'color':orange,'font-weight': 'bold' };
                    break;

                case 'analyzer-warnings':
                    return { 'color':orange,'font-weight': 'bold' };
                    break;

                case 'build-failed':
                    return { 'color':rouge,'font-weight': 'bold'};
                    break;


                case 'checkout-error':
                    return { 'color':rouge,'font-weight': 'bold' };
                    break;

                case 'internal-error':
                    return { 'color':rouge,'font-weight': 'bold' };
                    break;

                case 'internal-checkout-error':
                    return { 'color':rouge,'font-weight': 'bold' };
                    break;

                case 'internal-build-error':
            return { 'color':rouge,'font-weight': 'bold' };
                    break;

                case 'internal-processing-error':
            return { 'color':rouge,'font-weight': 'bold' };
                    break;

                case 'canceled':
            return { 'color':rouge,'font-weight': 'bold' };
                    break;

                case 'trigger-error':
            return { 'color':rouge,'font-weight': 'bold' };
                    break;

                case 'unknown':
                return { 'color':rouge,'font-weight': 'bold' };
                    break;
            }
        }
     }]);
})();