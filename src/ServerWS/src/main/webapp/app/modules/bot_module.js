"use strict";function Bot(a,b){this.id=a,this.name=b}!function(){var a=angular.module("bot-module",["ngMessages","ui.bootstrap","ws-module","shared-module"]);a.factory("callLoadAllBotWS",["callWS","sharedService",function(a,b){return function(c){var d=b.baseWS+"/bots",e=[{name:"token",value:b.getToken()}];a(d,"GET",e,null,function(a,b,d){var e=JSON.parse(b);c.bots=[],e.results.forEach(function(a){var b=new Bot(a._id,a.name);c.bots.push(b)}),c.bots.length>0&&-1==c.currentIndex?c.currentIndex=0:c.currentIndex=-1},function(a,d,e){c.bots=[],c.currentIndex=-1,b.setToken(null)})}}]),a.controller("ListBotsCtrl",["callLoadAllBotWS","sharedService","$scope",function(a,b,c){this.bots=[],this.currentIndex=-1;var d=this;b.getToken()&&a(d),c.$watch(angular.bind(this,function(){return b.getToken()}),function(b,c){b?a(d):d.bots=[]}),this.onBotClicked=function(a){this.currentIndex=a;var b=this.bots[this.currentIndex];c.$parent.$broadcast("botselected",b)}}])}();