//$('#zylabel_1').zzylabel({data:'name'});

(function($)
{
	function zzyLabel()
	{
		this.defaults={
				athis:null
  	    ,data:""
  	    ,color:''
  	    ,parent:''
  	    ,needgetlocal:true
		  ,showaslink:false
  	   };
	};
	

	zzyLabel.prototype=
	{
  	   draw:function()
  	   {
  		 let needgetlocal = this.defaults.needgetlocal;
 		 let parent = this.defaults.parent||'';
 		 if(parent != ''){
 			 if( parent.defaults.needgetlocal !== undefined){
 				needgetlocal = parent.defaults.needgetlocal;
 				this.defaults.needgetlocal = needgetlocal;
 			 }
 		 }
 		 
 		 if(needgetlocal){
 			addZzyDataCenterDatachangeListener("zzylancurrent",this);	 
 		 }
  		 
  		let athis=this.defaults["athis"];
  		let color = this.defaults["color"]||'';
  		if(color != ''){
  			
  			athis.attr('color',color);	
  		}
 		 if(parent != ''){
 			athis[0].zzyowner= parent; 
 			
 		 }

	     this.refreshData();
	   }
	   ,refreshData:function(){
		   let needgetlocal = this.defaults.needgetlocal;
		   let data = this.defaults.data;
	       let athis=this.defaults["athis"];
	       let showdata = "";
	       if(needgetlocal){
	    	   showdata=getLocaleItem(data);	 
	 		 }else{
	 			showdata=data;
			  }
	       let showaslink = this.defaults.showaslink;
	       if(showaslink){
	    	   showdata="<a href='#'>" + showdata +"</a>";
	       }
	       $(athis)[0].innerHTML=showdata;
		   
	   }

    };
	jQuery.fn.zzylabel=function(o)
	{
		let opts=(o===undefined?{}:o);
		let md=new zzyLabel();
		md.defaults=$.extend({},md.defaults,opts,{athis:$(this)});
		md.draw();
        
		return md;
	};

})(jQuery);