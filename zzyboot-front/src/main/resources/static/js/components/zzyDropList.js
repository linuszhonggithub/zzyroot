//$('#zylabel_1').zzylabel({data:'name'});

(function($)
{
	function zzyDropList()
	{
		this.defaults={
				athis:null
  	    ,listid:""
  	    ,label:""
  	    ,data:""
  	    ,parent:""
  	   };
	};
	

	zzyDropList.prototype=
	{
  	   draw:function()
  	   {
  		 if(this.defaults.parent!='') {
  			this.defaults.parent.defaults['zzydroplist'] = this; 
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
 		   let $nlist = $("<span>");
  			$nmenu.append($nlist);

 		  
 		  
 		  $nlabelin.zzylabel({data: this.defaults["label"]});
 		  let parent = this.defaults["parent"]||'';
 		  
		   $nlist.zzylist({listid:this.defaults["listid"],data:this.defaults["data"],parent:parent});
		   if(parent != ''){
			   parent.defaults['zzydroplistlabel'] = $nlabel;
		   }
		   
	   }

    };
	jQuery.fn.zzydroplist=function(o)
	{
		let opts=(o===undefined?{}:o);
		let md=new zzyDropList();
		md.defaults=$.extend({},md.defaults,opts,{athis:$(this)});
		md.draw();
        
		return md;
	};

})(jQuery);