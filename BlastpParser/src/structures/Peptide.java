package structures;

public class Peptide {
	private String accession = null;
	private int length = 0;
	private int align_start = -1;
	private int align_end = -1;
	
	public String getAccession() {
		return accession;
	}
	public void setAccession(String accession) {
		this.accession = accession;
	}
	public int getLength() {
		return length;
	}
	public void setLength(int length) {
		this.length = length;
	}
	public int getAlign_start() {
		return align_start;
	}
	public void setAlign_start(int align_start) {
		this.align_start = align_start;
	}
	public int getAlign_end() {
		return align_end;
	}
	public void setAlign_end(int align_end) {
		this.align_end = align_end;
	}	
}