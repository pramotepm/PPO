package sequence.compare;

public class CompareRegionType {
	private boolean isOverlap = false;
	private String overlapSide = null;
	private int n = 0;
	
	public CompareRegionType(int n) {
		this.n = n;
	}
	
	public void chkOverlap(int asStr, int asEnd, int pepStr, int pepEnd) {
		isOverlap = isOverlap(asStr, asEnd, pepStr, pepEnd);
	}
	
	private  boolean isOverlap(int asStr, int asEnd, int pepStr, int pepEnd) {
		// - A few region overlapped.
		// Peptide sequence                           |-------------|
		// Alternative Splicing (AS) sequence     |-------------|
		if ((asStr < pepStr && pepStr < asEnd) && asEnd < pepEnd) {
			overlapSide = "RIGHT";
			return asEnd - pepStr > this.n;
		}
		// - Peptide is shorter than AS.
		// Peptide sequence                           |---------|
		// Alternative Splicing (AS) sequence     |-----------------|
		else if (asStr < pepStr && pepEnd < asEnd)
			return true;
		// - AS is shorter than peptide.
		// Peptide sequence                       |-----------------|
		// Alternative Splicing (AS) sequence         |---------|
		else if (pepStr < asStr && asEnd < pepEnd)
			return asStr - pepStr > this.n && pepEnd - asEnd > this.n;
		// - A few region overlapped.
		// Peptide sequence                       |-------------|
		// Alternative Splicing (AS) sequence         |-------------|
		else if (pepStr < asStr && (asStr < pepEnd && pepEnd < asEnd)) {
			overlapSide = "LEFT";
			return pepEnd - asStr > this.n;
		}
		return false;
	}

	public boolean isOverlap() {
		return isOverlap;
	}
	
	public String getOverlapSide() {
		return overlapSide;
	}
}