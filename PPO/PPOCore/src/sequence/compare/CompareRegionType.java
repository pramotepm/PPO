package sequence.compare;

public class CompareRegionType {
	public enum OverlapSide {
		RIGHT,
		LEFT,
		PEPTIDE_OVER_ALTERNATIVE_SPLICING,
		PEPTIDE_WITHIN_ALTERNATIVE_SPLICING
	}
	
	private boolean isOverlap = false;
	private OverlapSide overlapSide = null;
	private int n = 0;
	
	public void setCompareLength(int n) {
		this.n = n;
	}
	
	public void chkOverlap(int asStr, int asEnd, int pepStr, int pepEnd) {
		isOverlap = isOverlap(asStr, asEnd, pepStr, pepEnd);
	}

	private  boolean isOverlap(int asStr, int asEnd, int pepStr, int pepEnd) {
//		System.out.println(String.format("%d %d %d %d", asStr, asEnd, pepStr, pepEnd));
		
		// - A few region overlapped.
		// Peptide sequence                           |-------------|
		// Alternative Splicing (AS) region       |-------------|
		if ((asStr < pepStr && pepStr < asEnd) && asEnd < pepEnd) {
			overlapSide = OverlapSide.RIGHT;
			return asEnd - pepStr + 1 >= this.n;
		}
		// - Peptide is shorter than AS.
		// Peptide sequence                           |---------|
		// Alternative Splicing (AS) region       |-----------------|
		else if (asStr < pepStr && pepEnd < asEnd) {
			overlapSide = OverlapSide.PEPTIDE_WITHIN_ALTERNATIVE_SPLICING;
			return true;
		}
		// - Peptide is longer than AS.
		// Peptide sequence                       |-----------------|
		// Alternative Splicing (AS) region           |---------|
		else if (pepStr < asStr && asEnd < pepEnd) {
			overlapSide = OverlapSide.PEPTIDE_OVER_ALTERNATIVE_SPLICING;
			return asStr - pepStr + 1 >= this.n && pepEnd - asEnd + 1 >= this.n;
		}
		// - A few region overlapped.
		// Peptide sequence                       |-------------|
		// Alternative Splicing (AS) region           |-------------|
		else if (pepStr < asStr && (asStr < pepEnd && pepEnd < asEnd)) {
			overlapSide = OverlapSide.LEFT;
			return pepEnd - asStr + 1 >= this.n;
		}
		return false;
	}

	public boolean isOverlap() {
		return isOverlap;
	}
	
	public OverlapSide getOverlapSide() {
		return overlapSide;
	}
}