(function($)
{
	function zzyRadio()
	{
		this.defaults={
				athis:null
  	    ,name:''
  	    ,code:''
  	    ,data:""
  	   };
	};
	

	zzyRadio.prototype=
	{
  	   draw:function()
  	   {
	     this.refreshData();
	   }
	   ,refreshData:function(){
		   let name = this.defaults.name;
		   let code = this.defaults.code;
		   let data = this.defaults.data;
	       let athis=this.defaults["athis"];
		   $(athis)[0].innerHTML="<input type='radio' name=''"+name+"'' value='"+code+"'>" + data;
	   }

    };
	jQuery.fn.zzyradio=function(o)
	{
		let opts=(o===undefined?{}:o);
		let md=new zzyRadio();
		md.defaults=$.extend({},md.defaults,opts,{athis:$(this)});
		md.draw();
        
		return md;
	};

})(jQuery);