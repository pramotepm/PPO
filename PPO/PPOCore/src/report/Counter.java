package report;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import sequence.compare.SplicingPositionException;
import structures.type.Type.IsoformType;
import structures.type.Type.SplicingType;

public class Counter implements Runnable {
	public File inputFilePath;
	
	private String isoformName;
	private IsoformType isoformType; 
	private int isoformLength;
	private String accession;
	private SplicingType splicingType;
	private int regionStrtPnt;
	private int regionEndPnt;
	private int regionLen;
	private int peptideStrPnt;
	private int peptideEndPnt;
	private int peptideLen;
	private boolean isConfirmedByExp;
	private boolean isOverlap;
	private boolean isSpecialCase;
	
	public Counter(File inputFilePath) {
		this.inputFilePath = inputFilePath;
	}
	
	private void extractHeader(String s) {
		if (s.contains(":")) {
			String[] temp = s.split(" ")[1].split(":");
			switch (temp[0]) {
				case "ISOFORM_NAME":
					isoformName = temp[1];
				break;
				case "ISOFORM_TYPE":
					isoformType = IsoformType.valueOf(temp[1]);
				break;
				case "ISOFORM_LENGTH":
					isoformLength = Integer.parseInt(temp[1]);
				break;
			}
		}
	}
	
	@Override
	public void run() {
		CountingFunction cf = new CountingFunction();
		isoformName = FilenameUtils.getName(inputFilePath.toString()); 
		isoformType =  isoformName.contains(".") ? IsoformType.VAR : IsoformType.REF;
		System.out.println(isoformName);
		try {
			List<String> readLines = FileUtils.readLines(inputFilePath);
			for (String readLine : readLines) {
				if (readLine.startsWith("#"))
					continue;
				else if (readLine.startsWith("//")) {
					extractHeader(readLine);
					continue;
				}
				extractingRecord(readLine);
//				try {
//					cf.countSplicingRegionLocation(isoformType, regionStrtPnt, regionEndPnt, regionLen, isOverlap, isoformName);
//				} catch (SplicingPositionException e) {
//					e.printStackTrace();
//				}
				if (isSpecialCase)
					cf.countSpecialCaseRegionOverlapType(splicingType);
				cf.countRegionOverlapType(isoformName, regionStrtPnt, regionEndPnt, splicingType, isoformType, isOverlap);
				cf.countASRegion(isoformName, regionStrtPnt, regionEndPnt, isoformType, isOverlap);
			}
			cf.countNumberOfOverlappingIsoform(isoformName, readLines);
			cf.countIsoformOverlapType(isoformName, readLines);
			cf.countNumberOfIsoform(isoformName, isoformType);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void extractingRecord(String inptLine) {
		String[] temp = inptLine.split("\t");
		accession = temp[0];
		splicingType = SplicingType.valueOf(temp[1]);
		regionStrtPnt = Integer.parseInt(temp[2]);
		regionEndPnt = Integer.parseInt(temp[3]);
		regionLen = Integer.parseInt(temp[4]);
		peptideStrPnt = Integer.parseInt(temp[5]);
		peptideEndPnt = Integer.parseInt(temp[6]);
		peptideLen = Integer.parseInt(temp[7]);
		isConfirmedByExp = temp[8].equals("--");
		isOverlap = !temp[9].equals("-");
		isSpecialCase = temp[9].equals("*");
	}
}