//$('#zylabel_1').zzylabel({data:'name'});

(function($)
{
	function zzyInput()
	{
		this.defaults={
				athis:null
  	    ,placeholder:''
  	    ,id:''
  	    ,type:'text'
  	    ,required:false
  	    ,readonly:false
  	    ,inputclass:''
  	    ,owner:''
		  ,glyphicon:''
		  ,value:''
  	   };
	};
	

	zzyInput.prototype=
	{
  	   draw:function()
  	   {
  		 addZzyDataCenterDatachangeListener("zzylancurrent",this);
  		 let athis = this.defaults["athis"];
  		athis.attr(zzyID,this.defaults.id);
  		athis.attr('type',this.defaults.type);
  		athis.attr('required',this.defaults.required);
  		 let inputclass = this.defaults.inputclass;
  		 if(this.defaults.required){
  			 inputclass += ' required'; 
  			
  		 }
  		athis.attr('readonly',this.defaults.readonly);
  		 if(inputclass != ''){
  			athis.attr('class', inputclass); 
  		 }
  		 let owner = this.defaults.owner||'';
  		 if(owner != ''){
  			athis[0].zzyowner= owner; 
  			
  		 }

  		 this.refreshData();
	     
	   }
	   ,refreshData:function(){
		   let placeholder = this.defaults.placeholder||'';
	       let athis=this.defaults["athis"];
	       let type = this.defaults.type;
	       if(type == 'button'){
	    	   athis.attr('value',getLocaleItem(placeholder));   
	       }else{
	    	   athis.attr('placeholder',getLocaleItem(placeholder));   
	       }
	       
	   }

    };
	jQuery.fn.zzyinput=function(o)
	{
		let opts=(o===undefined?{}:o);
		let md=new zzyInput();
		md.defaults=$.extend({},md.defaults,opts,{athis:$(this)});
		md.draw();
        
		return md;
	};

})(jQuery);