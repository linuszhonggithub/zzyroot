
(function($)
{
	function zzyModaltable()
	{
		this.defaults={
				athis:null
  	    ,tablename:""
		  ,parent:''
		  ,title: ''
  	   };
	};
	

	zzyModaltable.prototype=
	{
  	   draw:function()
  	   {
		 let tablename = this.defaults.tablename || '';
		 if(tablename == ''){
			 return;
		 }
		  addZzyDataCenterDatachangeListener(zzyPrefixTableHeader+tablename,this);
	     //this.refreshData();
	   }
	   ,refreshData:function(){
		   let parent = this.defaults.parent||'';
		  let athis=this.defaults["athis"];
		  let tablename = this.defaults.tablename;
		  let tableheader = zzyDataTableHeader[zzyPrefixTableHeader+tablename] || '';
		    
		  if(tableheader == ''){
			  return;
		  }
		  if(athis.children().length>0){
			  return;
		  }
		  let title = this.defaults.title;
		  let amodal = athis.zzymodal({title:title,parent:parent,id:tablename});
		  let modalbody = amodal.defaults.body;
		  let data=[];
		  let dataindex = 0;
		  for(let i = 0; i < tableheader.length; i++){
			  let ti = tableheader[i];
			  let id = ti.name;
			  let fieldtype = ti.fieldtype;
			  if(fieldtype == 'hidden'){
				  data[dataindex] = {id:id,type:fieldtype};
			  }else{
				  let required = false; if((ti.isrequired||'') == 'true'){required = true;}
				  let isuniq = false; if((ti.isunique||'') == 'true'){isuniq = true;}
				  let readonly = false; if((ti.isreadonly||'') == 'true'){readonly = true;}
				  let isprimary = false; if((ti.isprimary||'') == 'true'){isprimary = true;}
				  
				  data[dataindex] = {id:id,type:fieldtype, placeholder: ti.label,minlength:zzyGetInteger(ti.minlength), required:required, isuniq: isuniq,readonly:readonly,isprimary:isprimary};
			  }
			  dataindex++;
		  }
		  modalbody.zzyform({formid:tablename,data:data,parent:parent});
	   }

    };
	jQuery.fn.zzymodaltable=function(o)
	{
		let opts=(o===undefined?{}:o);
		let md=new zzyModaltable();
		md.defaults=$.extend({},md.defaults,opts,{athis:$(this)});
		md.draw();
        
		return md;
	};

})(jQuery);