package com.zzyboot.util;

import java.io.Serializable;

import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;

public class zzyPdfCell implements Serializable{
	  /**
		 * 
		 */
		private static final long serialVersionUID = 1L;
	private Boolean isBarcode=false;
	private Boolean isImg=false;
	  private String sAlign="Center";
	  private String sDesc;
	  private String sLabel;
	  private float fBarcodeWidth=0.8f;
	  private int iBarcodeHeight=24;
	  private Font tFont=FontFactory.getFont("COURIER",12);

	  public zzyPdfCell(){}
	  public zzyPdfCell(Boolean isbarcode,String salign,String desc){
		  this.isBarcode=isbarcode;
		  this.sAlign=salign;
		  this.sDesc=desc;
		  this.sLabel=null;
	  }
	  public String getsLabel() {
		return sLabel;
	}
	public void setsLabel(String sLabel) {
		this.sLabel = sLabel;
	}
	public float getfBarcodeWidth() {
		return fBarcodeWidth;
	}
	public void setfBarcodeWidth(float fBarcodeWidth) {
		this.fBarcodeWidth = fBarcodeWidth;
	}
	public zzyPdfCell(Boolean isbarcode,String salign,String desc,float width,int height,String label){
		  this.isBarcode=isbarcode;
		  this.sAlign=salign;
		  this.sDesc=desc;
		  this.iBarcodeHeight=height;
		  this.fBarcodeWidth=width;
		  this.sLabel=label;
	  }
	public int getiBarcodeHeight() {
		return iBarcodeHeight;
	}
	public void setiBarcodeHeight(int iBarcodeHeight) {
		this.iBarcodeHeight = iBarcodeHeight;
	}
	public Font gettFont() {
		return tFont;
	}
	public void settFont(Font tFont) {
		this.tFont = tFont;
	}
	public Boolean getIsBarcode() {
		return isBarcode;
	}
	public String getsDesc() {
		return sDesc;
	}
	public void setsDesc(String sDesc) {
		this.sDesc = sDesc;
	}
	public void setIsBarcode(Boolean isBarcode) {
		this.isBarcode = isBarcode;
	}
	public String getsAlign() {
		return sAlign;
	}
	public void setsAlign(String sAlign) {
		this.sAlign = sAlign;
	}
	public Boolean getIsImg() {
		return isImg;
	}
	public void setIsImg(Boolean isimg) {
		this.isImg = isimg;
	}


}
