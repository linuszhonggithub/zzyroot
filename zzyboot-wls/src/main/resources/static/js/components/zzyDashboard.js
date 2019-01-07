//$('#zylabel_1').zzydashboard({});

(function($)
{
	function zzyDashboard()
	{
		this.defaults={
				athis:null
  	    ,data:""
  	   };
	};
	

	zzyDashboard.prototype=
	{
  	   draw:function()
  	   {
	     let athis = this.defaults.athis;
  		 let $nWelcome = $("<span>");
  		athis.append($nWelcome);
  		let $nBody = $("<span>main body wls</span>");
  		athis.append($nBody);
  		$nWelcome.zzydashboardtop({needchangepassword:true});
		
	   }
	   ,refreshData:function(){
		  
	   }

    };
	jQuery.fn.zzydashboard=function(o)
	{
		let opts=(o===undefined?{}:o);
		let md=new zzyDashboard();
		md.defaults=$.extend({},md.defaults,opts,{athis:$(this)});
		md.draw();
        
		return md;
	};

})(jQuery);