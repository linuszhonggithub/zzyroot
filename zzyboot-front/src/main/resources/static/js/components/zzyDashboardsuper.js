//$('#zylabel_1').zzydashboard({});

(function($)
{
	function zzyDashboardsuper()
	{
		this.defaults={
				athis:null
  	    ,data:""
  	   };
	};
	

	zzyDashboardsuper.prototype=
	{
  	   draw:function()
  	   {
	     let athis = this.defaults.athis;
  		 let $nWelcome = $("<span>");
  		athis.append($nWelcome);
   		let $nBody = $("<div>");
   		athis.append($nBody);
  		$nWelcome.zzydashboardtop({});
  		$nBody.zzytabsuper({});
	   }
	   ,refreshData:function(){
		  
	   }

    };
	jQuery.fn.zzydashboardsuper=function(o)
	{
		let opts=(o===undefined?{}:o);
		let md=new zzyDashboardsuper();
		md.defaults=$.extend({},md.defaults,opts,{athis:$(this)});
		md.draw();
        
		return md;
	};

})(jQuery);