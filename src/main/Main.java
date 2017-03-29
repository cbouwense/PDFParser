package main;

import java.io.File;
import java.io.IOException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.pdfparser.*;

public class Main {

	public static void main(String[] args) throws IOException {
		
		try {
		    String text = getText(new File("C:\\Users\\Christian\\Google Drive\\Jobs\\Arnold\\sheriff_foreclosuresales_list.pdf"));
		    System.out.println("Text in PDF: " + text);
		} catch (IOException e) {
		    e.printStackTrace();
		}
		
	}
	
	static String getText(File pdfFile) throws IOException {
	    PDDocument doc = PDDocument.load(pdfFile);
	    PDFTextStripper stripper = new PDFTextStripper();
	    stripper.setSortByPosition(true);
	    return stripper.getText(doc);
	}

}
