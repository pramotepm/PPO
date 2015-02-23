package sequence.compare;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import sequence.compare.CompareRegionType.OverlapSide;
import sequence.reader.SequenceDirectoryReader;
import sequence.reader.SequenceFileReader;
import structures.IsoformPair.Isoform;

public class CompareSequenceType {
	// ------------ A Special Case -------------
	// Seq A : AAAACCXXXCCAA
	// Seq B : AAAACCAA
	// "CCXXX" was AS and was spliced out.
	// "AAAACC" of two seqeunces are the same, if there is a peptide overlapped with, these two sequences can express in protein level. 
	// -----------------------------------------
	
	private static HashMap<String, String> sequence;

	public static void initialFromDir(String dir) throws IOException {
		sequence = SequenceDirectoryReader.getSequenceFromDir(dir);
	}
	
	public static void initialFromFile(File dir) throws IOException {
		sequence = SequenceFileReader.getSequenceFromFile(dir);
	}
	
	private static boolean isNFirstMatches(String aminoA, String aminoB, int startPositionA, int startPositionB, int n) {
		// Adjust to String index base
		startPositionA--;
		startPositionB--;		
		String sA = sequence.get(aminoA);
		String sB = sequence.get(aminoB);
		if (startPositionA < 0 || startPositionB < 0 || startPositionA + n >= sA.length() || startPositionB + n >= sB.length())
			return false;		
//		return sA.substring(startPositionA, startPositionA + n).equals(sB.substring(startPositionB, startPositionB + n));
//		System.out.println("N=" + n);
//		System.out.println(sA.substring(startPositionA, startPositionA + n));
//		System.out.println(sB.substring(startPositionB, startPositionB + n)); 
		for (int i = 0; i < n; i++) {
			if (sA.charAt(startPositionA + i) != sB.charAt(startPositionB + i))
				return false;
		}
		return true;
	}
	
	@SuppressWarnings("incomplete-switch")
	public static boolean chkAfterSplice(Isoform ref, Isoform var, int pepStr, int pepEnd, OverlapSide side, String x) {
		int overlapLength;
		boolean b = false;
		if (side != null) {
			switch (ref.getSplicingType()) {
				case DEL:
				case ALT:
//					System.out.println(ref.getIsoformName());
//					System.out.println(x);
//					System.out.flush();
					switch (side) {
						case LEFT:
							// DELETE TYPE - LEFT OVERLAP
							overlapLength = pepEnd - ref.getStrPoint() + 1;
							b = isNFirstMatches(ref.getIsoformName(), var.getIsoformName(), ref.getStrPoint(), var.getEndPoint(), overlapLength);
						break;
						case RIGHT:
							// DELETE TYPE - RIGHT OVERLAP
							overlapLength = ref.getEndPoint() - pepStr + 1;
							b = isNFirstMatches(ref.getIsoformName(), var.getIsoformName(), pepStr, var.getEndPoint() - overlapLength + ((var.getStrPoint() == var.getEndPoint()) ? 1 : 0), overlapLength);
						break;
						default:
						break;
					}
				break;
			}
		}
		return b;
	}	
}