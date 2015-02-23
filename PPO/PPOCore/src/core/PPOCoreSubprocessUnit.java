package core;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import parser.ASParser;
import parser.BlastpParser;
import report.CountingFunction;
import sequence.compare.CompareRegionType;
import sequence.compare.CompareSequenceType;
import sequence.compare.SplicingPositionException;
import structures.IsoformPair;
import structures.IsoformPair.Isoform;
import structures.Peptide;
import structures.type.Type;
import config.ArgParser;

public class PPOCoreSubprocessUnit implements Runnable {
		private File FILEPATH;
		ASParser ASparser = null;
		BlastpParser blastp_parser = null;
		
		String comment = "# Acession, Splicing Type, AS Starting Point, AS Ending Point, AS Region Length, Peptide Starting Point, Peptide Ending Point, Peptide Length, Confirmed?, Express?";
		String header = "// --- HEADER ---\n"
					  + "// ISOFORM_NAME:%s\n"
					  + "// ISOFORM_TYPE:%s\n"
					  + "// ISOFORM_LENGTH:%d\n"
					  + "// ----------------";
		String specialCaseStringFormat = "%s\t%s\t%d\t%d\t%d\t%d\t%d\t%d\t%s\t*";
		String ASExpressStringFormat = "%s\t%s\t%d\t%d\t%d\t%d\t%d\t%d\t%s\t+";
		String ASNotExpressStringFormat = "%s\t%s\t%d\t%d\t%d\t%d\t%d\t%d\t%s\t-";
		CountingFunction counter = new CountingFunction();
		
		public PPOCoreSubprocessUnit(File FILEPATH) {
			ASparser = new ASParser();
			blastp_parser = new BlastpParser();
			this.FILEPATH = FILEPATH;
		}
		
		private void chkSpecialSplicingCase(HashSet<String> sb, Isoform isoform1, Isoform isoform2, CompareRegionType cmpRegion, int isoform1Strt, int isoform1End, Peptide p, int peptideStr, int peptideEnd) {
			String x = String.format("%d %d %d %d",isoform1Strt, isoform1End,	peptideStr, peptideEnd);
			if (CompareSequenceType.chkAfterSplice(isoform1, isoform2, peptideStr, peptideEnd, cmpRegion.getOverlapSide(), x)) {
				sb.add(String.format(specialCaseStringFormat, p.getAccession(),isoform2.getSplicingType(), isoform2.getStrPoint(), isoform2.getEndPoint(), isoform2.getASregionLength(), peptideStr, peptideEnd, p.getLength(), isoform2.getConfirmedByExperimentSymbol()));
				counter.countSpecialCaseRegionOverlapType(isoform2.getSplicingType());
			}
		}
		
		private void processOverlappingRegion(HashSet<String> sb1, HashSet<String> sb2, Isoform isoform1, Isoform isoform2) throws ParserConfigurationException, SAXException, IOException {
			String peptideFilePath = ArgParser.getPeptidePath() + "/" + isoform1.getIsoformName();
			CompareRegionType cmpRegion = new CompareRegionType();
			cmpRegion.setCompareLength(ArgParser.getPeptideCompareLength());
			if (new File(peptideFilePath).exists()) {
				blastp_parser.parse(peptideFilePath);
				int isoformStr = isoform1.getStrPoint();
				int isoformEnd = isoform1.getEndPoint();
				for (Peptide p : blastp_parser.getPeptides()) {
					int peptideStr = p.getAlign_start();
					int peptideEnd = p.getAlign_end();
					cmpRegion.chkOverlap(isoformStr, isoformEnd, peptideStr, peptideEnd);
					isoform1.setIsOverlap(cmpRegion.isOverlap());
					if (cmpRegion.isOverlap()) {
						if (isoform1.getIsoformType() == Type.IsoformType.REF) {
							chkSpecialSplicingCase(sb2, isoform1, isoform2, cmpRegion, isoformStr, isoformEnd, p, peptideStr, peptideEnd);
						}
						sb1.add(String.format(ASExpressStringFormat, p.getAccession(),isoform1.getSplicingType(), isoform1.getStrPoint(), isoform1.getEndPoint(), isoform1.getASregionLength(), peptideStr, peptideEnd, p.getLength(), isoform1.getConfirmedByExperimentSymbol()));						
						counter.countRegionOverlapType(isoform1);						
					}
					else {
						// Not overlapping region goes down here !
						sb1.add(String.format(ASNotExpressStringFormat, p.getAccession(),isoform1.getSplicingType(), isoform1.getStrPoint(), isoform1.getEndPoint(), isoform1.getASregionLength(), peptideStr, peptideEnd, p.getLength(), isoform1.getConfirmedByExperimentSymbol()));
					}
				}
			}
		}	
		
		private void writeToOutputDir(HashMap<String, HashSet<String>> overlapInformation, HashMap<String, String> headerInformation) throws FileNotFoundException, IOException {
			for (Entry<String, HashSet<String>> e : overlapInformation.entrySet()) {
				String isoformName = e.getKey();
				HashSet<String> information = e.getValue();
				
				StringBuilder sb = new StringBuilder();
				sb.append(comment);
				sb.append("\n");
				sb.append(headerInformation.get(isoformName));
				sb.append("\n");
				for (String s_out : information) {
					sb.append(s_out);
					sb.append("\n");
				}
				
				FileOutputStream fos = new FileOutputStream(ArgParser.getPPOPath() + "/" + isoformName);
				OutputStreamWriter osr = new OutputStreamWriter(fos);
				BufferedWriter bw = new BufferedWriter(osr);
				bw.write(sb.toString());
				bw.close();
				osr.close();
				fos.close();				
			
				counter.countNumberOfOverlappingIsoform(isoformName, information);
				counter.countIsoformOverlapType(isoformName, information);
			}
		}
		
		@Override
		public void run() {
			try {
				HashMap<String, HashSet<String>> overlapInformation = new HashMap<>();
				HashMap<String, String> headerInformation = new HashMap<>();

				if (FILEPATH.isFile()) {
					String files = FILEPATH.getName();
//					System.out.println(files);
					ASparser.parse(ArgParser.getIsoformPath() + "/" + files);
					IsoformPair[] isoform = ASparser.getIsoform();
										
					for (int j = 0; j < isoform.length; j++) {
						IsoformPair s = isoform[j];
						
						if (!overlapInformation.containsKey(s.Reference().getIsoformName()))
							overlapInformation.put(s.Reference().getIsoformName(), new HashSet<String>());
						if (!overlapInformation.containsKey(s.Variant().getIsoformName()))
							overlapInformation.put(s.Variant().getIsoformName(), new HashSet<String>());
						
						if (!headerInformation.containsKey(s.Reference().getIsoformName()))
							headerInformation.put(s.Reference().getIsoformName(), String.format(header, s.Reference().getIsoformName(), s.Reference().getIsoformType(), s.Reference().getReferenceIsoformLength()));
						if (!headerInformation.containsKey(s.Variant().getIsoformName()))
							headerInformation.put(s.Variant().getIsoformName(), String.format(header, s.Variant().getIsoformName(), s.Variant().getIsoformType(), s.Variant().getVariantIsoformLength()));
						
						processOverlappingRegion(overlapInformation.get(s.Reference().getIsoformName()), overlapInformation.get(s.Variant().getIsoformName()), s.Reference(), s.Variant());
						processOverlappingRegion(overlapInformation.get(s.Variant().getIsoformName()), null, s.Variant(), null);
					
//						for (Isoform x : s.getAllIsoform()) {
//							System.out.println(x.getIsoformName() + " " + String.valueOf(x.getStrPoint()) + " " + String.valueOf(x.getEndPoint()) + " " + x.getSplicingType() + " " + x.isOverlap());
//						}
												
						try {
							counter.countNumberOfIsoform(s.getAllIsoform());
							counter.countSplicingRegionLocation(s.getAllIsoform());
							counter.countASRegion(s.getAllIsoform());
						} catch (SplicingPositionException e) {
							System.out.println(e.getMessage());
						}						
					}					
					writeToOutputDir(overlapInformation, headerInformation);
				}
			} catch (IOException | ParserConfigurationException | SAXException e) {
				e.printStackTrace();
			}
		}
	}