package sequence.reader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

public class SequenceFileReader {
	public static HashMap<String, String> getSequenceFromFile(File file) throws IOException {
		HashMap<String, String> out = new HashMap<String, String>();
		FileInputStream fos = null;
		try {
			fos = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			throw new FileNotFoundException("File cannot be openned to read sequences in fasta format: " + e.getMessage());
		}
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
				out.put(proteinName, sequence);
			}
		}
		bw.close();
		osr.close();
		fos.close();
		return out;
	}
}