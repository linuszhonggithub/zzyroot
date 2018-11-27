(function($)
{
	function zzyTabSuper()
	{
		this.defaults={
				athis:null
  	   };
	};
	

	zzyTabSuper.prototype=
	{
  	   draw:function()
  	   {
  		   
  		let athis=this.defaults["athis"];
  		let index = 0;
  		//let code = [];
  		let data = [];
		data[index++] = {placeholder:"Home",id:'zzyhome',tasks:{type:'process',data:"supermain"}};//zi.label;
  		//aLanguage.zzyradiogroup({name:'zzylanguageradiogroup',code:code, data:data});
		athis.zzytab({data:data,parent: this,activeid:'zzyhome'});

  		
	     
	   },selectitem:function(value){
		   //alert(value);
		   /*let atab = this.defaults.zzytab;
		   let items = atab.defaults.items;
		   let itembody = items[value].tbody;
		   let bodyhtml = itembody[0].innerHTML;
		   if(bodyhtml == value){
			   //itembody[0].innerHTML = 'fsjaflkdsakfjkdsfk<bf>fdsjflksaj fksadlfkljfsa;l jjfdsk jfdsak ksdjfksajf jksaj fksaj sajdfksajfksajflsaffdsjflksaj fksadlfkljfsa;l jjfdsk jfdsak ksdjfksajf jksaj fksaj sajdfksajfksajflsaffdsjflksaj fksadlfkljfsa;l jjfdsk jfdsak ksdjfksajf jksaj fksaj sajdfksajfksajflsaffdsjflksaj fksadlfkljfsa;l jjfdsk jfdsak ksdjfksajf jksaj fksaj sajdfksajfksajflsaf<br>fsjaflkdsakfjkdsfk<bf>fdsjflksaj fksadlfkljfsa;l jjfdsk jfdsak ksdjfksajf jksaj fksaj sajdfksajfksajflsaf<br>fsjaflkdsakfjkdsfk<bf>fdsjflksaj fksadlfkljfsa;l jjfdsk jfdsak ksdjfksajf jksaj fksaj sajdfksajfksajflsaf<br>fsjaflkdsakfjkdsfk<bf>fdsjflksaj fksadlfkljfsa;l jjfdsk jfdsak ksdjfksajf jksaj fksaj sajdfksajfksajflsaf<br>fsjaflkdsakfjkdsfk<bf>fdsjflksaj fksadlfkljfsa;l jjfdsk jfdsak ksdjfksajf jksaj fksaj sajdfksajfksajflsaf<br>fsjaflkdsakfjkdsfk<bf>fdsjflksaj fksadlfkljfsa;l jjfdsk jfdsak ksdjfksajf jksaj ffdsjflksaj fksadlfkljfsa;l jjfdsk jfdsak ksdjfksajf jksaj fksaj sajdfksajfksajflsaffdsjflksaj fksadlfkljfsa;l jjfdsk jfdsak ksdjfksajf jksaj fksaj sajdfksajfksajflsaffdsjflksaj fksadlfkljfsa;l jjfdsk jfdsak ksdjfksajf jksaj fksaj sajdfksajfksajflsafksaj sajdfksajfksajflsaf<br>fsjaflkdsakfjkdsfk<bf>fdsjflksaj fksadlfkljfsa;l jjfdsk jfdsak ksdjfksajf jksaj fksaj sajdfksajfksajflsaf<br>fsjaflkdsakfjkdsfk<bf>fdsjflksaj fksadlfkljfsa;l jjfdsk jfdsak ksdjfksajf jksaj fksaj sajdfksajfksajflsaf<br>fsjaflkdsakfjkdsfk<bf>fdsjflksaj fksadlfkljfsa;l jjfdsk jfdsak ksdjfksajf jksaj fksaj sajdfksajfksajflsaf<br>fsjaflkdsakfjkdsfk<bf>fdsjflksaj fksadlfkljfsa;l jjfdsk jfdsak ksdjfksajf jksaj fksaj sajdfksajfksajflsaf<br>fsjaflkdsakfjkdsfk<bf>fdsjflksaj fksadlfkljfsa;l jjfdsk jfdsak ksdjfksajf jksaj fksaj sajdfksajfksajflsaf<br>fsjaflkdsakfjkdsfk<bf>fdsjflksaj fksadlfkljfsa;l jjfdsk jfdsak ksdjfksajf jksaj fksaj sajdfksajfksajflsaf<br>fsjaflkdsakfjkdsfk<bf>fdsjflksaj fksadlfkljfsa;l jjfdsk jfdsak ksdjfksajf jksaj fksaj sajdfksajfksajflsaf<br>fsjaflkdsakfjkdsfk<bf>fdsjflksaj fksadlfkljfsa;l jjfdsk jfdsak ksdjfksajf jksaj fksaj sajdfksajfksajflsaf<br>fsjaflkdsakfjkdsfk<bf>fdsjflksaj fksadlfkljfsa;l jjfdsk jfdsak ksdjfksajf jksaj fksaj sajdfksajfksajflsaf<br>fsjaflkdsakfjkdsfk<bf>fdsjflksaj fksadlfkljfsa;l jjfdsk jfdsak ksdjfksajf jksaj fksaj sajdfksajfksajflsaf<br>fsjaflkdsakfjkdsfk<bf>fdsjflksaj fksadlfkljfsa;l jjfdsk jfdsak ksdjfksajf jksaj fksaj sajdfksajfksajflsaf<br>fsjaflkdsakfjkdsfk<bf>fdsjflksaj fksadlfkljfsa;l jjfdsk jfdsak ksdjfksajf jksaj fksaj sajdfksajfksajflsaf<br>fsjaflkdsakfjkdsfk<bf>fdsjflksaj fksadlfkljfsa;l jjfdsk jfdsak ksdjfksajf jksaj fksaj sajdfksajfksajflsaf<br>fsjaflkdsakfjkdsfk<bf>fdsjflksaj fksadlfkljfsa;l jjfdsk jfdsak ksdjfksajf jksaj fksaj sajdfksajfksajflsaf<br>fsjaflkdsakfjkdsfk<bf>fdsjflksaj fksadlfkljfsa;l jjfdsk jfdsak ksdjfksajf jksaj fksaj sajdfksajfksajflsaf<br>';
		   }*/
	   }
    };
	jQuery.fn.zzytabsuper=function(o)
	{
		let opts=(o===undefined?{}:o);
		let md=new zzyTabSuper();
		md.defaults=$.extend({},md.defaults,opts,{athis:$(this)});
		md.draw();
        
		return md;
	};

})(jQuery);