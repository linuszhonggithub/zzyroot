//$('#zylabel_1').zzylabel({data:'name'});

(function($)
{
	function zzyLogin()
	{
		this.defaults={
				athis:null
  	    ,data:""
  	   };
	};
	

	zzyLogin.prototype=
	{
  	   draw:function()
  	   {
	     this.refreshData();
	   }
	   ,refreshData:function(){
	       var athis=this.defaults["athis"];
		   let $nlogin = $("<div>");
		   athis.append($nlogin);
		   let $nhr = $("<hr>");
		   athis.append($nhr);
		   let $nloginform = $("<div>");
		   athis.append($nloginform);

		   $nlogin.zzylanguage({});
		   $nloginform.zzyform({formid:'zzylogin',isgeneral:false,data:[
		   {placeholder:'Email or Cell',id:'login',required:true,glyphicon:'envelope'}
		   ,{placeholder:'Password',id:'password',type:'password',minlength:8,required:true,glyphicon:'lock'}
		   ],parent:this});
	   },beforesubmit:function(){
		   removeSessionkey();
	   },submit:function(){
		  
		   
	   }

    };
	jQuery.fn.zzylogin=function(o)
	{
		let opts=(o===undefined?{}:o);
		let md=new zzyLogin();
		md.defaults=$.extend({},md.defaults,opts,{athis:$(this)});
		md.draw();
        
		return md;
	};

})(jQuery);