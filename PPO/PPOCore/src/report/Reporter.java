package report;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.io.FilenameUtils;

public class Reporter {
	public static enum Variable {
		NUM_OF_REF,			// int
		NUM_OF_VAR,			// int
		REF_OVERLAP,		// int
		VAR_OVERLAP,		// int
		REF_NOT_OVERLAP,	// int
		VAR_NOT_OVERLAP		// int
	}

	private Reporter() {
		//
	}
	
	private static HashMap<Variable, AtomicInteger> val = new HashMap<>();
	
	@Override
	protected Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException();
	}
	
	public synchronized static void addValue(Variable v, int value) {
		if (val.get(v) == null)
			val.put(v, new AtomicInteger());
		val.get(v).addAndGet(value);
	}
	
	public static void writeReportFile(String filePath) throws IOException {
		writeReportFile(new File(filePath));
	}
	
	public static void writeReportFile(File outputDirectory) throws IOException {
		for (Variable v : Variable.values()) {
			FileOutputStream fos = new FileOutputStream(new File(FilenameUtils.normalizeNoEndSeparator(outputDirectory.toString(), true).concat("/" + v.toString() + ".stat")));
			OutputStreamWriter osr = new OutputStreamWriter(fos);
			BufferedWriter bw = new BufferedWriter(osr);
			if (val.get(v) != null)
				bw.write(val.get(v).get() + "\n");
			else
				bw.write("0\n");
			bw.close();
			osr.close();
			fos.close();
		}
	}
}