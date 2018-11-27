
(function($)
{
	function zzyModal()
	{
		this.defaults={
				athis:null
		  ,parent:''
		  ,title: ''
		  ,body:''
		  ,id:''
  	   };
	};
	

	zzyModal.prototype=
	{
  	   draw:function()
  	   {
 		 let parent = this.defaults.parent||'';
  		 
		  let athis=this.defaults["athis"];
 		 if(parent != ''){
 			athis[0].zzyowner= parent; 
 			
		  }
		  let $adiv0 = $("<div id='zzyModal_" + this.defaults.id +"' class='modal fade' role='dialog'>");
		  athis.append($adiv0);
		  let $adiv=$("<div class='modal-dialog'>");
		  $adiv0.append($adiv);
		  let $acontent=$("<div class='modal-content'>");
		  $adiv.append($acontent);
		  let $aheader=$("<div class='modal-header'>");
		  $acontent.append($aheader);
		  let $aheaderbtn=$("<button type='button' class='close' data-dismiss='modal'>&times;</button>");
		  $aheader.append($aheaderbtn);
		  $aheadertitle=$("<h4 class='modal-title'>");
		  $aheader.append($aheadertitle);
		  $aheadertitle.zzylabel({data:this.defaults.title});
		  $abody=$("<div class='modal-body'>");
		  $acontent.append($abody);
		  this.defaults.body = $abody;

		  /*$afooter=$("<div class='modal-footer'>");
		  $acontent.append($afooter);
		  $aclose=$("<button type=button data-dismiss='modal'>");
		  $afooter.append($aclose);
		  $aclose.zzylabel({data:'Close'});*/
		  
	     this.refreshData();
	   }
	   ,refreshData:function(){
		   
		   
	   }

    };
	jQuery.fn.zzymodal=function(o)
	{
		let opts=(o===undefined?{}:o);
		let md=new zzyModal();
		md.defaults=$.extend({},md.defaults,opts,{athis:$(this)});
		md.draw();
        
		return md;
	};

})(jQuery);