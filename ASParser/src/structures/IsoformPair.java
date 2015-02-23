package structures;

import structures.type.Type;

import structures.type.Type.IsoformType;
import structures.type.Type.SplicingType;

public class IsoformPair {
	public class Isoform {
		private SplicingType splicingType;
		private IsoformType isoformType;
		private int ASregionLength;
		private int strPoint;
		private int endPoint;
		private String isoformName;
		private int refIsoformLength;
		private int varIsoformLength;
		private boolean isConfirmedByExperiment;
		private String confirmedByExperimentSymbol;
		private boolean isOverlap;
		
		public void extractRecord(String record) {
			String[] spt = record.replaceAll("\\s+", " ").split(" ");
			String[] temp = spt[8].split("_");
			setSplicingType(temp[0]);
			setIsoformType(temp[1]);
			setIsComfirmedByExperiment(spt[1].equals("--"));
			setConfirmedByExperimentSymbol(spt[1]);
			setReferenceIsoformLength(Integer.parseInt(spt[3]));
			setVariantIsoformLength(Integer.parseInt(spt[7]));
			setASregionLength(Integer.parseInt(spt[12]));
			setStrPoint(Integer.parseInt(spt[10]));
			setEndPoint(Integer.parseInt(spt[11]));
			if (getIsoformType() == Type.IsoformType.REF)
				setIsoformName(spt[0]);
			else
				setIsoformName(spt[4]);
			setIsOverlap(false);
		}

		public SplicingType getSplicingType() {
			return splicingType;
		}

		private void setSplicingType(String splicingType) {
			switch (splicingType) {
				case "DEL":
					this.splicingType = Type.SplicingType.DEL;
				break;
				case "ALT":
					this.splicingType = Type.SplicingType.ALT;
				break;
				case "INS":
					this.splicingType = Type.SplicingType.INS;
				break;
			}
		}

		public IsoformType getIsoformType() {
			return isoformType;
		}

		private void setIsoformType(String isoformType) {
			switch (isoformType) {
				case "REF":
					this.isoformType = Type.IsoformType.REF;
				break;
				case "VAR":
					this.isoformType = Type.IsoformType.VAR;
				break;
			}
		}

		public int getASregionLength() {
			return ASregionLength;
		}

		private void setASregionLength(int aSregionLength) {
			ASregionLength = aSregionLength;
		}

		public int getStrPoint() {
			return strPoint;
		}

		private void setStrPoint(int strPoint) {
			this.strPoint = strPoint;
		}

		public int getEndPoint() {
			return endPoint;
		}

		public int getReferenceIsoformLength() {
			return refIsoformLength; 
		}
		
		public int getVariantIsoformLength() {
			return varIsoformLength;
		}
		
		public String getConfirmedByExperimentSymbol() {
			return confirmedByExperimentSymbol;
		}
		
		public boolean isConfirmedByExperiment() {
			return isConfirmedByExperiment;
		}
		
		public boolean isOverlap() {
			return isOverlap;
		}
		
		private void setEndPoint(int endPoint) {
			this.endPoint = endPoint;
		}

		public String getIsoformName() {
			return isoformName;
		}

		private void setIsoformName(String isoformName) {
			this.isoformName = isoformName.replaceFirst("-", ".");
		}
		
		private void setReferenceIsoformLength(int refIsoformLength) {
			this.refIsoformLength = refIsoformLength;
		}
		
		private void setVariantIsoformLength(int varIsoformLength) {
			this.varIsoformLength = varIsoformLength;
		}
		
		private void setIsComfirmedByExperiment(boolean isConfirmedByExperiment) {
			this.isConfirmedByExperiment = isConfirmedByExperiment;
		}
		
		private void setConfirmedByExperimentSymbol(String confirmedByExperimentSymbol) {
			this.confirmedByExperimentSymbol = confirmedByExperimentSymbol;
		}
		
		public void setIsOverlap(Boolean isOverlap) {
			this.isOverlap = this.isOverlap || isOverlap;
		}
	}
	private Isoform reference;
	private Isoform variant;
	
	public IsoformPair() {
		reference = new Isoform();
		variant = new Isoform();
	}
	
	public Isoform Reference() {
		return this.reference;
	}
	
	public Isoform Variant() {
		return this.variant;
	}
	
	public Isoform[] getAllIsoform() {
		return new Isoform[] { reference, variant };
	}
}