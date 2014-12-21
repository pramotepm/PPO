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
		
		public void extractRecord(String record) {
			String[] spt = record.replaceAll("\\s+", " ").split(" ");
			String[] temp = spt[8].split("_");
			setSplicingType(temp[0]);
			setIsoformType(temp[1]);
			setASregionLength(Integer.parseInt(spt[12]));
			setStrPoint(Integer.parseInt(spt[10]));
			setEndPoint(Integer.parseInt(spt[11]));
//			System.out.println(spt[10] + "  " + spt[11]);
			if (getIsoformType() == Type.IsoformType.REF)
				setIsoformName(spt[0]);
			else
				setIsoformName(spt[4]);
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

		private void setEndPoint(int endPoint) {
			this.endPoint = endPoint;
		}

		public String getIsoformName() {
			return isoformName;
		}

		private void setIsoformName(String isoformName) {
			this.isoformName = isoformName.replaceFirst("-", ".");
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
}
