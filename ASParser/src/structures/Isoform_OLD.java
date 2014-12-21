package structures;

import java.util.LinkedList;

public class Isoform_OLD {
	public enum IsoformType {
		REF,
		VAR
	}	
	public enum SplicingType {
		INS,
		DEL,
		ALT
	}
	private String isoformAccession = null;
	private int numberOfASRegion = 0; 
	private LinkedList<Integer> start = new LinkedList<Integer>();
	private LinkedList<Integer> end = new LinkedList<Integer>();
	private LinkedList<Integer> length = new LinkedList<Integer>();
	private LinkedList<IsoformType> isoformType = new LinkedList<IsoformType>();
	private LinkedList<SplicingType> splicingType = new LinkedList<SplicingType>();
	private LinkedList<String> withVariantIsofrom = new LinkedList<String>();
	private LinkedList<Integer> variantIsofromIndex = new LinkedList<Integer>();	

	public void setAccession(String accession) {
		this.isoformAccession = accession.replaceFirst("-", ".");
	}

	public void addASRegion(int startPosition, int endPosition, int length, String isoformType, String splicingType, String withVariantIsoform, Integer variantIsoformIndex) {
		if (!isDuplicatedSplicingRegion(startPosition, endPosition)) {
			this.start.add(startPosition);
			this.end.add(endPosition);
			this.length.add(length);
			
			if (isoformType.equals("REF"))
				this.isoformType.add(IsoformType.REF);
			else if (isoformType.equals("VAR"))
				this.isoformType.add(IsoformType.VAR);

			if (splicingType.startsWith("INS"))
				this.splicingType.add(SplicingType.INS);
			else if (splicingType.startsWith("DEL"))
				this.splicingType.add(SplicingType.DEL);
			else if (splicingType.startsWith("ALT"))
				this.splicingType.add(SplicingType.ALT);
			
			this.withVariantIsofrom.add(withVariantIsoform);
			this.variantIsofromIndex.add(variantIsoformIndex);
			
			this.numberOfASRegion++;
		}
	}
	
	public String getAccession() {
		return this.isoformAccession;
	}
	
	public int getNumberOfASRegion() {
		return this.numberOfASRegion;
	}
	
	public int getIsoformStartRegionAtIndex(int index) {
		return this.start.get(index);
	}

	public int getIsoformEndRegionAtIndex(int index) {
		return this.end.get(index);
	}

	public int getIsoformLengthAtIndex(int index) {
		return this.length.get(index);
	}
	
	public IsoformType getIsoformTypeAtIndex(int index) {
		return this.isoformType.get(index);
	}
	
	public SplicingType getSplicingTypeAtIndex(int index) {
		return this.splicingType.get(index);
	}

	public String getIsoformSpliceWith(int index) {
		return this.withVariantIsofrom.get(index);
	}
	
	public int getVariantIsoformIndex(int index) {
		Integer i = this.variantIsofromIndex.get(index);
		if (i == null)
			return -1;
		else
			return i.intValue();
	}
	
	private boolean isDuplicatedSplicingRegion(int start, int end) {
		return this.start.contains(start) && this.end.contains(end);
	}
}