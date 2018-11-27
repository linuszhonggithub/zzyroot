(function($)
{
	function zzyLanguage()
	{
		this.defaults={
				athis:null
  	    ,data:""
  	    ,needgetlocal:false
  	   };
	};
	

	zzyLanguage.prototype=
	{
  	   draw:function()
  	   {
  		   
  		let athis=this.defaults["athis"];
  		let index = 0;
  		//let code = [];
  		let data = [];
  		for(let i = 0; i < zzylan.length; i++){
  			let zi = zzylan[i]
  			data[index++] = {placeholder:zi.label,id:zi.code};
  		}
  		athis.zzydroplist({label:'Language', data:data,parent: this});

  		
  		
	     
	   },selectitem:function(value){
		   zzyDataCenter['zzylancurrent'] = value;
		   setLocal('currentlan',value);
		   zzyDataCenterDataChange('zzylancurrent', value);
	   }
    };
	jQuery.fn.zzylanguage=function(o)
	{
		let opts=(o===undefined?{}:o);
		let md=new zzyLanguage();
		md.defaults=$.extend({},md.defaults,opts,{athis:$(this)});
		md.draw();
        
		return md;
	};

})(jQuery);