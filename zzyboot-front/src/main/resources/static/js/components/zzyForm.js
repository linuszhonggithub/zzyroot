//$('#zylabel_1').zzylabel({data:'name'});

(function($)
{
	function zzyForm()
	{
		this.defaults={
				athis:null
  	    ,data:[]
  	    ,needsubmit:true
  	    ,formid:''
  	    ,parent:''
  	    ,difocusobj:undefined
		  ,items:[]
		  ,isgeneral:true
  	   };
	};
	

	zzyForm.prototype=
	{
  	   draw:function()
  	   {
  		 let parent = this.defaults["parent"]||'';
  		 let parentitemindex=0;
  		 if(parent!='') {
  			 parent.defaults['zzyform'] = this; 
  		 }
  		 let items = this.defaults.items;
  		 items = [];
  		 let itemsindex = 0;
		   let athis = this.defaults["athis"];
		   //athis[0].innerHTML = '';
	       let formid = this.defaults["formid"];
	       let data  = this.defaults["data"];
	       let $nform = $("<div></div>");
	       athis.append($nform);
		   for(let i = 0; i < data.length; i++){
			   let di = data[i];
			   let type = di.type||'text';
			   let msg = '';
			   let placeholder = di.placeholder||'';
			   let id = di.id || '';
			   let required = di.required||false;
			   let msgcolor='';
			   if(required){
				   msg='*';
				   msgcolor = 'red';
			   }
			   let minlength = di.minlength || 0;
			   if(minlength > 0){
				   msg += ' Length >= ' + minlength;
			   }
			   let maxlength = di.maxlength || 255;
			   if(maxlength < 255){
				   msg += ' Length < ' + maxlength;
			   }
			   let readonly = di.readonly || false;
			   let $diitem = $("<input>");
			   let $diitemgroup = $("<div class='input-group'>");
			   $nform.append($diitemgroup);
			   if(di.glyphicon||'' !=''){
				   $diitemgroup.append("<span class='input-group-addon'><i class='glyphicon glyphicon-" + di.glyphicon + "'></i></span>");
			   }

			   $diitemgroup.append($diitem);
			   $diitem.attr(zzyOlValue,'');
			   items[itemsindex++] = $diitem;
			   di['obj'] = $diitem;
			   $diitem.zzyinput({id:id,placeholder: placeholder, required:required, type:type, minlength:minlength, maxlength:maxlength, owner:this,readonly:readonly});
			   let $diitemmsg = $("<span>");
			   $diitemgroup.append($diitemmsg);
			   $diitemmsg.zzylabel({data:msg,color:msgcolor});
			   
			   if(type != 'hidden'){
					let $diitembr = $("<br>");
			   		$nform.append($diitembr);
			   }
			   
			   $diitem.on('keyup mousedown', function(e){
				   if(e !==undefined && (e.keyCode||0) == 13){
					   $(this)[0].zzyowner.keyupenter();
				   }else{
					   $(this)[0].zzyowner.setFocus($(this));
				   }
				   
			   });

		   }
		   let needsubmit = this.defaults["needsubmit"];
		   if(needsubmit){
			   let $diitemsubmit = $("<input>");
			   $nform.append($diitemsubmit);
			   $diitemsubmit.zzyinput({placeholder: 'Submit', type:'button', owner:this});
			   //let parent = this;
			   $diitemsubmit.click(function(){
				   $(this)[0].zzyowner.submit();
			   });
		   }
  		 //addZzyDataCenterDatachangeListener("zzylancurrent",this);
		   this.defaults.items = items;
		   this.clear();
	     this.refreshData();
		   this.submit();
	   },clear:function(){
		   let items = this.defaults.items;
		   for(let i = 0; i <items.length; i++){
			   let ii = items[i];
			   ii.attr(zzyOlValue,'');
			   ii.val('');
		   }
	   },edit:function(ti){
		   let items = this.defaults.items;
		   for(let i = 0; i <items.length; i++){
			   let ii = items[i];
			   let id = ii.attr(zzyID);
			   let value = ti[id];

			   ii.attr(zzyOlValue,value);
			   ii.val(value);
		   }
	   },focus:function(){
		   let difocusobj = this.defaults["difocusobj"];
		   if(difocusobj !== undefined){
			   difocusobj.focus();
		   }
	   },setFocus:function(obj){
		   this.defaults["difocusobj"] = obj;//register which one is on focus
		   //console.log("setFocus" + obj);
	   },keyupenter:function(){
		   let data  = this.defaults["data"];
		   let difocusobj = this.defaults["difocusobj"];
		   //console.log("after enter " + difocusobj);
		   let difocusfinded = false;
		   let difocusobjfirst;
		   let finalyfindnext = false;
		   for(let i = 0; i < data.length; i++){
			   let di = data[i];
			   let readonly = di.readonly || false;
			   if(readonly){
				   continue;
			   }
			   if(difocusobjfirst === undefined){
				   difocusobjfirst = di.obj;   
			   }
			   if(difocusobj === undefined){
				   difocusobj = di.obj;
				   this.setFocus(difocusobj);
				   
				   finalyfindnext = true;
				   break;
			   }
			   if(difocusobj === di.obj && !difocusfinded){
				   difocusfinded = true;
				   continue;
			   }
			   if(difocusfinded){
				   difocusobj = di.obj;
				   this.setFocus(difocusobj);
				   finalyfindnext = true;
				   break;
			   }
		   }
		   if(!finalyfindnext && difocusobjfirst != undefined){
			   difocusobj = difocusobjfirst;
			   this.setFocus(difocusobj);
		   }
		   this.focus();
	   }
	   ,refreshData:function(){
	   }
	   ,submit:function(){
		   
		   //check if all required item is filled

		   let data  = this.defaults["data"];
		   let param = '';
		   let paramSeg = '';
		   let changed = false;
		   let isedit = false;
		   	let formid = this.defaults["formid"];
		   let isgeneral = true;
		   if(this.defaults.isgeneral == false){
			   isgeneral = false;
		   }

		   for(let i = 0; i < data.length; i++){
			   let di = data[i];
			   let required = di.required||false;
			   let value = di.obj.val() || '';

			   let valueold = di.obj.attr(zzyOlValue)||'';
			   if(value != valueold){
				   changed=true;
			   }
			   if(required){ //check if the value is empty
				   
				   if(value == ''){
					   di.obj.focus();
					   this.setFocus(di.obj);
					   return;
				   }
			   }
			   let minlength = di.minlength || 0;
			   if(minlength > 0){
				   if(value.length < minlength){
					   zzyAlert("The length should >= " + minlength);
					   di.obj.focus();
					   this.setFocus(di.obj);
					   return;
				   }
			   }
			   let maxlength = di.maxlength || 255;
			   {
				   if(value.length > maxlength){
					   zzyAlert("The length should <= " + maxlength);
					   di.obj.focus();
					   this.setFocus(di.obj);
					   return;
				   }
			   }
			   let type = di.type||'text';
			   if(type == "password"){
				   value = sha256_digest(value);
				   //console.log("password 0 is " + value);
				   value = sha256_digest(value);
				   //console.log("password 1 is " + value);
			   }
			   if(i == 0 && value != ''){
				   isedit = true;
			   }
			   if(!isgeneral){
				   isedit = false;
			   }
			   if(i == 0){
				   param+= paramSeg + value;
					paramSeg = zzySplitItem;
					if(isedit){
						paramSeg = zzySplitLine;
					}   
			   }else{
			   	if(isedit){
				   if(value != valueold){
					   let thisid =  di.obj.attr(zzyID);
			   			param+= paramSeg + thisid+zzySplitItem+value;
				   }

			   	}else{
	   			   param+= paramSeg + value;
			   	}

			   }
		   }
		   if(!changed){
			   zzyAlert("No change, no submit.");
			   return;
		   }
		   let parent = this.defaults["parent"]||'';
		   if(parent != ''){
			   if(parent.beforesubmitcheck !== undefined){
				   let result = parent.beforesubmitcheck(param);
				   if(!result){
					   return;
				   }
			   }
			   
		   }
		   if(parent != ''){
			   if(parent.beforesubmit !== undefined){
				   parent.beforesubmit();   
			   }
			   
		   }
		   
		   //submit to db
		   if(isgeneral){
				sendajaxIPPort("zzytablesave",formid + zzySplitBlock + param);
		   }else{
				sendajaxIPPort(formid, param);
		   }
		   
		   this.focus();
		   
		   if(parent != ''){
			   if(parent.submit !== undefined){
				   parent.submit();
			   }
		   }
		   
	   }

    };
	jQuery.fn.zzyform=function(o)
	{
		let opts=(o===undefined?{}:o);
		let md=new zzyForm();
		md.defaults=$.extend({},md.defaults,opts,{athis:$(this)});
		md.draw();
        
		return md;
	};

})(jQuery);