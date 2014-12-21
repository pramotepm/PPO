package core.subprocess;

public class Worker_OLD implements Runnable {
//		private File FILEPATH;
//		private final String isoformPath = Config.isoformPath;
//		private final String peptidePath = Config.peptidePath;
//		private final String outputPath = Config.outputPath;
//		ASParser ASparser = null;
//		BlastpParser bp = null;
//		
//		public Worker_temp(File FILEPATH) {
//			ASparser = new ASParser();
//			bp = new BlastpParser();
//			this.FILEPATH = FILEPATH;
//		}
//		
		@Override
		public void run() {
//			try {
//				if (FILEPATH.isFile()) {
//					String files = FILEPATH.getName();
////					System.out.println(files);		
//					ASparser.parse(isoformPath + "/" + files);
//					Isoform[] isoform = ASparser.getIsoform();
//					for (int j = 0; j < isoform.length; j++) {
//						Isoform s = isoform[j];
//
//						FileOutputStream fos = new FileOutputStream(outputPath.concat("/" + s.getAccession()));
//						OutputStreamWriter osr = new OutputStreamWriter(fos);
//						BufferedWriter bw = new BufferedWriter(osr);
//
//						StringBuilder sb = new StringBuilder();
//						sb.append(s.getAccession());
//						sb.append(" ");
//						if (new File(peptidePath + "/" + s.getAccession()).exists()) {
//
////							System.out.println(peptidePath + "/" + s.getAccession());
//							bp.parse(peptidePath + "/" + s.getAccession());
////							System.out.println(bp.getProteinID());
//							for (int k = 0; k < s.getNumberOfASRegion(); k++) {
//								int isoformStr = s.getIsoformStartRegionAtIndex(k);
//								int isoformEnd = s.getIsoformEndRegionAtIndex(k);
//								for (Peptide p : bp.getPeptides()) {
//									int peptideStr = p.getAlign_start();
//									int peptideEnd = p.getAlign_end();
////									System.out.println("Isoform : " + isoformStr + " " + isoformEnd);
////									System.out.println("Peptide : " + peptideStr + " " + peptideEnd);
//									
//									
////									if (CompareRegionType.isOverlap(isoformStr, isoformEnd, peptideStr, peptideEnd)) {
////										bw.write(p.getAccession().concat("\n"));
////										sb.append(p.getAccession());
////										sb.append(" ");
////									}
//									
//									
//								}
//							}
//						}
//
//						bw.close();
//						osr.close();
//						fos.close();
//
//						System.out.println(sb.toString());						
//					}
//				}
//			} catch (SAXException | IOException | ParserConfigurationException e) {
//				e.printStackTrace();
//			}
		}
	}