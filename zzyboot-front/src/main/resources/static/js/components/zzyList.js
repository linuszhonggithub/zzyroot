//$('#zylabel_1').zzylabel({data:'name'});

(function($)
{
	function zzyList()
	{
		this.defaults={
  	    athis:null
  	    ,data:[]
  	    ,listid:''
  	    ,parent:''
  	    ,items:[]
  	   };
	};
	

	zzyList.prototype=
	{
  	   draw:function()
  	   {
  		 let parent = this.defaults["parent"]||'';
  		 let parentitemindex=0;
  		 if(parent!='') {
  			 parent.defaults['zzylist'] = this;
  			 
  		 }
  		 let items = this.defaults.items;
  		 items = [];
  		 let itemsindex = 0;
	       let athis = this.defaults["athis"];
	       let listid = this.defaults["listid"];
	       let data  = this.defaults["data"];
		   for(let i = 0; i < data.length; i++){
			   let di = data[i];
			   let msg = '';
			   let placeholder = di.placeholder||'';
			   let id = di.id || '';
			   let required = di.required||false;
			   let msgcolor='';
			   let type = di.type||'text';
			   if(type=='divider'){
				   athis.append($("<div class='dropdown-divider'>"));
				   continue;
			   }
			   let $diitem = $("<div class='dropdown-item' zzyid='"+id+"'>");
			   athis.append($diitem);
			   items[itemsindex++] = $diitem;
			   di['obj'] = $diitem;
			   $diitem.zzylabel({data:placeholder,color:msgcolor, parent:this.defaults.parent});
			   
			   
			   $diitem.on('click', function(){
				   let id = $(this).attr('zzyid'); 
				   $(this)[0].zzyowner.selectitem(id);
			   });

		   }
		   this.defaults.items = items;
	     this.refreshData();
  	   },refreshData:function(){
  	   
	   }

    };
	jQuery.fn.zzylist=function(o)
	{
		let opts=(o===undefined?{}:o);
		let md=new zzyList();
		md.defaults=$.extend({},md.defaults,opts,{athis:$(this)});
		md.draw();
        
		return md;
	};

})(jQuery);