(function($)
{
	function zzyTableHeader()
	{
		this.defaults={
  	    athis:null
		  ,tablename:''
		  ,parent:''
		  //cols:[]
		  ,table0: ''
		  ,rowheight:32
		  ,rowcnt:10
		  ,width:500
		  ,currentindex:1
		  ,otherrowcnt: 3
		  ,firstload: true
		  ,leftwidth: 200
		  ,scrollwidth:15
		  ,scrolltop:0
		  ,scrollheight:0
		  ,datacnt: 0
		  ,datacntlast: 0
		  ,asc: true
		  ,selectA: []
		
  	   };
	};
	

	zzyTableHeader.prototype=
	{
  	   draw:function()
  	   {

		   let tablename = this.defaults.tablename;
		   if(tablename==''){
			   return;
		   }
		   this.setrow();
		   addZzyDataCenterDatachangeListener(zzyPrefixTableHeader+tablename,this);
	       
  	   },setrow:function(){
   			 let parent = this.defaults.parent;
			 let width = parent.defaults.athis.width();
			 
			 let height = parent.defaults.athis.height();
			 //console.log("setrow width is " + width+", height is " + height);
			 let rowcnt = parseInt(height / this.defaults.rowheight) - this.defaults.otherrowcnt;
			 if(rowcnt > 0){
		   		this.defaults.rowcnt = rowcnt; 
		   		this.defaults.width = width;

			 }
			 
  	   },zzyscroll:function(){
			 let atest = this.defaults.tablescrollbardiv;
			 let scrolltop = atest[0].scrollTop;
			 let scrollheight = atest[0].scrollTopMax;
			 let currentindex = 0;
			 if(scrolltop > 0 && scrollheight > 0 && this.defaults.datacnt > 0){
				currentindex = parseInt((scrolltop * this.defaults.datacnt + this.defaults.rowheight) / scrollheight);
				if(currentindex > this.defaults.datacnt - this.defaults.rowcnt){
					currentindex = this.defaults.datacnt - this.defaults.rowcnt;
				} 
			 }
			 if(currentindex != this.defaults.currentindex){
				 this.defaults.currentindex = currentindex;
				 this.setbody();
			 }

	       
  	   },zzyresize:function(){
			 let rowcnt = this.defaults.rowcnt;

			 this.setrow();
			 if(rowcnt != this.defaults.rowcnt){
				 this.setbody();
			 }
			 let width = this.defaults.parent.defaults.athis.width();
			 if(width == 0){
				 width = window.width;
			 }
			 if(width>0){
 			    if(this.defaults.leftdiv!=undefined){
			 	  let leftwidth = this.defaults.leftdiv.width(); 
			 	  let rightwidth = width - leftwidth - 20 - this.defaults.scrollwidth;
			 	  this.defaults.rightdiv.attr('style','overflow:auto;width:'+rightwidth+'px');
			    }
			  }

  	   },refreshData:function(){
			 this.setheader();
  	   },setheader:function(){
			 let tablename = this.defaults.tablename;
			 if(tablename == ''){
				 return;
			 }
			  let tableheaderdata = zzyDataTableHeader[zzyPrefixTableHeader+tablename]||'';
			  if(tableheaderdata == ''){
				  return;
			  }
			let athis = this.defaults["athis"];
			if(athis.children().length<1){
				let $table0 = $('<table>');
				athis.append($table0);
			
				//$table0[0].innerHTML = '';
				let $tablebody0 = $("<tbody>");
				$table0.append($tablebody0);
			
				let $tabletr0 = $("<tr>");
				$tablebody0.append($tabletr0);
				let $tabletd0 = $("<td valign='top'>");
				$tabletr0.append($tabletd0);
				let $tabletd1 = $("<td valign='top'>");
				$tabletr0.append($tabletd1);
				let $tabletd2 = $("<td valign='top'>");
				$tabletr0.append($tabletd2);
			 	let $tablerightdiv = $("<div>");
			 	$tabletd1.append($tablerightdiv);
			 	this.defaults.rightdiv = $tablerightdiv;
			 	let $tablescrollbardiv = $("<div style='overflow-x:none;overflow-y:auto;width:" + this.defaults.scrollwidth + "px'>");
				 $tabletd2.append($tablescrollbardiv);
				 $tablescrollbardiv[0].zzyowner = this;
			 	this.defaults.tablescrollbardiv = $tablescrollbardiv;

			 	let $tablelefttable = $("<table class='table table-striped table-hover table-condensed' style='border-right: 1px dotted gray'>");
			 	let $tablerighttable = $("<table class='table table-striped table-hover table-condensed'>");
			 	
			 	$tabletd0.append($tablelefttable);
			 	this.defaults.leftdiv = $tablelefttable;
			 	$tablerightdiv.append($tablerighttable);

 			 	let $tableleftbody = $("<tbody>");
			 	let $tablerightbody = $("<tbody>");
			 	$tablelefttable.append($tableleftbody);
			 	$tablerighttable.append($tablerightbody);
			 	this.defaults.tableleft = $tableleftbody;
			 	this.defaults.tableright = $tablerightbody;
			}
			let $tablelefttable = this.defaults.tableleft;
			 let $tablerighttable = this.defaults.tableright;
			
			 let $tablelefttr = $("<tr>");
			 let $tablerighttr = $("<tr>");
			  $tablelefttable.append($tablelefttr);
			  $tablerighttable.append($tablerighttr);
			  
			  //let cols = this.defaults.cols;
			  let $col0 = $("<th class='text-nowrap'>#</th>");
			  $tablelefttr.append($col0);
			  let $col1 = $("<th class='text-nowrap'>");
			  $tablelefttr.append($col1);
			  let selectitem = $('<input type=checkbox>');
			  $col1.append(selectitem);
			  selectitem.click({obj:this},function(event){
				  event.data.obj.selectAll($(this));
			  });
			  let index = 0;
			  //cols[index++] = $col0;

			   //$diitem.append($diitemlabel);
			
			
			for(let i = 0; i < tableheaderdata.length; i++){
				let ti = tableheaderdata[i];
				let fieldtype = ti.fieldtype;
				if(fieldtype == 'hidden'){
					continue;
				}
				let name = ti.name;
				let label = ti.label;
				let isprimary = (ti.isprimary=="true");
				let iskeyword = (ti.iskeyword=="true");
				let isunique = (ti.isunique=="true");
				let isrequired = (ti.isrequired=="true");
				let minlength = ti.minlength;
				let isfrozen = (ti.isfrozen=="true");
				if(isprimary){isfrozen=true;}
				let $col = $("<th class='text-nowrap'>");
				//cols[index++] = $col;
				if(isfrozen){
					$tablelefttr.append($col);
				}else{
					$tablerighttr.append($col);
				}
				
				$col.zzylabel({data: label});
				$col.click({name:name,obj:this,fieldtype:fieldtype},function(event){
					event.data.obj.clicktitle(event.data.name,event.data.fieldtype);
				});
				  //athis.append($nlabel);
				//$nlabel.zzybutton({placeholder:label,parent:parent});
			}
			
  	   },selectAll:function(allItem){
			 let allchecked = false;
			 if(allItem.is(':checked')){
				 allchecked = true;
			 }
			 for(let i = 0; i < this.defaults.selectA.length; i++){
				 let si = this.defaults.selectA[i];
				 si.attr('checked',allchecked);
				 this.defaults.checkArray[si.attr('zzyid')].ischecked = allchecked;
			 }
			 this.defaults.selectAllStatus = allchecked;
			 
  	   },clicktitle:function(name,fieldtype){
			 let tablename = this.defaults.tablename;
			 if(tablename == ''){
				 return;
			 }
 			  let tabledata = zzyDataTableBody[zzyPrefixTableBody+tablename]||'';
			  if(tabledata == ''){
				  return;
			  }

			 let asc = this.defaults.asc;
			 let sortcol = this.defaults.sortcol||'';
			 if(sortcol == name){
				if(asc){asc = false; }
				else{asc = true; }
			 }else{
				 asc = true;
			 }
			 this.defaults.sortcol = name;
			 this.defaults.asc = asc;
			 ZzyDataSort({data: tabledata, name: name, asc:asc, filetype:fieldtype, triggername:zzyPrefixTableBody+tablename});
  	   },setbody:function(){
			 
			 let tablename = this.defaults.tablename;
			 if(tablename == ''){
				 return;
			 }
			let tableattrdata = zzyDataTableAttr[zzyPrefixTableAttr+tablename]||'';
			if(tableattrdata == ''){
				  return;
			}

			 let tableheaderdata = zzyDataTableHeader[zzyPrefixTableHeader+tablename]||'';
			  if(tableheaderdata == ''){
				  return;
			  }
			  let tabledata = zzyDataTableBody[zzyPrefixTableBody+tablename]||'';
			  if(tabledata == ''){
				  return;
			  }
			let datalength = tabledata.length;
			
			  this.defaults.datacnt = datalength;
			  if(this.defaults.datacntlast < this.defaults.datacnt){
				 this.defaults.currentindex = datalength - this.defaults.rowcnt;
			  }
			  this.defaults.datacntlast = datalength;;
			  let $tablescrollbardiv = this.defaults.tablescrollbardiv;
			  $tablescrollbardiv[0].innerHTML=='';
			  if($tablescrollbardiv[0].innerHTML=='' && datalength>0){
				  let $tablescrolbar = $("<table class='table table-condensed'>");
			 	$tablescrollbardiv.append($tablescrolbar);
			 	let $tablescrollbody = $("<tbody>");
				 $tablescrolbar.append($tablescrollbody);
				 let checkArrayExists = true;
				 if(this.defaults.checkArray == undefined){
					this.defaults.checkArray = {};
					checkArrayExists = false;
				 }
				  for(let i = 0; i <= datalength; i++){
					  let ti = tabledata[i];
					  if(ti === undefined){
					    continue;
				  	  }
					  let $scroltr = $('<tr>');
					$tablescrollbody.append($scroltr);
					let $scroltd = $("<td class='text-nowrap'>");
					$scroltr.append($scroltd);
					$scroltd.zzylabel({data: '&nbsp;',needgetlocal:false});
					if(!checkArrayExists){
						this.defaults.checkArray[ti.id]={ischecked : false};
					}
				  }
				  $tablescrollbardiv.scroll(function(){
					   let pobj = $(this)[0].zzyowner;
						setTimeout(function(pobj) {
            				pobj.zzyscroll();
        				}, 500,pobj);

					  //$(this)[0].zzyowner.zzyscroll();
				  });
			  }
			  let parent = this.defaults.parent;
			
			  let tableleft = this.defaults.tableleft;
			  let tableright = this.defaults.tableright;
			  //let $tablelefttable = this.defaults.tablelefttable;
			 //let $tablerighttable = this.defaults.tablerighttable;
			  tableleft[0].innerHTML='';
			  tableright[0].innerHTML='';
				this.setheader();
			  //$tablelefttable.append(tableleft);
			 //$tablerighttable.append(tableright);
			 let rowcnt = this.defaults.rowcnt;
			let currentindex = this.defaults.currentindex;
			this.defaults.selectA = [];
			let selectAindex = 0;
			  for(let i = currentindex; i < currentindex + rowcnt; i++){
				  let ti = tabledata[i];
				  if(ti === undefined){
					  continue;
				  }
				  //add edit or delete in line
				  let $tablelefttr = $("<tr>");
				  tableleft.append($tablelefttr);
				  let $tablerighttr = $("<tr>");
				  tableright.append($tablerighttr);
				  let $td0 = $("<td class='text-nowrap'>"+(i+1)+"</td>");
				  $tablelefttr.append($td0);
				  let $td1 = $("<td class='text-nowrap'>");
				  $tablelefttr.append($td1);
				  let selectitem = $('<input type=checkbox>');
				  selectitem.attr('zzyid' + ti.id);
				  if(this.defaults.checkArray[tabledata[i].id].ischecked){
					selectitem.attr('checked',true);
				  }
				  selectitem.click({obj:this,id:tabledata[i].id},function(event){
					  let checked = false;
					  if($(this).attr('checked')){
						  checked = true;
					  }
					  
					  event.data.obj.defaults.checkArray[id].ischecked = checked;
					  let atest='';
				  });
					this.defaults.selectA[selectAindex++] = selectitem;
				  $td1.append(selectitem);
				  if((tableattrdata['candelete']||'')!='false'){
					  let $btnDelete = $('<span>');
					  $td1.append($btnDelete);
					  //$btnDelete.zzylabel({data: 'delete'});
					  $btnDelete.zzybutton({placeholder:'Delete',parent:parent,zzyclass:'link',zzysize:'xs',id:zzyDelete + ti.id,confirmmsg:zzyDeleteMsg});
				  }
				  if((tableattrdata['canedit']||'')!='false'){
					  let $btnEdit = $('<span>');
					  $td1.append($btnEdit);
					  //$btnEdit.zzylabel({data: 'Edit'});
					  $btnEdit.zzybutton({placeholder:'Edit',zzyclass:'link',zzysize:'xs',parent:parent,id:zzyEdit + ti.id,modal:zzyModalName + tablename});
				  }
				  for(let j = 0; j < tableheaderdata.length; j++){
					  let tj = tableheaderdata[j];
					  let fieldtype = tj.fieldtype;
					let name = tj.name;
					  
					if(fieldtype == 'hidden'){
						/*let $inputcol = $("<input>");
						$td1.append($inputcol);
						$inputcol.zzyinput({type:fieldtype,value: ti[name]});*/
						continue;
					}
					let label = tj.label;
					let isprimary = (tj.isprimary=="true");
					let iskeyword = (tj.iskeyword=="true");
					let isunique = (tj.isunique=="true");
					let isrequired = (tj.isrequired=="true");
					let minlength = tj.minlength;
					let isfrozen = (tj.isfrozen=="true");
					if(isprimary){isfrozen=true;}
					let $col = $("<td class='text-nowrap'>");
					//cols[index++] = $col;
					if(isfrozen){
						$tablelefttr.append($col);
					}else{
						$tablerighttr.append($col);
					}
					let labelValue = zzyDbToGui(ti[name],fieldtype);
					$col.zzylabel({data: labelValue,needgetlocal:false});
				     
				  }


			  }
			  $tablescrollbardiv.attr('style','overflow-x:none;overflow-y:auto;width:' + this.defaults.scrollwidth + 'px; height:' + ((rowcnt + 1) * this.defaults.rowheight ) +'px');
			  if(this.defaults.firstload){
				  this.defaults.firstload = false;
				  this.defaults.rightdiv.attr('style','overflow:auto;width:'+(window.innerWidth - this.defaults.leftwidth - this.defaults.scrollwidth)+'px');
			  }
			  
			  
	   }

    };
	jQuery.fn.zzytableheader=function(o)
	{
		let opts=(o===undefined?{}:o);
		let md=new zzyTableHeader();
		md.defaults=$.extend({},md.defaults,opts,{athis:$(this)});
		md.draw();
        zzywindowresizeobjs[zzywindowresizeobjs.length] = md;
		return md;
	};

})(jQuery);