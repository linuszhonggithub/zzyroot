(function($)
{
	function zzyRadioGroup()
	{
		this.defaults={
				athis:null
  	    ,name:''
  	    ,code:[]
  	    ,data:[]
		,clickvalue:''
  	   };
	};
	

	zzyRadioGroup.prototype=
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
	       for(let i = 0; i < code.length; i++){
	    	   let ci = code[i];
	    	   let di = data[i];
	    	   let htmli = "<input type='radio' name='"+name+"' value='"+ci+"'>";
		       let $nHolding = $(htmli);
		       athis.append($nHolding);
		       
		       let $nHolding1 = $("<span>  "+di+"  </span>");
		       athis.append($nHolding1);
	    	   
	       }
		   
	   }

    };
	jQuery.fn.zzyradiogroup=function(o)
	{
		let opts=(o===undefined?{}:o);
		let md=new zzyRadioGroup();
		md.defaults=$.extend({},md.defaults,opts,{athis:$(this)});
		md.draw();
        
		return md;
	};

})(jQuery);