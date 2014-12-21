package sequence.reader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

public class SequenceDirectoryReader {
	
	private static String[] fileRead(File f) throws IOException {
		FileInputStream fos = new FileInputStream(f);
		InputStreamReader osr = new InputStreamReader(fos);
		BufferedReader bw = new BufferedReader(osr);
		String line = null;
		String proteinName = null;
		String sequence = null;
		while((line = bw.readLine()) != null) {
			if (line.startsWith(">")) {
				proteinName = line.replaceFirst(">", "");
			}
			else {
				sequence = line;
			}
		}
		bw.close();
		osr.close();
		fos.close();
		return new String[] {proteinName, sequence};
	}
	
	public static HashMap<String, String> getSequenceFromDir(String dir) throws IOException {
		System.out.println(dir);
		HashMap<String, String> out = new HashMap<String, String>();
		File folder = new File(dir);
		File[] listOfFiles = folder.listFiles();
		for (int i = 0; i < listOfFiles.length; i++) {
			System.out.println(listOfFiles[i]);
			String[] o = fileRead(listOfFiles[i]);
			out.put(o[0], o[1]);
		}
		return out;
	}
	
	public static void main(String[] args) {
	}
}