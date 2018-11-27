
function zzyAlert(msg){
	alert(getLocaleItem(msg));
}
function zzyConfirm(astr){
	return confirm(getLocaleItem(astr));
}

function zzyIsLetter(c){
	return c.toLowerCase() != c.toUpperCase();
}
function  zzyGetInteger(value){
	if((value||"")=="")return value;
	return parseInt(value);
}
function getLocal(name){
	return localStorage.getItem(zzyPrefix + name)||'';
}
function setLocal(name,value){
	localStorage.setItem(zzyPrefix + name,value);
}
function getMod(source, exponent, divider){
	let rtn = 1;
	for(let i = 0; i < exponent; i++){
		rtn *= source;
		rtn = rtn % divider;
	}
	return rtn;
}
function removeLocal(name){
	localStorage.removeItem(zzyPrefix + name);
}
function getSessionkey(url,data){
	let rtn = getLocal('sessionkey');
	if(rtn == ''){
		let arandomsession = Math.floor(Math.random() * 1000);
		let param = getMod(zzydhbase,arandomsession,zzyprime);
		let isasnc = true;
		zzyLastUrl = url;
		zzyLastData = data;
		zzyArandomsession = arandomsession;
		sendajaxIPPortAsnc('getsessionkey','param=' + param, isasnc);
		/*let msg = sendajaxIPPortAsnc('getsessionkey','param=' + param, isasnc);
		/if(msg.indexOf(zzySuccess) == 0){
			   msg = msg.substring(zzySuccess.length);
			   msg = getMod(msg,arandomsession,zzyprime);
			   msg = sha256_digest(msg+'');
			   setLocal('sessionkey',msg);
			   rtn = getLocal('sessionkey');
		}*/
		//alert('calc session key');
			
	}
	
	//console.log("getSessinkey rtn is " + rtn);
	return rtn;
}
function AjaxSuccess(url,msg){
	
	if(msg.indexOf(zzyFail) >= 0 && url !== '/zzytablesave'){

		//zzyAlert(msg);
		if(msg === ZZYFAILNoSessionKey || msg === ZZYFAILInvalidLogin){
			if(msg === ZZYFAILInvalidLogin){
				zzyAlert(msg);
			}
			redirectToLogin();
		}
		
		return;
	}
	
	if(msg.indexOf(zzySuccess)==0 && msg.length > zzySuccess.length){
		if(url == '/getsessionkey'){
			msg = msg.substring(zzySuccess.length);
			let arandomsession = zzyArandomsession;
			   msg = getMod(msg,arandomsession,zzyprime);
			   msg = sha256_digest(msg+'');
			   setLocal('sessionkey',msg);
			   //rtn = getLocal('sessionkey');
			   if(zzyLastUrl != ''){
			   	sendajaxURLcacheAsnc(zzyLastUrl,zzyLastData,"post",false, true);
			   	zzyLastUrl='';
				   zzyLastData='';
				   zzyArandomsession='';
			   }
	    }else{
			let strMsg1 = msg.substring(zzySuccess.length);
			let secretekey = getSessionkey('','');
			msg = zzySuccess + decryptByDES(strMsg1, secretekey);
			console.log('msg after decrypt is ' + msg);
			if(url == '/zzylogin'){
			 let param = msg.substring(zzySuccess.length);
			   let paramA = param.split(zzySplitItem);
			   let index = 0;
			   let url = paramA[index++];
			   let username = paramA[index++];
			   let token = paramA[index++];
			   let role = paramA[index++];
			   setLocal('username',username);
			   setLocal('token',token);
			   setLocal('role',role);
			   self.location = url;
			}else if(url == '/isuservalid'){
				if(msg.indexOf(zzySuccess) != 0){
					redirectToLogin();
				}
				
			}else if(url == '/zzygettabledata'){
				let param = msg.substring(zzySuccess.length);
				processTableData(param);
			}else if(url == '/zzytablesave'){
				zzyAlert(msg);
				getTableData();
			}
		} 
		

	}
	return msg;
}
function gettableheaderuniq(tablename){
	let result = zzyDataTableHeaderUniq[zzyPrefixTableHeader+tablename];
	if(result !== undefined){
		return result;
	}
	let tableheader = zzyDataTableHeader[zzyPrefixTableHeader+tablename];
	result = {};
	for(let i = 0; i < tableheader.length; i++){
		let ti = tableheader[i];
		result[ti.name] = i;
	}
	zzyDataTableHeaderUniq[zzyPrefixTableHeader+tablename] = result;
	return result;
}
function gettablebodyuniq(tablename){
	let tablebody = zzyDataTableBody[zzyPrefixTableBody+tablename];
	let result = {};
	for(let i = 0; i < tablebody.length; i++){
		let ti = tablebody[i];
		result[ti.id] = i;
	}
	return result;
}
function settablebodyi(headerj,tablebodyj,valuenew){
	let oldvalue = tablebodyj[headerj.name]||'';
	if(valuenew == 'null'){
		valuenew = '';
	}
	if(valuenew == oldvalue){
		return false;
	}
	tablebodyj[headerj.name] = valuenew;
	return true;
}
function processTableData(param){
let paramA = param.split(zzySplitBlock);
			if(paramA.length <1){
				return; 
			}
			let now = paramA[0];
			for(let i = 1; i < paramA.length; i++){
				let pi = paramA[i];
				let piA = pi.split(zzySplitLine);
				let tablename = piA[0].toLowerCase();
				if(tablename.indexOf(zzyLogName)>0){
					tablename = tablename.substring(0,tablename.indexOf(zzyLogName));
					let tableheaderuniq = gettableheaderuniq(tablename);
					let tableheader = zzyDataTableHeader[zzyPrefixTableHeader+tablename]||'';
					let tablebody = zzyDataTableBody[zzyPrefixTableBody+tablename]||'';
					let tablebodyuniq = gettablebodyuniq(tablename);
					let indexbody = 0;
					let deleteids=[];
					let deleteindex = 0;
					for(let j = 1; j < piA.length; j++){
						let pj = piA[j];
						let pjA = pj.split(zzySplitItem);
						let pjaindex = 0;
						let udpatestatus = pjA[pjaindex++]; 
						let lineid = pjA[pjaindex++]||'';
						let colname = pjA[pjaindex++];
						if(tableheaderuniq[colname]===undefined){
							continue;
						}
						let valuenew = pjA[pjaindex++]||'';
						let valueold= pjA[pjaindex++]||'';
						if(valueold == 'null'){valueold='';}
						if(tablebodyuniq[lineid]===undefined){
							if(udpatestatus ==  'd'){
								continue;
							}
							tablebodyuniq[lineid]=tablebody.length;
							tablebody[tablebody.length]={id:lineid};
						}
						if(udpatestatus ==  'd'){
							if(lineid == ''){
								continue;
							}
								indexbody++;
								deleteids[deleteindex++] = lineid;
								continue;
						}
						let tablebodyj = tablebody[tablebodyuniq[lineid]];
						let headerj = tableheader[tableheaderuniq[colname]];
						let changed = settablebodyi(headerj,tablebodyj,valuenew);
						if(changed){
							indexbody++;
						}
					}
					if(indexbody > 0){
						if(deleteids.length > 0){
							for(let j = deleteids.length - 1; j >= 0; j--){
								let lineid = deleteids[j];
								tablebody.splice(tablebodyuniq[lineid],1);
							}
						}
						zzyDataCenterDataChangeNoData(zzyPrefixTableBody+tablename);
						zzyDataTable[tablename]={lasttime:now};
					}
					continue;
				}
				let tableattr = {};
				if(tablename.indexOf(zzySplitItem)>0){
					let ptA = tablename.split(zzySplitItem); 
					tablename = ptA[0];
					for(let j = 1; j < ptA.length; j++){
						let pj = ptA[j];
						let pjA = pj.split(":");
						tableattr[pjA[0]] = pjA[1];
					}
					zzyDataTableAttr[zzyPrefixTableAttr+tablename] = tableattr;
					zzyDataCenterDataChangeNoData(zzyPrefixTableAttr+tablename);

				}
				
				let tableheader = [];
				let indexheader = 0;
				for(let j = 1; j < piA.length; j++){
					let pj = piA[j];
					let pjA = pj.split(zzySplitItem);
					if(pjA[0] != zzyTYPEHEADER){
						continue;
					}
						//is header
						
						tableheader[indexheader] = {};
						for(let k = 1; k< pjA.length; k++){
							let pk = pjA[k];
							let pkA = pk.split(":");
							tableheader[indexheader][pkA[0]] = pkA[1];	
						}
						indexheader++;
				}
				if(indexheader > 0){
					zzyDataTableHeader[zzyPrefixTableHeader+tablename] = tableheader;
					zzyDataCenterDataChangeNoData(zzyPrefixTableHeader+tablename);
					//zzyDataTableBody[zzyPrefixTableBody+tablename]=''; //clean the body if header is changed
				}
				//get header columns
				tableheader = zzyDataTableHeader[zzyPrefixTableHeader+tablename];
				let indexbody = 0;
				let tablebody = zzyDataTableBody[zzyPrefixTableBody+tablename]||'';
				if(tablebody === ''){
					tablebody = [];
				}
				for(let j = 1; j < piA.length; j++){
					let pj = piA[j]||'';
					if(pj == '' ){
						continue;
					}
					let pjA = pj.split(zzySplitItem);
					if(pjA[0] === zzyTYPEHEADER){
						continue;
					}
					//is body
					let tj = {};
					tablebody[indexbody++] = tj;	
					for(let k = 0; k< tableheader.length; k++){
						let tk = tableheader[k];
						let pk = pjA[k];
						settablebodyi(tk,tj,pk);
						//tj[tk.name] = pk;
								
					}
					
				}
				if(indexbody > 0){
					zzyDataTableBody[zzyPrefixTableBody+tablename] = tablebody;
					zzyDataCenterDataChangeNoData(zzyPrefixTableBody+tablename);
				}
				zzyDataTable[tablename]={lasttime:now};

			}
}
function removeSessionkey(){
	removeLocal('sessionkey');
}
function encryptByDES(message, key) {
	let keyHex = CryptoJS.enc.Utf8.parse(key);
	let encrypted = CryptoJS.DES.encrypt(message, keyHex,
			{ mode: CryptoJS.mode.ECB, padding: CryptoJS.pad.Pkcs7 });
	let result =  encrypted.toString();
	//result = result.split('+').join('%2B'); 
	result = result.replace(/\+/g,'%2B');
	return result;
}
function decryptByDES(ciphertext, key) {
    let keyHex = CryptoJS.enc.Utf8.parse(key);
    // direct decrypt ciphertext
    let decrypted = CryptoJS.DES.decrypt({
        ciphertext: CryptoJS.enc.Base64.parse(ciphertext)
    }, keyHex, {
        mode: CryptoJS.mode.ECB,
        padding: CryptoJS.pad.Pkcs7
    });
    return decrypted.toString(CryptoJS.enc.Utf8);
}
function setTableData(tabledata,parent){
	let table = tabledata.table;
	let where = tabledata.where;
	if(zzyDataTable[table] === undefined){
		zzyDataTable[table]={lasttime:0};
	}
	getTableData();
}
function getTableData(){
	let param='';
	let paramsep='';
	for(let i in zzyDataTable){
		if(!zzyDataTable.hasOwnProperty(i)){
				continue;
		}
		let ii = zzyDataTable[i];
		param+=paramsep+i+zzySplitItem+ii.lasttime;
		paramsep=zzySplitLine;

	}
	//let rtn = 
	sendajaxIPPort('zzygettabledata',param);
	
}
function getZzyBodyHeight(aheight){
	return  window.innerHeight - zzyHeaderHeight - aheight;
}
function isDataTypeNumber(fieldtype){
	let isnumber = false;
	if(fieldtype == 'int'){
		isnumber = true;
	}else if(fieldtype == 'datetime'){
		isnumber = true;
	} 
	return isnumber;
}
function ZzyDataSort(param){
	let data = param.data;
	if(data == undefined || data.length < 1){
		return;
	}
	let name = param.name||'';
	if(name == ''){
		return;
	}
	let asc = param.asc;
	if(asc === undefined){
		asc = true;
	}
	let sign = 1;
	if(!asc)sign = -1;
	
	//let fieldtype = param.fieldtype||'';

	//let isnumber = isDataTypeNumber(fieldtype);
	data.sort(function (obj1, obj2){
		let n1 = obj1[name]||'';
		let n2 = obj2[name]||'';
		if(n1 == n2){
			return 0;
		}
		if(n1 < n2){
			return -1 * sign;
		}
		return sign;
	});

	let triggername = param.triggername || '';
	if(triggername != ''){
		zzyDataCenterDataChangeNoData(triggername);
	}
	
}
function zzyDbToGui(avalue,fieldtype){
	if(fieldtype == 'datetime'){
		return datetimetostring(avalue);
	}
	return avalue;
}
function datetimetostring(avalue){
	if((avalue||'') == ''){
			return '';
		}
	let adate = new Date(parseInt(avalue));
	return datetostring(adate);
}
function datetostring(adate){
	let y = adate.getFullYear();
	let m = adate.getMonth() + 1;
	if(m.length < 2) {m = '0' + m;}
	let d = adate.getDate();
	if(d.length < 2) {d = '0' + d;}
	let h = adate.getHours()+'';
	if(h.length < 2) {h = '0' + h;}
	let mm = adate.getMinutes();
	if(mm.length < 2) {mm = '0' + mm;}
	//let s = adate.getSeconds();
	return y + zzyDateSep + m + zzyDateSep + d + ' ' +h + ':' + mm;// + ':' +s;  
}