var zzyPrefix = "zzy";
var zzyPrefixTable = zzyPrefix + "table_";
var zzyPrefixTableHeader = zzyPrefix + "tableheader_";
var zzyPrefixTableAttr = zzyPrefix + "tableattr_";
var zzyPrefixTableBody = zzyPrefix + "tablebody_";
var zzyLogName = zzyPrefix + "log";
var zzyModalName = zzyPrefix + "Modal_";
var zzyDelete = zzyPrefix + "delete_";
var zzyEdit = zzyPrefix + "edit_";
var zzyID = zzyPrefix + "id";
var zzyOlValue = zzyPrefix + "oldvalue";
var zzySplitItem = "zy~";
var zzySplitLine = "zy!";
var zzySplitBlock = "zy#";
var zzySplitMail = "zy:";
var zzySuccess = "Success:";
var zzyTYPEHEADER="type:header"
var zzySerial = 1;
var zzySuccessChangepassword = zzySuccess + "password changed";
var zzyFail = "Fail:";
var zzyFailnewpassNmatch = zzyFail + "new password is not match";
var zzyDeleteMsg = 'Are you sure delete this record?';
var zzyprime=91473769;
var zzydhbase=2;
var zzyDataCenter={};
var zzyDataCenterDatachangeListener={};
var zzyDataProcess={};
var zzyDataTable={};
var zzyDataTableHeader={};
var zzyDataTableHeaderUniq={};
var zzyDataTableAttr={};
var zzyDataTableBody={};
var zzyHeaderHeight = 100;
var ZZYFAILNoSessionKey = zzyFail + 'no session key';
var ZZYFAILInvalidLogin = zzyFail  +'invalid login info';
var zzywindowresizeobjs=[];
var zzyLastUrl = '';
var zzyLastData = '';
var zzyArandomsession='';
var zzyDateSep = '-';

function zzyDataCenterDataChangeNoData(dataname){
	//let dataObj = zzyDataCenterDatachangeListener[dataname];
	$.each(zzyDataCenterDatachangeListener[dataname],function(index,obj){
		obj.refreshData();
	});
}
function zzyDataCenterDataChange(dataname, data){
	zzyDataCenter[dataname] = data;
	zzyDataCenterDataChangeNoData(dataname);
}


function addZzyDataCenterDatachangeListener(dataname, obj){
	if(zzyDataCenterDatachangeListener[dataname] === undefined){
		zzyDataCenterDatachangeListener[dataname] = [];
	}
	zzyDataCenterDatachangeListener[dataname][zzyDataCenterDatachangeListener[dataname].length]=obj;
}

zzyDataProcess['supermain']=[{placeholder:'Super User',id:'superuser',badge:'',type:''}
  ,{placeholder:'Projects',id:'project',badge:'',type:''}
];

zzyDataProcess['superuser']=[{type:'table',placeholder:'Super User',id:'zzyuser',where:''}
];