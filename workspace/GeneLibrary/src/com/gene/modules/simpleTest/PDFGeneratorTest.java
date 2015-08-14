package com.gene.modules.simpleTest;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfWriter;


public class PDFGeneratorTest
{
	public static void main(String[] args)
	{
		PDFGenerator p = new PDFGenerator();
		try {
			p.createPdf4();
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}

class PDFGenerator
{
	public void createPdf() throws DocumentException, IOException
	{
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream("sample.pdf"));
        document.open();
        document.add(new Paragraph("Hello World!"));
        document.close();
    }
	
	public void createPdf2() throws DocumentException, IOException
	{
		Rectangle pagesize = new Rectangle(216f, 720f);
        Document document = new Document(pagesize, 36f, 72f, 108f, 180f);
        PdfWriter.getInstance(document, new FileOutputStream("sample2.pdf"));
        document.open();
        document.add(new Paragraph("Hello World! Hello People! " +"Hello Sky! Hello Sun! Hello Moon! Hello Stars!"));
        document.close();
    }
	
	public void createPdf3() throws DocumentException, IOException
	{
		Document document = new Document();
        ByteArrayOutputStream baos = new ByteArrayOutputStream(); //memory
        PdfWriter.getInstance(document, baos); // write to memory
        document.open();
        document.add(new Paragraph("Hello World!"));
        document.close();
	 
        // let's write the file in memory to a file anyway
        FileOutputStream fos = new FileOutputStream("sample3.pdf");
        fos.write(baos.toByteArray());
        fos.close();
	}
	
	public void createPdf4() throws DocumentException, IOException
	{
		Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("sample4.pdf"));
        writer.setPdfVersion(PdfWriter.VERSION_1_7);
        document.open();
        document.add(new Paragraph("Hello World!"));
        document.close();
	}
}
