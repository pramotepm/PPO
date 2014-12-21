package parser;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;

import structures.IsoformPair;

public class ASParser {
	private IsoformPair[] isoformOut;
		
	public IsoformPair[] getIsoform() {
		return isoformOut;
	}
	
	public void parse(String FileName) throws IOException {
		LinkedList<IsoformPair> isoform = new LinkedList<IsoformPair>();
		String line1, line2;
		
		FileInputStream fis = new FileInputStream(FileName);
		InputStreamReader isr = new InputStreamReader(fis);
		BufferedReader br = new BufferedReader(isr);
		
		while((line1 = br.readLine()) != null && (line2 = br.readLine()) != null) {
			IsoformPair isoPair = new IsoformPair();
			isoPair.Reference().extractRecord(line1);
			isoPair.Variant().extractRecord(line2);
			isoform.add(isoPair);
		}
		br.close();
		isr.close();
		fis.close();
		isoformOut = isoform.toArray(new IsoformPair[isoform.size()]);
	}
	
	public static void main(String[] args) {
		String url = "/Users/pramotepm/Desktop/pas/ZO3_HUMAN.pas";
		ASParser x = new ASParser();
		try {
			x.parse(url);
			System.out.println(x.getIsoform().length);
			for (IsoformPair i : x.getIsoform()) {
				System.out.println(i.Reference().getIsoformType().toString());
				System.out.println(i.Reference().getStrPoint());
				System.out.println(i.Reference().getEndPoint());
				System.out.println(i.Reference().getSplicingType().toString());
				System.out.println(i.Variant().getIsoformType().toString());
				System.out.println(i.Variant().getStrPoint());
				System.out.println(i.Variant().getEndPoint());
				System.out.println(i.Variant().getSplicingType().toString());
				System.out.println("----");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}