"use strict";!function(){var a=angular.module("ws-module",[]);a.factory("callWS",["$http",function(a){return function(b,c,d,e,f,g){var h;h=d?d.slice():[],h.push({name:"Access-Control-Allow-Origin",value:"*"});var i={url:b,method:c,content:e};i.content||(i.content=""),i.processHttpResponse=function(a,b){var c=a.status,d=a.data,e=a.headers();b(c,d,e)};for(var j={},k=0;k<h.length;k++){var l=h[k];j[l.name]=l.value}a({transformResponse:[function(a){return a}],method:i.method,url:i.url,headers:j,data:i.content}).then(function(a){i.processHttpResponse(a,f)},function(a){i.processHttpResponse(a,g)})}}])}();