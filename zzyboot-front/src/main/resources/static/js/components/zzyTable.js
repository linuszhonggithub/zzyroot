//$('#zylabel_1').zzylabel({data:'name'});

(function($)
{
	function zzyTable()
	{
		this.defaults={
  	    athis:null
  	    ,data:''
  	    ,parent:''
		  ,items:[]
		  ,zzyheader:''
  	   };
	};
	

	zzyTable.prototype=
	{
  	   draw:function()
  	   {
  		 let parent = this.defaults["parent"]||'';
  		 if(parent!='') {
  			 parent.defaults['zzytable'] = this;
  			 
  		 }
	       let athis = this.defaults["athis"];
		   let tabledata  = this.defaults.data;
		   if(tabledata == ''){
			   return;
		   }
		   let tablename = tabledata.table;
		   let where = tabledata.where;
		   setTableData(tabledata,this);

		   let $nAttr = $("<div width=100% overflow=auto>");
		   athis.append($nAttr);
		   this.attr = $nAttr;
		   $nAttr.zzytableattr({tablename:tablename,parent:this});
		   this.defaults.attrpanel = $nAttr; 


		   let $nHeader = $("<div width=100% overflow=auto>");
		   athis.append($nHeader);
		   this.defaults.zzyheader = $nHeader.zzytableheader({tablename:tablename,parent:this});

		   addZzyDataCenterDatachangeListener(zzyPrefixTableBody+tablename,this);
		//this.zzyresize();
		//this.defaults.zzyheader.zzyresize();
		   //this.refreshData();
		   
  	   },zzybtnclick:function(id){
			 if(id=='zzytablenew'){
				 this.defaults.zzyform.clear();
			 }else if(id.indexOf(zzyDelete)==0){
	 		    let tabledata  = this.defaults.data;
		   		if(tabledata == ''){
			   		return;
		   		}
		   		let tablename = tabledata.table;

				 let deleteid = id.substring(zzyDelete.length);
				 sendajaxIPPort("zzytablesave",tablename + zzySplitBlock + deleteid);
			 }else if(id.indexOf(zzyEdit)==0){
	 		    let tabledata  = this.defaults.data;
		   		if(tabledata == ''){
			   		return;
		   		}
				   let tablename = tabledata.table;
			    let tabledata0 = zzyDataTableBody[zzyPrefixTableBody+tablename]||'';
			    if(tabledata0 == ''){
				  return;
				}
				 let editid = id.substring(zzyEdit.length);
				for(let i = 0; i < tabledata0.length; i++){
				  let ti = tabledata0[i];
				  
				  if(ti.id == editid ){
					  this.defaults.zzyform.edit(ti);
					  break;
				  }
				}
			 }
  	   },refreshData:function(){
			let zzyheader = this.defaults.zzyheader;
			if(zzyheader == ''){
				return;
			}
			zzyheader.setbody();
			
	   }

    };
	jQuery.fn.zzytable=function(o)
	{
		let opts=(o===undefined?{}:o);
		let md=new zzyTable();
		md.defaults=$.extend({},md.defaults,opts,{athis:$(this)});
		md.draw();
        
		return md;
	};

})(jQuery);