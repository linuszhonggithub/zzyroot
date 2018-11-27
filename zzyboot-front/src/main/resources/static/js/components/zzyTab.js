
(function($)
{
	function zzyTab()
	{
		this.defaults={
  	    athis:null
  	    ,data:[]
  	    ,parent:''
		  ,items:[]
		  ,headerheight:40

  	   };
	};
	

	zzyTab.prototype=
	{
  	   draw:function()
  	   {
  		 let parent = this.defaults["parent"]||'';
  		 if(parent!='') {
  			 parent.defaults['zzytab'] = this;
  			 
  		 }
  		
  		 
		   let athis = this.defaults["athis"];
		   
	       let $ul = $("<ul class='nav nav-tabs'>");
		   athis.append($ul);
		   this.defaults.tabtop = $ul;
	       let $tabbody = $("<div class='tab-content'>");
	       athis.append($tabbody);
			this.defaults.tabbody = $tabbody;
		   let data  = this.defaults["data"];
	       let activeid = this.defaults["activeid"]||data[0].id;
		   for(let i = 0; i < data.length; i++){
			   this.add(data[i],false);
		   }
		   this.setActive(activeid);
		 this.zzyresize();
		   this.refreshData();
		 
  	   },add:function(di,isnew){
			if(isnew){
				this.defaults.data[this.defaults.data.length]=di;
			}
			let items = this.defaults.items;
			let id = di.id || '';
			if(items[id] !== undefined){
				this.setActive(id);
				return;
			}
			let $tabbody = this.defaults.tabbody;
			let $ul = this.defaults.tabtop;
			let placeholder = di.placeholder||'';
			let itemdesc="<li zzyid='"+id+"' data-toggle='tab' href='#zzytab"+zzySerial+"'>";
			let $diitem = $(itemdesc);
			$ul.append($diitem);
			let itembody0 = "<div class='tab-pane fade' id='zzytab"+zzySerial+"'>";
			zzySerial++;
			let $diitembody0 = $(itembody0);
			$tabbody.append($diitembody0);

			let itembody = "<div>"+id+"</div>";
			let $diitembody = $(itembody);
			$diitembody0.append($diitembody);
			$diitembody.attr('style','overflow: auto;width:100%');

			items[id]={ttop:$diitem,tbody:$diitembody};
			di['obj'] = $diitem;
			let msgcolor = '';
			$diitem.zzylabel({data:placeholder,color:msgcolor, parent:this.defaults.parent, showaslink:true});
			this.defaults.items = items;
			   
			$diitem.on('shown.bs.tab', function(){
				   let id = $(this).attr('zzyid'); 
				   
				   $(this)[0].zzyowner.selectitem(id);
			});
			this.setItemBodyHeight(items[id]);
  	   },getbodyheight:function(){
			 return getZzyBodyHeight(this.defaults.headerheight);
			 
  	   },refreshData:function(){
  	   },setActive:function(id){
			 let item = this.defaults.items[id]; 
			 item.ttop.tab('show');
			let itembody = item.tbody;
			let bodyhtml = itembody[0].innerHTML;
		   if(bodyhtml == id){
			   this.drawbody(id,itembody);
		   }
  	   },drawbody:function(id,itembody){
			let data = this.defaults.data;
			for(let i = 0; i < data.length; i++){
				let di = data[i];
				if(di.id == id){
					this.drawbodytask(di, itembody);
					break;
				}
			}
  	   },drawbodytask:function(di,itembody){
		  let tasks = di.tasks;
		  if(tasks === undefined){
			  return;
		  }
		  let tasktype = tasks.type;
		  itembody[0].innerHTML="";
		  if(tasktype == "process"){
			itembody.zzyview({dataid:tasks.data, parent:this});
		  }else if(tasktype == "table"){
			  let table = tasks.id;
			  let where = tasks.where;
			  itembody.zzytable({data:{table:table,where:where}, parent:this});
			  
		  }
	   },zzybtnclick:function(value){
		   let tasks = zzyDataProcess[value]||'';
		   if(tasks == ''){
			   return;
		   }
		   for(let i = 0; i < tasks.length; i++){
			   let ti = tasks[i];
			   if(ti.type == 'table'){
				   let ispopup = false;
				   if(ti.ispopup){
					   ispopup=true;
				   }
				   let di = {id:ti.id,placeholder:ti.placeholder,parent:this.defaults.parent,tasks:ti};
				   if(!ispopup){
						this.add(di,true);
						this.setActive(ti.id);
						continue;
				   }
				   
			   }
		   }
  	   },setItemBodyHeight:function(itemi){
			 itemi.tbody.height(this.getbodyheight());
	   },zzyresize:function(){
		 let items = this.defaults.items;
		 for(let i in items){
			 if(!items.hasOwnProperty(i)){
				continue;
			 }
			 let ii = items[i];
			 this.setItemBodyHeight(ii);
		 }
	   }

    };
	jQuery.fn.zzytab=function(o)
	{
		let opts=(o===undefined?{}:o);
		let md=new zzyTab();
		md.defaults=$.extend({},md.defaults,opts,{athis:$(this)});
		md.draw();
        zzywindowresizeobjs[zzywindowresizeobjs.length] = md;
		return md;
	};

})(jQuery);