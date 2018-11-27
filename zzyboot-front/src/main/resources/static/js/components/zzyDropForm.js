//$('#zylabel_1').zzylabel({data:'name'});

(function($)
{
	function zzyDropForm()
	{
		this.defaults={
				athis:null
  	    ,formid:""
  	    ,label:""
  	    ,data:""
		  ,parent:""
		  ,isgeneral:true
  	   };
	};
	

	zzyDropForm.prototype=
	{
  	   draw:function()
  	   {
  		 if(this.defaults.parent!='') {
  			this.defaults.parent.defaults['zzydropform'] = this; 
  		 }
	     this.refreshData();
	   }
	   ,refreshData:function(){
	       var athis=this.defaults["athis"];
		   let $ndiv = $("<scan class='dropdown'>");
		   athis.append($ndiv);
		   let $nlabel = $("<a class='dropdown-toggle' href='#' role='link' data-toggle='dropdown' aria-haspopup='true' aria-expanded='false'>");
    		$ndiv.append($nlabel);
    		this.defaults['nlabel'] = $nlabel;
 		   let $nlabelin = $("<span>");
 		  $nlabel.append($nlabelin);
		   let $nmenu = $("<div class='dropdown-menu'>");
   			$ndiv.append($nmenu);
 		   let $nform = $("<span>");
  			$nmenu.append($nform);

 		  
 		  
 		  $nlabelin.zzylabel({data: this.defaults["label"]});
 		  let parent = this.defaults["parent"]||'';
 		  
		   $nform.zzyform({formid:this.defaults["formid"],isgeneral:this.defaults.isgeneral,data:this.defaults["data"],parent:parent});
		   if(parent != ''){
			   parent.defaults['zzydropformlabel'] = $nlabel;
		   }
		   
	   }

    };
	jQuery.fn.zzydropform=function(o)
	{
		let opts=(o===undefined?{}:o);
		let md=new zzyDropForm();
		md.defaults=$.extend({},md.defaults,opts,{athis:$(this)});
		md.draw();
        
		return md;
	};

})(jQuery);