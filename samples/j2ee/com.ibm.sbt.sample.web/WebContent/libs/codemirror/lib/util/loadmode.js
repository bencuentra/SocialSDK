/*  (c) Copyright IBM Corp. 2010 - Licensed under the Apache License, Version 2.0 */

(function(){if(!CodeMirror.modeURL){CodeMirror.modeURL="../mode/%N/%N.js";}var _1={};function _2(_3,n){var _4=n;return function(){if(--_4==0){_3();}};};function _5(_6,_7){var _8=CodeMirror.modes[_6].dependencies;if(!_8){return _7();}var _9=[];for(var i=0;i<_8.length;++i){if(!CodeMirror.modes.hasOwnProperty(_8[i])){_9.push(_8[i]);}}if(!_9.length){return _7();}var _a=_2(_7,_9.length);for(var i=0;i<_9.length;++i){CodeMirror.requireMode(_9[i],_a);}};CodeMirror.requireMode=function $DBbr_(_b,_c){if(typeof _b!="string"){_b=_b.name;}if(CodeMirror.modes.hasOwnProperty(_b)){return _5(_b,_c);}if(_1.hasOwnProperty(_b)){return _1[_b].push(_c);}var _d=document.createElement("script");_d.src=CodeMirror.modeURL.replace(/%N/g,_b);var _e=document.getElementsByTagName("script")[0];_e.parentNode.insertBefore(_d,_e);var _f=_1[_b]=[_c];var _10=0,_11=setInterval(function(){if(++_10>100){return clearInterval(_11);}if(CodeMirror.modes.hasOwnProperty(_b)){clearInterval(_11);_1[_b]=null;_5(_b,function(){for(var i=0;i<_f.length;++i){_f[i]();}});}},200);};CodeMirror.autoLoadMode=function $DBbs_(_12,_13){if(!CodeMirror.modes.hasOwnProperty(_13)){CodeMirror.requireMode(_13,function(){_12.setOption("mode",_12.getOption("mode"));});}};}());