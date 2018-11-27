let index = 0;
var zzylan = [];
zzylan[index++] = {'code':'en',label:'English'};
zzylan[index++] = {'code':'zh',label:'\u4E2D\u6587'};
index = 0;
let locallan = getLocal('currentlan');
zzyDataCenter['zzylancurrent'] = zzylan[0].code;
if(locallan != ''){
	zzyDataCenter['zzylancurrent'] = locallan;
}
var zzylanDetail={};
var zzylanDetailCache={};
var i18n={};

zzyDataCenter[i18n] = i18n;
function getLocaleItemOne(item){
	return getLocaleItemTwo(item,item);
}
function getLocaleItemTwo(item,newitem){
	let rtnii = '';
	if(zzylanDetail[newitem] != undefined){
		rtnii = zzylanDetail[newitem][zzyDataCenter['zzylancurrent']] || '';
	}
	if(rtnii == ''){
		if(newitem.length < 2){
			return item;	
		}
	}else{
		return rtnii;
	}
	return getLocaleItemTwo(item, newitem.substring(0, newitem.length - 1));
}
function getLocaleItem(item){
	if(zzyDataCenter['zzylancurrent'] == zzylan[0].code){
		return item;
	}
	let itemshort = item||'';
	if(itemshort == ''){
      return item;
    }
	itemshort = itemshort.toLocaleLowerCase();
	itemshort = itemshort.trim();
	let rtn = '';
	
	if(zzylanDetail[itemshort] != undefined){
		rtn = zzylanDetail[itemshort][zzyDataCenter['zzylancurrent']] || '';
	}
	if(rtn != ''){
		return rtn; 
	}
	
	if(zzylanDetailCache[itemshort] != undefined){
		rtn = zzylanDetailCache[itemshort][zzyDataCenter['zzylancurrent']] || '';
	}
	if(rtn != ''){
		return rtn; 
	}

	
	let rtnA = [];
	let index = 0;
	let itemhortnew = itemshort+" a";
	let itemshortA = itemhortnew.split('');
	let rtntmp = '';
	for(let i = 0; i < itemshortA.length; i++){
		let ii = itemshortA[i];
		if(zzyIsLetter(ii)){
			rtntmp += ii;
			continue;
		}
		
		let rtnii = rtntmp;
		//if(zzylanDetail[rtntmp] != undefined){
			rtnii = getLocaleItemOne(rtntmp)|| '';
		//}
		if(rtnii == rtntmp){
			rtnA[index++] = {value:rtnii, type:'nochange'};
		}else{
			rtnA[index++] = {value:rtnii, type:'changed'};
		}
		rtntmp = '';
		if(ii === ' '){
			rtnA[index++] = {value:' ', type:'space'};
			continue;
		}
		rtnA[index++] = {value:ii, type:'notletter'};
		
	}
	/*if(rtntmp != ''){
		let rtnii = rtntmp;
		if(zzylanDetail[rtntmp] != undefined){
			rtnii = getLocaleItemOne(rtntmp)|| '';
		}
		if(rtnii == rtntmp){
			rtnA[index++] = {value:rtnii, type:'nochange'};
		}else{
			rtnA[index++] = {value:rtnii, type:'changed'};
		}

	}*/
	for(let i = 0; i < rtnA.length; i++){
		let ri = rtnA[i];
		if(ri.type == 'changed' || ri.type == 'notletter'){
			rtn += getLocalItemLast(rtnA, i);
			rtn += ri.value;
		}else if(i == rtnA.length - 1){
			rtn += getLocalItemLast(rtnA, i + 1);
		}
	}
	if(zzylanDetailCache[itemshort] === undefined){
		zzylanDetailCache[itemshort]={};
	}

	zzylanDetailCache[itemshort][zzyDataCenter['zzylancurrent']] = rtn;
	return rtn;
}
function getLocalItemLast(rtnA, j){//find all before i and not changed items
	let rtn = '';
	for(let i = j - 1; i >=0; i--){
		let ri = rtnA[i];
		if(ri.type == 'changed' || ri.type == 'notletter'){
			break;
		}
		rtn = ri.value + rtn;
	}
	rtn = rtn.trim();
	if(rtn == ''){
		return rtn;
	}
	let rtni = '';
	if(zzylanDetail[rtn] != undefined){
		rtni = zzylanDetail[rtn][zzyDataCenter['zzylancurrent']] || '';
	}
	if(rtni != ''){
		rtn = rtni;
	}
	return rtn;
}