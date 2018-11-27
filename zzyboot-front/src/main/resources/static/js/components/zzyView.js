//$('#zylabel_1').zzylabel({data:'name'});

(function($)
{
	function zzyView()
	{
		this.defaults={
  	    athis:null
  	    ,dataid:''
  	    ,parent:''
  	    ,items:[]
  	   };
	};
	

	zzyView.prototype=
	{
  	   draw:function()
  	   {
  		 let parent = this.defaults["parent"]||'';
  		 if(parent!='') {
  			 parent.defaults['zzyview'] = this;
  			 
  		 }
  		 let items = this.defaults.items;
  		 items = [];
  		 let itemsindex = 0;
	       let athis = this.defaults["athis"];
	       let data  = zzyDataProcess[this.defaults.dataid];
		   for(let i = 0; i < data.length; i++){
			   let di = data[i];
			   let placeholder = di.placeholder||'';
			   let id = di.id || '';
			   let type = di.type||'text';
			   if(type=='arrow'){
				   athis.append($("<div class='col-xs-1' align=center> >> </div>"));
				   continue;
			   }
			   let diitemhtml = "<div class='col-xs-2'>";
			   let $diitem = $(diitemhtml);
			   athis.append($diitem);
			   let $dicontent = $("<scan>");
			   $diitem.append($dicontent);
			   let badge = di.badge||'';
			   $dicontent.zzybutton({id:id,badge:badge,placeholder:placeholder,parent:parent,zzysize:'lg'})
			   items[itemsindex++] = $diitem;
			   di['obj'] = $diitem;
				

		   }
		   this.defaults.items = items;
	     this.refreshData();
  	   },refreshData:function(){
  	   },selectitem:function(id){
  	    alert(id);
	   }

    };
	jQuery.fn.zzyview=function(o)
	{
		let opts=(o===undefined?{}:o);
		let md=new zzyView();
		md.defaults=$.extend({},md.defaults,opts,{athis:$(this)});
		md.draw();
        
		return md;
	};

})(jQuery);