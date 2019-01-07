package com.zzyboot.service;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

import org.springframework.stereotype.Component;

import com.zzyboot.common.util.ZzyCommon;
import com.zzyboot.util.pdfHeaderFooter;

@Component
public class PdfService {
	public String getPdf(String filename, String rptname, String[] header, String[][] body,String[] widthA, String username, String lan){
		
		filename = ZzyCommon.getFilenameRight(filename);
		//System.out.println("=============lan is " + lan);
		try {
			OutputStream out = new FileOutputStream(filename);
			pdfHeaderFooter phf = new pdfHeaderFooter();
			
			if(rptname.equals("rptpdf")){
				String[] newheader = new String[5];
				int index = 0;
				String title = "Report";
				if(lan.equals("zh")){
					title = "报表";
				}
				newheader[index++] = title;
				{
					String sWidth = "";
					String sWidthSep = "";
					for(int i = 0; i < widthA.length; i ++){
						sWidth += sWidthSep + widthA[i];
						sWidthSep = ",";
					}
					newheader[index++] = sWidth;
				}
				{
					String sWidth = "";
					String sWidthSep = "";
					for(int i = 0; i < header.length; i ++){
						sWidth += sWidthSep + "left";
						sWidthSep = ",";
					}
					newheader[index++] = sWidth;
				}
				{
					String sWidth = "";
					String sWidthSep = "";
					for(int i = 0; i < header.length; i ++){
						sWidth += sWidthSep + header[i];
						sWidthSep = ",";
					}
					newheader[index++] = sWidth;
				}
				newheader[index++] = username;
				header = newheader;
				
			}
			phf.outPdf(out, filename, rptname, header, body,lan);
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return filename;
	}
}
