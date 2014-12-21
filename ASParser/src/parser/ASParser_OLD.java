package parser;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;

import structures.Isoform_OLD;

public class ASParser_OLD {
	private Isoform_OLD[] isoformOut;
		
	public Isoform_OLD[] getIsoform() {
		return isoformOut;
	}
	
	public void parse(String FileName) throws IOException {
		LinkedList<Isoform_OLD> isoform = new LinkedList<Isoform_OLD>();
		LinkedList<String> chk = new LinkedList<String>();
		Isoform_OLD sf = null;
		String readedLine;
		
		FileInputStream fis = new FileInputStream(FileName);
		InputStreamReader isr = new InputStreamReader(fis);
		BufferedReader br = new BufferedReader(isr);
		
		Isoform_OLD referenceIsoform = new Isoform_OLD();
		String referenceIsoformName = null;
		
		while((readedLine = br.readLine()) != null) {
			String[] spt = readedLine.replaceAll("\\s+", " ").split(" ");
			referenceIsoformName = spt[0];
			String variantIsoformName = spt[4];
			String splicingType = spt[8];
			int ASregionLength = Integer.parseInt(spt[12]);
			int strPoint = Integer.parseInt(spt[10]);
			int endPoint = Integer.parseInt(spt[11]);
			if (!chk.contains(variantIsoformName)) {
				if (sf != null)
					isoform.add(sf);
				sf = new Isoform_OLD();
				sf.setAccession(variantIsoformName);
				chk.add(variantIsoformName);
			}		
			if (ASregionLength != 0) {
				if (splicingType.endsWith("_REF"))
					referenceIsoform.addASRegion(strPoint, endPoint, ASregionLength, "REF", splicingType, variantIsoformName, sf.getNumberOfASRegion());
				else if (splicingType.endsWith("_VAR"))
					sf.addASRegion(strPoint, endPoint, ASregionLength, "VAR", splicingType, null, null);
			}
		}
		if (sf != null) {
			isoform.add(sf);
			referenceIsoform.setAccession(referenceIsoformName);
			isoform.add(0, referenceIsoform);
		}
		br.close();
		isr.close();
		fis.close();
		isoformOut = isoform.toArray(new Isoform_OLD[isoform.size()]);
	}
	
	public static void main(String[] args) {
		String url = "/Users/pramotepm/Desktop/pas/ZO3_HUMAN.pas";
		ASParser_OLD x = new ASParser_OLD();
		try {
			x.parse(url);
			System.out.println(x.getIsoform().length);
			for (Isoform_OLD i : x.getIsoform()) {
				System.out.println(i.getAccession());
				for (int j = 0; j < i.getNumberOfASRegion(); j++)
					System.out.println(i.getIsoformStartRegionAtIndex(j) + " " + i.getIsoformEndRegionAtIndex(j));
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}