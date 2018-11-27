//$('#zylabel_1').zzylabel({data:'name'});

(function($)
{
	function zzyTableAttr()
	{
		this.defaults={
  	    athis:null
		  ,tablename:''
		  ,parent:''
		  ,cols:[]
		  ,modal:''
  	   };
	};
	

	zzyTableAttr.prototype=
	{
  	   draw:function()
  	   {
		   let tablename = this.defaults.tablename;
		   if(tablename==''){
			   return;
		   }
		   addZzyDataCenterDatachangeListener(zzyPrefixTableAttr+tablename,this);
	       
	     //this.refreshData();
  	   },refreshData:function(){
			 let tablename = this.defaults.tablename;
			 if(tablename == ''){
				 return;
			 }
			let tableattrdata = zzyDataTableAttr[zzyPrefixTableAttr+tablename]||'';
			if(tableattrdata == ''){
				  return;
			}
			   let athis = this.defaults["athis"];
			   if(athis.children().length>0){
				   return;
			   }
 			let parent=this.defaults.parent;
			  let $newModal = $("<span>");
				athis.append($newModal);
			  let title = tablename;
			  if((tableattrdata.label||'')!=''){
				  title = tableattrdata.label; 
			  }
			  $newModal.zzymodaltable({title:title,parent:parent,tablename:tablename});
			  let $newContent = $("<span>");
			  athis.append($newContent);
			  if((tableattrdata['caninsert']||'')!='false'){
				$btnNew = $('<span>');
				$newContent.append($btnNew);
				$btnNew.zzybutton({placeholder:'New',parent:parent,id:'zzytablenew',zzysize:'sm',modal:zzyModalName + tablename});
				
			  }
			  if((tableattrdata['candelete']||'')!='false'){
				$btnDelete = $('<span>');
				$newContent.append($btnDelete);
				$btnDelete.zzybutton({placeholder:'Delete',parent:parent,zzysize:'sm',id:'zzytabledelete',confirmmsg:zzyDeleteMsg});
				
			  }
	   }

    };
	jQuery.fn.zzytableattr=function(o)
	{
		let opts=(o===undefined?{}:o);
		let md=new zzyTableAttr();
		md.defaults=$.extend({},md.defaults,opts,{athis:$(this)});
		md.draw();
        
		return md;
	};

})(jQuery);