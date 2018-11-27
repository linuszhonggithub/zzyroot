//$('#zylabel_1').zzydashboard({});

(function($)
{
	function zzyDashboardTop()
	{
		this.defaults={
				athis:null
  	    ,data:""
  	   };
	};
	

	zzyDashboardTop.prototype=
	{
  	   draw:function()
  	   {
			 isUserValid();
	     let athisoriginal = this.defaults.athis;
	     let athis = $("<nav class='navbar navbar-fixed-tope'>");
	     athisoriginal.append(athis);
	     let $but=$("<button type='button' class='navbar-toggle' data-toggle='collapse' data-target='#zzyNavbar'><span class='glyphicon glyphicon-download'></span></button>");
	     athis.append($but);
	     let $nWelcome = $("<a class='navbar-brand' href='#'>");
  		 //let $nWelcome = $("<span></span>");
  		athis.append($nWelcome);
  		$nWelcome.zzylabel({data:'Welcome : '});
  		
		  let isvaliduser = true;
		  isUserValid();
		/*if(!isUserValid()){//redirecd to login page
  				return;
		}*/
  		let $nUserName = $("<a class='navbar-brand' href='#'>" + getLocal("username") + "</a>");
  		athis.append($nUserName);
  		
  		$divcollapse = $("<div class='collapse navbar-collapse' id='zzyNavbar'>");
  		athis.append($divcollapse);
  		$nul0 = $("<ul class='nav navbar-nav'>");
  		$divcollapse.append($nul0);
  		$nul0li0 = $("<li>");
  		$nul0.append($nul0li0);
		let $nLanguage = $("<a href='#'>");
		$nul0li0.append($nLanguage);
		$nLanguage.zzylanguage({});

		if(this.defaults.needchangepassword){
	  		$nul0li1 = $("<li>");
	  		$nul0.append($nul0li1);
			let $nChangePassword = $("<a href='#'>");
			$nul0li1.append($nChangePassword);
			$nChangePassword.zzychangepassword({});
		}

		$nul1 = $("<ul class='nav navbar-nav navbar-right'>");
		$divcollapse.append($nul1);
		
		$nul1li0 = $("<li>");
  		$nul1.append($nul1li0);
  		$nul1li00 = $("<a href='#'>");
  		$nul1li0.append($nul1li00);
  		$nul1li00.append($("<span class='glyphicon glyphicon-log-in'>"));
  		
		let $nLogoff = $("<a href='#'>");
		$nul1li00.append($nLogoff);

		$nLogoff.zzylabel({data: 'Log Off'});
		//$nLogoff.zzyinput({placeholder: 'Log Off', type:'button', inputclass:'glyphicon glyphicon-log-in'});
		
		//athis.append($("<hr/>"));
	    $nLogoff.click(function(){
	    	removeLocal('username');
	    	removeLocal('token');
	    	redirectToLogin();
		});
		$(window).bind('zzyresizeEnd', function() {
    		for(let i = 0; i < zzywindowresizeobjs.length; i++){
				let zi = zzywindowresizeobjs[i];
				if(zi.zzyresize !== undefined){
					zi.zzyresize();
				}
			}
		});
	    $(window).resize(function() {
        	if(this.resizeTO) clearTimeout(this.resizeTO);
        	this.resizeTO = setTimeout(function() {
            	$(this).trigger('zzyresizeEnd');
        	}, 500);
    	});
	    
		
	   }
	   ,refreshData:function(){
		  
	   }

    };
	jQuery.fn.zzydashboardtop=function(o)
	{
		let opts=(o===undefined?{}:o);
		let md=new zzyDashboardTop();
		md.defaults=$.extend({},md.defaults,opts,{athis:$(this)});
		md.draw();
        
		return md;
	};

})(jQuery);