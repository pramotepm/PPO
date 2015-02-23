package report;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.io.FilenameUtils;

public class NumberAggregator {
	public static enum Variable {
		// Number of Ref/Var Isoform
		C1_NUM_OF_REF_ISOFORM,
		C1_NUM_OF_VAR_ISOFORM,
		
		C1_NUM_OF_REF_OVERLAP_ISOFORM,
		C1_NUM_OF_VAR_OVERLAP_ISOFORM,
		
		C1_NUM_OF_REF_NOT_OVERLAP_ISOFORM,
		C1_NUM_OF_VAR_NOT_OVERLAP_ISOFORM,
		
		// Number of Ref/Var Isoform Splicing Type
		C2_VAR_ISOFORM_OVERLAP_TYPE_ALT_DEL_INS,
		C2_VAR_ISOFORM_OVERLAP_TYPE_ALT_DEL,
		C2_VAR_ISOFORM_OVERLAP_TYPE_DEL_INS,
		C2_VAR_ISOFORM_OVERLAP_TYPE_ALT_INS,
		C2_VAR_ISOFORM_OVERLAP_TYPE_ALT,
		C2_VAR_ISOFORM_OVERLAP_TYPE_DEL,
		C2_VAR_ISOFORM_OVERLAP_TYPE_INS,
		
		C2_REF_ISOFORM_OVERLAP_TYPE_ALT_DEL_INS,
		C2_REF_ISOFORM_OVERLAP_TYPE_ALT_DEL,
		C2_REF_ISOFORM_OVERLAP_TYPE_DEL_INS,
		C2_REF_ISOFORM_OVERLAP_TYPE_ALT_INS,
		C2_REF_ISOFORM_OVERLAP_TYPE_ALT,
		C2_REF_ISOFORM_OVERLAP_TYPE_DEL,
		C2_REF_ISOFORM_OVERLAP_TYPE_INS,
		
		// Number of Ref/Var Region
		C3_NUM_OF_REGION,
		C3_NUM_OF_REF_REGION,
		C3_NUM_OF_VAR_REGION,
		C3_NUM_OF_OVERLAP_REF_REGION,
		C3_NUM_OF_OVERLAP_VAR_REGION,
		
		// Number of Ref/Var Region Splicing Type
		C4_REF_OVERLAP_REGION_DEL_TYPE,
		C4_REF_OVERLAP_REGION_INS_TYPE,
		C4_REF_OVERLAP_REGION_SUB_TYPE,
		
		C4_VAR_OVERLAP_REGION_DEL_TYPE,
		C4_VAR_OVERLAP_REGION_INS_TYPE,
		C4_VAR_OVERLAP_REGION_SUB_TYPE,
		
		C4_VAR_OVERLAP_REGION_SPECIAL_DEL_TYPE,
		C4_VAR_OVERLAP_REGION_SPECIAL_INS_TYPE,
		C4_VAR_OVERLAP_REGION_SPECIAL_SUB_TYPE,
		
		// Number of Ref/Var Region Splicing Position
		C5_REF_OVERLAP_REGION_POSITION_N_TERMINAL,
		C5_REF_OVERLAP_REGION_POSITION_C_TERMINAL,
		C5_REF_OVERLAP_REGION_POSITION_MIDDLE,
		C5_REF_OVERLAP_REGION_POSITION_ALL_REGION,
		
		C5_VAR_OVERLAP_REGION_POSITION_N_TERMINAL,
		C5_VAR_OVERLAP_REGION_POSITION_C_TERMINAL,
		C5_VAR_OVERLAP_REGION_POSITION_MIDDLE,
		C5_VAR_OVERLAP_REGION_POSITION_ALL_REGION		
	}

	private NumberAggregator() {
		
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