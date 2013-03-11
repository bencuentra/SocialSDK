/*  (c) Copyright IBM Corp. 2010 - Licensed under the Apache License, Version 2.0 */

(function(){function _1(_2,f){for(var i=0,e=_2.length;i<e;++i){f(_2[i]);}};function _3(_4,_5){if(!Array.prototype.indexOf){var i=_4.length;while(i--){if(_4[i]===_5){return true;}}return false;}return _4.indexOf(_5)!=-1;};function _6(_7,_8,_9,_a){var _b=_7.getCursor(),_c=_9(_7,_b),_d=_c;if(!/^[\w$_]*$/.test(_c.string)){_c=_d={start:_b.ch,end:_b.ch,string:"",state:_c.state,type:_c.string=="."?"property":null};}while(_d.type=="property"){_d=_9(_7,{line:_b.line,ch:_d.start});if(_d.string!="."){return;}_d=_9(_7,{line:_b.line,ch:_d.start});if(_d.string==")"){var _e=1;do{_d=_9(_7,{line:_b.line,ch:_d.start});switch(_d.string){case ")":_e++;break;case "(":_e--;break;default:break;}}while(_e>0);_d=_9(_7,{line:_b.line,ch:_d.start});if(_d.type=="variable"){_d.type="function";}else{return;}}if(!_f){var _f=[];}_f.push(_d);}return {list:_10(_c,_f,_8,_a),from:{line:_b.line,ch:_c.start},to:{line:_b.line,ch:_c.end}};};CodeMirror.javascriptHint=function $DBbp_(_11,_12){return _6(_11,_13,function(e,cur){return e.getTokenAt(cur);},_12);};function _14(_15,cur){var _16=_15.getTokenAt(cur);if(cur.ch==_16.start+1&&_16.string.charAt(0)=="."){_16.end=_16.start;_16.string=".";_16.type="property";}else{if(/^\.[\w$_]*$/.test(_16.string)){_16.type="property";_16.start++;_16.string=_16.string.replace(/\./,"");}}return _16;};CodeMirror.coffeescriptHint=function $DBbq_(_17,_18){return _6(_17,_19,_14,_18);};var _1a=("charAt charCodeAt indexOf lastIndexOf substring substr slice trim trimLeft trimRight "+"toUpperCase toLowerCase split concat match replace search").split(" ");var _1b=("length concat join splice push pop shift unshift slice reverse sort indexOf "+"lastIndexOf every some filter forEach map reduce reduceRight ").split(" ");var _1c="prototype apply call bind".split(" ");var _13=("break case catch continue debugger default delete do else false finally for function "+"if in instanceof new null return switch throw true try typeof var void while with").split(" ");var _19=("and break catch class continue delete do else extends false finally for "+"if in instanceof isnt new no not null of off on or return switch then throw true try typeof until void while with yes").split(" ");function _10(_1d,_1e,_1f,_20){var _21=[],_22=_1d.string;function _23(str){if(str.indexOf(_22)==0&&!_3(_21,str)){_21.push(str);}};function _24(obj){if(typeof obj=="string"){_1(_1a,_23);}else{if(obj instanceof Array){_1(_1b,_23);}else{if(obj instanceof Function){_1(_1c,_23);}}}for(var _25 in obj){_23(_25);}};if(_1e){var obj=_1e.pop(),_26;if(obj.type=="variable"){if(_20&&_20.additionalContext){_26=_20.additionalContext[obj.string];}_26=_26||window[obj.string];}else{if(obj.type=="string"){_26="";}else{if(obj.type=="atom"){_26=1;}else{if(obj.type=="function"){if(window.jQuery!=null&&(obj.string=="$"||obj.string=="jQuery")&&(typeof window.jQuery=="function")){_26=window.jQuery();}else{if(window._!=null&&(obj.string=="_")&&(typeof window._=="function")){_26=window._();}}}}}}while(_26!=null&&_1e.length){_26=_26[_1e.pop().string];}if(_26!=null){_24(_26);}}else{for(var v=_1d.state.localVars;v;v=v.next){_23(v.name);}_24(window);_1(_1f,_23);}return _21;};})();