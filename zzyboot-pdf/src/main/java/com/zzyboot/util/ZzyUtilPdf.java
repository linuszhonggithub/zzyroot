package com.zzyboot.util;

public class ZzyUtilPdf {
	public static void setPdfNewpageHeader(String rptname,String[] strHeader,int sbindex){
		if(rptname.equals("rptorderprint")){
			if(sbindex==1){
				strHeader[0]="Parts "+strHeader[0];
			}
		}
	}
}
