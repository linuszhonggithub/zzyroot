//var port=':8085';
//var ip_addr = "http://"+document.location.hostname + port;
function isUserValid(){
	
	let username = getLocal('username');
	if(username === ''){
		redirectToLogin();
		return false;
	}
	
	let token = getLocal('token');
	//check if user token is valid
	//let rtn = 
	sendajaxIPPortAsnc('isuservalid',username + zzySplitItem + token,true);
	/*if(rtn.indexOf(zzySuccess) == 0){
		return true;
	}
	redirectToLogin();*/
}
function redirectToLogin(){
	//window.location.href="http://"+ip_addr+"/login.html";
	self.location="http://"+document.location.hostname+":8085" + "/zzyLogin.html";
	//window.location.href="http://"+document.location.hostname+":8085" + "/login.html";
}

