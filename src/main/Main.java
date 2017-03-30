package main;

import java.io.File;
import java.io.IOException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

public class Main {

	public static void main(String[] args) throws IOException {
		
		try {
			
		    String text = getText(new File("C:\\Users\\Christian\\Google Drive\\Jobs\\Arnold\\sheriff_foreclosuresales_list.pdf"));
		    //System.out.println("Text in PDF: " + text.substring(0, 5000));
		    formatText(text);
		    
		} catch (IOException e) {
			
		    e.printStackTrace();
		    
		}
		
	}
	
	public static String getText(File pdfFile) throws IOException {
		
	    PDDocument doc = PDDocument.load(pdfFile);
	    PDFTextStripper stripper = new PDFTextStripper();
	    // Extract the text in order (doesn't do that by default)
	    stripper.setSortByPosition(true);
	    
	    return stripper.getText(doc);
	    
	}

	/**
	 * 
	 * @param s - Text extracted from the pdf in unformatted string form.
	 * @return f - Text formatted into CSV. Extracts File Number, Court Case Number,
	 * 			   Plaintiff, Defendant, Attorney, Location, Original Sale Date,
	 * 			   and Sold Price. (NOTE: CURRENTLY CANNOT DO SOLD PRICE)
	 */
	public static String formatText(String s) {
		
		// Replace all of the commas in the pdf with spaces
		s = s.replace(',', ' ');
		
		String f = "";
		String afterAttorney = "";
		int i = 0;
		
		while(s.length() > 71) {
			// Append "File Number" onto our fresh string.
			f += ",File Number,";
			// Concatenate the value for File Number.
			f = f.concat(s.substring(s.indexOf("File Number:  ")+14, s.indexOf("File Number:  ")+22));
			
			f += ",Court Case No.,";
			f = f.concat(s.substring(s.indexOf("Court Case No.:  ")+17, s.indexOf("Court Case No.:  ")+28));
					
			// Find the end of Location
			i = s.indexOf("Plaintiff");
			while (i != s.indexOf("Defendant")) {
				i++;
			}
			f += ",Plaintiff,";
			f = f.concat(s.substring(s.indexOf("Plaintiff ")+10, i));
			
			f += ",Defendant,";
			f = f.concat(s.substring(s.indexOf("Defendant ")+10, s.indexOf("Attorney")-2));
			
			i = s.indexOf("Attorney");
			while (!s.substring(i, i+2).equals("Sc") && !s.substring(i, i+2).equals("SO") && !s.substring(i, i+2).equals("MI")) {
				i++;
			}
			f += ",Attorney,";
			f = f.concat(s.substring(s.indexOf("Attorney ")+9, i));
			
			i = s.indexOf("Location");
			while (s.charAt(i) != '\n') {
				i++;
			}
			f += ",Location,";
			f = f.concat(s.substring(s.indexOf("Location: ")+10, i-1));
	     
			f += ",Original Sale Date,";
			f = f.concat(s.substring(s.indexOf("Sale Date: ")+11, s.indexOf("File Number: ")-1));
			
			i = s.indexOf("Plaintiff");
			while (!s.substring(i, i+2).equals("  ")) {
				i++;
			}
			//f += ",Sold Price,";
			//f = f.concat(s.substring(i+2, s.indexOf("Defendant")-1));
			
			// Chop off the stuff we just worked with
			i = s.indexOf("Location");
			while (s.charAt(i) != '\n') {
				i++;
			}
			
			s = s.substring(i-1, s.length());
			System.out.println("s.length = " + s.length());
		}
		
		System.out.println("\n\n" + f);
		System.out.println("s length: " + s.length());
		
		return f;
		
	}
	
}
