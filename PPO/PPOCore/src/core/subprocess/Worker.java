package core.subprocess;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import parser.ASParser;
import parser.BlastpParser;
import report.Reporter;
import sequence.compare.CompareRegionType;
import sequence.compare.CompareSequenceType;
import structures.IsoformPair;
import structures.IsoformPair.Isoform;
import structures.Peptide;
import structures.type.Type;
import config.ArgParser;

public class Worker implements Runnable {
		private File FILEPATH;
		ASParser ASparser = null;
		BlastpParser blastp_parser = null;
		
		public Worker(File FILEPATH) {
			ASparser = new ASParser();
			blastp_parser = new BlastpParser();
			this.FILEPATH = FILEPATH;
		}
		
		private void addOverlap(StringBuilder sb1, StringBuilder sb2, Isoform isoform1, Isoform isoform2) throws ParserConfigurationException, SAXException, IOException {
			String peptideFilePath = ArgParser.getPeptidePath() + "/" + isoform1.getIsoformName();
			CompareRegionType cmpRegion = new CompareRegionType(ArgParser.getPeptideCompareLength());
			if (new File(peptideFilePath).exists()) {
				blastp_parser.parse(peptideFilePath);
				int isoformStr = isoform1.getStrPoint();
				int isoformEnd = isoform1.getEndPoint();
				for (Peptide p : blastp_parser.getPeptides()) {
					int peptideStr = p.getAlign_start();
					int peptideEnd = p.getAlign_end();
					cmpRegion.chkOverlap(isoformStr, isoformEnd,	peptideStr, peptideEnd);					
					if (cmpRegion.isOverlap()) {
						if (isoform1.getIsoformType() == Type.IsoformType.REF) {
							String x = String.format("%d %d %d %d",isoformStr, isoformEnd,	peptideStr, peptideEnd);
							if (CompareSequenceType.chkAfterSplice(isoform1, isoform2, peptideStr, peptideEnd, cmpRegion.getOverlapSide(), x))
								sb2.append(String.format("%s\t%s\t%d\t%d\t%d\t%d\t%d\t%d*\n", p.getAccession(),isoform2.getSplicingType(), isoform2.getStrPoint(), isoform2.getEndPoint(), isoform2.getASregionLength(), peptideStr, peptideEnd, p.getLength()));
						}
						sb1.append(String.format("%s\t%s\t%d\t%d\t%d\t%d\t%d\t%d\n", p.getAccession(),isoform1.getSplicingType(), isoform1.getStrPoint(), isoform1.getEndPoint(), isoform1.getASregionLength(), peptideStr, peptideEnd, p.getLength()));
					}
				}
			}
		}
		
		@Override
		public void run() {
			try {
				HashMap<String, StringBuilder> overlapInformation = new HashMap<String, StringBuilder>(); 
				if (FILEPATH.isFile()) {
					String files = FILEPATH.getName();
					System.out.println(files);
					ASparser.parse(ArgParser.getIsoformPath() + "/" + files);
					IsoformPair[] isoform = ASparser.getIsoform();
					
					// Collect statistical information					
					if (isoform.length > 0)
						Reporter.addValue(Reporter.Variable.NUM_OF_REF, 1);					
					
					
					
					for (int j = 0; j < isoform.length; j++) {
						IsoformPair s = isoform[j];
						
						if (!overlapInformation.containsKey(s.Reference().getIsoformName()))
							overlapInformation.put(s.Reference().getIsoformName(), new StringBuilder());
						if (!overlapInformation.containsKey(s.Variant().getIsoformName()))
							overlapInformation.put(s.Variant().getIsoformName(), new StringBuilder());
						
						addOverlap(overlapInformation.get(s.Reference().getIsoformName()), overlapInformation.get(s.Variant().getIsoformName()), s.Reference(), s.Variant());
						addOverlap(overlapInformation.get(s.Variant().getIsoformName()), null, s.Variant(), null);
						
						// Collect statistical information						
						Reporter.addValue(Reporter.Variable.NUM_OF_VAR, 1);
					}
					
					/*
					 * write information of peptide that overlapped with protein to a file
					 */
					for (Entry<String, StringBuilder> e : overlapInformation.entrySet()) {
						String ASName = e.getKey();
						String information = e.getValue().toString();
						
						// Collect statistical information
						if (information.length() > 0) {
							// The ISOFORM which its name contains "." is VARIANT ISOFORM.
							if (ASName.contains("."))
								Reporter.addValue(Reporter.Variable.VAR_OVERLAP, 1);
							else
								Reporter.addValue(Reporter.Variable.REF_OVERLAP, 1);
						}
						else {
							if (ASName.contains("."))
								Reporter.addValue(Reporter.Variable.VAR_NOT_OVERLAP, 1);
							else
								Reporter.addValue(Reporter.Variable.REF_NOT_OVERLAP, 1);							
						}
						
						FileOutputStream fos = new FileOutputStream(ArgParser.getPpoPath() + "/" + ASName);
						OutputStreamWriter osr = new OutputStreamWriter(fos);
						BufferedWriter bw = new BufferedWriter(osr);
						bw.write(information);
						bw.close();
						osr.close();
						fos.close();
					}
				
					Reporter.writeReportFile(ArgParser.getStatPath());
				}
			} catch (IOException | ParserConfigurationException | SAXException e) {
				e.printStackTrace();
			}
		}
	}