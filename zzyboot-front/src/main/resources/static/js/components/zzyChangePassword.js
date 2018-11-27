//$('#zylabel_1').zzylabel({data:'name'});

(function($)
{
	function zzyChangePassword()
	{
		this.defaults={
				athis:null
		,zzyform:''
  	   };
	};
	

	zzyChangePassword.prototype=
	{
  	   draw:function()
  	   {
  		 let athis=this.defaults["athis"];
  		athis.zzydropform({label:'Change Password',formid:"changepassword",isgeneral:false,data:[
  		   {placeholder:'Old Password',type:'password',minlength:8,required:true}
		   ,{placeholder:'New Password',type:'password',minlength:8,required:true}
		   ,{placeholder:'Repeat Password',type:'password',minlength:8,required:true}],parent: this});
  		 
	     this.refreshData();
	   }
	   ,beforesubmitcheck:function(data){
		   let dataA = data.split(zzySplitItem);
		   if(dataA[1] !== dataA[2]){
			   zzyAlert(zzyFailnewpassNmatch);
			   return false;
		   }
		   if(dataA[1] == dataA[0]){
			   //"password not change");
			   return false;
		   }
		   
		   return true;
	   }
	   ,refreshData:function(){
	   },submit:function(result){
		   if(result.indexOf(zzyFail) == 0){
			   zzyAlert(result);
			   
		   }else{
			   zzyAlert(zzySuccessChangepassword);
			   let zzyform = this.defaults.zzyform;
			   zzyform.clear();
			   let zzydropformlabel = this.defaults.zzydropform.defaults.nlabel;
			   zzydropformlabel.dropdown("toggle");
		   }
	   }

    };
	jQuery.fn.zzychangepassword=function(o)
	{
		let opts=(o===undefined?{}:o);
		let md=new zzyChangePassword();
		md.defaults=$.extend({},md.defaults,opts,{athis:$(this)});
		md.draw();
        
		return md;
	};

})(jQuery);