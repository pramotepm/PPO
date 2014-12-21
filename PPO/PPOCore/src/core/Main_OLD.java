package core;

public class Main_OLD {
	public static void main(String[] args) {
//		BlastpParser bp = new BlastpParser();
//		ASParser ASparser = new ASParser();
//		try {
//			String isoformPath = "/Users/pramotepm/Desktop/pas";
//			String peptidePath = "/Users/pramotepm/Desktop/as_seq_out";
//			String outputPath = "/Users/pramotepm/Desktop/express_out_S";
//			
//			String files;
//			File folder = new File(isoformPath);
//			File[] listOfFiles = folder.listFiles();
//			int i = 0;
////					while (!listOfFiles[i].getName().equals("C1QT6_HUMAN.pas")) {
////						i++;
////					}
//			
////			ExecutorService executor = Executors.newFixedThreadPool(8);
//			
//			for (; i < listOfFiles.length; i++) {
//				if (listOfFiles[i].isFile()) {
//					files = listOfFiles[i].getName();
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
////							System.out.println(peptidePath + "/" + s.getAccession());
//							bp.parse(peptidePath + "/" + s.getAccession());
////							System.out.println(bp.getProteinID());
////							for (int k = 0; k < s.getNumberOfASRegion(); k++) {
////								int isoformStr = s.getIsoformStartRegionAtIndex(k);
////								int isoformEnd = s.getIsoformEndRegionAtIndex(k);
////								for (Peptide p : bp.getPeptides()) {
////									int peptideStr = p.getAlign_start();
////									int peptideEnd = p.getAlign_end();
////									System.out.println("Isoform : " + isoformStr + " " + isoformEnd);
////									System.out.println("Peptide : " + peptideStr + " " + peptideEnd);
////									if (CompareRegionType.isOverlap(isoformStr, isoformEnd, peptideStr, peptideEnd)) {
////										bw.write(p.getAccession().concat("\n"));
////										sb.append(p.getAccession());
////										sb.append(" ");
////									}
//								}
//							}
//						}
////						
////						bw.close();
////						osr.close();
////						fos.close();
//						
//						System.out.println(sb.toString());						
//					}
//				}
////				Runnable worker = new Worker(listOfFiles[i]);
////				executor.execute(worker);
//			}
////			executor.shutdown();
////			while (!executor.isTerminated());
//			System.out.println("END");
//		}
//		catch (SAXException | IOException | ParserConfigurationException e) {
//			e.printStackTrace();
//		}
	}
}