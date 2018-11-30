package com.zzyboot.util;

import java.io.Serializable;

import com.itextpdf.text.pdf.PdfPTable;

public class zzyPdfTable implements Serializable {
	private static final long serialVersionUID = 1L;
	private float[] HeaderFloat;
	private float HeaderHeight=20,FootHeight=20;
	private PdfPTable tHeader,tFoot;
	private zzyPdfCell[] Column;
	private String[][] Body;
	 public zzyPdfTable(){}
	 public zzyPdfTable(float[] headerfloat,float headerheight,PdfPTable theader,zzyPdfCell[] column,float footheight,PdfPTable tfoot,String[][] body){
		 this.HeaderFloat=headerfloat;
			this.HeaderHeight=headerheight;
			this.tHeader=theader;
			this.Column=column;
			this.FootHeight=footheight;
			this.tFoot=tfoot;
			this.Body=body;
	 }
	public float getFootHeight() {
		return FootHeight;
	}
	public String[][] getBody() {
		return Body;
	}
	public void setBody(String[][] body) {
		if(body!=null){
			for(int i=0;i<body.length;i++){
				for(int j=0;j<body[i].length;j++){
					if(body[i][j]==null || body[i][j].equals("null") || body[i][j].equals("N/A"))body[i][j]="";
				}
			}
		}
		Body = body;
	}
	public void setFootHeight(float footHeight) {
		FootHeight = footHeight;
	}
	public PdfPTable gettFoot() {
		return tFoot;
	}
	public void settFoot(PdfPTable tFoot) {
		this.tFoot = tFoot;
	}

	public zzyPdfCell[] getColumn() {
		return Column;
	}
	public void setColumn(zzyPdfCell[] column) {
		Column = column;
	}
	public PdfPTable gettHeader() {
		return tHeader;
	}
	public void settHeader(PdfPTable tHeader) {
		this.tHeader = tHeader;
	}
	public float getHeaderHeight() {
		return HeaderHeight;
	}
	public void setHeaderHeight(float headerHeight) {
		HeaderHeight = headerHeight;
	}
	public float[] getHeaderFloat() {
		return HeaderFloat;
	}
	public void setHeaderFloat(float[] headerFloat) {
		HeaderFloat = headerFloat;
	}
}
