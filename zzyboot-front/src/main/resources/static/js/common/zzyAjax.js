function sendajaxIPPort(strCmd,data){
    return sendajaxIPPortAsnc(strCmd,data,true);
	  
}
function sendajaxIPPortAsnc(strCmd,data, isasnc){
	let url=ip_addr+"/"+strCmd;
  let type = 'post';
    return sendajaxURLAsnc(url,data,type,isasnc);
}
function sendajaxURLAsnc(url,data,type,isasnc){
	return sendajaxURLcacheAsnc(url,data,type,false,isasnc);
}
function sendajaxURL(url,data,type){
	return sendajaxURLAsnc(url,data,type,true);
}
function sleep(n) {
        var start = new Date().getTime();
        //  console.log('休眠前：' + start);
        while (true) {
            if (new Date().getTime() - start > n) {
                break;
            }
        }
        // console.log('休眠后：' + new Date().getTime());
    }

function sendajaxURLcache(url,data,type,needcache){
	return sendajaxURLcacheAsnc(url,data,type,needcache, true);
}
function sendajaxURLcacheAsnc(url,data,type,needcache, isasnc){
	if(url === undefined){
		return;
	}
	let strMsg="";
	let myurl = url;
	//add username, token to data
	if(url.indexOf('getsessionkey')<0){

		let secretekey = getSessionkey(url,data);
		if(secretekey == ''){
			return;
		}
		//let mydata = data;
		//let secretekeymy = secretekey;
		let username = getLocal("username")||"";
		let token = getLocal("token")||"";
		let param = username + zzySplitItem + token;
		data += zzySplitItem + zzySplitLine + zzySplitItem + param;
		data = encryptByDES(data, secretekey);
		//let datanew = data;
		//let mydatanew=data;
	}
	
	$.ajax(
  {
   dataType:"text",
   type:type,
   url:url,
	 data:data,
	 async: isasnc,
   success:function(msg)
   {
	  
	  if(msg!==undefined && msg!=null){
		 strMsg= msg;
			if(isasnc){
				//console.log("url is " + url+",msg is " + msg);
					AjaxSuccess(url,msg);
			}
		}
      //strMsg= trim(msg);
      
   },error:function(x,t,m){
	   if(t==="timeout"){
		   strMsg="Failed, timeout, please check if server is down";
	   }else{
		   strMsg="Failed,"+t;
	   }
	   zzyAlert(strMsg);
	}		
	});
	
	/*//console.log('strMsg is ' + strMsg);
	if(needcache){
		zzysearchcache[cachedata] = {time: Date.now() / 1000,data:strMsg};
	}
	if(strMsg == ZZYFAILNoSessionKey){
		redirectToLogin();
	}
	
	if(url.indexOf('getsessionkey')<0 && strMsg.indexOf(zzySuccess)==0 && strMsg.length > zzySuccess.length){
		let strMsg1 = strMsg.substring(zzySuccess.length);
		let secretekey = getSessionkey(url,data);
		strMsg = zzySuccess + decryptByDES(strMsg1, secretekey);
	}
	if(strMsg.indexOf(zzyFail) == 0){
		zzyAlert(strMsg);
	}
	return strMsg;*/
}
function CheckUrl(url)
{
  var strMsg="";
  $.ajax(
  {
   dataType:"text",
   type:"post",
   url:url,
   success:function(msg)
   {
	  strMsg="true";
   },async: true
   ,error:function(x,t,m){
	   strMsg="false";
}
   
  });
  return (strMsg=="true");
}
function fileCallBasic(strCmd,checkvalid)
{
 if(checkvalid)
   if(!checkUserValid())return;
 strCmd=strCmd.replace(/%/g,"MYPERCENT");
  var strMsg="";
  var thisIP = myIP;
  var url = "http://"+thisIP+":"+myPort+"/"+systemRoot+"/a.do";
  var data="user="+mylogind+"&companycurrent="+companycurrent+"&action="+strCmd;
  var type = "post";
  
  $.ajax(
  {
   dataType:"text",
   type:type,
   
   url:url,data:data,
   success:function(msg)
   {
     strMsg=msg;
   },async: true
   ,error:function(x,t,m){
	   if(t==="timeout"){
		   strMsg="Failed, timeout, please check if server is down";
	   }else{
		   strMsg="Failed,"+t;
	   }
	}
  });
  return strMsg;
}
function sqlCallBasicOne(strCmd){
	return sqlCallBasicOneIP('',strCmd);
}
function sqlCallBasicOneIP(save_addr,strCmd){
	var aRtn=sqlCallBasicIP(save_addr,strCmd);
	if(aRtn===undefined)return "";
	if(aRtn.length<1)return "";
	return aRtn[0][0];
}
function getProperty(name,avalue){
	var artn= mapget(harshlist,name)||'';
	if(artn=='')return avalue;
	return artn.avalue;
	/*for(var i=0;i<harshlist.length;i++){
	  var hi=harshlist[i];
	  if(hi.name==name)return hi.avalue;
	}
	return avalue;*/
}
