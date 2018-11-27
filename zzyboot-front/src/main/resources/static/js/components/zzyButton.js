
(function($)
{
	function zzyButton()
	{
		this.defaults={
				athis:null
  	    ,placeholder:''
  	    ,id:''
  	    ,zzyclass:''
  	    ,zzysize:''
  	    ,parent:''
		  ,glyphicon:''
		  ,badge:''
		  ,modal:''
		  ,confirmmsg:''
  	   };
	};
	

	zzyButton.prototype=
	{
  	   draw:function()
  	   {
		   let athis = this.defaults["athis"];
		   
			   let diitemhtml = "<button";
			   let id = this.defaults["id"];
			   if(id!=''){
				diitemhtml+=" "+zzyID+"='"+id+"'";
			   }
			   let zzyclass = this.defaults.zzyclass;
			   if(zzyclass == '') zzyclass='primary';
				   diitemhtml+=" class='btn btn-" + zzyclass;
			   	let zzysize = this.defaults.zzysize;
				   if(zzysize!=''){
				   	diitemhtml+=" btn-" + zzysize;
				   
			   		}
				   diitemhtml += "'";
			   if(this.defaults.modal!=''){
				   diitemhtml+=" data-toggle='modal' data-target='#"+this.defaults.modal+"'";
			   }
			   diitemhtml+=">";
			   $diitem = $(diitemhtml);

			   athis.append($diitem);
			   $diitemlabel = $("<span>");
			   $diitem.append($diitemlabel);
			   $diitemlabel.zzylabel({data:this.defaults.placeholder});
   			let badge = this.defaults.badge;
			if(badge!=''){
				$badgeobj=$("<span class='badge'>" + badge+ "</span>");
				$diitem.append($badgeobj);
			}
			let parent=this.defaults.parent;
			if(parent != ''){
 					$diitem[0].zzyowner= parent; 
 			
 		 		}			   
			   let confirmmsg = this.defaults.confirmmsg;
			   $diitem.attr('zzyconfirm',confirmmsg);
			   $diitem.on('click', function(){
				   let id = $(this).attr(zzyID);
				   let goParent = true; 
				   let confirmmsg=$(this).attr('zzyconfirm');
				   if(confirmmsg !==''){
					goParent = zzyConfirm(confirmmsg);
				   }
				   if(goParent){
					$(this)[0].zzyowner.zzybtnclick(id);
				   }
				   
			   });
	     
	   }
	   ,refreshData:function(){
		   
	   }

    };
	jQuery.fn.zzybutton=function(o)
	{
		let opts=(o===undefined?{}:o);
		let md=new zzyButton();
		md.defaults=$.extend({},md.defaults,opts,{athis:$(this)});
		md.draw();
        
		return md;
	};

})(jQuery);