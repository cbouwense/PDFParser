package main;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

public class Main {

	public static void main(String[] args) throws IOException {
		
		PrintWriter out = new PrintWriter("values.csv");
		
		try {
			
		    String text = getText(new File("C:\\Users\\Christian\\Google Drive\\Jobs\\Arnold\\sheriff_foreclosuresales_list.pdf"));
		    System.out.println(text.substring(0, 2000));
		    out.println(formatText(text));
		    
		} catch (IOException e) {
			
		    e.printStackTrace();
		    
		}
		
		out.close();
		
	}
	
	public static String getText(File pdfFile) throws IOException {
		
	    PDDocument doc = PDDocument.load(pdfFile);
	    PDFTextStripper stripper = new PDFTextStripper();
	    String result = "";
	    
	    // Extract the text in order (doesn't do that by default)
	    stripper.setSortByPosition(true);
	    
	    result = stripper.getText(doc);
	    doc.close();
	    
	    return result;
	    
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
		
		// First row in CSV to mark what each column represents
		String f = "File Number,Court Case No.,Plaintiff,Defendant,Attorney,Sale Price,Location,Original Sale Date,Current Sale Date\n";
		
		// Temporary string to hold first wave of formatting
		String t = "";
		
		// This will hold either "Scheduled" "Sale Price" or "Minimum Bid"
		String afterAttorney = "";
		
		// Indexer for when we need to step through string (explained where used)
		int i = 0;
		
		while(s.length() > 71) {
			// Concatenate the value for File Number
			t = t.concat(s.substring(s.indexOf("File Number:  ")+14, s.indexOf("File Number:  ")+22) + ",");
			
			// Court Case No.
			t = t.concat(s.substring(s.indexOf("Court Case No.:  ")+17, s.indexOf("Court Case No.:  ")+28) + ",");
					
			// Plaintiff
			t = t.concat(s.substring(s.indexOf("Plaintiff ")+10, s.indexOf("Defendant")-12) + ",");
			
			// Defendant
			t = t.concat(s.substring(s.indexOf("Defendant ")+10, s.indexOf("Attorney")-2) + ",");
			
			// Attorney
			i = s.indexOf("Attorney");
			afterAttorney = s.substring(i, i+2);
			while (!afterAttorney.equals("Sc") && !afterAttorney.equals("SO") && !afterAttorney.equals("MI")) {
				i++;
				afterAttorney = s.substring(i, i+2);
			}
			t = t.concat(s.substring(s.indexOf("Attorney ")+9, i) + ",");
			
			// Sale Price
			if (afterAttorney.equals("Sc") || afterAttorney.equals("MI")) {
				t = t.concat(",");
			}
			else if (afterAttorney.equals("SO")) {
				t = t.concat(s.substring(s.indexOf("SOLD")+4, s.indexOf("SOLD")+15) + ",");

			}
			
			// Location
			i = s.indexOf("Location");
			while (s.charAt(i) != '\n') {
				i++;
			}
			t = t.concat(s.substring(s.indexOf("Location: ")+10, i-1) + ",");
	     				
			// Original Sale Date
			t = t.concat(s.substring(s.indexOf("Original Sale Date: ")+21, s.indexOf("File Number: ")-1) + ",");
			
			// Current Sale Date
			t = t.concat(s.substring(s.indexOf("Current Sale Date: ")+19, s.indexOf("Plaintiff ")-1) + ",");
			
			// Chop off the stuff we just worked with
			i = s.indexOf("Location");
			while (s.charAt(i) != '\n') {
				i++;
			}
			
			s = s.substring(i-1, s.length());
			
			// Get rid of all newlines and carriage returns in t (messes up CSV formatting)
			t = t.replaceAll("\n", "");
			t = t.replaceAll("\r", "");
			
			// Add a newline at the end of t 
			t = t.concat("\n");
			
			// Add t to f
			f = f.concat(t);
			
			t = "";
			
		}
		
		f = f.replaceAll("\\r\\n", "");
		return f;
		
	}
	
}
