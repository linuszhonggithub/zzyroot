package com.zzyboot.util;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.imageio.ImageIO;




 
import com.itextpdf.text.Anchor;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.pdf.Barcode128;
import com.itextpdf.text.pdf.BarcodeQRCode;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.FontSelector;
import com.itextpdf.text.pdf.PdfAnnotation;
import com.itextpdf.text.pdf.PdfBorderArray;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfDestination;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;
import com.zzyboot.common.util.MoneyToWord;
import com.zzyboot.common.util.ZzyCommon;
 
public class pdfHeaderFooter{
	public static Font columnfont=new Font(FontFamily.HELVETICA, 8, Font.BOLD);
	public static Font datafont=new Font(FontFamily.HELVETICA, 8);
	public static Font titlefont=new Font(FontFamily.HELVETICA, 22,Font.BOLD);
 public pdfHeaderFooter(){}
    /** The resulting PDF file. */
    /**
     * Creates a PDF document.
     * @param filename the path to the new PDF document
     * @throws    DocumentException 
     * @throws    IOException
     * @throws    SQLException
     */
    public void createPdf(String filename,String rptname,String[] strHeader,String[][] strBody)
    {
    	try {
			createPdf(filename,rptname,strHeader,strBody,null);
		} catch (SQLException e) {
			//  Auto-generated catch block
			e.printStackTrace();
		} catch (DocumentException e) {
			//  Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			//  Auto-generated catch block
			e.printStackTrace();
		}
    }
    public void drawcheckdetail(PdfContentByte cb,String checkno,String[][] strBody,int itop,String sdate,String samount,String scompany,String scurrency){
      //draw company
        cb.showTextAligned(Element.ALIGN_LEFT, scompany, 0,itop, 0);
        //check no
        cb.showTextAligned(Element.ALIGN_LEFT, "Cheque NO:", 195,itop, 0);
        cb.showTextAligned(Element.ALIGN_LEFT, checkno,245,itop, 0);
        //amount
        cb.showTextAligned(Element.ALIGN_LEFT, "Amount:", 312,itop, 0);
        cb.showTextAligned(Element.ALIGN_LEFT, scurrency+samount,348,itop, 0);
        //date
        cb.showTextAligned(Element.ALIGN_LEFT, "Date:", 439,itop, 0);
        cb.showTextAligned(Element.ALIGN_LEFT, sdate,463,itop, 0);
        
        itop-=21;
        int[] ileft=new int[]{7,100,208,343,468};
        cb.showTextAligned(Element.ALIGN_LEFT, "Date", ileft[0],itop, 0);
        cb.showTextAligned(Element.ALIGN_LEFT, "Invoice No.", ileft[1],itop, 0);
        cb.showTextAligned(Element.ALIGN_LEFT, "Gross Amount", ileft[2],itop, 0);
        cb.showTextAligned(Element.ALIGN_LEFT, "Disc. Taken", ileft[3],itop, 0);
        cb.showTextAligned(Element.ALIGN_LEFT, "Net Amount", ileft[4],itop, 0);
        ileft=new int[]{ileft[0],ileft[1],265,390,517};
        for(int i=0;i<strBody.length;i++){
        	itop-=21;
        	String[] si=strBody[i];
            cb.showTextAligned(Element.ALIGN_LEFT, si[0], ileft[0],itop, 0);
            cb.showTextAligned(Element.ALIGN_LEFT, si[1], ileft[1],itop, 0);
            cb.showTextAligned(Element.ALIGN_RIGHT, scurrency+si[2], ileft[2],itop,0);
            cb.showTextAligned(Element.ALIGN_RIGHT, scurrency+si[3], ileft[3],itop,0);
            cb.showTextAligned(Element.ALIGN_RIGHT, scurrency+si[4], ileft[4],itop,0);
        }
        

    }
    public void createPdf(String filename,String rptname,String[] strHeader,String[][] strBody,Rectangle pagesize)
        throws SQLException, DocumentException, IOException {
    	//check if strBody have newpage
    	if(strBody!=null){
    		ArrayList<String[][]> newpagecnt=new ArrayList<String[][]>();
    		ZzyCommon.getArrayListFromTag(newpagecnt,strBody,"newpage");
    		if(newpagecnt.size()>1){
    			int sbindex=0;
    			String[] filenameA=new String[newpagecnt.size()];
    			for(String[][] sb:newpagecnt){
    				filenameA[sbindex]=filename+"_"+sbindex;
    				ZzyUtilPdf.setPdfNewpageHeader(rptname,strHeader,sbindex);
    				if(ZzyCommon.fileExists(filenameA[sbindex]))ZzyCommon.fileDelete(filenameA[sbindex]);
    				if(sbindex>0){
    					String[][] sbtmp=new String[sb.length+1][sb[0].length];
    					sbtmp[0]=new String[sb[0].length];
    					for(int i=0;i<sb[0].length;i++){
    						sbtmp[0][i]="...";
    					}
    					for(int i=0;i<sb.length;i++){
    						String[] si=sb[i];
    						sbtmp[i+1]=si;
    					}
    					sb=sbtmp;
    				}
    				if(sbindex<newpagecnt.size()-1){
    					String[][] sbtmp=new String[sb.length+1][sb[0].length];
    					sbtmp[sb.length]=new String[sb[0].length];
    					for(int i=0;i<sb[0].length;i++){
    						sbtmp[sb.length][i]="...";
    					}
    					for(int i=0;i<sb.length;i++){
    						String[] si=sb[i];
    						sbtmp[i]=si;
    					}
    					sb=sbtmp;
    				}
    				createPdf(filenameA[sbindex],rptname,strHeader,sb,pagesize);
    				sbindex++;
    			}
    			mergePdfs(filename,filenameA);
    			return;
    		}
    	}
    	// create a database connection
        // step 1
    	Document document;
    	//if(pagesize==null)document = new Document(PageSize.A4, 50, 50, 50, 50);
    	if(pagesize==null)document = new Document();//PageSize.A4, 50, 50, 50, 50);
    	else{
    		document = new Document(pagesize);
    	} 
        
        // step 2
        PdfWriter writer=PdfWriter.getInstance(document, new FileOutputStream(filename));
        // step 3
        //Rectangle rect = new Rectangle(30, 30, 550, 500);
        //writer.setBoxSize("art", rect);

        //HeaderFooterPageEvent event = new HeaderFooterPageEvent();
        //writer.setPageEvent(event);
        document.open();
        PdfContentByte cb = writer.getDirectContent();
        // step 4
        //for (int i=0;i<5;i++) {
        
        
        if(rptname.equals("cheque")){
        	cb.beginText();
        	Font helvetica = new Font(FontFamily.HELVETICA, 11);
            BaseFont bf_helv = helvetica.getCalculatedBaseFont(false);

        	Font helvetica1 = new Font(FontFamily.HELVETICA, 13,Font.BOLD);
            BaseFont bf_helv1 = helvetica1.getCalculatedBaseFont(false);
            //Date
            int itop=732;
            cb.setFontAndSize(bf_helv, 11);
            cb.showTextAligned(Element.ALIGN_LEFT, "DATE", 487, itop, 0);
            int iindex=0;
            String sdate=strHeader[iindex++];
            cb.showTextAligned(Element.ALIGN_LEFT,sdate , 531, itop, 0);
            String scurrency=strHeader[iindex++];
            String strcurrency=scurrency;
            if(strcurrency.equals("$"))strcurrency="Dollars";
            //Check amount
            itop-=28;
            int ileft=63;
			MoneyToWord ctn=new MoneyToWord();
			String amt = ctn.toString(strHeader[iindex],strcurrency);
            
			ColumnText ct = new ColumnText(cb);
			  ct.setSimpleColumn(new Phrase(new Chunk(amt, helvetica)),
			                     ileft,itop-30, ileft+400,itop+25, 25, Element.ALIGN_LEFT | Element.ALIGN_TOP);
			   
			
			
            //cb.showTextAligned(Element.ALIGN_LEFT, amt, ileft, itop, 0);
            //$
            cb.showTextAligned(Element.ALIGN_LEFT, scurrency, 487, itop, 0);

            //$ amount
            
            String samount=strHeader[iindex++];
            cb.showTextAligned(Element.ALIGN_LEFT, samount, 532, itop, 0);
            
            cb.setFontAndSize(bf_helv1, 13);
            //Company name
            itop-=43;
            String scompany=strHeader[iindex++];
            cb.showTextAligned(Element.ALIGN_LEFT,scompany ,ileft+2, itop, 0);

            cb.setFontAndSize(bf_helv, 11);
            //address
            itop-=29;
            cb.showTextAligned(Element.ALIGN_LEFT, strHeader[iindex++], ileft, itop, 0);
            //address 1
            String address1=strHeader[iindex++]+", "+strHeader[iindex++];
            String postalcode=" "+strHeader[iindex++];
            String country=strHeader[iindex++];
            if(!country.equals("CA"))address1+=" "+country+" ";
            itop-=15;
            cb.showTextAligned(Element.ALIGN_LEFT, address1+postalcode, ileft, itop, 0);
            // draw line
            cb.setLineWidth((float)0.1);
            itop-=51;
            cb.moveTo(5, itop);
            cb.lineTo(54,itop);
            cb.stroke();
            //draw detail
            cb.setFontAndSize(bf_helv, 9);
            itop-=63;
            String checkno=strHeader[iindex++];
            drawcheckdetail(cb,checkno,strBody,itop,sdate,samount,scompany,scurrency);
            itop-=281;
            drawcheckdetail(cb,checkno,strBody,itop,sdate,samount,scompany,scurrency);
            cb.endText();
            ct.go();
        	//table.setTotalWidth(500);
        	//table.writeSelectedRows(0, 2, 0, 2, 2, 800, cb);
        }else if(rptname.equals("barcode128item")){
        	cb.beginText();
        	Font helvetica = new Font(FontFamily.HELVETICA, 6);
            BaseFont bf_helv = helvetica.getCalculatedBaseFont(false);

        	//Font helvetica1 = new Font(FontFamily.HELVETICA, 6,Font.BOLD);
            //Date
            //draw detail
            cb.setFontAndSize(bf_helv, 6);
            int itop=735;
            int iwidth0=148;
            int iverticalinterval=36;
            for(int i=0;i<strBody.length;i++){
            	int ileft=33;
            	
            	int iindex=0;
            	
            	String[] si=strBody[i];
            	for(int j=0;j<4;j++){
            		
            			Barcode128 code128 = new Barcode128();
            			code128.setBaseline(5);
            			String barcodedesc=si[iindex++];
            			if(barcodedesc==null)continue;
            			if(barcodedesc.indexOf("[")<0)continue;
            			String barcodelabel=barcodedesc.substring(0,barcodedesc.indexOf("["));
            			barcodedesc=barcodedesc.substring(barcodedesc.indexOf("[")+1);
            			barcodedesc=barcodedesc.substring(0,barcodedesc.indexOf("]"));
            			barcodelabel=barcodelabel.replaceAll("_","-");
            			if(barcodelabel.indexOf(" CNP")>0)barcodelabel=barcodelabel.substring(0,barcodelabel.indexOf(" CNP"));
            			String sprice=si[iindex++];
            			if(sprice.length()>0){
            				String sdig=".00";
            				if(sprice.indexOf(".")>0){
            					sdig=sprice.substring(sprice.indexOf("."));
            					if(sdig.length()<3)sdig+="0";
            					sprice=sprice.substring(0,sprice.indexOf("."));
            				}
            				sprice=sprice+sdig;
            			}
            			int ilength=barcodelabel.length();
            			ilength+=sprice.length();
            			int ispacelength=0;
            			if(sprice.length()>0){
            				ispacelength=28-ilength;
            			}
            			String sspace="";
            			for(int k=0;k<ispacelength;k++)sspace+=" ";
            			barcodelabel+=sspace+sprice;
            			code128.setCode(barcodedesc);
            			if(!code128.getCode().equals(barcodedesc)){
            				return;
            			}
            			code128.setSize(6);
            	        code128.setFont(bf_helv);
            	        code128.setAltText(barcodelabel);
                    	code128.setBarHeight(18);
                    	code128.setX(1f);
                    	Image  imageEAN = code128.createImageWithBarcode(cb, null, null);
                    	imageEAN.setAbsolutePosition(ileft,itop-10);
                    	cb.addImage(imageEAN);
                    	ileft+=iwidth0;
            		
            	}
            	
                itop-=iverticalinterval;
            }
            cb.endText();
        }else if(rptname.equals("partlabel")){
        	cb.beginText();
        	int itop=135;
        	int iseg=15;
        	// draw line
            cb.setLineWidth((float)0.1);
            cb.moveTo(100, 30);
            cb.lineTo(100,itop+5);
            cb.stroke();
        	Font fitemcode = new Font(FontFamily.HELVETICA, 12,Font.ITALIC);
            BaseFont bf_itemcode = fitemcode.getCalculatedBaseFont(false);
        	Font fitembold = new Font(FontFamily.HELVETICA, 10,Font.BOLD);
            BaseFont bf_itembold = fitembold.getCalculatedBaseFont(false);

        	Font helvetica = new Font(FontFamily.HELVETICA, 10);
            BaseFont bf_helv = helvetica.getCalculatedBaseFont(false);

            //Item
            
            //cb.setFontAndSize(bf_helv, 12);
            cb.setFontAndSize(bf_itemcode, 12);
            int iindex=0;
            String itemcode=strHeader[iindex++];
            cb.showTextAligned(Element.ALIGN_LEFT,itemcode , 20, itop, 0);
            //img
            String itemimg=strHeader[iindex++];
            //itop-=20;
            //BufferedImage buffered=ImageIO.read(new File("C:/zhongzeyu/zhongzeyuproc/jboss/server/default/deploy/zhongzeyuWeb.war/upload/"+itemimg));
            BufferedImage buffered=ImageIO.read(new File("C:/zhongzeyu/zhongzeyuwork/uploadimages/uploadimages/Images/this/"+itemimg));
            BufferedImage buffer=grayImage(buffered);
            File outputfile = new File("savedbuffer.png");
            ImageIO.write(buffer, "png", outputfile); // Write the Buffered Image into an output file
            Image img  = Image.getInstance("savedbuffer.png");
            img.setAbsolutePosition(15, 40);
            //img.scalePercent(80);
            //img.scaleToFit(95f, 130f);
            img.scaleToFit(59f, 81f);
            cb.addImage(img);
            

           
            String stype=strHeader[iindex++];
            cb.setFontAndSize(bf_itembold, 10);
            itop-=3;
            //type
            int ileft=110;
            int ileft1=ileft+170;
            cb.showTextAligned(Element.ALIGN_LEFT, stype, ileft, itop, 0);
            
            //$ price
            cb.setFontAndSize(bf_helv, 10);
            
            String sprice=strHeader[iindex++];
            cb.showTextAligned(Element.ALIGN_RIGHT, "$ "+sprice, ileft1, itop, 0);
            itop-=iseg;
            for(int i=0;i<strBody.length;i++){
            	String[] si=strBody[i];
                cb.showTextAligned(Element.ALIGN_LEFT, si[0], ileft,itop, 0);
                cb.showTextAligned(Element.ALIGN_RIGHT, si[1], ileft1,itop, 0);
                itop-=iseg;
            }
            String scomment=strHeader[iindex++];
            cb.setFontAndSize(bf_helv, 8);
            cb.showTextAligned(Element.ALIGN_LEFT, scomment, ileft, 20, 0);

            cb.endText();
        }else if(rptname.equals("shippinglabel")){
        	int iindex=0;
        	String imgname=strHeader[iindex++];
        	String companyname=strHeader[iindex++];
        	String address0=strHeader[iindex++];
        	String address1=strHeader[iindex++];
        	String pono=strHeader[iindex++];
        	String sono=strHeader[iindex++];
        	String sdate=strHeader[iindex++];
        	String comment=strHeader[iindex++];
        	String spage=strHeader[iindex++];
        	Font helvetica = new Font(FontFamily.HELVETICA, 20);
            BaseFont bf_helv = helvetica.getCalculatedBaseFont(false);
            int pagecnt=new Integer(spage);
            

        	int pageindex=1;
        	
        	
            BufferedImage buffered=ImageIO.read(new File(imgname));
            BufferedImage buffer=grayImage(buffered);
            File outputfile = new File("imgbuffer.png");
            ImageIO.write(buffer, "png", outputfile); // Write the Buffered Image into an output file
            Image img  = Image.getInstance("imgbuffer.png");
            img.setAbsolutePosition(0, 0);
            img.scalePercent(93);
            //img.scaleToFit(95f, 130f);
            //img.scaleToFit(59f, 81f);
            
            for(int p=0;p<pagecnt;p++){
            	cb.addImage(img);
                
                
                cb.beginText();

                cb.setFontAndSize(bf_helv, 20);
                cb.showTextAligned(Element.ALIGN_LEFT,companyname , 120, 225, 0);
                cb.showTextAligned(Element.ALIGN_LEFT,address0 , 50, 200, 0);
                cb.showTextAligned(Element.ALIGN_LEFT,address1 , 50, 175, 0);
                //cb.setFontAndSize(bf_helv, 12);
                int ileft=155;
                int itop=130;
                cb.showTextAligned(Element.ALIGN_LEFT,pono , ileft, itop, 0);//pono
                cb.showTextAligned(Element.ALIGN_LEFT,sono , ileft, 105, 0);
                cb.showTextAligned(Element.ALIGN_LEFT,sdate , 340, itop, 0);
                
                //cb.showTextAligned(Element.ALIGN_LEFT,comment , ileft+20, 85, 30);
                ColumnText ct=new ColumnText(cb);
                Phrase pComment=new Phrase(comment);
                ct.setSimpleColumn(pComment, ileft+20, 105, ileft+350,45, 15,Element.ALIGN_LEFT);
                ct.go();
                
                cb.setFontAndSize(bf_helv, 12);
                cb.showTextAligned(Element.ALIGN_LEFT,(pageindex++)+"/"+spage , 450, 25, 0);
                cb.endText();
                if(p==pagecnt-1)continue;
                document.newPage();
            }
            
            /*cb.addImage(img);
           
            
            cb.beginText();

            cb.setFontAndSize(bf_helv, 20);
            cb.showTextAligned(Element.ALIGN_LEFT,companyname , 120, 225, 0);
            cb.showTextAligned(Element.ALIGN_LEFT,address0 , 50, 200, 0);
            cb.showTextAligned(Element.ALIGN_LEFT,address1 , 50, 175, 0);
            //cb.setFontAndSize(bf_helv, 12);
            int ileft=160;
            int itop=130;
            cb.showTextAligned(Element.ALIGN_LEFT,pono , ileft, itop, 0);//pono
            cb.showTextAligned(Element.ALIGN_LEFT,sono , ileft, 105, 0);
            cb.showTextAligned(Element.ALIGN_LEFT,sdate , 340, itop, 0);
            cb.showTextAligned(Element.ALIGN_LEFT,comment , ileft+20, 85, 0);
            cb.setFontAndSize(bf_helv, 12);
            cb.showTextAligned(Element.ALIGN_LEFT,(pageindex++)+"/"+spage , 450, 25, 0);
            cb.endText();
            document.newPage();
            cb.addImage(img);

        	cb.beginText();

            cb.setFontAndSize(bf_helv, 20);
            cb.showTextAligned(Element.ALIGN_LEFT,companyname, 120, 225, 0);
            cb.showTextAligned(Element.ALIGN_LEFT,address0, 50, 200, 0);
            cb.showTextAligned(Element.ALIGN_LEFT,address1 , 50, 175, 0);
            //cb.setFontAndSize(bf_helv, 12);
            ileft=160;
            itop=130;
            cb.showTextAligned(Element.ALIGN_LEFT,pono , ileft, itop, 0);//pono
            cb.showTextAligned(Element.ALIGN_LEFT,sono , ileft, 105, 0);
            cb.showTextAligned(Element.ALIGN_LEFT,sdate, 340, itop, 0);
            cb.showTextAligned(Element.ALIGN_LEFT,comment , ileft+20, 85, 0);
            cb.setFontAndSize(bf_helv, 12);
            cb.showTextAligned(Element.ALIGN_LEFT,(pageindex++)+"/"+spage , 450, 25, 0);
            
            cb.endText();*/
        }else{
        	PdfPTable table=getTable(cb,rptname,strHeader,strBody);
            //table.setSkipLastFooter(true);
            table.setExtendLastRow(true);
        	document.add(table);
        	
        } 
          //  document.newPage();
        //}
        // step 5
        document.close();
    }
 
    /**
     * Creates a table with screenings.
     * @param connection the database connection
     * @param day a film festival day
     * @return a table with screenings
     * @throws SQLException
     * @throws DocumentException
     * @throws IOException
     */
    
    @SuppressWarnings("deprecation")
	public PdfPTable getTable(PdfContentByte cb,String rptname,String[] strHeader,String[][] strBody)
        throws SQLException, DocumentException, IOException {
    	// Create a table with 7 columns
       	zzyPdfTable ztb=new zzyPdfTable();
       	
       	int iIndex=0;
       	for(int i=0;i<strHeader.length;i++){
       		strHeader[i]=filterinvalidstr(strHeader[i]);
       	}
       	if(strBody!=null)
       	for(int i=0;i<strBody.length;i++){
       		String[] si=strBody[i];
       		for(int j=0;j<si.length;j++){
       			si[j]=filterinvalidstr(si[j]).replaceAll("pdfseg",",").replaceAll("&nbsp;"," ");
       			si[j]=ZzyCommon.removeHtmlTag(si[j]);
       		}
       	}
    	if(rptname.equals("barcodecomponent")){
    		ztb.setHeaderFloat(new float[] {10});
            // header
    		PdfPTable tHeader = new PdfPTable(new float[] {30,10});
    		tHeader.getDefaultCell().setBorder(Rectangle.NO_BORDER);
            tHeader.addCell(new Phrase("Barcode"));
            tHeader.addCell(new Phrase(strHeader[1]));
            ztb.settHeader(tHeader);
    		ztb.setHeaderHeight(20);
    		//columne header
    		zzyPdfCell col1=new zzyPdfCell(true,"center","",2.3f,64,"");
    		ztb.setColumn(new zzyPdfCell[]{col1});
    		PdfPTable tfoot = new PdfPTable(new float[] {1});
    		PdfPCell[] cfoot=new PdfPCell[1];

    		cfoot[0]=new PdfPCell(new Phrase("Print date:"+ZzyCommon.getNow(),new Font(FontFamily.HELVETICA, 8,Font.BOLD)));
    		cfoot[0].setHorizontalAlignment(Element.ALIGN_LEFT);

    		setTableNoborder(tfoot,cfoot);
            ztb.settFoot(tfoot);
    		ztb.setFootHeight(1);
    		ztb.setBody(strBody);      		
    	}else if(rptname.equals("barcodelocation")){
    		ztb.setHeaderFloat(new float[] {10,10,10});
            // header
    		PdfPTable tHeader = new PdfPTable(new float[] {30,10});
    		tHeader.getDefaultCell().setBorder(Rectangle.NO_BORDER);
            tHeader.addCell(new Phrase("Barcode"));
            tHeader.addCell(new Phrase(strHeader[1]));
            ztb.settHeader(tHeader);
    		ztb.setHeaderHeight(20);
    		//columne header
    		//zzyPdfCell col1=new zzyPdfCell(true,"center","",2.3f,64,"");
    		int widthforqr=90;

    		zzyPdfCell col1=new zzyPdfCell(true,"center","",widthforqr,widthforqr,"qrcodelabel");
    		zzyPdfCell col2=new zzyPdfCell(true,"center","",widthforqr,widthforqr,"qrcodelabel");
    		zzyPdfCell col3=new zzyPdfCell(true,"center","",widthforqr,widthforqr,"qrcodelabel");
    		ztb.setColumn(new zzyPdfCell[]{col1,col2,col3});
    		PdfPTable tfoot = new PdfPTable(new float[] {1});
    		PdfPCell[] cfoot=new PdfPCell[1];

    		cfoot[0]=new PdfPCell(new Phrase("Print date:"+ZzyCommon.getNow(),new Font(FontFamily.HELVETICA, 8,Font.BOLD)));
    		cfoot[0].setHorizontalAlignment(Element.ALIGN_LEFT);

    		setTableNoborder(tfoot,cfoot);
            ztb.settFoot(tfoot);
    		ztb.setFootHeight(1);
    		for(int i=0;i<strBody.length;i++){
    			String[] si=strBody[i];
    			for(int j=0;j<si.length;j++){
    				String sj=si[j];
    				sj=sj.replaceAll("_", "-");
    				strBody[i][j]=sj;
    			}
    		}
    		ztb.setBody(strBody);      		
    	}else if(rptname.equals("barcodeitem")){
    		ztb.setHeaderFloat(new float[] {5});
            // header
    		PdfPTable tHeader = new PdfPTable(new float[] {30,10});
    		tHeader.getDefaultCell().setBorder(Rectangle.NO_BORDER);
            tHeader.addCell(new Phrase(strHeader[0]));
            tHeader.addCell(new Phrase(strHeader[1]));
            ztb.settHeader(tHeader);
    		ztb.setHeaderHeight(20);
    		//columne header
    		zzyPdfCell col1=new zzyPdfCell(true,"center","",2.3f,64,strHeader[0]);
    		//zzyPdfCell col1=new zzyPdfCell(true,"center","",1.3f,30,strHeader[0]);
    		ztb.setColumn(new zzyPdfCell[]{col1});
    		PdfPTable tfoot = new PdfPTable(new float[] {1});
    		PdfPCell[] cfoot=new PdfPCell[1];

    		cfoot[0]=new PdfPCell(new Phrase("Print date:"+ZzyCommon.getNow(),new Font(FontFamily.HELVETICA, 8,Font.BOLD)));
    		cfoot[0].setHorizontalAlignment(Element.ALIGN_LEFT);

    		setTableNoborder(tfoot,cfoot);
            ztb.settFoot(tfoot);
    		ztb.setFootHeight(1);
    		ztb.setBody(strBody);       
    	}else if(rptname.equals("barcodeitemone")){
    		ztb.setHeaderFloat(new float[] {5});
            // header
    		PdfPTable tHeader = new PdfPTable(new float[] {30,10});
    		tHeader.getDefaultCell().setBorder(Rectangle.NO_BORDER);
            tHeader.addCell(new Phrase(""));
            tHeader.addCell(new Phrase(""));
            ztb.settHeader(tHeader);
    		ztb.setHeaderHeight(20);
    		//columne header
    		zzyPdfCell col1=new zzyPdfCell(true,"center","",2.3f,64,strHeader[0]);
    		//zzyPdfCell col1=new zzyPdfCell(true,"center","",1.3f,30,strHeader[0]);
    		ztb.setColumn(new zzyPdfCell[]{col1});
    		PdfPTable tfoot = new PdfPTable(new float[] {1});
    		PdfPCell[] cfoot=new PdfPCell[1];

    		cfoot[0]=new PdfPCell(new Phrase("Print date:"+ZzyCommon.getNow(),new Font(FontFamily.HELVETICA, 8,Font.BOLD)));
    		cfoot[0].setHorizontalAlignment(Element.ALIGN_LEFT);

    		setTableNoborder(tfoot,cfoot);
            ztb.settFoot(tfoot);
    		ztb.setFootHeight(1);
    		ztb.setBody(strBody);       
    	    	
    	}else if(rptname.equals("barcodepartbook")){
    		ztb.setHeaderFloat(new float[] {2,2,2,2});
            // header
    		PdfPTable tHeader = new PdfPTable(new float[] {30,10});
    		tHeader.getDefaultCell().setBorder(Rectangle.NO_BORDER);
            tHeader.addCell(new Phrase("Barcode for Component"));
            //tHeader.addCell(new Phrase(strHeader[0]));
            ztb.settHeader(tHeader);
    		ztb.setHeaderHeight(20);
    		//columne header
    		//zzyPdfCell col1=new zzyPdfCell(false,"left","Item Code");
    		//zzyPdfCell col2=new zzyPdfCell(false,"left","Description");
    		zzyPdfCell col1=new zzyPdfCell(true,"center","Model#",60,60,"qrcodelabel");
    		zzyPdfCell col2=new zzyPdfCell(false,"left","Desc");
    		zzyPdfCell col3=new zzyPdfCell(true,"center","Model#",60,60,"qrcodelabel");
    		zzyPdfCell col4=new zzyPdfCell(false,"left","Desc");

//    		zzyPdfCell col3=new zzyPdfCell(true,"center","",2.3f,64,"");
    		ztb.setColumn(new zzyPdfCell[]{col1,col2,col3,col4});
    		PdfPTable tfoot = new PdfPTable(new float[] {1});
    		PdfPCell[] cfoot=new PdfPCell[1];

    		cfoot[0]=new PdfPCell(new Phrase("Print date:"+ZzyCommon.getNow(),new Font(FontFamily.HELVETICA, 8,Font.BOLD)));
    		cfoot[0].setHorizontalAlignment(Element.ALIGN_LEFT);

    		setTableNoborder(tfoot,cfoot);
            ztb.settFoot(tfoot);
    		ztb.setFootHeight(1);
    		ztb.setBody(strBody);      		
    	/*}else if(rptname.equals("cheque")){
    		ztb.setHeaderFloat(new float[] {1});
            // header
    		PdfPTable tHeader = new PdfPTable(new float[] {1});
    		int iindex=0;
    		int icindex=0;
    		PdfPTable tTop=new PdfPTable(new float[]{9,1,2});
            PdfPCell[] pCellTop=new PdfPCell[9];
            Font fTN=datafont;
            pCellTop[icindex++]=new PdfPCell(new Phrase(" ",fTN));
            pCellTop[icindex++]=new PdfPCell(new Phrase("DATE",fTN));
            pCellTop[icindex]=new PdfPCell(new Phrase(strHeader[iindex++],fTN));

            pCellTop[icindex++]=new PdfPCell(new Phrase(" ",fTN));
            pCellTop[icindex++]=new PdfPCell(new Phrase(" ",fTN));
            pCellTop[icindex++]=new PdfPCell(new Phrase(" ",fTN));

            pCellTop[icindex++]=new PdfPCell(new Phrase(strHeader[iindex++],fTN));
            pCellTop[icindex++]=new PdfPCell(new Phrase(strHeader[iindex++],fTN));
            pCellTop[icindex++]=new PdfPCell(new Phrase(strHeader[iindex++],fTN));
            //pCellTop[0].setHorizontalAlignment(Element.ALIGN_RIGHT);
            setTableNoborder(tTop,pCellTop);
            tHeader.addCell(tTop);
            tHeader.addCell(new Phrase(" ",fTN));
            tHeader.addCell(new Phrase(" ",fTN));
            tHeader.addCell(new Phrase("  "+strHeader[iindex++],new Font(FontFamily.HELVETICA, 12,Font.BOLD)));
            tHeader.addCell(new Phrase(" ",fTN));
            tHeader.addCell(new Phrase(strHeader[iindex++],fTN));
            tHeader.addCell(new Phrase(strHeader[iindex++]+","+strHeader[iindex++]+" "+strHeader[iindex++],fTN));
            tHeader.addCell(new Phrase(strHeader[iindex++],fTN));
            tHeader.addCell(new Phrase(" ",fTN));
            tHeader.addCell(new Phrase(" ",fTN));
            tHeader.addCell(new Phrase(" ",fTN));

            ztb.settHeader(tHeader);

    		tHeader.getDefaultCell().setBorder(Rectangle.NO_BORDER);
            //tHeader.addCell(new Phrase(strHeader[0]));
            ztb.settHeader(tHeader);
    		ztb.setHeaderHeight(320);
    		
    		//columne header
    		//zzyPdfCell col1=new zzyPdfCell(false,"left","Item Code");
    		//zzyPdfCell col2=new zzyPdfCell(false,"left","Description");
    		
//    		zzyPdfCell col3=new zzyPdfCell(true,"center","",2.3f,64,"");
    		PdfPTable tfoot = new PdfPTable(new float[] {1});
    		PdfPCell[] cfoot=new PdfPCell[1];

    		cfoot[0]=new PdfPCell(new Phrase(" "));

    		setTableNoborder(tfoot,cfoot);
            ztb.settFoot(tfoot);
    		ztb.setFootHeight(1);
    		ztb.setBody(strBody);    */  		
    		
    	}else if(rptname.equals("inspect")){
    		ztb.setHeaderFloat(new float[] {5,5,12,20});
            // header
    		PdfPTable tHeader = new PdfPTable(new float[] { 38,5,50});
    		tHeader.getDefaultCell().setBorder(Rectangle.NO_BORDER);
    		PdfPCell cellCompany=new PdfPCell(getMyLogo(strHeader[iIndex++]));
    		cellCompany.setBorder(Rectangle.NO_BORDER);
            tHeader.addCell(cellCompany);
            
            tHeader.addCell(new Phrase(""));
            PdfPTable tRight=new PdfPTable(new float[]{1});
            PdfPCell[] pCellRight=new PdfPCell[2];
            Font fT=columnfont;
            Font fTN=datafont;
            pCellRight[0]=new PdfPCell(new Phrase("INSPECT:"+strHeader[iIndex],titlefont));
            pCellRight[0].setHorizontalAlignment(Element.ALIGN_RIGHT);
            PdfPTable tRight1=new PdfPTable(new float[]{1,1,1,1});
            PdfPCell[] pCellRight1=new PdfPCell[20];
            pCellRight1[0]=new PdfPCell(new Phrase("Inspect #:",fT));
            pCellRight1[1]=new PdfPCell(new Phrase(strHeader[iIndex++],fTN));
            pCellRight1[2]=new PdfPCell(new Phrase("Inspect By:",fT));
            pCellRight1[3]=new PdfPCell(new Phrase(strHeader[iIndex++],fTN));
            pCellRight1[4]=new PdfPCell(new Phrase("Date:",fT));
            pCellRight1[5]=new PdfPCell(new Phrase(strHeader[iIndex++],fTN));
            pCellRight1[6]=new PdfPCell(new Phrase("PONO:",fT));
            pCellRight1[7]=new PdfPCell(new Phrase(strHeader[iIndex++],fTN));
            pCellRight1[8]=new PdfPCell(new Phrase("Model#:",fT));
            pCellRight1[9]=new PdfPCell(new Phrase(strHeader[iIndex++],fTN));
            pCellRight1[10]=new PdfPCell(new Phrase("Lamping:",fT));
            pCellRight1[11]=new PdfPCell(new Phrase(strHeader[iIndex++],fTN));
            pCellRight1[12]=new PdfPCell(new Phrase("Container#:",fT));
            pCellRight1[13]=new PdfPCell(new Phrase(strHeader[iIndex++],fTN));
            pCellRight1[14]=new PdfPCell(new Phrase("Qty Received:",fT));
            pCellRight1[15]=new PdfPCell(new Phrase(strHeader[iIndex++],fTN));
            pCellRight1[16]=new PdfPCell(new Phrase("Factory:",fT));
            pCellRight1[17]=new PdfPCell(new Phrase(strHeader[iIndex++],fTN));
            pCellRight1[18]=new PdfPCell(new Phrase("Qty Inspect:",fT));
            pCellRight1[19]=new PdfPCell(new Phrase(strHeader[iIndex++],fTN));
            setTableNoborder(tRight1,pCellRight1);
            pCellRight[1]=new PdfPCell(tRight1);
            setTableNoborder(tRight,pCellRight);
            tHeader.addCell(tRight);
            ztb.settHeader(tHeader);
      		ztb.setHeaderHeight(150);

    		//columne header
    		
    		zzyPdfCell col1=new zzyPdfCell(false,"left","Desc");
    		zzyPdfCell col2=new zzyPdfCell(false,"left","Qty");
    		zzyPdfCell col3=new zzyPdfCell(false,"left","Comment");
    		zzyPdfCell col4=new zzyPdfCell(false,"center","Img");
    		col4.setIsImg(true);
    		ztb.setColumn(new zzyPdfCell[]{col1,col2,col3,col4});
    		PdfPTable tfoot = new PdfPTable(new float[] {1});
    		PdfPCell[] cfoot=new PdfPCell[1];

    		cfoot[0]=new PdfPCell(new Phrase("Print date:"+ZzyCommon.getNow(),columnfont));
    		setTableNoborder(tfoot,cfoot);
            ztb.settFoot(tfoot);
    		ztb.setFootHeight(30);
    		
    		ztb.setBody(strBody);       		
    	}else if(rptname.equals("invoice") || rptname.equals("erpinvoice")){
    		float[] afloat=new float[] {15,5,5,30,8,10};
    		if(rptname.equals("erpinvoice"))afloat=new float[] {15,5,5,30,8,10,10};
    		ztb.setHeaderFloat(afloat);
            // header
    		PdfPTable tHeader = new PdfPTable(new float[] { 4,1,5});
    		tHeader.getDefaultCell().setBorder(Rectangle.NO_BORDER);
    		String mycompanyid=strHeader[iIndex++];
    		PdfPCell cellCompany=new PdfPCell(getMyLogo(mycompanyid));
    		cellCompany.setBorder(Rectangle.NO_BORDER);
            tHeader.addCell(cellCompany);
            
            tHeader.addCell(new Phrase(""));
            PdfPTable tRight=new PdfPTable(new float[]{1});
            PdfPCell[] pCellRight=new PdfPCell[4];
            Font fT=columnfont;
            Font fTN=datafont;
            pCellRight[0]=new PdfPCell(new Phrase("",titlefont));
            pCellRight[1]=new PdfPCell(new Phrase("",titlefont));
            pCellRight[2]=new PdfPCell(new Phrase(strHeader[iIndex++],titlefont));
            PdfPTable tRight1=new PdfPTable(new float[]{1,1});
            PdfPCell[] pCellRight1=new PdfPCell[8];
            pCellRight1[0]=new PdfPCell(new Phrase("Date:",fT));
            pCellRight1[1]=new PdfPCell(new Phrase(strHeader[iIndex++],fTN));
            pCellRight1[2]=new PdfPCell(new Phrase("INVOICE NO:",fT));
            pCellRight1[3]=new PdfPCell(new Phrase(strHeader[iIndex++],fTN));
            pCellRight1[4]=new PdfPCell(new Phrase("CUSTOMER NO:",fT));
            pCellRight1[5]=new PdfPCell(new Phrase(strHeader[iIndex++],fTN));
            pCellRight1[6]=new PdfPCell(new Phrase("REFERENCE:",fT));
            pCellRight1[7]=new PdfPCell(new Phrase(strHeader[iIndex++],fTN));
            setTableNoborder(tRight1,pCellRight1);
            pCellRight[3]=new PdfPCell(tRight1);
            setTableNoborder(tRight,pCellRight);
            tHeader.addCell(tRight);

            tHeader.addCell(new Phrase(" ",fT));
            tHeader.addCell(new Phrase("",fT));
            tHeader.addCell(new Phrase("",fT));

            tHeader.addCell(new Phrase("BILL TO:",fT));
            tHeader.addCell(new Phrase("",fT));
            tHeader.addCell(new Phrase("SHIP TO:",fT));

            tHeader.addCell(new Phrase(strHeader[iIndex++],fTN));
            tHeader.addCell(new Phrase("",fTN));
            tHeader.addCell(new Phrase(strHeader[iIndex++],fTN));
            tHeader.addCell(new Phrase(strHeader[iIndex++],fTN));
            tHeader.addCell(new Phrase("",fTN));
            tHeader.addCell(new Phrase(strHeader[iIndex++],fTN));
            tHeader.addCell(new Phrase(strHeader[iIndex++],fTN));
            tHeader.addCell(new Phrase("",fTN));
            tHeader.addCell(new Phrase(strHeader[iIndex++],fTN));
            tHeader.addCell(new Phrase(strHeader[iIndex++],fTN));
            tHeader.addCell(new Phrase("",fTN));
            tHeader.addCell(new Phrase(strHeader[iIndex++]+"    "+strHeader[iIndex++],fTN));
            //tHeader.addCell(new Phrase(strHeader[iIndex++],fTN));
            tHeader.addCell(new Phrase("  ",fT));
            tHeader.addCell(new Phrase("",fT));
            tHeader.addCell(new Phrase("",fT));

            PdfPTable tTitle=new PdfPTable(new float[]{8,5,8,5,8,12});
            PdfPCell[] pcTitle=new PdfPCell[12];
            Font cfont1=new Font(FontFamily.HELVETICA, 8, Font.NORMAL,BaseColor.WHITE);
            //pCellRight1[0]=new PdfPCell(new Phrase("DATE :",cfont1));
            //pCellRight1[0].setBackgroundColor(BaseColor.BLACK);

            pcTitle[0]=new PdfPCell(new Phrase("SALES REP",cfont1));
            pcTitle[1]=new PdfPCell(new Phrase("ORDER DATE",cfont1));
            pcTitle[2]=new PdfPCell(new Phrase("PACKING SLIP NO",cfont1));
            pcTitle[3]=new PdfPCell(new Phrase("SHIP DATE",cfont1));
            pcTitle[4]=new PdfPCell(new Phrase("CUSTOMER PO NO",cfont1));
            pcTitle[5]=new PdfPCell(new Phrase("TERMS",cfont1));
            pcTitle[0].setBackgroundColor(BaseColor.BLACK);
            pcTitle[1].setBackgroundColor(BaseColor.BLACK);
            pcTitle[2].setBackgroundColor(BaseColor.BLACK);
            pcTitle[3].setBackgroundColor(BaseColor.BLACK);
            pcTitle[4].setBackgroundColor(BaseColor.BLACK);
            pcTitle[5].setBackgroundColor(BaseColor.BLACK);

            
            pcTitle[6]=new PdfPCell(new Phrase(strHeader[iIndex++],fTN));
            pcTitle[7]=new PdfPCell(new Phrase(strHeader[iIndex++],fTN));
            pcTitle[8]=new PdfPCell(new Phrase(strHeader[iIndex++],fTN));
            pcTitle[9]=new PdfPCell(new Phrase(strHeader[iIndex++],fTN));
            pcTitle[10]=new PdfPCell(new Phrase(strHeader[iIndex++],fTN));
            pcTitle[11]=new PdfPCell(new Phrase(strHeader[iIndex++],fTN));
            for(int i=0;i<pcTitle.length;i++){
            	pcTitle[i].setHorizontalAlignment(Element.ALIGN_CENTER);
            	tTitle.addCell(pcTitle[i]);
            }
            PdfPCell cTitle=new PdfPCell(tTitle);
            cTitle.setColspan(3);
            tHeader.addCell(cTitle);
            ztb.settHeader(tHeader);
    		ztb.setHeaderHeight(230);

    		//columne header
    		
    		zzyPdfCell col0=new zzyPdfCell(false,"left","ITEM/PART NO.");
    		zzyPdfCell col1=new zzyPdfCell(false,"right","QTY");
    		zzyPdfCell col2=new zzyPdfCell(false,"left","UNIT");
    		zzyPdfCell col3=new zzyPdfCell(false,"left","DESCRIPTION");
    		zzyPdfCell col4=new zzyPdfCell(false,"right","UNIT PRICE");
    		zzyPdfCell col5=new zzyPdfCell(false,"right","TOTAL");
    		zzyPdfCell col6=new zzyPdfCell(false,"right","DISCOUNT");
    		
    		zzyPdfCell[] bodyCell=new zzyPdfCell[]{col0,col1,col2,col3,col4,col5};
    		if(rptname.equals("erpinvoice"))bodyCell=new zzyPdfCell[]{col0,col1,col2,col3,col4,col5,col6};
    		ztb.setColumn(bodyCell);

    		
    		PdfPTable tfoot = new PdfPTable(new float[] {1});
    		PdfPCell[] cfoot=new PdfPCell[3];
    		PdfPTable tfoot0 = new PdfPTable(new float[] {3,2});
    		PdfPCell[] cfoot0=new PdfPCell[2];

    		PdfPTable tfoot1 = new PdfPTable(new float[] {1});
    		PdfPCell[] cfoot1=new PdfPCell[2];
    		
    		cfoot1[0]=new PdfPCell(new Phrase("Comments",columnfont));
    		cfoot1[0].setHorizontalAlignment(Element.ALIGN_LEFT);

    		cfoot1[1]=new PdfPCell(new Phrase(" "+strHeader[iIndex++],datafont));
    		cfoot1[1].setHorizontalAlignment(Element.ALIGN_LEFT);
    		
    		String ecofee=strHeader[iIndex++];
    		String subtotal=strHeader[iIndex++];
    		String discount=strHeader[iIndex++];
    		String freight=strHeader[iIndex++];
    		String pst=strHeader[iIndex++];
    		String gst=strHeader[iIndex++];
    		String total=strHeader[iIndex++];
    		String DiscountDesc=strHeader[iIndex++];
    		int footheight=120;
    		int footheight1=70;
    		if(DiscountDesc.length()<1)DiscountDesc="Discount";
    		
    		int hlen=4;
    		if(ecofee.length()>0 && !ecofee.equals(".00")){
    			hlen+=2;
    		}else{footheight-=10;footheight1-=10;}
    		if(discount.length()>0 && !discount.equals(".00")){
    			hlen+=2;
    		}else{footheight-=10;footheight1-=10;}
    		if(freight.length()>0 && !freight.equals(".00")){
    			hlen+=2;
    		}else{footheight-=10;footheight1-=10;}
    		if(pst.length()>0 && !pst.equals(".00")){
    			hlen+=2;
    		}else{footheight-=10;footheight1-=10;}
    		if(gst.length()>0 && !gst.equals(".00")){
    			hlen+=2;
    		}else{footheight-=10;footheight1-=10;}
    		
    		
    		
    		cfoot1[1].setFixedHeight(footheight1);
    		setTableNoborder(tfoot1,cfoot1);
    		cfoot0[0]=new PdfPCell(tfoot1);
    		
    		PdfPTable tfoot20 = new PdfPTable(new float[] {1});
    		PdfPTable tfoot2 = new PdfPTable(new float[] {10,6});

    		PdfPCell[] cfoot2=new PdfPCell[hlen];
    		int ifoot2=0;
    		cfoot2[ifoot2]=new PdfPCell(new Phrase("Sub Total",datafont));
    		cfoot2[ifoot2++].setHorizontalAlignment(Element.ALIGN_LEFT);
    		cfoot2[ifoot2]=new PdfPCell(new Phrase(subtotal,datafont));
    		cfoot2[ifoot2++].setHorizontalAlignment(Element.ALIGN_RIGHT);
    		if(discount.length()>0 && !discount.equals(".00")){
        		cfoot2[ifoot2]=new PdfPCell(new Phrase(DiscountDesc,datafont));
        		cfoot2[ifoot2++].setHorizontalAlignment(Element.ALIGN_LEFT);
        		cfoot2[ifoot2]=new PdfPCell(new Phrase(discount,datafont));
        		cfoot2[ifoot2++].setHorizontalAlignment(Element.ALIGN_RIGHT);
    		}
    		if(freight.length()>0 && !freight.equals(".00")){
        		cfoot2[ifoot2]=new PdfPCell(new Phrase("Freight",datafont));
        		cfoot2[ifoot2++].setHorizontalAlignment(Element.ALIGN_LEFT);
        		cfoot2[ifoot2]=new PdfPCell(new Phrase(freight,datafont));
        		cfoot2[ifoot2++].setHorizontalAlignment(Element.ALIGN_RIGHT);
    			
    		}
    		if(pst.length()>0 && !pst.equals(".00")){
        		cfoot2[ifoot2]=new PdfPCell(new Phrase("PST",datafont));
        		cfoot2[ifoot2++].setHorizontalAlignment(Element.ALIGN_LEFT);
        		cfoot2[ifoot2]=new PdfPCell(new Phrase(pst,datafont));
        		cfoot2[ifoot2++].setHorizontalAlignment(Element.ALIGN_RIGHT);
    			
    		}
    		if(gst.length()>0 && !gst.equals(".00")){
        		cfoot2[ifoot2]=new PdfPCell(new Phrase("GST/HST",datafont));
        		cfoot2[ifoot2++].setHorizontalAlignment(Element.ALIGN_LEFT);
        		cfoot2[ifoot2]=new PdfPCell(new Phrase(gst,datafont));
        		cfoot2[ifoot2++].setHorizontalAlignment(Element.ALIGN_RIGHT);
    			
    		}

    		if(ecofee.length()>0 && !ecofee.equals(".00")){

        		cfoot2[ifoot2]=new PdfPCell(new Phrase("Eco Fee",new Font(FontFamily.HELVETICA, 8,Font.UNDERLINE)));
        		cfoot2[ifoot2++].setHorizontalAlignment(Element.ALIGN_LEFT);
        		cfoot2[ifoot2]=new PdfPCell(new Phrase(ecofee,new Font(FontFamily.HELVETICA, 8,Font.UNDERLINE)));
        		cfoot2[ifoot2++].setHorizontalAlignment(Element.ALIGN_RIGHT);
    			
    		}

    		cfoot2[ifoot2]=new PdfPCell(new Phrase("Total",columnfont));
    		cfoot2[ifoot2++].setHorizontalAlignment(Element.ALIGN_LEFT);
    		//cfoot2[11]=new PdfPCell(new Phrase("",columnfont));
    		cfoot2[ifoot2]=new PdfPCell(new Phrase(total,columnfont));
    		cfoot2[ifoot2++].setHorizontalAlignment(Element.ALIGN_RIGHT);

    		setTableNoborder(tfoot2,cfoot2);
    		tfoot20.addCell(tfoot2);
    		cfoot0[1]=new PdfPCell(tfoot20);
    		setTableNoborder(tfoot0,cfoot0);
    		cfoot[0]=new PdfPCell(tfoot0);
    		String hststr="hstgstno";
    		if(mycompanyid.equals("2719"))hststr+="llc";
    		cfoot[1]=new PdfPCell(new Phrase("HST/GST#: "+System.getProperty(hststr)+"  Please pay by invoice terms. Thank you for your business.",columnfont));
    		cfoot[1].setHorizontalAlignment(Element.ALIGN_CENTER);
    		cfoot[2]=new PdfPCell(new Phrase("Please give us your feedback visit "+System.getProperty("suveylink"),new Font(FontFamily.HELVETICA, 8,Font.BOLD)));
    		cfoot[2].setHorizontalAlignment(Element.ALIGN_CENTER);
    		setTableNoborder(tfoot,cfoot);
            ztb.settFoot(tfoot);
    		ztb.setFootHeight(footheight);
    		ztb.setBody(strBody);    
    	}else if(rptname.equals("invoicestatement")){
    		
    		ztb.setHeaderFloat(new float[] {5,20,5,5,5});
            // header
    		PdfPTable tHeader = new PdfPTable(new float[] { 12,1,20});
    		tHeader.getDefaultCell().setBorder(Rectangle.NO_BORDER);
    		PdfPCell cellCompany=new PdfPCell(getMyLogo(strHeader[iIndex++],new String[]{strHeader[iIndex++],strHeader[iIndex++],strHeader[iIndex++],strHeader[iIndex++],strHeader[iIndex++],strHeader[iIndex++],strHeader[iIndex++],strHeader[iIndex++]}));
    		cellCompany.setBorder(Rectangle.NO_BORDER);
            tHeader.addCell(cellCompany);
            tHeader.addCell(new Phrase(""));
            PdfPTable tRight=new PdfPTable(new float[]{1});
            PdfPCell[] pCellRight=new PdfPCell[4];
            Font fT=columnfont;
            Font fTN=datafont;
            pCellRight[0]=new PdfPCell(new Phrase("",titlefont));
            pCellRight[1]=new PdfPCell(new Phrase("",titlefont));
            pCellRight[2]=new PdfPCell(new Phrase("Statement ["+strHeader[iIndex++]+"]",titlefont));
            pCellRight[3]=new PdfPCell(new Phrase("DATE: ["+strHeader[iIndex++]+"]",fTN));
            setTableNoborder(tRight,pCellRight);
            tHeader.addCell(tRight);
            /*tHeader.addCell(new Phrase("    "));tHeader.addCell(new Phrase("    "));tHeader.addCell(new Phrase("    "));
            tHeader.addCell(new Phrase("    "+strHeader[iIndex++],fTN));
            tHeader.addCell(new Phrase(""));tHeader.addCell(new Phrase(""));
            tHeader.addCell(new Phrase("          "+strHeader[iIndex++],fTN));
            tHeader.addCell(new Phrase("",fTN));tHeader.addCell(new Phrase(""));
            tHeader.addCell(new Phrase("          "+strHeader[iIndex++]+","+strHeader[iIndex++]+" "+strHeader[iIndex++],fTN));
            tHeader.addCell(new Phrase("",fTN));tHeader.addCell(new Phrase(""));
            tHeader.addCell(new Phrase("          "+strHeader[iIndex++],fTN));
            tHeader.addCell(new Phrase("",fTN));tHeader.addCell(new Phrase(""));
            tHeader.addCell(new Phrase("           Phone: "+strHeader[iIndex++]+"    Fax: "+strHeader[iIndex++],fTN));
            tHeader.addCell(new Phrase(" ",fTN));tHeader.addCell(new Phrase(" "));
            tHeader.addCell(new Phrase(" "));tHeader.addCell(new Phrase(" "));tHeader.addCell(new Phrase(" "));
            */
            //PdfPTable tTitle=new PdfPTable(new float[]{5,5,5,5,5,6});
            
            /*PdfPTable tRight1=new PdfPTable(new float[]{1});
            
            PdfPCell[] pCellRight1=new PdfPCell[9];
            int ii=0;
            pCellRight1[ii++]=new PdfPCell(new Phrase(" ",fTN));
            pCellRight1[ii++]=new PdfPCell(new Phrase(" ",fTN));
            pCellRight1[ii++]=new PdfPCell(new Phrase(" ",fTN));
            pCellRight1[ii++]=new PdfPCell(new Phrase(" "+strHeader[iIndex++],fTN));
            pCellRight1[ii++]=new PdfPCell(new Phrase("          "+strHeader[iIndex++],fTN));
            pCellRight1[ii++]=new PdfPCell(new Phrase("          "+strHeader[iIndex++]+","+strHeader[iIndex++]+" "+strHeader[iIndex++],fTN));
            pCellRight1[ii++]=new PdfPCell(new Phrase("          "+strHeader[iIndex++],fTN));
            pCellRight1[ii++]=new PdfPCell(new Phrase("          Phone:"+strHeader[iIndex++]+"    Fax: "+strHeader[iIndex++],fTN));
            pCellRight1[ii++]=new PdfPCell(new Phrase("          ",fTN));
            setTableNoborder(tRight1,pCellRight1);
            */
            //tHeader.addCell(new PdfPCell(tRight1));
            //tHeader.getDefaultCell().setBorder(Rectangle.NO_BORDER);
            //ic.setBorder(Rectangle.NO_BORDER);
            
            PdfPTable tTitle=new PdfPTable(new float[]{5,20,6});
            PdfPCell[] pcTitle=new PdfPCell[6];
            int iindex=0;
            pcTitle[iindex++]=new PdfPCell(new Phrase("Current",fT));
            pcTitle[iindex++]=new PdfPCell(new Phrase("Past          Due",fT));
            //pcTitle[iindex-1].setBackgroundColor(new BaseColor(220,220,220));
            pcTitle[iindex++]=new PdfPCell(new Phrase("Total Due",fT));

            PdfPTable tTitlesub0=new PdfPTable(new float[]{5});
            PdfPCell[] pcTitlecell0=new PdfPCell[2];
            pcTitlecell0[0]=new PdfPCell(new Phrase("0-30 Days",fTN));
            pcTitlecell0[1]=new PdfPCell(new Phrase(strHeader[iIndex++],fTN));
            for(int i=0;i<pcTitlecell0.length;i++){
            	pcTitlecell0[i].setHorizontalAlignment(Element.ALIGN_CENTER);
            	tTitlesub0.addCell(pcTitlecell0[i]);
            }
            pcTitle[iindex++]=new PdfPCell(tTitlesub0);
            
            PdfPTable tTitlesub1=new PdfPTable(new float[]{5,5,5,5});
            PdfPCell[] pcTitlecell1=new PdfPCell[8];
            pcTitlecell1[0]=new PdfPCell(new Phrase("31-45 Days",fTN));
            pcTitlecell1[1]=new PdfPCell(new Phrase("46-60 Days",fTN));
            pcTitlecell1[2]=new PdfPCell(new Phrase("61-90 Days",fTN));
            pcTitlecell1[3]=new PdfPCell(new Phrase("Over 90 Days",fTN));
            pcTitlecell1[4]=new PdfPCell(new Phrase(strHeader[iIndex++],fTN));
            pcTitlecell1[5]=new PdfPCell(new Phrase(strHeader[iIndex++],fTN));
            pcTitlecell1[6]=new PdfPCell(new Phrase(strHeader[iIndex++],fTN));
            pcTitlecell1[7]=new PdfPCell(new Phrase(strHeader[iIndex++],fTN));
            for(int i=0;i<pcTitlecell1.length;i++){
            	pcTitlecell1[i].setHorizontalAlignment(Element.ALIGN_CENTER);
            	tTitlesub1.addCell(pcTitlecell1[i]);
            }
            
            pcTitle[iindex++]=new PdfPCell(tTitlesub1);
            //pcTitle[iindex-1].setBackgroundColor(new BaseColor(220,220,220));

            
            PdfPTable tTitlesub2=new PdfPTable(new float[]{5});
            PdfPCell[] pcTitlecell2=new PdfPCell[1];
            pcTitlecell2[0]=new PdfPCell(new Phrase(strHeader[iIndex++],fT));
            for(int i=0;i<pcTitlecell2.length;i++){
            	pcTitlecell2[i].setHorizontalAlignment(Element.ALIGN_CENTER);
            	tTitlesub2.addCell(pcTitlecell2[i]);
            }
            pcTitle[iindex++]=new PdfPCell(tTitlesub2);

            for(int i=0;i<pcTitle.length;i++){
            	pcTitle[i].setHorizontalAlignment(Element.ALIGN_CENTER);
            	tTitle.addCell(pcTitle[i]);
            }
            PdfPCell cTitle=new PdfPCell(tTitle);
            cTitle.setColspan(3);
            tHeader.addCell(cTitle);
            ztb.settHeader(tHeader);
    		ztb.setHeaderHeight(255);

    		//columne header
    		
    		zzyPdfCell col0=new zzyPdfCell(false,"left","Date");
    		zzyPdfCell col1=new zzyPdfCell(false,"left","Description");
    		zzyPdfCell col2=new zzyPdfCell(false,"left","PONO");
    		zzyPdfCell col3=new zzyPdfCell(false,"right","Amount");
    		zzyPdfCell col4=new zzyPdfCell(false,"right","Balance");
    		ztb.setColumn(new zzyPdfCell[]{col0,col1,col2,col3,col4});

    		
    		PdfPTable tfoot = new PdfPTable(new float[] {1});
    		PdfPCell[] cfoot=new PdfPCell[3];
    		cfoot[0]=new PdfPCell(new Phrase("      "+strHeader[iIndex++],columnfont));
    		cfoot[1]=new PdfPCell(new Phrase("      ",new Font(FontFamily.HELVETICA, 8,Font.BOLD)));
    		cfoot[2]=getThanks();
    		//new PdfPCell(new Phrase("Thank you for your business.",new Font(FontFamily.HELVETICA, 8,Font.BOLD)));
    		//cfoot[2].setHorizontalAlignment(Element.ALIGN_CENTER);
    		setTableNoborder(tfoot,cfoot);
            ztb.settFoot(tfoot);
    		ztb.setFootHeight(40);
    		ztb.setBody(strBody); 
    		
    	}else if(rptname.equals("loctranadvice")){
    		ztb.setHeaderFloat(new float[] {20,13,8,6,16,20,13,8,6,16});
            // header
    		PdfPTable tHeader = new PdfPTable(new float[] {10,10});
    		tHeader.getDefaultCell().setBorder(Rectangle.NO_BORDER);
            tHeader.addCell(new Phrase("Location Transfer Advise"));
            tHeader.addCell(new Phrase("Print Time:"+ZzyCommon.getNow()));
            //tHeader.addCell(new Phrase(strHeader[0]));
            ztb.settHeader(tHeader);
    		ztb.setHeaderHeight(40);
    		//columne header
    		//zzyPdfCell col1=new zzyPdfCell(false,"left","Item Code");
    		//zzyPdfCell col2=new zzyPdfCell(false,"left","Description");
    		zzyPdfCell col1=new zzyPdfCell(false,"left","Model#");
    		zzyPdfCell col2=new zzyPdfCell(false,"left","Type");
    		zzyPdfCell col3=new zzyPdfCell(false,"left","From");
    		zzyPdfCell col4=new zzyPdfCell(false,"left","Qty");
    		zzyPdfCell col5=new zzyPdfCell(false,"left","To");
    		zzyPdfCell col6=new zzyPdfCell(false,"left","Model#");
    		zzyPdfCell col7=new zzyPdfCell(false,"left","Type");
    		zzyPdfCell col8=new zzyPdfCell(false,"left","From");
    		zzyPdfCell col9=new zzyPdfCell(false,"left","Qty");
    		zzyPdfCell col10=new zzyPdfCell(false,"left","To");

//    		zzyPdfCell col3=new zzyPdfCell(true,"center","",2.3f,64,"");
    		ztb.setColumn(new zzyPdfCell[]{col1,col2,col3,col4,col5,col6,col7,col8,col9,col10});
    		PdfPTable tfoot = new PdfPTable(new float[] {1});
    		PdfPCell[] cfoot=new PdfPCell[1];

    		cfoot[0]=new PdfPCell(new Phrase("Print date:"+ZzyCommon.getNow(),new Font(FontFamily.HELVETICA, 8,Font.BOLD)));
    		cfoot[0].setHorizontalAlignment(Element.ALIGN_LEFT);

    		setTableNoborder(tfoot,cfoot);
            ztb.settFoot(tfoot);
    		ztb.setFootHeight(1);
    		ztb.setBody(strBody);     		

    	}else if(rptname.equals("orderlabel")){
    		ztb.setHeaderFloat(new float[] { 5,5,5,5,5});
            // header
    		PdfPTable tHeader = new PdfPTable(new float[] { 5,25});
    		tHeader.getDefaultCell().setBorder(Rectangle.NO_BORDER);
            
            tHeader.addCell(new Phrase("Page:"+strHeader[iIndex++]));
            tHeader.addCell(new Phrase(ZzyCommon.getToday()+""));
            PdfPCell alogin=new PdfPCell(new Phrase(strHeader[iIndex++]));
            alogin.setHorizontalAlignment(Element.ALIGN_CENTER);
            tHeader.addCell(alogin);
            tHeader.addCell(getBarcodeCell(cb,strHeader[iIndex++],1.8f,44,null));
            tHeader.addCell(new Phrase("Ship To:"));
            tHeader.addCell(new Phrase(strHeader[iIndex++],titlefont));
            tHeader.addCell(new Phrase(""));
            tHeader.addCell(new Phrase(strHeader[iIndex++],new Font(FontFamily.HELVETICA, 15)));
            tHeader.addCell(new Phrase(""));
            tHeader.addCell(new Phrase(strHeader[iIndex++]+","+strHeader[iIndex++]+" "+strHeader[iIndex++],new Font(FontFamily.HELVETICA, 15)));
            //tHeader.addCell(new Phrase(strHeader[iIndex++]));
            //tHeader.addCell(getBarcodeCell(cb,strHeader[iIndex++]));

            ztb.settHeader(tHeader);
    		ztb.setHeaderHeight(280);

    		//columne header
    		
    		zzyPdfCell col0=new zzyPdfCell(false,"right","Length");
    		zzyPdfCell col1=new zzyPdfCell(false,"right","Width");
    		zzyPdfCell col2=new zzyPdfCell(false,"right","Height");
    		zzyPdfCell col3=new zzyPdfCell(false,"right","Weight");
    		zzyPdfCell col4=new zzyPdfCell(false,"right","CTN");
    		ztb.setColumn(new zzyPdfCell[]{col0,col1,col2,col3,col4});
    		PdfPTable tfoot = new PdfPTable(new float[] {1});
    		PdfPCell[] cfoot=new PdfPCell[1];

    		cfoot[0]=new PdfPCell(new Phrase(""));
    		setTableNoborder(tfoot,cfoot);
            ztb.settFoot(tfoot);
    		ztb.setFootHeight(10);
    		ztb.setBody(strBody);    		
    	}else if(rptname.equals("orderpartspo") || rptname.equals("orderpartspooriginal")){
    		ztb.setHeaderFloat(new float[] { 10,15,3,3,3});
            // header
    		PdfPTable tHeader = new PdfPTable(new float[] { 5,1,5});
    		tHeader.getDefaultCell().setBorder(Rectangle.NO_BORDER);
    		PdfPCell cellCompany=new PdfPCell(getMyLogo(strHeader[iIndex++]));cellCompany.setBorder(Rectangle.NO_BORDER);
            tHeader.addCell(cellCompany);
            
            tHeader.addCell(new Phrase(""));
            PdfPTable tRight=new PdfPTable(new float[]{1});
            PdfPCell[] pCellRight=new PdfPCell[4];
            Font fT=columnfont;
            Font fTN=datafont;
            pCellRight[0]=new PdfPCell(new Phrase("",titlefont));
            pCellRight[1]=new PdfPCell(new Phrase("",titlefont));
            pCellRight[2]=new PdfPCell(new Phrase("Parts PO",titlefont));
            PdfPTable tRight1=new PdfPTable(new float[]{1,1});
            PdfPCell[] pCellRight1=new PdfPCell[4];
            pCellRight1[0]=new PdfPCell(new Phrase("DATE :",fT));
            pCellRight1[1]=new PdfPCell(new Phrase(strHeader[iIndex++],fTN));
            pCellRight1[2]=new PdfPCell(new Phrase("P.O. NO. :",fT));
            pCellRight1[3]=new PdfPCell(new Phrase(strHeader[iIndex++],fTN));
            setTableNoborder(tRight1,pCellRight1);
            pCellRight[3]=new PdfPCell(tRight1);
            setTableNoborder(tRight,pCellRight);
            tHeader.addCell(tRight);
            ztb.settHeader(tHeader);
    		ztb.setHeaderHeight(120);

    		//columne header
    		
    		zzyPdfCell col0=new zzyPdfCell(false,"left","ITEM");
    		zzyPdfCell col1=new zzyPdfCell(false,"left","DESCRIPTION");
    		zzyPdfCell col2=new zzyPdfCell(false,"right","QTY");
    		zzyPdfCell col3=new zzyPdfCell(false,"right","Pick");
    		zzyPdfCell col4=new zzyPdfCell(false,"right","On B/O");
    		ztb.setColumn(new zzyPdfCell[]{col0,col1,col2,col3,col4});

    		
    		PdfPTable tfoot = new PdfPTable(new float[] {1});
    		PdfPCell[] cfoot=new PdfPCell[2];

    		cfoot[0]=new PdfPCell(new Phrase("  REMARKS:",columnfont));
    		cfoot[0].setHorizontalAlignment(Element.ALIGN_LEFT);

    		cfoot[1]=new PdfPCell(new Phrase("    "+strHeader[iIndex++],titlefont));
    		cfoot[1].setHorizontalAlignment(Element.ALIGN_LEFT);
    		setTableNoborder(tfoot,cfoot);
            ztb.settFoot(tfoot);
    		ztb.setFootHeight(80);
    		String[][] strBody1=new String[strBody.length][5];
    		for(int i=0;i<strBody.length;i++){
    			String[] si=strBody[i];
    			String[] si1=new String[5];
    			for(int j=0;j<5;j++)si1[j]="";
    			for(int j=0;j<si.length;j++){
    				String sj=si[j];
    				si1[j]=sj;
    			}
    			strBody1[i]=si1;
    		}
    		strBody=strBody1;
    		ztb.setBody(strBody);
    			
    		//bottom
    		//body    	
    		

    	}else if(rptname.equals("parttransinplan")){
    		ztb.setHeaderFloat(new float[] { 12, 3,15,3});

    		PdfPTable tHeader = new PdfPTable(new float[] { 5});
    		tHeader.setWidthPercentage(100f);
    		tHeader.getDefaultCell().setUseAscender(true);
    		tHeader.getDefaultCell().setUseDescender(true);
    		
            PdfPCell[] cellHeader=new PdfPCell[2];
            cellHeader[0]=new PdfPCell(new Phrase("Container In ["+strHeader[2]+"]",titlefont));
            cellHeader[0].setHorizontalAlignment(Element.ALIGN_LEFT);
            cellHeader[1]=getBarcodeCell(cb,strHeader[iIndex++]);
            PdfPTable tH=new PdfPTable(new float[]{1});
            setTableNoborder(tH,cellHeader);
            PdfPCell cell=new PdfPCell(tH);            
            cell.setFixedHeight(80);
            tHeader.addCell(cell);
            ztb.settHeader(tHeader);
    		ztb.setHeaderHeight(80);
    		//columne header
    		//zzyPdfCell col0=new zzyPdfCell(false,"left","Item Code");
    		zzyPdfCell col0=new zzyPdfCell(true,"left","ITEM/PART NO",25,25,"qrorlabel");
    		
    		zzyPdfCell col1=new zzyPdfCell(false,"right","Ship In");
    		zzyPdfCell col2=new zzyPdfCell(false,"left","Note");
    		zzyPdfCell col3=new zzyPdfCell(false,"center","Inspect");
    		ztb.setColumn(new zzyPdfCell[]{col0,col1,col2,col3});
            //foot
    		PdfPTable tfoot = new PdfPTable(new float[] {1});
    		PdfPCell[] cfoot=new PdfPCell[1];
    		cfoot[0]=new PdfPCell(new Phrase(""+strHeader[iIndex++],columnfont));
    		cfoot[0].setHorizontalAlignment(Element.ALIGN_LEFT);
    		setTableNoborder(tfoot,cfoot);
            ztb.settFoot(tfoot);
    		ztb.setFootHeight(10);
    		//body
    		ztb.setBody(strBody); 
    	}else if(rptname.equals("printpartlabel")){
    		ztb.setHeaderFloat(new float[] { 5,1});
            // header
    		PdfPTable tHeader = new PdfPTable(new float[] { 5,15});
    		tHeader.getDefaultCell().setBorder(Rectangle.NO_BORDER);
            
            tHeader.addCell(new Phrase("SHIP TO:"));
            tHeader.addCell(new Phrase(strHeader[iIndex++],titlefont));
            tHeader.addCell(new Phrase(""));
            tHeader.addCell(new Phrase(strHeader[iIndex++],new Font(FontFamily.HELVETICA, 15)));
            tHeader.addCell(new Phrase(""));
            tHeader.addCell(new Phrase(strHeader[iIndex++]+","+strHeader[iIndex++]+" "+strHeader[iIndex++],new Font(FontFamily.HELVETICA, 15)));
            tHeader.addCell(new Phrase("PO NUMBER:"+strHeader[iIndex++]));
            tHeader.addCell(new Phrase("Date:"+strHeader[iIndex++]));
            ztb.settHeader(tHeader);
    		ztb.setHeaderHeight(280);

    		//columne header
    		
    		zzyPdfCell col0=new zzyPdfCell(false,"left","ITEM");
    		zzyPdfCell col1=new zzyPdfCell(false,"right","QTY");
    		ztb.setColumn(new zzyPdfCell[]{col0,col1});
    		PdfPTable tfoot = new PdfPTable(new float[] {1});
    		PdfPCell[] cfoot=new PdfPCell[1];

    		cfoot[0]=new PdfPCell(new Phrase(strHeader[iIndex++]));
    		setTableNoborder(tfoot,cfoot);
            ztb.settFoot(tfoot);
    		ztb.setFootHeight(10);
    		ztb.setBody(strBody);        		
    	}else if(rptname.equals("reservepo")){
    		ztb.setHeaderFloat(new float[] { 10, 5,20,3});

    		PdfPTable tHeader = new PdfPTable(new float[] { 5});
    		tHeader.setWidthPercentage(100f);
    		tHeader.getDefaultCell().setUseAscender(true);
    		tHeader.getDefaultCell().setUseDescender(true);
    		
            PdfPCell[] cellHeader=new PdfPCell[3];
            cellHeader[0]=new PdfPCell(new Phrase("Reserve Ticket",titlefont));
            cellHeader[0].setHorizontalAlignment(Element.ALIGN_LEFT);
            cellHeader[1]=getBarcodeCell(cb,strHeader[iIndex++]);
            cellHeader[2]=new PdfPCell(new Phrase("From:"+strHeader[iIndex++],new Font(FontFamily.HELVETICA, 16,Font.BOLD)));
            PdfPTable tH=new PdfPTable(new float[]{1});
            setTableNoborder(tH,cellHeader);
            PdfPCell cell=new PdfPCell(tH);            
            cell.setFixedHeight(100);
            tHeader.addCell(cell);
            ztb.settHeader(tHeader);
    		ztb.setHeaderHeight(100);
    		//columne header
    		zzyPdfCell col0=new zzyPdfCell(false,"left","Item Code");
    		zzyPdfCell col1=new zzyPdfCell(false,"center","Action");
    		zzyPdfCell col2=new zzyPdfCell(false,"left","Note");
    		zzyPdfCell col3=new zzyPdfCell(false,"right","Qty");
    		ztb.setColumn(new zzyPdfCell[]{col0,col1,col2,col3});
            //foot
    		PdfPTable tfoot = new PdfPTable(new float[] {1});
    		PdfPCell[] cfoot=new PdfPCell[1];
    		cfoot[0]=new PdfPCell(new Phrase("Comments:"+strHeader[iIndex++],columnfont));
    		cfoot[0].setHorizontalAlignment(Element.ALIGN_LEFT);
    		setTableNoborder(tfoot,cfoot);
            ztb.settFoot(tfoot);
    		ztb.setFootHeight(40);
    		//body
    		ztb.setBody(strBody); 
    	}else if(rptname.equals("rma")){
    		ztb.setHeaderFloat(new float[] { 10,25,5,3,5,10,20});
    		PdfPTable tHeader = new PdfPTable(new float[] { 5});
    		tHeader.setWidthPercentage(100f);
    		tHeader.getDefaultCell().setUseAscender(true);
    		tHeader.getDefaultCell().setUseDescender(true);
    		
            PdfPCell[] cellHeader=new PdfPCell[10];
            cellHeader[0]=new PdfPCell(new Phrase("RMA",titlefont));
            cellHeader[0].setHorizontalAlignment(Element.ALIGN_LEFT);
            cellHeader[1]=getBarcodeCell(cb,strHeader[iIndex++]);
            cellHeader[2]=new PdfPCell(new Phrase("ISSUE DATE: "+strHeader[iIndex++],columnfont));
            cellHeader[3]=new PdfPCell(new Phrase("FROM:",columnfont));
            cellHeader[4]=new PdfPCell(new Phrase("RMA COMMENTS:",columnfont));
            cellHeader[5]=new PdfPCell(new Phrase(strHeader[iIndex++],new Font(FontFamily.HELVETICA, 10,Font.NORMAL)));
            cellHeader[6]=new PdfPCell(new Phrase(strHeader[iIndex+2],new Font(FontFamily.HELVETICA, 10,Font.NORMAL)));
            cellHeader[7]=new PdfPCell(new Phrase(strHeader[iIndex++],new Font(FontFamily.HELVETICA, 10,Font.NORMAL)));
            cellHeader[8]=new PdfPCell(new Phrase("",new Font(FontFamily.HELVETICA, 10,Font.NORMAL)));
            cellHeader[9]=new PdfPCell(new Phrase(strHeader[iIndex++],new Font(FontFamily.HELVETICA, 10,Font.NORMAL)));
            PdfPTable tH=new PdfPTable(new float[]{5,5});
            setTableNoborder(tH,cellHeader);
            PdfPCell cell=new PdfPCell(tH);            
            cell.setFixedHeight(100);
            tHeader.addCell(cell);
            ztb.settHeader(tHeader);
    		ztb.setHeaderHeight(100);
    		//columne header
    		zzyPdfCell col0=new zzyPdfCell(false,"left","Model#");
    		zzyPdfCell col1=new zzyPdfCell(false,"right","Description");
    		zzyPdfCell col2=new zzyPdfCell(false,"right","Price");
    		zzyPdfCell col3=new zzyPdfCell(false,"right","Qty");
    		zzyPdfCell col4=new zzyPdfCell(false,"right","Total");
    		zzyPdfCell col5=new zzyPdfCell(false,"left","PO No");
    		zzyPdfCell col6=new zzyPdfCell(false,"left","Reason");
    		ztb.setColumn(new zzyPdfCell[]{col0,col1,col2,col3,col4,col5,col6});
            //foot
    		PdfPTable tfoot = new PdfPTable(new float[] {1});
    		PdfPCell[] cfoot=new PdfPCell[1];
    		cfoot[0]=new PdfPCell(new Phrase(strHeader[iIndex+1],columnfont));
    		cfoot[0].setHorizontalAlignment(Element.ALIGN_LEFT);
    		setTableNoborder(tfoot,cfoot);
            ztb.settFoot(tfoot);
    		ztb.setFootHeight(40);
    		//body
    		ztb.setBody(strBody);   
    		
    	}else if(rptname.equals("rptorder") || rptname.equals("rptorderprint")  || rptname.equals("rptorderprintnosum")){
    		
    		ztb.setHeaderFloat(new float[] { 3, 10,4, 23, 5, 7, 5, 5 });
            // header
    		PdfPTable tHeader = new PdfPTable(new float[] { 5, 15});
    		tHeader.setWidthPercentage(100f);
    		tHeader.getDefaultCell().setUseAscender(true);
    		tHeader.getDefaultCell().setUseDescender(true);
    		
            Font f=new Font(FontFamily.HELVETICA, 20, Font.BOLD);
            PdfPCell[] cellHeader=new PdfPCell[2];
            cellHeader[0]=new PdfPCell(new Phrase(strHeader[iIndex++],f));
            cellHeader[0].setHorizontalAlignment(Element.ALIGN_CENTER);
            String orderno=strHeader[iIndex++];
            cellHeader[1]=getBarcodeCell(cb,orderno);
            PdfPTable tH=new PdfPTable(new float[]{1});
            setTableNoborder(tH,cellHeader);
            PdfPCell cell=new PdfPCell(tH);            
            cell.setFixedHeight(80);
            cell.setBorder(Rectangle.NO_BORDER);
            tHeader.addCell(cell);
            Font shiptofontbold=new Font(FontFamily.HELVETICA, 14, Font.BOLD);
            Font shiptofont=new Font(FontFamily.HELVETICA, 11);
            
            PdfPTable tShipto=new PdfPTable(new float[]{1});
            tShipto.getDefaultCell().setBorder(Rectangle.NO_BORDER);
            
            PdfPCell celltShipto=new PdfPCell(new Phrase(strHeader[iIndex++],shiptofontbold));

            celltShipto.setBorder(Rectangle.NO_BORDER);
            tShipto.addCell(celltShipto);
            PdfPCell celltShiptoCompany=new PdfPCell(new Phrase(strHeader[iIndex++],shiptofont));
            celltShiptoCompany.setBorder(Rectangle.NO_BORDER);
            tShipto.addCell(celltShiptoCompany);
            //System.out.println("pdfHeaderFooter strHeader[iIndex] is "+strHeader[iIndex]);
            PdfPCell celltShiptoAddress=new PdfPCell(new Phrase(strHeader[iIndex++],shiptofont));
            celltShiptoAddress.setBorder(Rectangle.NO_BORDER);
            tShipto.addCell(celltShiptoAddress);
            PdfPCell celltShiptoAddress1=new PdfPCell(new Phrase(strHeader[iIndex++],shiptofont));
            celltShiptoAddress1.setBorder(Rectangle.NO_BORDER);
            tShipto.addCell(celltShiptoAddress1);
            String partpono=strHeader[iIndex++];
            String partpono1=strHeader[iIndex++];
            if(partpono1.length()>0){
            	String pono=strHeader[9];
            	if(partpono1.indexOf("99")==0){
            		partpono+="                                                 Part PO#:["+partpono1+"]";
            	}else{
                	if(pono.toLowerCase().indexOf("-p")>0){
                		partpono+="                                                 Main PT#:["+partpono1+"]";	
                	}else{
                		//if is part
                		if(partpono1.toLowerCase().indexOf("-p")>0)
                		    partpono+="                                                 Part PT#:["+partpono1+"]";
                		else
                			partpono+="                                                 Part PO#:["+partpono1+"]";
                	}
            		
            	}
            		
            }
            PdfPCell celltShiptoAddress2=new PdfPCell(new Phrase(partpono,shiptofont));
            celltShiptoAddress2.setBorder(Rectangle.NO_BORDER);
            tShipto.addCell(celltShiptoAddress2);

            
            PdfPCell cellshipto = new PdfPCell(tShipto);
            cellshipto.setHorizontalAlignment(Element.ALIGN_LEFT);
            cellshipto.setFixedHeight(80);
            cellshipto.setBorder(Rectangle.NO_BORDER);
            tHeader.addCell(cellshipto);
            
            f.setSize(12);
            PdfPCell celldate = new PdfPCell(new Phrase(strHeader[iIndex++],f));
            celldate.setHorizontalAlignment(Element.ALIGN_CENTER);
            celldate.setVerticalAlignment(Element.ALIGN_MIDDLE);
            //cellshipto.setFixedHeight(50);
            tHeader.addCell(celldate);

            PdfPTable tMisc=new PdfPTable(new float[]{2,2,3});
            tMisc.getDefaultCell().setBorder(Rectangle.NO_BORDER);
            Font miscfontbold=new Font(FontFamily.HELVETICA, 11, Font.BOLD);
            Font miscfont=new Font(FontFamily.HELVETICA, 11);

            PdfPCell tMCust=new PdfPCell(new Phrase("CUSTOMER P.O.",miscfontbold));
            tMCust.setBorder(Rectangle.RIGHT);
            tMCust.setHorizontalAlignment(Element.ALIGN_CENTER);
            tMisc.addCell(tMCust);
            PdfPCell tMSHIP=new PdfPCell(new Phrase("SHIP",miscfontbold));
            tMSHIP.setBorder(Rectangle.RIGHT);
            tMSHIP.setHorizontalAlignment(Element.ALIGN_CENTER);
            tMisc.addCell(tMSHIP);
            PdfPCell tMSHIPVIA=new PdfPCell(new Phrase("SHIP VIA",miscfontbold));
            tMSHIPVIA.setBorder(Rectangle.NO_BORDER);
            tMSHIPVIA.setHorizontalAlignment(Element.ALIGN_CENTER);
            tMisc.addCell(tMSHIPVIA);
            PdfPCell tMC1=new PdfPCell(new Phrase(strHeader[iIndex++],miscfont));
            tMC1.setBorder(Rectangle.RIGHT);
            tMC1.setHorizontalAlignment(Element.ALIGN_CENTER);
            tMisc.addCell(tMC1);        
            PdfPCell tMC2=new PdfPCell(new Phrase(strHeader[iIndex++],miscfont));
            tMC2.setBorder(Rectangle.RIGHT);
            tMC2.setHorizontalAlignment(Element.ALIGN_CENTER);
            tMisc.addCell(tMC2);
            PdfPCell tMC3=new PdfPCell(new Phrase(strHeader[iIndex++],miscfont));
            tMC3.setBorder(Rectangle.NO_BORDER);
            tMC3.setHorizontalAlignment(Element.ALIGN_CENTER);
            tMisc.addCell(tMC3);
            
            tHeader.addCell(tMisc);
            ztb.settHeader(tHeader);
    		ztb.setHeaderHeight(130);
    		//columne header
    		zzyPdfCell col0=new zzyPdfCell(false,"center","NS");

    		zzyPdfCell col1=new zzyPdfCell(false,"left","ITEM/PART NO.");
    		if(rptname.indexOf("rptorderprint")>-1)col1=new zzyPdfCell(true,"left","ITEM/PART NO",25,25,"qrorlabel");
    		zzyPdfCell col10=new zzyPdfCell(false,"center","GROUP");
    		zzyPdfCell col2=new zzyPdfCell(false,"left","DESCRIPTION");

    		zzyPdfCell col3=new zzyPdfCell(false,"right","ORDER");
    		zzyPdfCell col4=new zzyPdfCell(false,"right","PICK");
    		zzyPdfCell col5=new zzyPdfCell(false,"right","On B/O");
    		zzyPdfCell col6=new zzyPdfCell(false,"left","");
    		ztb.setColumn(new zzyPdfCell[]{col0,col1,col10,col2,col3,col4,col5,col6});
            //foot
    		PdfPTable tfoot = new PdfPTable(new float[] {1});
    		tfoot.setWidthPercentage(100f);
    		tfoot.getDefaultCell().setUseAscender(true);
    		tfoot.getDefaultCell().setUseDescender(true);
            Font afootf=new Font();
            afootf.setSize(14);
            PdfPTable tcomment=new PdfPTable(new float[]{1,2});
            tcomment.addCell(new PdfPCell(new Phrase(strHeader[iIndex++],new Font(FontFamily.HELVETICA, 20, Font.BOLD))));
            tcomment.addCell(new PdfPCell(new Phrase(strHeader[iIndex++],afootf)));
            //Phrase afoot1=new Phrase(strHeader[iIndex++],afootf);
            //PdfPCell cellfoot1 = new PdfPCell(afoot1);
            PdfPCell cellfoot1 = new PdfPCell(tcomment);
            
            cellfoot1.setHorizontalAlignment(Element.ALIGN_LEFT);
            cellfoot1.setFixedHeight(90);
            tfoot.addCell(cellfoot1);
            Phrase afoot2=new Phrase("PICKED BY:_________  CHECKED BY:_________ PACKED BY:_________",afootf);
            PdfPCell cellfoot2 = new PdfPCell(afoot2);
            cellfoot2.setHorizontalAlignment(Element.ALIGN_LEFT);
            cellfoot2.setFixedHeight(30);
            //cellfoot2.setBorder(Rectangle.BOX);
            tfoot.addCell(cellfoot2);
            ztb.settFoot(tfoot);
    		ztb.setFootHeight(120);
    		//body

    		ztb.setBody(strBody);        		
    	}else if(rptname.equals("rptorderpackslip")){
    		ztb.setHeaderFloat(new float[] { 4, 8, 3,2,3});
            // header
    		PdfPTable tHeader = new PdfPTable(new float[] { 15,1,15});
    		tHeader.getDefaultCell().setBorder(Rectangle.NO_BORDER);
    		PdfPCell cellCompany=new PdfPCell(getMyLogo(strHeader[iIndex++]));cellCompany.setBorder(Rectangle.NO_BORDER);
            tHeader.addCell(cellCompany);
            
            tHeader.addCell(new Phrase(""));
            PdfPTable tRight=new PdfPTable(new float[]{1});
            PdfPCell[] pCellRight=new PdfPCell[2];
            Font fT=columnfont;
            Font fTN=datafont;
            pCellRight[0]=new PdfPCell(new Phrase("Packing Slip",titlefont));
            PdfPTable tRight1=new PdfPTable(new float[]{1,1});
            PdfPCell[] pCellRight1=new PdfPCell[6];
            pCellRight1[0]=new PdfPCell(new Phrase("WAREHOUSE :",fT));
            pCellRight1[1]=new PdfPCell(new Phrase(strHeader[iIndex++],fTN));
            pCellRight1[2]=new PdfPCell(new Phrase("SHIP DATE :",fT));
            pCellRight1[3]=new PdfPCell(new Phrase(strHeader[iIndex++],fTN));
            pCellRight1[4]=new PdfPCell(new Phrase("P.SLIP NO. :",fT));
            pCellRight1[5]=new PdfPCell(new Phrase(strHeader[iIndex++],fTN));
            setTableNoborder(tRight1,pCellRight1);
            pCellRight[1]=new PdfPCell(tRight1);
            setTableNoborder(tRight,pCellRight);
            tHeader.addCell(tRight);

            tHeader.addCell(new Phrase(" ",fT));
            tHeader.addCell(new Phrase("",fT));
            tHeader.addCell(new Phrase("",fT));

            
            tHeader.addCell(new Phrase("BILL TO:",fT));
            tHeader.addCell(new Phrase("",fT));
            tHeader.addCell(new Phrase("SHIP TO:",fT));

            tHeader.addCell(new Phrase(strHeader[iIndex++],fTN));
            tHeader.addCell(new Phrase("",fTN));
            tHeader.addCell(new Phrase(strHeader[iIndex++],fTN));
            tHeader.addCell(new Phrase(strHeader[iIndex++],fTN));
            tHeader.addCell(new Phrase("",fTN));
            tHeader.addCell(new Phrase(strHeader[iIndex++],fTN));
            tHeader.addCell(new Phrase(strHeader[iIndex++],fTN));
            tHeader.addCell(new Phrase("",fTN));
            tHeader.addCell(new Phrase(strHeader[iIndex++],fTN));
            tHeader.addCell(new Phrase(strHeader[iIndex++],fTN));
            tHeader.addCell(new Phrase("",fTN));
            tHeader.addCell(new Phrase(strHeader[iIndex++]+"    "+strHeader[iIndex++],fTN));
            
            
            tHeader.addCell(new Phrase(" ",fT));
            tHeader.addCell(new Phrase("",fT));
            tHeader.addCell(new Phrase("",fT));

            
            PdfPTable tTitle=new PdfPTable(new float[]{1,1,1,1,1});
            PdfPCell[] pcTitle=new PdfPCell[10];
            Font cfont1=new Font(FontFamily.HELVETICA, 8, Font.NORMAL,BaseColor.WHITE);
            pcTitle[0]=new PdfPCell(new Phrase("ORDER DATE",cfont1));
            pcTitle[1]=new PdfPCell(new Phrase("CUSTOMER P.O.",cfont1));
            pcTitle[2]=new PdfPCell(new Phrase("INVOICE NUMBER",cfont1));
            pcTitle[3]=new PdfPCell(new Phrase("SHIP VIA",cfont1));
            pcTitle[4]=new PdfPCell(new Phrase("SALESPERSON",cfont1));
            pcTitle[0].setBackgroundColor(BaseColor.BLACK);
            pcTitle[1].setBackgroundColor(BaseColor.BLACK);
            pcTitle[2].setBackgroundColor(BaseColor.BLACK);
            pcTitle[3].setBackgroundColor(BaseColor.BLACK);
            pcTitle[4].setBackgroundColor(BaseColor.BLACK);
            
            pcTitle[5]=new PdfPCell(new Phrase(strHeader[iIndex++],fTN));
            pcTitle[6]=new PdfPCell(new Phrase(strHeader[iIndex++],fTN));
            pcTitle[7]=new PdfPCell(new Phrase(strHeader[iIndex++],fTN));
            pcTitle[8]=new PdfPCell(new Phrase(strHeader[iIndex++],fTN));
            pcTitle[9]=new PdfPCell(new Phrase(strHeader[iIndex++],fTN));
            for(int i=0;i<pcTitle.length;i++){
            	pcTitle[i].setHorizontalAlignment(Element.ALIGN_CENTER);
            	tTitle.addCell(pcTitle[i]);
            }
            PdfPCell cTitle=new PdfPCell(tTitle);
            cTitle.setColspan(3);
            tHeader.addCell(cTitle);
            ztb.settHeader(tHeader);
    		ztb.setHeaderHeight(230);

    		//columne header
    		
    		zzyPdfCell col0=new zzyPdfCell(false,"left","ITEM/PART NO.");
    		zzyPdfCell col1=new zzyPdfCell(false,"left","DESCRIPTION");
    		zzyPdfCell col2=new zzyPdfCell(false,"right","ORDERED");
    		zzyPdfCell col3=new zzyPdfCell(false,"right","SHIPPED");
    		zzyPdfCell col4=new zzyPdfCell(false,"right","BACKORDER");
    		ztb.setColumn(new zzyPdfCell[]{col0,col1,col2,col3,col4});

    		
    		PdfPTable tfoot = new PdfPTable(new float[] {1});
    		PdfPCell[] cfoot=new PdfPCell[2];
    		PdfPTable tfoot0 = new PdfPTable(new float[] {9,1,6});
    		PdfPCell[] cfoot0=new PdfPCell[9];

    		/*PdfPTable tfoot1 = new PdfPTable(new float[] {1});
    		PdfPCell[] cfoot1=new PdfPCell[2];
    		
    		cfoot1[0]=new PdfPCell(new Phrase("    Packing Slip Comments:",columnfont));
    		cfoot1[0].setHorizontalAlignment(Element.ALIGN_LEFT);

    		cfoot1[1]=new PdfPCell(new Phrase("    "+strHeader[iIndex++],datafont));
    		cfoot1[1].setHorizontalAlignment(Element.ALIGN_LEFT);
    		cfoot1[1].setFixedHeight(60);
    		setTableNoborder(tfoot1,cfoot1);
    		cfoot0[0]=new PdfPCell(tfoot1);*/
    		cfoot0[0]=new PdfPCell(new Phrase(" ",columnfont));
    		cfoot0[1]=new PdfPCell(new Phrase(" ",columnfont));
    		cfoot0[2]=new PdfPCell(new Phrase(" ",columnfont));

    		
    		cfoot0[3]=getComment("COMMENTS",strHeader[iIndex++],60);
    		PdfPTable tfoot2 = new PdfPTable(new float[] {1});
    		PdfPCell[] cfoot2=new PdfPCell[6];
    		
    		cfoot2[0]=new PdfPCell(new Phrase("    ",columnfont));
    		cfoot2[0].setHorizontalAlignment(Element.ALIGN_LEFT);
    		cfoot2[0].setFixedHeight(10);
    		String cartoncnt=strHeader[iIndex++];
    		int charlength=30-cartoncnt.length();
    		for(int i=0;i<charlength;i++){
    			cartoncnt=" "+cartoncnt+" ";
    		}
   			cfoot2[1]=new PdfPCell(new Phrase("["+cartoncnt+"]",new Font(FontFamily.HELVETICA, 10,Font.UNDERLINE)));
    		cfoot2[1].setHorizontalAlignment(Element.ALIGN_LEFT);
    		//cfoot2[1].setFixedHeight(2);
    		cfoot2[2]=new PdfPCell(new Phrase("TOTAL SHIPMENT PIECE COUNT",columnfont));
    		cfoot2[2].setHorizontalAlignment(Element.ALIGN_LEFT);
    		
    		cfoot2[3]=new PdfPCell(new Phrase("    ",columnfont));
    		cfoot2[3].setHorizontalAlignment(Element.ALIGN_LEFT);
    		cfoot2[3].setFixedHeight(10);
    		cfoot2[4]=new PdfPCell(new Phrase("_____________________________",columnfont));
    		cfoot2[4].setHorizontalAlignment(Element.ALIGN_LEFT);
    		//cfoot2[4].setFixedHeight(2);
    		cfoot2[5]=new PdfPCell(new Phrase("DRIVER SIGNATURE",columnfont));
    		cfoot2[5].setHorizontalAlignment(Element.ALIGN_LEFT);

    		setTableNoborder(tfoot2,cfoot2);
    		cfoot0[4]=new PdfPCell(new Phrase(" ",columnfont));
    		cfoot0[5]=new PdfPCell(tfoot2);
    		cfoot0[6]=new PdfPCell(new Phrase(" ",columnfont));
    		cfoot0[7]=new PdfPCell(new Phrase(" ",columnfont));
    		cfoot0[8]=new PdfPCell(new Phrase(" ",columnfont));

    		setTableNoborder(tfoot0,cfoot0);
    		cfoot[0]=new PdfPCell(tfoot0);
    		cfoot[1]=getThanks();
    		//cfoot[1]=new PdfPCell(new Phrase("Thank you for your business.",new Font(FontFamily.HELVETICA, 9,Font.BOLD)));
    		//cfoot[1].setHorizontalAlignment(Element.ALIGN_CENTER);
    		setTableNoborder(tfoot,cfoot);
            ztb.settFoot(tfoot);
    		ztb.setFootHeight(118);
    		for(int i=0;i<strBody.length;i++){
    			String[] siA=strBody[i];
    			String sorderqty=siA[2];
    			String sshipqty=siA[3];
    			if(sorderqty!=null && sorderqty.length()>0){
    				if(sshipqty==null || sshipqty.length()<1){
    					siA[3]="0";
    					siA[4]=siA[2];
    				}
    				
    			}
    		}
    		ztb.setBody(strBody);
    			
    		//bottom
    		//body
    	}else if(rptname.equals("rptpdf")){
    		
    		String title=strHeader[iIndex++];
    		String s0=strHeader[iIndex++];
    		String[] s0A=s0.split(",");
    		float[] af=new float[s0A.length];
    		for(int i=0;i<s0A.length;i++){
    			af[i]=new Float(s0A[i]);
    		}

    		ztb.setHeaderFloat(af);
    		
    		String s01=strHeader[iIndex++];
    		String[] s01A=s01.split(",");
    		
    		
    		String s1=strHeader[iIndex++];
    		
    		PdfPTable tHeader = new PdfPTable(new float[] { 5});
    		tHeader.setWidthPercentage(100f);
    		tHeader.getDefaultCell().setUseAscender(true);
    		tHeader.getDefaultCell().setUseDescender(true);
    		
            PdfPCell[] cellHeader=new PdfPCell[1];
            cellHeader[0]=new PdfPCell(new Phrase(title,titlefont));
            cellHeader[0].setHorizontalAlignment(Element.ALIGN_LEFT);
            PdfPTable tH=new PdfPTable(new float[]{1});
            setTableNoborder(tH,cellHeader);
            PdfPCell cell=new PdfPCell(tH);            
            cell.setFixedHeight(50);
            cell.setBorder(Rectangle.NO_BORDER);
            tHeader.addCell(cell);
            ztb.settHeader(tHeader);
    		ztb.setHeaderHeight(50);
    		//columne header
    		//zzyPdfCell col0=new zzyPdfCell(false,"left","Item Code");
    		
    		zzyPdfCell[] cols=new zzyPdfCell[s0A.length];
    		String[] s1A=s1.split(",");
    		for(int i=0;i<s0A.length;i++){
    			cols[i]=new zzyPdfCell(false,s01A[i],s1A[i]);
    		}
    		ztb.setColumn(cols);
            //foot
    		PdfPTable tfoot = new PdfPTable(new float[] {1});
    		PdfPCell[] cfoot=new PdfPCell[1];
    		cfoot[0]=new PdfPCell(new Phrase("",columnfont));
    		cfoot[0].setHorizontalAlignment(Element.ALIGN_LEFT);
    		setTableNoborder(tfoot,cfoot);
            ztb.settFoot(tfoot);
    		ztb.setFootHeight(10);
    		//body
    		ztb.setBody(strBody);     		
    	}else if(rptname.equals("quote")){
    		ztb.setHeaderFloat(new float[] { 5, 10,8, 5,20});
            // header
    		PdfPTable tHeader = new PdfPTable(new float[] { 25,1,40});
    		tHeader.getDefaultCell().setBorder(Rectangle.NO_BORDER);
    		PdfPCell cellCompany=new PdfPCell(getMyLogo(strHeader[iIndex++]));
    		cellCompany.setBorder(Rectangle.NO_BORDER);
            tHeader.addCell(cellCompany);
            
            tHeader.addCell(new Phrase(""));
            PdfPTable tRight=new PdfPTable(new float[]{1});
            PdfPCell[] pCellRight=new PdfPCell[2];
            Font fT=columnfont;
            Font fTN=datafont;
            pCellRight[0]=new PdfPCell(new Phrase("Quote No.  "+strHeader[iIndex++],titlefont));
            pCellRight[0].setHorizontalAlignment(Element.ALIGN_RIGHT);
            PdfPTable tRight1=new PdfPTable(new float[]{5,6,16});
            String sdate=strHeader[iIndex++];
            String scompany=strHeader[iIndex++];
            String quotetype=strHeader[iIndex++];
            String jobname=strHeader[iIndex++];
            String sales=strHeader[iIndex++];
            
            PdfPCell[] pCellRight1=new PdfPCell[15];
            int i1index=0;
            pCellRight1[i1index++]=new PdfPCell(new Phrase("",fT));
            pCellRight1[i1index++]=new PdfPCell(new Phrase("",fT));
            pCellRight1[i1index++]=new PdfPCell(new Phrase("",fT));
            pCellRight1[i1index++]=new PdfPCell(new Phrase("",fT));
            pCellRight1[i1index++]=new PdfPCell(new Phrase("Date :",fT));
            pCellRight1[i1index++]=new PdfPCell(new Phrase(sdate,fTN));
            pCellRight1[i1index++]=new PdfPCell(new Phrase("",fT));
            pCellRight1[i1index++]=new PdfPCell(new Phrase("Company :",fT));
            pCellRight1[i1index++]=new PdfPCell(new Phrase(scompany,fTN));
            pCellRight1[i1index++]=new PdfPCell(new Phrase("",fT));
            if(quotetype.equals("Regular")){
                pCellRight1[i1index++]=new PdfPCell(new Phrase("Job Name :",fT));
                pCellRight1[i1index++]=new PdfPCell(new Phrase(jobname,fTN));
            	
            }else{
                pCellRight1[i1index++]=new PdfPCell(new Phrase("Quote Type :",fT));
                pCellRight1[i1index++]=new PdfPCell(new Phrase(quotetype,fTN));
            }
            pCellRight1[i1index++]=new PdfPCell(new Phrase("",fT));
            pCellRight1[i1index++]=new PdfPCell(new Phrase("Sales Rep :",fT));
            pCellRight1[i1index++]=new PdfPCell(new Phrase(sales,fTN));
            setTableNoborder(tRight1,pCellRight1);
            pCellRight[1]=new PdfPCell(tRight1);
            setTableNoborder(tRight,pCellRight);
            tHeader.addCell(tRight);

            ztb.settHeader(tHeader);
    		ztb.setHeaderHeight(130);

    		//columne header
    		
    		zzyPdfCell col0=new zzyPdfCell(false,"center","Qty");
    		zzyPdfCell col1=new zzyPdfCell(false,"center","Item Code");
    		zzyPdfCell col2=new zzyPdfCell(false,"center","STYLE NO.");
    		zzyPdfCell col3=new zzyPdfCell(false,"center","Unit Price");
    		zzyPdfCell col4=new zzyPdfCell(false,"center","Comments");
    		ztb.setColumn(new zzyPdfCell[]{col0,col1,col2,col3,col4});

    		
    		PdfPTable tfoot = new PdfPTable(new float[] {1});
    		PdfPCell[] cfoot=new PdfPCell[2];
    		PdfPTable tfoot0 = new PdfPTable(new float[] {1});
    		PdfPCell[] cfoot0=new PdfPCell[2];

    		PdfPTable tfoot1 = new PdfPTable(new float[] {24,1,18,1});
    		PdfPCell[] cfoot1=new PdfPCell[8];
    		cfoot1[0]=new PdfPCell(new Phrase("",datafont));
    		cfoot1[0].setFixedHeight(10);
    		cfoot1[1]=new PdfPCell(new Phrase("",datafont));
    		cfoot1[2]=new PdfPCell(new Phrase("",datafont));
    		cfoot1[3]=new PdfPCell(new Phrase("",datafont));
    		
    		cfoot1[4]=getComment("NOTES:",strHeader[iIndex++],60);
    		cfoot1[5]=new PdfPCell(new Phrase("",datafont));
    		
    		PdfPTable tfoot5 = new PdfPTable(new float[] {1});
    		PdfPCell[] cfoot5=new PdfPCell[2];
    		cfoot5[0]=new PdfPCell(new Phrase(strHeader[iIndex++],new Font(FontFamily.HELVETICA, 9,Font.BOLD)));
    		cfoot5[0].setFixedHeight(16);
    		cfoot5[0].setHorizontalAlignment(Element.ALIGN_CENTER);
    		cfoot5[0].setBorder(Rectangle.BOTTOM);
    		cfoot5[1]=new PdfPCell(new Phrase(""));
    		cfoot5[1].setBorder(Rectangle.NO_BORDER);
    		tfoot5.addCell(cfoot5[0]);
    		tfoot5.addCell(cfoot5[1]);
    		cfoot1[6]=new PdfPCell(tfoot5);
    		cfoot1[6].setHorizontalAlignment(Element.ALIGN_CENTER);
    		cfoot1[7]=new PdfPCell(new Phrase("",datafont));

    		//cfoot1[5].setFixedHeight(16);
    		
    		/*cfoot1[0].setBorder(Rectangle.NO_BORDER);tfoot1.addCell(cfoot1[0]);
    		cfoot1[1].setBorder(Rectangle.NO_BORDER);tfoot1.addCell(cfoot1[1]);
    		cfoot1[2].setBorder(Rectangle.NO_BORDER);tfoot1.addCell(cfoot1[2]);
    		cfoot1[3].setBorder(Rectangle.NO_BORDER);tfoot1.addCell(cfoot1[3]);
    		cfoot1[4].setBorder(Rectangle.NO_BORDER);tfoot1.addCell(cfoot1[4]);
    		cfoot1[5].setFixedHeight(16);
    		cfoot1[5].setBorder(Rectangle.BOTTOM);tfoot1.addCell(cfoot1[5]);*/
    		setTableNoborder(tfoot1,cfoot1);
    		cfoot0[0]=new PdfPCell(tfoot1);
    		cfoot0[1]=new PdfPCell(new Phrase(""));
    		cfoot0[1].setFixedHeight(10);
    		setTableNoborder(tfoot0,cfoot0);
    		
    		cfoot[0]=new PdfPCell(tfoot0);
    		
    		cfoot[1]=getThanks();

    		//cfoot[1].setHorizontalAlignment(Element.ALIGN_CENTER);
    		
    		setTableNoborder(tfoot,cfoot);
    		
            ztb.settFoot(tfoot);
            
    		ztb.setFootHeight(113);
    		
    		ztb.setBody(strBody);
    			    		
    	}else if(rptname.equals("warehouselocation")){
    		ztb.setHeaderFloat(new float[] {1, 1});
    		PdfPTable tHeader = new PdfPTable(new float[] { 5});
    		tHeader.setWidthPercentage(100f);
    		tHeader.getDefaultCell().setUseAscender(true);
    		tHeader.getDefaultCell().setUseDescender(true);
    		
            PdfPCell[] cellHeader=new PdfPCell[1];
            cellHeader[0]=new PdfPCell(new Phrase("Warehouse "+strHeader[iIndex++],titlefont));
            cellHeader[0].setHorizontalAlignment(Element.ALIGN_CENTER);
            PdfPTable tH=new PdfPTable(new float[]{5});
            setTableNoborder(tH,cellHeader);
            PdfPCell cell=new PdfPCell(tH);            
            cell.setFixedHeight(40);
            tHeader.addCell(cell);
            ztb.settHeader(tHeader);
    		ztb.setHeaderHeight(40);
    		//columne header
    		boolean isbarcode=false;
    		if(strHeader[0].equals("Virtual Skids"))isbarcode=true;
    		zzyPdfCell col0=new zzyPdfCell(isbarcode,"center","Name");
    		zzyPdfCell col1=new zzyPdfCell(isbarcode,"center","Name");
    		
    		ztb.setColumn(new zzyPdfCell[]{col0,col1});
            //foot
    		PdfPTable tfoot = new PdfPTable(new float[] {1});
    		PdfPCell[] cfoot=new PdfPCell[1];
    		cfoot[0]=new PdfPCell(new Phrase("",columnfont));
    		cfoot[0].setHorizontalAlignment(Element.ALIGN_LEFT);
    		setTableNoborder(tfoot,cfoot);
            ztb.settFoot(tfoot);
    		ztb.setFootHeight(10);
    		//body
    		ztb.setBody(strBody);   
    		
    	}
        //PdfPTable table = new PdfPTable(new float[] { 2, 1, 2, 5, 1, 3, 2 });
        PdfPTable table = new PdfPTable(ztb.getHeaderFloat());

        table.setWidthPercentage(100f);
        table.getDefaultCell().setUseAscender(true);
        table.getDefaultCell().setUseDescender(true);
        // Add the first header row
        //Font f = new Font();
        //f.setColor(BaseColor.WHITE);
        //header table
        
        //Phrase aphrase=new Phrase("",f);
        PdfPCell cell = new PdfPCell(ztb.gettHeader());
        //cell.setBackgroundColor(BaseColor.BLACK);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setColspan(ztb.getHeaderFloat().length);
        cell.setFixedHeight(ztb.getHeaderHeight());
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);
        // Add the second header row twice
        //table.getDefaultCell().setBackgroundColor(BaseColor.);
        zzyPdfCell[] sColumn=ztb.getColumn();
        if(sColumn==null)return table;
        Font cfont=new Font(FontFamily.HELVETICA, 8, Font.NORMAL,BaseColor.WHITE);
        for(int i=0;i<sColumn.length;i++){
        	PdfPCell cellColumn=new PdfPCell(new Phrase(sColumn[i].getsDesc().toUpperCase(),cfont));
        	cellColumn.setHorizontalAlignment(Element.ALIGN_CENTER);
        	cellColumn.setBackgroundColor(BaseColor.BLACK);
        	cellColumn.setFixedHeight(16);
        	table.addCell(cellColumn);
        }
        
        PdfPTable pdffoot=ztb.gettFoot();
        /*int rowStart=0;
        int rowEnd=5;
        float xPos=50;
        float yPos=500;
        pdffoot.setTotalWidth(500);
        pdffoot.writeSelectedRows(rowStart, rowEnd, xPos, yPos, cb);*/
        PdfPCell cellfoot = new PdfPCell(pdffoot);
        //cell.setBackgroundColor(BaseColor.BLACK);
        cellfoot.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellfoot.setColspan(ztb.getHeaderFloat().length);
        cellfoot.setFixedHeight(ztb.getFootHeight());
        cellfoot.setBorder(Rectangle.NO_BORDER);
        //cellfoot.setTop(800-ztb.getFootHeight());
        table.addCell(cellfoot);
        
       
        
        table.getDefaultCell().setBackgroundColor(null);
        // There are 2 special rows
        table.setHeaderRows(3);
        
        // One of them is a footer
        table.setFooterRows(1);
        // Now let's loop over the screenings
        
        String[][] sbody=ztb.getBody();
        
       // PdfPTable tablebody = new PdfPTable(ztb.getHeaderFloat());
        if(sbody!=null){
        	
        	boolean havegroup=false;
        	if(rptname.equals("rptpdf")){
            	for (int i=0;i<sbody.length;i++) {
                	String[] si=sbody[i];
                	if(si[0]!=null && Character.isWhitespace(si[0].charAt(0))){
                		havegroup=true;
                		break;
                	}
                }
        		
        	}
        	int fontsizedefault=10;
        	BaseColor bcdefault=new BaseColor(250,250,250);
        	boolean bColor=true;
        	for (int i=0;i<sbody.length;i++) {
            	String[] si=sbody[i];
            	int fontsize=fontsizedefault;
            	//if is sub, fontsize=9
            	if(havegroup && si[0]!=null && si[0].length()>0 && Character.isWhitespace(si[0].charAt(0))){
            		fontsize=8;	
            	}else if(havegroup && si[0]!=null && si[0].length()>0){
            		bColor=!bColor;
            	}
            	for(int j=0;j<sColumn.length;j++){
            		String sj=si[j];
            		if(sj==null)sj="";
            		PdfPCell cellj;
            		if(sColumn[j].getIsBarcode() && sj.length()>0){
            			/*if(sj.indexOf(",")>0){
            				String slabel=sj.substring(sj.indexOf(",")+1);
            				sj=sj.substring(0,sj.indexOf(","));
            				cellj=getBarcodeCell(cb,sj,sColumn[j].getfBarcodeWidth(),sColumn[j].getiBarcodeHeight(),sColumn[j].getsLabel());
            			}
            			else*/ 
            			cellj=getBarcodeCell(cb,sj,sColumn[j].getfBarcodeWidth(),sColumn[j].getiBarcodeHeight(),sColumn[j].getsLabel()+"("+sj+")");
            		}else if(sColumn[j].getIsImg()){
                        cellj=getImgCell(cb,sj);
            			
            		}else{
            			if(havegroup && fontsize==fontsizedefault){
            				cellj = new PdfPCell(new Phrase(sj,new Font(FontFamily.HELVETICA, fontsize,Font.BOLD)));
            				cellj.setBorder(Rectangle.TOP|Rectangle.LEFT|Rectangle.RIGHT);
            				
            			}else{
            				cellj = new PdfPCell(new Phrase(sj,new Font(FontFamily.HELVETICA, fontsize)));	
                			if(havegroup){
                				if(i==sbody.length-1){
                					cellj.setBorder(Rectangle.BOTTOM|Rectangle.LEFT|Rectangle.RIGHT);
                				}else cellj.setBorder(Rectangle.LEFT|Rectangle.RIGHT);	
                			}
            			}
            			if(havegroup){
            				
            				cellj.setBackgroundColor(bColor?bcdefault:BaseColor.WHITE);
            				
            			}
            			//cellj.setFixedHeight(14);
            		}
            		
            		cellj.setHorizontalAlignment(Element.ALIGN_CENTER);
            		cellj.setVerticalAlignment(Element.ALIGN_TOP);
            		if(sColumn[j].getsAlign().equals("left")){
            			if(!sColumn[j].getIsBarcode()){
            				cellj.setHorizontalAlignment(Element.ALIGN_LEFT);
            			}else{
            				boolean isqrcode=false;
            		    	boolean isbarcode=true;
            		    	String barcodedesc=sj;
            		    	String strDesc=barcodedesc;
            		    	String barcodelabel=sColumn[j].getsLabel()+"("+sj+")";
            		    	if(barcodedesc.indexOf("[")>0 && barcodedesc.indexOf("]")>barcodedesc.indexOf("[")){
            		    		strDesc=barcodedesc.substring(0,barcodedesc.indexOf("["));
            		    		barcodedesc=barcodedesc.substring(barcodedesc.indexOf("[")+1,barcodedesc.indexOf("]"));
            		    	}
            		    	if(barcodelabel.indexOf("qrcode")==0){isqrcode=true;}
            		    	if(barcodelabel.indexOf("qrorlabel")==0){
            		    		if(!barcodedesc.equals(strDesc)){
            		    			//isqrcode=true;
            		    			//showqrandcodelabel=true;
            		    		}else{ isqrcode=false;isbarcode=false;}
            		    	}
            		    	if(!isbarcode && !isqrcode)cellj.setHorizontalAlignment(Element.ALIGN_LEFT);
            			}
            				
            		}
            		else if(sColumn[j].getsAlign().equals("right"))cellj.setHorizontalAlignment(Element.ALIGN_RIGHT);
            		table.addCell(cellj);
            		
            	}
            }
        }
       // table.addCell(tablebody);
        
        return table;
    }
    public static PdfPCell getThanks(){
    	PdfPCell pc=new PdfPCell(new Phrase("Thank you for your business.",new Font(FontFamily.HELVETICA, 9,Font.BOLD)));
		pc.setBackgroundColor(BaseColor.LIGHT_GRAY);
		pc.setHorizontalAlignment(Element.ALIGN_CENTER);
		pc.setFixedHeight(16);
		return pc;
	}
    public static PdfPCell getComment(String title,String content,int iheight){
    	PdfPTable table=new PdfPTable(new float[]{1});
    	PdfPCell[] acell=new PdfPCell[2];
        acell[0]=new PdfPCell(new Phrase(title,new Font(FontFamily.HELVETICA, 9, Font.NORMAL,BaseColor.WHITE)));
        acell[0].setBackgroundColor(BaseColor.BLACK);
        acell[0].setHorizontalAlignment(Element.ALIGN_CENTER);
        acell[0].setFixedHeight(16);
		acell[1]=new PdfPCell(new Phrase("  "+content,new Font(FontFamily.HELVETICA, 9)));
		acell[1].setHorizontalAlignment(Element.ALIGN_LEFT);
		acell[1].setFixedHeight(iheight);
		table.addCell(acell[0]);
		table.addCell(acell[1]);
		return new PdfPCell(table);
	}
    public static PdfPTable getMyLogo(String mycompanyid){
    	return getMyLogo(mycompanyid,null);
    }
    public static PdfPTable getMyLogo(String mycompanyid,String[] companyinfo){
	    int cindex=0;
    	String companyname="";
    	String address=""; 
    	String city="";
    	String province="";
    	String postalcode="";
    	String countrycode="";
    	String phone="";
    	String fax=""; 
    	int companyinfolength=0;
	    if(companyinfo!=null){
	    	companyinfolength=companyinfo.length;
	    	companyname=companyinfo[cindex++];
	    	address=""; if(companyinfolength>cindex)address=companyinfo[cindex++];
	    	city=""; if(companyinfolength>cindex)city=companyinfo[cindex++];
	    	province=""; if(companyinfolength>cindex)province=companyinfo[cindex++];
	    	postalcode=""; if(companyinfolength>cindex)postalcode=companyinfo[cindex++];
	    	countrycode=""; if(companyinfolength>cindex)countrycode=companyinfo[cindex++];
	    	phone=""; if(companyinfolength>cindex)phone=companyinfo[cindex++];
	    	fax=""; if(companyinfolength>cindex)fax=companyinfo[cindex++];
	    }
		
    	PdfPTable table=new PdfPTable(new float[]{1});
    	/*Image img;
		try {
			img = Image.getInstance("C:/zhongzeyu/zhongzeyuproc/jboss/server/default/deploy/zhongzeyuWeb.war/Images/this/pdflogo.png");
			img.scalePercent(33);
			PdfPCell cell = new PdfPCell();
		      cell.addElement(new Chunk(img, 0, 0,true));
		      table.addCell(cell);
		      
		} catch (BadElementException e) {
			//  Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedURLException e) {
			//  Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			//  Auto-generated catch block
			e.printStackTrace();
		}
		return table;*/
    	cindex=5;
    	if(companyinfolength>0)cindex+=9;
    	PdfPCell[] acellCompany=new PdfPCell[cindex];
        cindex=0;
    	try {
    		//System.out.println(ZzyCommon.strBasicPath + "/Images/this/pdflogo.png");
			Image img = Image.getInstance(ZzyCommon.ZZYFilePath + "/Images/this/pdflogo.png");
			img.scalePercent(33);
			acellCompany[cindex] = new PdfPCell();
			acellCompany[cindex++].addElement(new Chunk(img, 0, 0,true));
		      
		} catch (BadElementException e) {
			//  Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedURLException e) {
			//  Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			//  Auto-generated catch block
			e.printStackTrace();
		}
        Font dFont=datafont;    	
        Font dFontBold=columnfont;    	
        String saddress=System.getProperty("defaultAddress");if(saddress==null)saddress="8432 157 Street";
        String scity=System.getProperty("defaultCity");if(scity==null)scity="Vancouver";
        String sState=System.getProperty("defaultState");if(sState==null)sState="BC";
        String sCountry=System.getProperty("defaultCountry");if(sCountry==null)sCountry="CA";
        String sPostcode=System.getProperty("myCompanyPostcode");if(sPostcode==null)sPostcode="V3Z 6M3";
        String sTel=System.getProperty("myCompanyTel");if(sTel==null)sTel="604.726.9260";
        String sFax=System.getProperty("myCompanyFax");if(sFax==null)sFax="604.538.7196";
        String sTol=System.getProperty("myCompanyTelFree");if(sTol==null)sTol="1.855.85K.UZCO(5.8926)";
        String sWeb=System.getProperty("myCompanyWeb");if(sWeb==null)sWeb="www..com";
        
        acellCompany[cindex++]=new PdfPCell(new Phrase(saddress+" "+scity+","+sState+" "+sCountry+" "+sPostcode,dFont));
        
		acellCompany[cindex++]=new PdfPCell(new Phrase(sWeb,dFont));
		acellCompany[cindex++]=new PdfPCell(new Phrase("",dFont));
		
		PdfPTable table12=new PdfPTable(new float[]{5,40});
	    PdfPCell[] acellCompany22=new PdfPCell[6];
	    acellCompany22[0]=new PdfPCell(new Phrase("Tel:",dFontBold));
	    acellCompany22[0].setHorizontalAlignment(Element.ALIGN_LEFT);
	    acellCompany22[1]=new PdfPCell(new Phrase(sTel,dFont));
	    acellCompany22[1].setHorizontalAlignment(Element.ALIGN_LEFT);
	    acellCompany22[2]=new PdfPCell(new Phrase("Fax:",dFontBold));
	    acellCompany22[2].setHorizontalAlignment(Element.ALIGN_LEFT);
	    acellCompany22[3]=new PdfPCell(new Phrase(sFax,dFont));
	    acellCompany22[3].setHorizontalAlignment(Element.ALIGN_LEFT);
	    acellCompany22[4]=new PdfPCell(new Phrase("Tol:",dFontBold));
	    acellCompany22[4].setHorizontalAlignment(Element.ALIGN_LEFT);
	    acellCompany22[5]=new PdfPCell(new Phrase(sTol,dFont));
	    acellCompany22[5].setHorizontalAlignment(Element.ALIGN_LEFT);
	    
	    setTableNoborder(table12,acellCompany22);
		
		acellCompany[cindex++]=new PdfPCell(table12);
		if(companyinfolength>0){
			acellCompany[cindex++]=new PdfPCell(new Phrase(" ",dFont));
			acellCompany[cindex++]=new PdfPCell(new Phrase(" ",dFont));
			acellCompany[cindex++]=new PdfPCell(new Phrase(" ",dFont));
			acellCompany[cindex++]=new PdfPCell(new Phrase(companyname,dFont));
			acellCompany[cindex++]=new PdfPCell(new Phrase("  "+address,dFont));
			String cityinfo="";
			if(city.length()>0)cityinfo=city;
			if(province.length()>0)cityinfo+=","+province;
			if(postalcode.length()>0)cityinfo+=" "+postalcode;
			acellCompany[cindex++]=new PdfPCell(new Phrase("  "+cityinfo,dFont));
			acellCompany[cindex++]=new PdfPCell(new Phrase("  "+countrycode,dFont));
			String phoneinfo="";
			if(phone.length()>0)phoneinfo="Phone: "+phone;
			if(fax.length()>0)phoneinfo+="     Fax: "+fax;
			acellCompany[cindex++]=new PdfPCell(new Phrase("  "+phoneinfo,dFont));
			acellCompany[cindex++]=new PdfPCell(new Phrase("  ",dFont));
		}
		

    	
    	setTableNoborder(table,acellCompany);
    	return table;
    }
    
    public static BufferedImage grayImage(final BufferedImage srcImg) {
        int iw = srcImg.getWidth();
        int ih = srcImg.getHeight();
        Graphics2D srcG = srcImg.createGraphics();
        RenderingHints rhs = srcG.getRenderingHints();
 
        ColorSpace cs = ColorSpace.getInstance(ColorSpace.CS_GRAY);
        ColorConvertOp theOp = new ColorConvertOp(cs, rhs);
        BufferedImage dstImg = new BufferedImage(iw, ih,
                BufferedImage.TYPE_INT_RGB);
 
        theOp.filter(srcImg, dstImg);
        return dstImg;
    }
    public static void setTableNoborder(PdfPTable table,PdfPCell[] cells){
    	table.getDefaultCell().setBorder(Rectangle.NO_BORDER);
    	for(int i=0;i<cells.length;i++){
    		PdfPCell ic=cells[i];
    		if(ic!=null){
    		 ic.setBorder(Rectangle.NO_BORDER);
    		 table.addCell(ic);
    		}else{
    			//System.out.println("cell "+i+" is null");
    		}
    	}
    }
    /**
     * Main method.
     * @param args no arguments needed
     * @throws DocumentException 
     * @throws IOException
     * @throws SQLException
     */
    public static PdfPTable getHeaderTable(int x, int y,boolean needtime,boolean needpage,String otherinfo) {
        PdfPTable table = new PdfPTable(new float[]{1});
        table.setTotalWidth(527);
        //table.setWidthPercentage(100f);
        /*table.setLockedWidth(true);
        table.getDefaultCell().setFixedHeight(20);
          
        //table.getDefaultCell().setBorder(Rectangle.BOTTOM);
        table.getDefaultCell().setBorder(Rectangle.NO_BORDER);
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
        */
        //table.addCell(String.format("Page %d of %d", x, y));
        String stitle="";
        if(needpage)stitle+="Page "+x+"of "+y;
        if(needtime){
        	
        	stitle+="["+ZzyCommon.getTodayShort()+":"+ZzyCommon.getTimeShort()+"]";
        }
        stitle+=otherinfo;
        
        PdfPCell pc=new PdfPCell(new Phrase(stitle,new Font(FontFamily.HELVETICA, 7)));
        //pc.setBorder(Rectangle.BOTTOM);
        pc.setBorder(Rectangle.NO_BORDER);
        pc.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(pc);
        
        return table;
   }
    public void outPdf(OutputStream out,String filename,String rptname,String[] strHeader,String[][] strBody){
    	outPdf(out,filename,rptname,strHeader,strBody,0,0,true);
    }
    public void outPdf(OutputStream out,String filename,String rptname,String[] strHeader,String[][] strBody,float ipagesizew,float ipagesizeh, boolean needtime){
    	Rectangle pagesize=null;
    	if(ipagesizew>0)pagesize=new Rectangle(ipagesizew,ipagesizeh);
        try {
 			try {
				new pdfHeaderFooter().createPdf(filename,rptname,strHeader,strBody,pagesize);
				PdfReader reader = new PdfReader(filename);
		        
		        // Create a stamper
		        // Loop over the pages and add a header to each page
				boolean needpage=false;
				String otherinfo="";
				if(needtime){
					otherinfo=" print by "+strHeader[strHeader.length-1];
				}
		        int n = reader.getNumberOfPages();
			        PdfStamper stamper= new PdfStamper(reader, out);
			        for (int i = 1; i <= n; i++) {
			            if(n>1)needpage=true;
			            getHeaderTable(i, n,needtime,needpage,otherinfo).writeSelectedRows(0, -50, 30, 813, stamper.getOverContent(i));
			        }
			        stamper.close();
			        //System.out.println("pdfHeaderFooter remove file "+filename);
			        reader.close();
			} catch (SQLException e) {
				//  Auto-generated catch block
				//e.printStackTrace();
			}
	        
		        
		} catch (DocumentException e) {
			//  Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			//  Auto-generated catch block
			e.printStackTrace();
		}

    	
    }
    private PdfPCell getBarcodeCell(PdfContentByte cb,String barcodedesc){
    	return getBarcodeCell(cb,barcodedesc,0,0,null);
    }
    private PdfPCell getBarcodeCell(PdfContentByte cb,String barcodedesc,float ibwidth,int ibheight,String barcodelabel){
    	String strDesc=barcodedesc;
    	Image imageEAN=null;
    	boolean isqrcode=false;
    	boolean haslabel=true;
    	boolean isbarcode=true;
    	boolean showqrandcodelabel=false;
    	if(barcodedesc.indexOf("[")>0 && barcodedesc.indexOf("]")>barcodedesc.indexOf("[")){
    		strDesc=barcodedesc.substring(0,barcodedesc.indexOf("["));
    		barcodedesc=barcodedesc.substring(barcodedesc.indexOf("[")+1,barcodedesc.indexOf("]"));
    	}
    	if(barcodelabel==null)barcodelabel=strDesc;
    	if(barcodelabel.indexOf("qrcode")==0){isqrcode=true;}
    	if(barcodelabel.indexOf("qrorlabel")==0){
    		if(!barcodedesc.equals(strDesc)){
    			//isqrcode=true;
    			//showqrandcodelabel=true;
    		}else{ isqrcode=false;isbarcode=false;}
    	}
    	//System.out.println("isqrcode is "+isqrcode);
    	if(isqrcode){
    		String swidth=ibwidth+"";
    		if(swidth.indexOf(".")>0)swidth=swidth.substring(0,swidth.indexOf("."));
    		int iwidth=new Integer(swidth).intValue();
    		 
            BarcodeQRCode qrcode = new BarcodeQRCode(barcodedesc, iwidth, ibheight, null);
            
            try {
				imageEAN = qrcode.getImage();
			} catch (BadElementException e) {
				//  Auto-generated catch block
				e.printStackTrace();
			}
    	}else if(!isbarcode){
    		
    	}else{
            Barcode128 code128 = new Barcode128();
            code128.setBaseline(12f);
            code128.setCode(barcodedesc);
            if(!code128.getCode().equals(barcodedesc)){
            	return null;
            }
            //System.out.println(code128.getSize());
            code128.setSize(14);
            BaseFont bf;
    		try {
    			bf = BaseFont.createFont(BaseFont.TIMES_BOLD, "Cp1252", false);
    	        code128.setFont(bf);
    		} catch (DocumentException e) {
    			//  Auto-generated catch block
    			e.printStackTrace();
    		} catch (IOException e) {
    			//  Auto-generated catch block
    			e.printStackTrace();
    		}
            code128.setAltText(barcodelabel);
            if(ibheight>0){
            	code128.setBarHeight(ibheight);
            	code128.setX(ibwidth);
            }else{
            	code128.setBarHeight(24);
            	code128.setX(0.8f);
            }
            if(barcodedesc.indexOf("691759")>=0){
            	//code128.setSize(28);
            	//code128.setX(3f);
            	code128.setBarHeight(50);
            	code128.setX(100);
            }
            if(barcodelabel.indexOf("qrorlabel")==0){
            	code128.setSize(6);
            	//code128.setFont(bf_helv);
            	if(barcodelabel.indexOf("(")>0)barcodelabel=barcodelabel.substring(barcodelabel.indexOf("(")+1);
            	if(barcodelabel.indexOf(")")>0)barcodelabel=barcodelabel.substring(0,barcodelabel.lastIndexOf(")"));
            	if(barcodelabel.indexOf("[")>0)barcodelabel=barcodelabel.substring(0,barcodelabel.indexOf("["));
            	code128.setAltText(barcodelabel);
            	code128.setBarHeight(18);
            	code128.setX(0.8f);
            }
            
                imageEAN = code128.createImageWithBarcode(cb, null, null);
                if(barcodedesc.indexOf("691759")>=0){
                	java.awt.Image awtImage=code128.createAwtImage(Color.BLACK,Color.WHITE);
                	BufferedImage bImage= new BufferedImage(102,40, BufferedImage.TYPE_INT_RGB);
                	Graphics2D g = bImage.createGraphics();
                	g.drawImage(awtImage, 0, 0, null);
                	g.dispose();

                	try {
                		ImageIO.write(bImage, "jpg", new File("c:/barcodecheck/barcodeimg/"+barcodedesc+".jpg"));
                		topdfbarcode("c:/barcodecheck/barcodeimgvertical/"+barcodedesc+".pdf",barcodedesc);
                	} catch (IOException e) {
                		e.printStackTrace();
                	}
                   /* code128.setSize(13);
            		try {
            			//bf = BaseFont.createFont(BaseFont.HELVETICA_BOLD, "Cp1252", false);
            			bf = BaseFont.createFont(BaseFont.HELVETICA, "Cp1252", false);
            	        code128.setFont(bf);
            		} catch (DocumentException e) {
            			e.printStackTrace();
            		} catch (IOException e) {
            			e.printStackTrace();
            		}
                    code128.setAltText(barcodedesc);
                    	code128.setBarHeight(50);
                    	code128.setX(1.5f);
    			
                	awtImage=code128.createAwtImage(Color.BLACK,Color.WHITE);
                	
                	bImage= new BufferedImage(102,40, BufferedImage.TYPE_INT_RGB);
                	
                	g = bImage.createGraphics();
                	g.drawImage(awtImage, 0, 0, null);
                	
                	g.dispose();

                	try {
                		ImageIO.write(bImage, "jpg", new File("c:/barcodecheck/barcodeimgvertical/"+barcodedesc+".jpg"));
                	} catch (IOException e) {
                		e.printStackTrace();
                	}

                    */
    			
            }
            
    	}

        PdfPCell cell;
        if(barcodelabel.indexOf("qrcodelabel")<0){haslabel=false;}
        if(showqrandcodelabel){haslabel=true;}
        if(!haslabel && isbarcode){
          cell = new PdfPCell(imageEAN,false);
        }else if(!isbarcode){
        	cell=new PdfPCell(new Phrase(strDesc,new Font(FontFamily.HELVETICA, 11)));
        }else{
        	PdfPTable table=new PdfPTable(new float[]{1});
        	table.getDefaultCell().setBorder(Rectangle.NO_BORDER);
        	//PdfPCell c1=new PdfPCell(imageEAN,false);
        	PdfPCell c1=new PdfPCell();
        	c1.setImage(imageEAN);
        	c1.setBorder(Rectangle.NO_BORDER);
            //c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            //c1.setVerticalAlignment(Element.ALIGN_BOTTOM);
            table.addCell(c1);
            
        	//PdfPCell c2=new PdfPCell(new Phrase(strDesc));
        	PdfPCell c2=new PdfPCell(new Phrase(strDesc,new Font(FontFamily.HELVETICA, 10,Font.BOLD)));
        	c2.setBorder(Rectangle.NO_BORDER);
            c2.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(c2);
        	cell=new PdfPCell(table);
			cell.setFixedHeight(ibheight+10);
        }
        //cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        //cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        
        return cell;
   }
    private PdfPCell getImgCell(PdfContentByte cb,String imgfile){
        PdfPCell cell=null;
		  String imgfilename="";
		  if(imgfile!=null && imgfile.length()>0){
			  String tmpimgname=imgfile;
			  if(tmpimgname.indexOf(".")>0){
				  String tmpsuffix=tmpimgname.substring(tmpimgname.lastIndexOf(".")+1).toLowerCase();
				  if(ZzyCommon.isInArray(new String[]{"png","jpg","gif","jpeg","bmp"},tmpsuffix)){
					  imgfilename=imgfile;	  
				  }
			  }
		  }
        if(imgfilename.length()<1){
        	if(imgfile!=null && imgfile.indexOf(".")>0){
        		String tmpsuffix=imgfile.substring(imgfile.lastIndexOf(".")+1);
        	Anchor anchor = new Anchor(tmpsuffix);
        	anchor.setReference(
        			"http://upload/"+imgfile.substring(imgfile.lastIndexOf("\\")+1)+"?"+ZzyCommon.getNow());
        		return new PdfPCell(anchor);
        	}else return new PdfPCell(new Phrase("")); 
        }
        try{
        Image imageEAN = Image.getInstance(imgfile);
        //imageEAN.scalePercent(15000f/imageEAN.getWidth());
        imageEAN.scaleToFit(150f,150f);
        if(imgfile.length()<1){
          cell = new PdfPCell(imageEAN,false);
          cell.setPadding(10);
        }else{
        	PdfPTable table=new PdfPTable(new float[]{1});
        	table.getDefaultCell().setBorder(Rectangle.NO_BORDER);
        	PdfPCell c1=new PdfPCell(imageEAN,false);
        	c1.setBorder(Rectangle.NO_BORDER);
            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            c1.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table.addCell(c1);
            
        	//PdfPCell c2=new PdfPCell(new Phrase(strDesc));
        	cell=new PdfPCell(table);
        }
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
    	} catch(Exception e){
    	      e.printStackTrace();
        }
    	cell.setFixedHeight(180);
        return cell;
   }
    private String filterinvalidstr(String strRtn){
  	  if(strRtn==null)return "";
		  strRtn=strRtn.replaceAll("MYADDPLUS", "+");
		  strRtn=strRtn.replaceAll("MYAND", "&");
		  strRtn=strRtn.replaceAll("MYRETURN", "\r");
		  strRtn=strRtn.replaceAll("MYNEWLINE", "\n");
		  strRtn=strRtn.replaceAll("mysplititem", "~");
		  strRtn=strRtn.replaceAll("mysplitline", "~");
		  strRtn = strRtn.replaceAll("<br>", "\r\n");
		  strRtn = strRtn.replaceAll("<br />", "\r\n");
		  return strRtn;
   }
    public static void manipulatePdf()
    throws IOException, DocumentException {
		String src="c:/temp/mytestbarcode.pdf";
		String dest="c:/temp/mytestbarcode1.pdf";
		String IMG="c:/temp/inventory.png";

    	PdfReader reader = new PdfReader(src);
    PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(dest));
    Image img = Image.getInstance(IMG);
    float x = 10;
    float y = 10;
    float w = img.getScaledWidth();
    float h = img.getScaledHeight();
    img.setAbsolutePosition(x, y);
    stamper.getOverContent(1).addImage(img);
    Rectangle linkLocation = new Rectangle(x, y, x + w, y + h);
    PdfDestination destination = new PdfDestination(PdfDestination.FIT);
    PdfAnnotation link = PdfAnnotation.createLink(stamper.getWriter(),
            linkLocation, PdfAnnotation.HIGHLIGHT_INVERT,
            reader.getNumberOfPages(), destination);
    link.setBorder(new PdfBorderArray(0, 0, 0));
    stamper.addAnnotation(link, 1);
    stamper.close();
}
    public static void test(){
    	try {
    		String src="c:/temp/mytestbarcode.pdf";
    		String dest="c:/temp/mytestbarcode1.pdf";
    		String barcode="10691759012115";
    		PdfReader reader = new PdfReader(src);
    	    PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(dest));

    	    
    	    Barcode128 code128 = new Barcode128();
            code128.setBaseline(12f);
            code128.setCode(barcode);
            //System.out.println(code128.getSize());
            code128.setSize(13);
            BaseFont bf;
    		try {
    			//bf = BaseFont.createFont(BaseFont.HELVETICA_BOLD, "Cp1252", false);
    			bf = BaseFont.createFont(BaseFont.HELVETICA, "Cp1252", false);
    	        code128.setFont(bf);
    		} catch (DocumentException e) {
    			e.printStackTrace();
    		} catch (IOException e) {
    			e.printStackTrace();
    		}
            code128.setAltText(barcode);
            	code128.setBarHeight(50);
            	code128.setX(1.5f);
            	
            	
            	Image  imageEAN = code128.createImageWithBarcode(stamper.getOverContent(1), null, null);
                
                java.awt.Image awtImage=code128.createAwtImage(Color.BLACK,Color.WHITE);
                BufferedImage bImage= new BufferedImage(102,40, BufferedImage.TYPE_INT_RGB);
                Graphics2D g = bImage.createGraphics();
                g.drawImage(awtImage, 0, 0, null);
                g.dispose();
                
                
    	    float x = 251;
    	    float y = 33;
    	    imageEAN.setAbsolutePosition(x, y);
    	    imageEAN.setRotationDegrees(90);
    	    
        	
        	try {
        		ImageIO.write(bImage, "jpg", new File("c:/barcodecheck/barcodeimgvertical/"+barcode+".jpg"));
        	} catch (IOException e) {
        		e.printStackTrace();
        	}

    	    
    	    stamper.getOverContent(1).addImage(imageEAN);
    	    
    	    stamper.close();
    	    reader.close();
		} catch (Exception e) {
			//  Auto-generated catch block
			e.printStackTrace();
		}
    }
    public static void topdfbarcode(String filename,String barcode){
    	try {
    		Document document = new Document(new Rectangle(70, 180), 5, 5, 5, 5);
            // step 2
            PdfWriter writer=PdfWriter.getInstance(document, new FileOutputStream(filename));
            document.open();
            PdfContentByte cb = writer.getDirectContent();
            
    		
    	    Barcode128 code128 = new Barcode128();
            code128.setBaseline(12f);
            code128.setCode(barcode);
            //System.out.println(code128.getSize());
            code128.setSize(15);
            BaseFont bf;
    		try {
    			//bf = BaseFont.createFont(BaseFont.HELVETICA_BOLD, "Cp1252", false);
    			//bf = BaseFont.createFont(BaseFont.HELVETICA, "Cp1252", false);
    			bf = BaseFont.createFont("c:/windows/fonts/arial.ttf", BaseFont.WINANSI, BaseFont.EMBEDDED);
    	        code128.setFont(bf);
    		} catch (DocumentException e) {
    			e.printStackTrace();
    		} catch (IOException e) {
    			e.printStackTrace();
    		}
            code128.setAltText(barcode);
            	code128.setBarHeight(50);
            	code128.setX(1.5f);
            	Image  imageEAN = code128.createImageWithBarcode(cb, null, null);
    	    float x = 5;
    	    float y = 5+(14-barcode.length())*3.8f;
    	    imageEAN.setAbsolutePosition(x, y);
    	    imageEAN.setRotationDegrees(90);
    	    document.add(imageEAN);
    	    document.close();
        	
    	    //cb.addImage(imageEAN);
		} catch (Exception e) {
			//  Auto-generated catch block
			e.printStackTrace();
		}
		//pdfReader.convertToImg(filepath+strPlus+".pdf",filepath+strPlus);
    }
    public static void main(String[] args)
        throws SQLException, DocumentException, IOException {
    	pdfHeaderFooter phf=new pdfHeaderFooter();
    	

    	//String strRptName="inspect";
    	//String strRptName="parttransinplan";
    	//String strRptName="rma";
    	//String strRptName="rptorderpackslip";
    	//String strRptName="barcodeitem";
    	//String strRptName="barcodeitemone";
    	//String strRptName="rptorder";
    	//String strRptName="rptorderprint";
    	//String strRptName="orderlabel";
        //String strRptName="reservepo";
       // String strRptName="invoicestatement";
    	//String strRptName="barcodepartbook";
        //String strRptName="barcodecomponent";
    	//String strRptName="barcodelocation";
    	//String strRptName="loctranadvice";
       // String strRptName="orderpartspo";
    	//String strRptName="invoice";
    	//String strRptName="erpinvoice";
        //String strRptName="unicode";
    	//String strRptName="printpartlabel";
    	String strRptName="quote";
    	//String strRptName="cheque";
       //String strRptName="partlabel";
    	//String strRptName="rptpdf";
    	//String strRptName="barcode128item";
    	//String strRptName="test";
    	//String strRptName="test1";
    	//String strRptName="shippinglabel";
    	
    	//String strRptName="barcodeitemvertical";
    	if(strRptName.equals("test")){
    		test();
    		return;
    	}else if(strRptName.equals("test1")){
    		String filepath="c:/temp";
    		String barcode="10691759012115";

    		topdfbarcode(filepath+"/"+barcode+".pdf",barcode);
    		barcode="691759012115";
    		topdfbarcode(filepath+"/"+barcode+".pdf",barcode);
    		return;
    	}
    	
    	File afile=new File("c:/temp/mytest1.pdf");afile.delete();
    	afile=new File("c:/temp/mytest.pdf");afile.delete();
		OutputStream out=new FileOutputStream("c:/temp/mytest1.pdf");
		  String[] strHeader=null;
		  String[][] strBody=null;

			if(strRptName.equals("barcodecomponent")){
				  strHeader=new String[]{"barcodecomponent","linus"};
				  strBody=new String[15][1];
				  for(int i=1;i<7;i++){
					  strBody[i]=new String[]{"LAB"+i+"[LAB"+i+"]"};
				  }
					phf.outPdf(out,"c:/temp/mytest.pdf",strRptName,strHeader,strBody,82*4,85*6,true);					
			}else if(strRptName.equals("barcodelocation")){
				  strHeader=new String[]{"barcodecomponent","linus"};
				  strBody=new String[10][1];
				  /*for(int i=1;i<11;i++){
					  strBody[i-1]=new String[]{"LAB"+(i*3-2)+"[LAB"+(i*3-2)+"]"
							  ,"LAB"+(i*3-1)+"[LAB"+(i*3-1)+"]"
							  ,"LAB"+(i*3-0)+"[LAB"+(i*3-0)+"]"
							  };
				  }*/
				  strBody[0]=new String[]{"EB3036_BK[691759024784]","EB1611-BK[691759023527]","EB1711-BK[691759023589]"};
				  strBody[1]=new String[]{"EB2836-BK[691759024746]","EB1611-BK[691759023527]","EB1711-BK[691759023589]"};
				  strBody[2]=new String[]{"EB3036-BK[691759024784]","EB1611-BK[691759023527]","EB1711-BK[691759023589]"};
				  strBody[3]=new String[]{"EB3036-BK[691759024784]","EB1611-BK[691759023527]","EB1711-BK[691759023589]"};
				  strBody[4]=new String[]{"EB3036-BK[691759024784]","EB1611-BK[691759023527]","EB1711-BK[691759023589]"};
				  
				  strBody[5]=new String[]{"EB3036-BK[691759024784]","EB1611-BK[691759023527]","EB1711-BK[691759023589]"};
				  strBody[6]=new String[]{"EB3036-BK[691759024784]","EB1611-BK[691759023527]","EB1711-BK[691759023589]"};
				  strBody[7]=new String[]{"EB2836-BK[691759024746]","EB1611-BK[691759023527]","EB1711-BK[691759023589]"};
				  strBody[8]=new String[]{"EB3036-BK[691759024784]","EB1611-BK[691759023527]","EB1711-BK[691759023589]"};
				  strBody[9]=new String[]{"EB3036-BK[691759024784]","EB1611-BK[691759023527]","EB1711-BK[691759023589]"};
					phf.outPdf(out,"c:/temp/mytest.pdf",strRptName,strHeader,strBody,82*4,85*6,true);					
			}else if(strRptName.equals("barcode128item")){
				  strHeader=new String[]{"barcodecomponent","linus"};
				  int irow=20;
				  strBody=new String[irow][11];
				  /*for(int i=1;i<11;i++){
					  strBody[i-1]=new String[]{"LAB"+(i*3-2)+"[LAB"+(i*3-2)+"]"
							  ,"LAB"+(i*3-1)+"[LAB"+(i*3-1)+"]"
							  ,"LAB"+(i*3-0)+"[LAB"+(i*3-0)+"]"
							  };
				  }*/
				  for(int i=0;i<irow;i++){
					  strBody[i]=new String[]{i+"EB3036-BK[691759024784]","$460.00","EB3036-BK[691759024784]","$460.00","EB3036-BK[691759024784]","$460.00","EB3036-BK[691759024784]","$460.00"};
				  }
				  phf.outPdf(out,"c:/temp/mytest.pdf",strRptName,strHeader,strBody,612,792,true);					
			}else if(strRptName.equals("barcodeitemvertical")){
				  strHeader=new String[]{"barcodecomponent","linus"};
				  int irow=2;
				  strBody=new String[irow][11];
				  /*for(int i=1;i<11;i++){
					  strBody[i-1]=new String[]{"LAB"+(i*3-2)+"[LAB"+(i*3-2)+"]"
							  ,"LAB"+(i*3-1)+"[LAB"+(i*3-1)+"]"
							  ,"LAB"+(i*3-0)+"[LAB"+(i*3-0)+"]"
							  };
				  }*/
				  for(int i=0;i<irow;i++){
					  strBody[i]=new String[]{i+"EB3036-BK[691759024784]","$460.00","EB3036-BK[691759024784]","$460.00","EB3036-BK[691759024784]","$460.00","EB3036-BK[691759024784]","$460.00"};
				  }
					phf.outPdf(out,"c:/temp/mytest.pdf",strRptName,strHeader,strBody,612,792,true);
					
			}else if(strRptName.equals("barcodeitem")){
				  strHeader=new String[]{"42154[box]","linus"};
				  strBody=new String[5][1];
					strBody[0]=new String[]{"206917590021959"};
					strBody[1]=new String[]{"691759002195"};
					strBody[2]=new String[]{"691759002195"};
					strBody[3]=new String[]{"691759002195"};
					strBody[4]=new String[]{"691759002195"};
					phf.outPdf(out,"c:/temp/mytest.pdf",strRptName,strHeader,strBody,82*4,85*6,true);					
			}else if(strRptName.equals("barcodeitemone")){
				  strHeader=new String[]{""};
				  strBody=new String[2][1];
					strBody[0]=new String[]{"691759002195"};
					strBody[1]=new String[]{"691759002185"};
					phf.outPdf(out,"c:/temp/mytest.pdf",strRptName,strHeader,strBody,82*4,85*6,true);					
					//phf.outPdf(out,"c:/temp/mytest.pdf",strRptName,strHeader,strBody,122*4,122*6);					
					//phf.outPdf(out,"c:/temp/mytest.pdf",strRptName,strHeader,strBody,new Rectangle(70*4,70*6));					
			}else if(strRptName.equals("barcodepartbook")){
				  strHeader=new String[]{"linus"};
				  strBody=new String[15][1];
				  for(int i=0;i<15;i++){
					  //strBody[i]=new String[]{"acomponent"+i,"acomponent desc "+i,i+"10"};
					  strBody[i]=new String[]{"2/52382 DIFFUSER[32432]","sdf sfddsaffdsfdsf "+i,"3/5825* GLASS CAP[3242]","dsafdsa dsafdsafsa"};
				  }
			}else if(strRptName.equals("cheque")){
				  //strHeader=new String[]{"2015-06-16","$","1,111,111,772.51","AGENTS N.D INC","2935 DONAT STREET","MONTREAL","QC","H1L 5K9","CA","1890"};
				  strHeader=new String[]{"2015-06-16","$","111,772.51","AGENTS N.D INC","2935 DONAT STREET","MONTREAL","QC","H1L 5K9","CA","1890"};
				  strBody=new String[1][5];
				  for(int i=0;i<strBody.length;i++){
					  //strBody[i]=new String[]{"acomponent"+i,"acomponent desc "+i,i+"10"};
					  strBody[i]=new String[]{"2015-02-28","2015-02 "+i,"11,772.51",".00","11,772.51"};
				  }
				  phf.outPdf(out,"c:/temp/mytest.pdf",strRptName,strHeader,strBody,612,792,true);
			}else if(strRptName.equals("inspect")){
				  strHeader=new String[]{"2719","inspectno","inspectby","inspectdate","pono","modelnumber","lamping","containernumber","quantityreceived","factory","quantityinspected"};
				  strBody=new String[3][4];
					strBody[0]=new String[]{"box condition:","Fail:1","box broken","C:/temp/2.jpg"};
					strBody[1]=new String[]{"box condition:","Fail:2","box broken","C:/temp/1.JPG"};
					strBody[2]=new String[]{"box condition:","Fail:2","box broken","C:/temp/beckyqiu.pdf"};
					
			}else if(strRptName.equals("erpinvoice")){
//				strHeader=new String[]{"2013-02-27","17279-0","ROYAUME LUMINAIRE LANAUDIERE","ROYAUME LUMINAIRE LA","3275 DE LA PINIERE","3275 DE LA PINIERE","TERREBONNE,QC J6X 4P7","TERREBONNE,QC J6X 4P",""                   ,"","(450) 477-8666","2013-02-27","RMA 1981","026427","Pick up from MTL","AGENTS ND" ,""};
				strHeader=new String[]{"2719","Invoice","2013-10-07","040287","00000139","US SKID","LIGHTING INCORPORATED","LIGHTING INC. AUSTIN","PO BOX 266556","10401 BURNET ROAD","HOUSTON,TX 77207","AUSTIN,TX 78758","USA","US","(713) 641-6628","BRANDEN STEWART","2013-10-03","23910-0","2013-10-07","451032","Net 30 Days","comment","-5.00","23.00","1.00","1.00","1.00","1.00","US$199,999.08","Promotion Discount"};
			  strBody=new String[51][7];
			  for(int i=0;i<strBody.length;i++){
				strBody[i]=new String[]{"444006"+i,""+i,"EA","8LT DRUM PNT,GLS ROD SHADE W/CRYS"+i,"268.200","268.20","10"};
			  }
			}else if(strRptName.equals("invoicestatement")){
				
				//strHeader=new String[]{"1","2014-01-29","CARRINGTON LIGHTING","2513 5TH AVE N.W","CALGARY","AB","T2N 0T4","CA","(403) 264-5483","(403) 264-5800","28,686.60","6,003.25",".00","150.26",".00","34,840.11","For copies of invoices, please contact "+System.getProperty("emailccinvoice").substring(0,System.getProperty("emailccinvoice").indexOf("@"))+" at 604-227-6635 or "+System.getProperty("emailccinvoice")+"."};
				strHeader=new String[]{"2719","CARRINGTON LIGHTING","2513 5TH AVE N.W","CALGARY","AB","T2N 0T4","CA","(403) 264-5483","(403) 264-5800","1","2014-01-29","28,686.60","6,003.25",".00","150.26",".00","34,840.11","For copies of invoices, please contact lisa or call lisa at 604-227-6635"};

				strBody=new String[51][6];
			  for(int i=0;i<strBody.length;i++){
				strBody[i]=new String[]{"2013-11-06"+i,"Invoice 04185"+i,"PO for 04185"+i,"150.26","150.26"};
			  }
			}else if(strRptName.equals("loctranadvice")){
				strHeader=new String[]{};
			  strBody=new String[51][5];
			  for(int i=0;i<strBody.length;i++){
				strBody[i]=new String[]{"sdfdsafdsfdsf"+i,"Regular","C11F","30","H10C H11B H11C","sdfdsafdsfdsf"+i,"Job Order","C11F","30","H10C H11B H11C"};
			  }
			}else if(strRptName.equals("orderlabel")){
				  strHeader=new String[]{"1/2","linus","25688-0","shiptoname","shiptoadderss","cty","st","v2a 9c2","Vitran","334324324324"};
				  strBody=new String[1][5];
					strBody[0]=new String[]{"l","w","h","w","c"};
				  phf.outPdf(out,"c:/temp/mytest.pdf",strRptName,strHeader,strBody,82*6,82*4,true);
			}else if(strRptName.equals("orderpartspo") || strRptName.equals("orderpartspooriginal")){
				  strHeader=new String[]{"2719","Oct 21 13","996083-0","UNIVERSAL LAMP 24397-0"};
				  strBody=new String[50][3];
				  for(int i=0;i<strBody.length;i++){
					strBody[i]=new String[]{"410108 STRAND"+i,i+"CRYSTAL STRAND FOR 410108","1"};
				  }
			}else if(strRptName.equals("partlabel")){
				//strHeader=new String[]{"PD12732","PD12732_label.png","Pendant","550.00","18 Light"};
				  strHeader=new String[]{"PD12732","shiplabel.png","Pendant","550.00","18 Light"};
				  strBody=new String[8][2];
					strBody[0]=new String[]{"LAMP TYPE","LED Board, 120V"};
					strBody[1]=new String[]{"DIMENSIONS","D21-3/5\" x H125-1/8\""};
					strBody[2]=new String[]{"INITIAL LUMENS","6500lm"};
					strBody[3]=new String[]{"DELIVERED LUMENS","2600lm*"};
					strBody[4]=new String[]{"FINISHED","BLACK-BK"};
					strBody[5]=new String[]{"","BRUSHED NICKEL - BN"};
					strBody[6]=new String[]{"","WHITE - WH"};
					strBody[7]=new String[]{"","WALNUT - WT"};
					phf.outPdf(out,"c:/temp/mytest.pdf",strRptName,strHeader,strBody,303,156,true);
			}else if(strRptName.equals("printpartlabel")){
				
				  strHeader=new String[]{"acompany name","address","city","province","postalcode","pono","orderdate","1/5"};
				  strBody=new String[15][2];
				  for(int i=0;i<strBody.length;i++){
					strBody[i]=new String[]{"itemname"+i,i+""};
				  }
			}else if(strRptName.equals("quote")){
				
				  strHeader=new String[]{"2719","Q-150430-C","May 11,2015","Ocean Pacific myand Lighting","Container","15-159 7670 Kingsway - OP","salesrep","notes dsfds fds dsfs dsdf dsfs dsf ","QUOTATION IS VALID FOR 180 DAYS"};
				  strBody=new String[50][5];
				  for(int i=0;i<strBody.length;i++){
					strBody[i]=new String[]{"qty"+i+"-"+"1111",i+"item code",i+"type","$108.00"+i,"comments "+i};
				  }
				  
			}else if(strRptName.equals("reservepo")){
//				strHeader=new String[]{"2013-02-27","17279-0","ROYAUME LUMINAIRE LANAUDIERE","ROYAUME LUMINAIRE LA","3275 DE LA PINIERE","3275 DE LA PINIERE","TERREBONNE,QC J6X 4P7","TERREBONNE,QC J6X 4P",""                   ,"","(450) 477-8666","2013-02-27","RMA 1981","026427","Pick up from MTL","AGENTS ND" ,""};
				strHeader=new String[]{"1_34324","acompany dsfjdsalkfjdsakfdsa","comment dafsajflkdsajflkdsajflkdsafkdsafajfs"};
			  strBody=new String[2][4];
				strBody[0]=new String[]{"42154","Reserve","A12A[1]","1"};
				strBody[1]=new String[]{"42156","Release","O12A[1]","-1"};
			}else if(strRptName.equals("rptorder")){
//				strHeader=new String[]{"2013-02-27","17279-0","ROYAUME LUMINAIRE LANAUDIERE","ROYAUME LUMINAIRE LA","3275 DE LA PINIERE","3275 DE LA PINIERE","TERREBONNE,QC J6X 4P7","TERREBONNE,QC J6X 4P",""                   ,"","(450) 477-8666","2013-02-27","RMA 1981","026427","Pick up from MTL","AGENTS ND" ,""};
				strHeader=new String[]{"Surrey","orderno","Ship To:[ RUSH ]","shiptoname","shiptoaddress","shiptocity,shiptostate shiptozipcode","shiptocountry","partpono","orderdate","pono-p","shipdate","carrier","pttype","comment"};
			  strBody=new String[20][7];
			  for(int i=0;i<10;i++){
				strBody[i]=new String[]{"N","31108B678901234567["+i+"]","grp","8LT CHD,BLK CH MTL,BLK SILK","3"+i,"1"+i,"2"+i,""};
			  }
			  for(int i=10;i<20;i++){
					strBody[i]=new String[]{"S","31108B678901234567","grp","8LT CHD,BLK CH MTL,BLK SILK","3"+i,"1"+i,"2"+i,""};
				  }
			}else if(strRptName.equals("rptorderprint")){
//				strHeader=new String[]{"2013-02-27","17279-0","ROYAUME LUMINAIRE LANAUDIERE","ROYAUME LUMINAIRE LA","3275 DE LA PINIERE","3275 DE LA PINIERE","TERREBONNE,QC J6X 4P7","TERREBONNE,QC J6X 4P",""                   ,"","(450) 477-8666","2013-02-27","RMA 1981","026427","Pick up from MTL","AGENTS ND" ,""};
				strHeader=new String[]{"Job Orders","orderno","Ship To:[ RUSH ]","shiptoname","\u4E2D\u56FD\u5317\u4EAC\u5E02\u4E30\u53F0\u533A\u5317\u4EAC\u5357\u7AD9\u8DEF\u5317\u4EAC\u5357\u4E2D\u56FD\u5317\u4EAC\u5E02\u4E30\u53F0\u533A\u5317\u4EAC\u5357\u7AD9\u8DEF\u5317\u4EAC\u5357","shiptocity,shiptostate shiptozipcode","shiptocountry","partpono","orderdate","pono-p","shipdate","carrier\r\nAN:dsfds","pttype","comment","linus"};
			  strBody=new String[20][7];
			  for(int i=0;i<10;i++){
				strBody[i]=new String[]{"N","440/1*** WIRE ASS[34343]","gr"+i,"8LT CHD,BLK CH MTL,BLK SILK","3"+i,"1"+i,"2"+i,""};
			  }
			  for(int i=10;i<15;i++){
					strBody[i]=new String[]{"S","31108B678901234567","gr"+i,"8LT CHD,BLK CH MTL,BLK SILK","3"+i,"1"+i,"2"+i,""};
				  }
			  strBody[10][0]="newpageS";
				strBody[15]=new String[]{"newpageS","Main part","","Barcode need to be printed","3","","",""};
				strBody[16]=new String[]{"S","  440/1*** WIRE ASS[12345]","","","3*1","","",""};
				strBody[17]=new String[]{"S","  440/1*** WIRE ASS","","","3*2","","",""};
				strBody[18]=new String[]{"S","Main part 1","","Barcode need to be printed","1","","",""};
				strBody[19]=new String[]{"S","  440/1*** WIRE ASS","","","1*1","","",""};
			  
			  
			}else if(strRptName.equals("rptorderpackslip")){
//			strHeader=new String[]{"2013-02-27","17279-0","ROYAUME LUMINAIRE LANAUDIERE","ROYAUME LUMINAIRE LA","3275 DE LA PINIERE","3275 DE LA PINIERE","TERREBONNE,QC J6X 4P7","TERREBONNE,QC J6X 4P",""                   ,"","(450) 477-8666","2013-02-27","RMA 1981","026427","Pick up from MTL","AGENTS ND" ,""};
			strHeader=new String[]{"2719","Montreal","2013-02-27","17279-0","ROYAUME LUMINAIRE LANAUDIERE","ROYAUME LUMINAIRE LA","3275 DE LA PINIERE","3275 DE LA PINIERE","TERREBONNE,QC J6X 4P7","TERREBONNE,QC J6X 4P",",QC J6X 4P"         ,"","(450)477-8666","2013-02-27","RMA 1981","026427","Pick up from MTL","AGENTS ND" ,"DSFFDS dkfj sadjfkdsf asfdjdsakfjdskaf sajfdkds sadfjdsk asfdjkj sfdjk sfdjkj kjkafsdskj DSFFDS dkfj sadjfkdsf asfdjdsakfjdskaf sajfdkds sadfjdsk asfdjkj sfdjk sfdjkj kjkafsdskj DSFFDS dkfj sadjfkdsf asfdjdsakfjdskaf sajfdkds sadfjdsk asfdjkj sfdjk sfdjkj kjkafsdskj DSFFDS dkfj sadjfkdsf asfdjdsakfjdskaf sajfdkds sadfjdsk asfdjkj sfdjk sfdjkj kjkafsdskj","17"};
		  //strHeader=new String[]{"2013-02-27"  ,"17279-0","LANDO LIGHTING"             ,"210 CLARENCE STREET" ,"BRAMPTON,ON L6W 1T4",""                 ,"LANDO LIGHTING"      ,"210 CLARENCE STREET"  ,"BRAMPTON,ON L6W 1T4","","(905)453-6403" ,"Oct 17 12" ,"RMA 1575","020821","Vitran-Direct"   ,"C.MESHWORK","some comments"};
		  strBody=new String[46][5];
		  for(int i=0;i<strBody.length;i++){
			strBody[i]=new String[]{"31108B"+i,"8LT CHD,BLK CH MTL,BLK SILK SHD UNAUTHORIZED RETURN NO DEFECT FOUND",""+i,"3"+i,"1"+i};
		  }
		}else if(strRptName.equals("rptpdf")){
			strHeader=new String[]{"atitle","10,20,10,30","left,right,center,right","field0,field1,field2,field3"};
			strBody=new String[20][4];
			  for(int i=0;i<strBody.length;i++){
					strBody[i]=new String[]{"31108B"+i,"8LT CHD,BLK CH MTL,BLK SILK SHD UNAUTH",""+i,"3"+i};
				  }
			
		}else if(strRptName.equals("parttransinplan")){
			  strHeader=new String[]{"81","B12F,E12H,H12G,P12B,P12C,P12D,P12E,P12F,P12G,P12H,P12J,D11J,F11J,J11G,O11I,P11D,B10D,D10H,C9J,E9D,F9B,I9H,L9I,L9,JN9B,O9E,P9H,F8I,I8H,M8J,E7H,L7H,M7D,M7E,N7E,A6I,B6F,E5E,E5F,H5H,L5D,D4J,F4I,M4C,P4J,B3G,E3E,E3I,F3I,H3I,P3H,E2,,J2J,K2G,M2C,M2I,M2J,N2H,N2I,N2J,P2E,P2F,P2G,P2I,P2J,A1E,D1D,K1F,L1B,L1F,M1J,N1G,N1H,N1I,N1J,O1A,O1B,O1C,O1D,O1,O1F,P1A,P1B,P1C,P1D,P1E,P1F,P1G,P1H,P1I,P1J,M1K,N1K,M1L,N1L,Y12A,Y12B,Y12C,Y12D,Y12E,Y12F,Y12G,Y12H,Y12I,Y12J,Y12K,Y12L,Y11B,Y11C,Y11E,Y11F,Y11G,Y11H,Y11I,Y11J,Y11K,Y11L,Y10A,Y10B,Y10C,Y10K,Y9B,Y9E,Y9F,Y9H,Y9K,Y8K,Y3G,Z9A,Z9B,Z9C,Z9D,Z9E,Z9F,Z9G,Z9H,Z9I,Z9J,Z9K,Z9L,Z9M,Z8A,Z8B,Z8C,Z8D,Z8E,Z8F,Z8G,Z8H,Z8I,Z8J,Z8K,Z8L,Z8M,Z7A,Z7B,Z7C,Z7D,Z7E,Z7F,Z7G,Z7H,Z7I,Z7J,Z7K,Z7L,Z7M,Z6A,Z6B,Z6C,Z6D,Z6E,Z6F,Z6G,Z6H,Z6I,Z6J,Z6K,Z6L,Z6M,Z5A,Z5B,Z5C,Z5D,Z5E,Z5F,Z5G,Z5H,Z5I,Z5J,Z5K,Z5L,Z5M,Z4A,Z4B,Z4C,Z4D,Z4E,Z4F,Z4G,Z4H,Z4I,Z4J,Z4K,Z4L,Z4M,Z3A,Z3B,Z3C,Z3D,Z3E,Z3F,Z3G,Z3H,Z3I,Z3J,Z3K,Z3L,Z3M,Z2A,Z2B,Z2C,Z2D,Z2E,Z2F,Z2G,Z2H,Z2I,Z2J,Z2K,Z2L,Z2M,Z1A,Z1B,Z1C,Z1D,Z1E,Z1F,Z1G,Z1H,Z1I,Z1J,Z1K,Z1L,Z1M,QC1,QC2,QC10,QC Floor","batcnno"};
			  strBody=new String[100][3];
			  for(int i=0;i<50;i++){
				strBody[i]=new String[]{"31108B"+i,i+"","",i+""};
			  }
			  for(int i=50;i<100;i++){
					strBody[i]=new String[]{"dasfsadf[324]",i+"","",i+""};
				  }
		}else if(strRptName.equals("rma")){
			  strHeader=new String[]{"wr19","2012-11-21","420 VAN HORNE AVENUE","BRANDON,MB R7A 1C9","CA","df",""};
			  strBody=new String[100][6];
			  for(int i=0;i<strBody.length;i++){
				strBody[i]=new String[]{"42153"+i,"3LT PNT CLR","1.00","2","2","apono","glass broken"};
			  }
		}else if(strRptName.equals("shippinglabel")){
			  strHeader=new String[]{"C:/zhongzeyu/zhongzeyuwork/uploadimages/uploadimages/Images/this/shiplabel.png","LIVING LIGHTING #28","1742 BANK STREET","address1","FALLFLYER","61305-0","2016-10-25","comment dsfdsf dsfdsf dsafdsaf dsafdsf dsafds dsfds sAD SAFDSAFDSAF AFDSAFDSFDSAF DSAFDSFDSF DSAFDSAFDSAF DSFDSFDSAF FDSFDSFDSFD DSFDSAFDSAF FDFDFD FDFSFSDFSDF DSFDFDF SD","5"};
			  strBody=new String[1][1];
				strBody[0]=new String[]{"nothing"};
				phf.outPdf(out,"c:/temp/mytest.pdf",strRptName,strHeader,strBody,85*6,82*4,true);
			  
		}else if(strRptName.equals("warehouselocation")){
			  strHeader=new String[]{"Virtual Skids"};
			  strBody=new String[30][2];
			  for(int i=0;i<strBody.length;i++){
				strBody[i]=new String[]{"fjdsal32490832"+i,"kjfasoier3290"+i};
			  }
		}
        //mergePdfs("c:/temp/mytests.pdf", new String []{"c:/temp/mytest.pdf","c:/temp/rptorder_23754_0.pdf"});
			if(strRptName.equals("unicode")){
				
				String[][] MOVIES = {
			        {
			            "STSong-Light", "UniGB-UCS2-H",
			            "Movie title: House of The Flying Daggers (China)",
			            "directed by Zhang Yimou",
			            "\u5341\u950a\u57cb\u4f0f"
			        },
			        {
			            "KozMinPro-Regular", "UniJIS-UCS2-H",
			            "Movie title: Nobody Knows (Japan)",
			            "directed by Hirokazu Koreeda",
			            "\u8ab0\u3082\u77e5\u3089\u306a\u3044"
			        },
			        {
			            "HYGoThic-Medium", "UniKS-UCS2-H",
			            "Movie title: '3-Iron' aka 'Bin-jip' (South-Korea)",
			            "directed by Kim Ki-Duk",
			            "\ube48\uc9d1"
			        }
			    };
				 Document document = new Document();
			        // step 2
			        PdfWriter.getInstance(document, new FileOutputStream("c:/temp/unicode.pdf"));
			        // step 3
			        document.open();
			        FontSelector selector = new FontSelector();
			        Font f1 = FontFactory.getFont(FontFactory.HELVETICA, 12);
			        f1.setColor(BaseColor.BLUE);
			        BaseFont bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
			        Font FontChinese=new Font(bfChinese,12,Font.NORMAL);
			        Font f2 = FontFactory.getFont("MSung-Light",
			                "UniCNS-UCS2-H", BaseFont.NOT_EMBEDDED);
			        f2.setColor(BaseColor.RED);
			        selector.addFont(f1);
			        selector.addFont(f2);
			        //Phrase ph = selector.process(MOVIES[0][4]);
			        Paragraph t=new Paragraph("\u5341\u950a\u57cb\u4f0f",FontChinese);
			        document.add(t);
			        
			        // step 4
			        BaseFont bf;
			        Font font;
			 
			        for (int i = 0; i < 3; i++) {
			            bf = BaseFont.createFont(MOVIES[i][0], MOVIES[i][1], BaseFont.NOT_EMBEDDED);
			            font = new Font(bf, 12);
			            document.add(new Paragraph(bf.getPostscriptFontName(), font));
			            for (int j = 2; j < 5; j++)
			                document.add(new Paragraph(MOVIES[i][j], font));
			            document.add(Chunk.NEWLINE);
			        }
			        // step 5
			        document.close();				 
				 
			}else{
				if(!ZzyCommon.isInArray(new String[]{"barcodeitem","orderlabel","barcodecomponent","barcodelocation","cheque","partlabel","barcodeitem","orderlabel","barcodecomponent","barcodelocation","barcode128item"},strRptName))
			        phf.outPdf(out,"c:/temp/mytest.pdf",strRptName,strHeader,strBody);
				
			}
        
    }
    public void mergePdfs(String fileName, String [] childPdfs) {
    	try {

    	    Document doc = new Document();
    	    PdfCopy copyDoc =  new PdfCopy(doc, new FileOutputStream(fileName));
    	    doc.open();
    	    for (int i = 0; i < childPdfs.length; i++) {
    	        PdfReader reader = new PdfReader(childPdfs [i]);
    	        int pageCnt = reader.getNumberOfPages();
    	        for (int j = 1; j <= pageCnt; j++) {
    	            copyDoc.addPage(copyDoc.getImportedPage(reader, j));
    	        }
    	        reader.close();
    	    }
    	    doc.close();

    	} catch (Exception e) {
    	    throw  new RuntimeException(e);
    	}
    }    
	public void splitPdf(String pdfFile,String pageinfo,String outfilename){
    	try {

    	    Document doc = new Document();
    	    PdfCopy copyDoc =  new PdfCopy(doc, new FileOutputStream(outfilename));
    	    doc.open();
    	        PdfReader reader = new PdfReader(pdfFile);
    	        int pageCnt = reader.getNumberOfPages();
    	        String[] pageinfoA=pageinfo.split(",");
    	        ArrayList<int[]> iPages=new ArrayList<int[]>();
    	        for(int i=0;i<pageinfoA.length;i++){
    	        	String pi=pageinfoA[i];
    	        	String[] piA=pi.split("-");
    	        	String sb=piA[0],se=piA[0];
    	        	if(piA.length>1){
    	        		se=piA[1];
    	        	}
    	        	int[] iA=new int[]{(new Integer(sb)).intValue(),(new Integer(se)).intValue()};
    	        	//System.out.println("iA["+iA[0]+","+iA[1]+"]");
    	        	iPages.add(iA);
    	        }
    	        for (int j = 1; j <= pageCnt; j++) {
    	        	
    	        	for(int[] i:iPages){
    	        		//System.out.println("i["+i[0]+","+i[1]+"],"+j);
    	        		if(j>=i[0] && j<=i[1]){
    	        			//System.out.println("pdfHeaderFooter copy page "+j);
    	        			copyDoc.addPage(copyDoc.getImportedPage(reader, j));
    	        		}
        	        		
    	        	}
    	            
    	        }
    	        reader.close();
    	    doc.close();

    	} catch (Exception e) {
    	    throw  new RuntimeException(e);
    	}

	}    
    
}
