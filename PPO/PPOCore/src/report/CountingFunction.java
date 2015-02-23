package report;

import java.util.Collection;
import java.util.HashSet;

import report.NumberAggregator.Variable;
import sequence.compare.SplicingPositionException;
import structures.IsoformPair.Isoform;
import structures.type.Type.IsoformType;
import structures.type.Type.SplicingType;

public class CountingFunction {		
	HashSet<String> duplicatedOverlapType;
	HashSet<String> ASRegionPoolCheck;
	HashSet<String> ASOverlapRegionPoolCheck;
	HashSet<String> isoformNamePoolCheck;
	HashSet<String> countSplicingRegionLocationPool;
	
	public CountingFunction() {
		duplicatedOverlapType = new HashSet<>();
		ASRegionPoolCheck = new HashSet<>();
		isoformNamePoolCheck = new HashSet<>();
		countSplicingRegionLocationPool = new HashSet<>();
		ASOverlapRegionPoolCheck = new HashSet<>();
	}
	
	//**
	private void _countASRegion(String isoformName, int regionoStrPnt, int regionEndPnt, IsoformType isoformType, boolean isOverlap) {
		String key = createKey(isoformName, String.valueOf(regionoStrPnt), String.valueOf(regionEndPnt));
		if (!ASRegionPoolCheck.contains(key)) {
			ASRegionPoolCheck.add(key);
			switch (isoformType) {
				case REF:
					NumberAggregator.addValue(NumberAggregator.Variable.C3_NUM_OF_REF_REGION, 1);
				break;
				case VAR:
					NumberAggregator.addValue(NumberAggregator.Variable.C3_NUM_OF_VAR_REGION, 1);
				break;
			}
			NumberAggregator.addValue(NumberAggregator.Variable.C3_NUM_OF_REGION, 1);
		}
		
		if (isOverlap) {
			String key2 = createKey(isoformName, String.valueOf(regionoStrPnt), String.valueOf(regionEndPnt));
			if (!ASOverlapRegionPoolCheck.contains(key2)) {
//				System.out.println(isoformName + " " + String.valueOf(regionoStrPnt) + " " + String.valueOf(regionEndPnt));
				ASOverlapRegionPoolCheck.add(key2);
				switch (isoformType) {
					case REF:
						NumberAggregator.addValue(NumberAggregator.Variable.C3_NUM_OF_OVERLAP_REF_REGION, 1);
					break;
					case VAR:						
						NumberAggregator.addValue(NumberAggregator.Variable.C3_NUM_OF_OVERLAP_VAR_REGION, 1);
					break;
				}
			}
		}
	}
	
	private void _countNumberOfIsoform(String isoformName, IsoformType isoformType) {
		if (!isoformNamePoolCheck.contains(isoformName)) {
			isoformNamePoolCheck.add(isoformName);
			switch (isoformType) {
				case REF:
					NumberAggregator.addValue(NumberAggregator.Variable.C1_NUM_OF_REF_ISOFORM, 1);
				break;
				case VAR:
					NumberAggregator.addValue(NumberAggregator.Variable.C1_NUM_OF_VAR_ISOFORM, 1);
				break;
			}
		}		
	}
	
	private void _countRegionOverlapType(String isoformName, int regionStrPnt, int regionEndPnt, SplicingType splicingType, IsoformType isoformType, boolean isOverlap) {
		if (isOverlap) {
			String key = createKey(isoformName, String.valueOf(regionStrPnt), String.valueOf(regionEndPnt), splicingType.toString());
			if (!duplicatedOverlapType.contains(key)) {
				duplicatedOverlapType.add(key);
				switch (isoformType) {
					case REF:
						switch (splicingType) {
							case ALT:
								NumberAggregator.addValue(Variable.C4_REF_OVERLAP_REGION_SUB_TYPE, 1);
								break;
							case DEL:
								NumberAggregator.addValue(Variable.C4_REF_OVERLAP_REGION_DEL_TYPE, 1);
								break;
							case INS:
								NumberAggregator.addValue(Variable.C4_REF_OVERLAP_REGION_INS_TYPE, 1);
								break;
						}
					break;
					case VAR:
						switch (splicingType) {
							case ALT:
								NumberAggregator.addValue(Variable.C4_VAR_OVERLAP_REGION_SUB_TYPE, 1);
								break;
							case DEL:
								NumberAggregator.addValue(Variable.C4_VAR_OVERLAP_REGION_DEL_TYPE, 1);
								break;
							case INS:
								NumberAggregator.addValue(Variable.C4_VAR_OVERLAP_REGION_INS_TYPE, 1);
								break;
						}
					break;
				}
			}
		}
	}

	private void _countSplicingRegionLocation(IsoformType isoformType, int regionStrPnt, int regionEndPnt, int seqLength, String isoformName, boolean isOverlap) throws SplicingPositionException {
		String key = createKey(isoformName, String.valueOf(regionStrPnt), String.valueOf(regionEndPnt));
		if (isOverlap && !countSplicingRegionLocationPool.contains(key)) {
			countSplicingRegionLocationPool.add(key);
			switch (isoformType) {
				case REF:
					if (regionStrPnt == 1 && regionEndPnt == seqLength) {
						NumberAggregator.addValue(Variable.C5_REF_OVERLAP_REGION_POSITION_ALL_REGION, 1);
						throw new SplicingPositionException(String.format("Isoform [%s] has AS, position starting at [%d] to [%d], which its length equals isoform's length.", isoformName, regionStrPnt, regionEndPnt));
					}
					else if (regionStrPnt == 1 && regionEndPnt != seqLength)
						NumberAggregator.addValue(Variable.C5_REF_OVERLAP_REGION_POSITION_N_TERMINAL, 1);
					else if (regionStrPnt != 1 && regionEndPnt == seqLength)
						NumberAggregator.addValue(Variable.C5_REF_OVERLAP_REGION_POSITION_C_TERMINAL, 1);
					else
						NumberAggregator.addValue(Variable.C5_REF_OVERLAP_REGION_POSITION_MIDDLE, 1);
				break;
				case VAR:
					if (regionStrPnt == 1 && regionEndPnt == seqLength) {
						NumberAggregator.addValue(Variable.C5_VAR_OVERLAP_REGION_POSITION_ALL_REGION, 1);
						throw new SplicingPositionException(String.format("Isoform [%s] has AS, position starting at [%d] to [%d], which its length equals isoform's length.", isoformName, regionStrPnt, regionEndPnt));
					}
					else if (regionStrPnt == 1 && regionEndPnt != seqLength)
						NumberAggregator.addValue(Variable.C5_VAR_OVERLAP_REGION_POSITION_N_TERMINAL, 1);
					else if (regionStrPnt != 1 && regionEndPnt == seqLength)
						NumberAggregator.addValue(Variable.C5_VAR_OVERLAP_REGION_POSITION_C_TERMINAL, 1);
					else
						NumberAggregator.addValue(Variable.C5_VAR_OVERLAP_REGION_POSITION_MIDDLE, 1);
				break;
			}
		}
	}
	
	public void countASRegion(Isoform[] s) {
		for (Isoform sElement : s) {
			_countASRegion(sElement.getIsoformName(), sElement.getStrPoint(), sElement.getEndPoint(), sElement.getIsoformType(), sElement.isOverlap());
		}
	}	
	
	public void countASRegion(String isoformName, int regionoStrPnt, int regionEndPnt, IsoformType isoformType, boolean isOverlap) {
		_countASRegion(isoformName, regionoStrPnt, regionEndPnt, isoformType, isOverlap);
	}

	public void countIsoformOverlapType(String isoformName, Collection<String> overlappingRecords) {
		boolean ALT = false,
				DEL = false,
				INS = false;
		for (String record : overlappingRecords) {
			if (record.endsWith("\t+")) {
				if (record.contains("\tALT\t"))
					ALT = true;
				else if (record.contains("\tDEL\t"))
					DEL = true;
				else if (record.contains("\tINS\t"))
					INS = true;
			}
		}
		if (!isoformName.contains(".")) {
			if (ALT && DEL && INS)
				NumberAggregator.addValue(NumberAggregator.Variable.C2_REF_ISOFORM_OVERLAP_TYPE_ALT_DEL_INS, 1);
			else if (ALT && DEL)
				NumberAggregator.addValue(NumberAggregator.Variable.C2_REF_ISOFORM_OVERLAP_TYPE_ALT_DEL, 1);
			else if (DEL && INS)
				NumberAggregator.addValue(NumberAggregator.Variable.C2_REF_ISOFORM_OVERLAP_TYPE_DEL_INS, 1);
			else if (INS && ALT)
				NumberAggregator.addValue(NumberAggregator.Variable.C2_REF_ISOFORM_OVERLAP_TYPE_ALT_INS, 1);
			else if (ALT)
				NumberAggregator.addValue(NumberAggregator.Variable.C2_REF_ISOFORM_OVERLAP_TYPE_ALT, 1);
			else if (DEL)
				NumberAggregator.addValue(NumberAggregator.Variable.C2_REF_ISOFORM_OVERLAP_TYPE_DEL, 1);
			else if (INS)
				NumberAggregator.addValue(NumberAggregator.Variable.C2_REF_ISOFORM_OVERLAP_TYPE_INS, 1);
		} 
		else {
			if (ALT && DEL && INS)
				NumberAggregator.addValue(NumberAggregator.Variable.C2_VAR_ISOFORM_OVERLAP_TYPE_ALT_DEL_INS, 1);
			else if (ALT && DEL)
				NumberAggregator.addValue(NumberAggregator.Variable.C2_VAR_ISOFORM_OVERLAP_TYPE_ALT_DEL, 1);
			else if (DEL && INS)
				NumberAggregator.addValue(NumberAggregator.Variable.C2_VAR_ISOFORM_OVERLAP_TYPE_DEL_INS, 1);
			else if (INS && ALT)
				NumberAggregator.addValue(NumberAggregator.Variable.C2_VAR_ISOFORM_OVERLAP_TYPE_ALT_INS, 1);
			else if (ALT)
				NumberAggregator.addValue(NumberAggregator.Variable.C2_VAR_ISOFORM_OVERLAP_TYPE_ALT, 1);
			else if (DEL)
				NumberAggregator.addValue(NumberAggregator.Variable.C2_VAR_ISOFORM_OVERLAP_TYPE_DEL, 1);
			else if (INS)
				NumberAggregator.addValue(NumberAggregator.Variable.C2_VAR_ISOFORM_OVERLAP_TYPE_INS, 1);				
		}
	}
	
	public void countNumberOfIsoform(Isoform[] s) {
		for (Isoform _s : s) {
			_countNumberOfIsoform(_s.getIsoformName(), _s.getIsoformType());
		}
	}
	
	public void countNumberOfIsoform(String isoformName, IsoformType isoformType) {
		_countNumberOfIsoform(isoformName, isoformType);
	}
	
	public void countNumberOfOverlappingIsoform(String isoformName, Collection<String> overlappingRecords) {
		for (String record : overlappingRecords) {
			if (record.endsWith("+")) {
				if (isoformName.contains("."))
					NumberAggregator.addValue(NumberAggregator.Variable.C1_NUM_OF_VAR_OVERLAP_ISOFORM, 1);
				else
					NumberAggregator.addValue(NumberAggregator.Variable.C1_NUM_OF_REF_OVERLAP_ISOFORM, 1);
				return;
			}
		}
		if (isoformName.contains("."))
			NumberAggregator.addValue(NumberAggregator.Variable.C1_NUM_OF_VAR_NOT_OVERLAP_ISOFORM, 1);
		else
			NumberAggregator.addValue(NumberAggregator.Variable.C1_NUM_OF_REF_NOT_OVERLAP_ISOFORM, 1);							
	}
	
	public void countRegionOverlapType(Isoform isoform) {
		_countRegionOverlapType(isoform.getIsoformName(), 
								isoform.getStrPoint(), 
								isoform.getEndPoint(), 
								isoform.getSplicingType(),
								isoform.getIsoformType(),
								isoform.isOverlap());
	}
	
	public void countRegionOverlapType(String isoformName, int regionStrPnt, int regionEndPnt, SplicingType splicingType, IsoformType isoformType, boolean isOverlap) {
		_countRegionOverlapType(isoformName, regionStrPnt, regionEndPnt, splicingType, isoformType, isOverlap);
	}

	public void countSpecialCaseRegionOverlapType(SplicingType type) {
		switch (type) {
			case ALT:
				NumberAggregator.addValue(Variable.C4_VAR_OVERLAP_REGION_SPECIAL_SUB_TYPE, 1);
			break;
			case DEL:
				NumberAggregator.addValue(Variable.C4_VAR_OVERLAP_REGION_SPECIAL_DEL_TYPE, 1);
			break;
			case INS:
				NumberAggregator.addValue(Variable.C4_VAR_OVERLAP_REGION_SPECIAL_INS_TYPE, 1);
			break;
		}
	}
	
	public void countSplicingRegionLocation(Isoform[] s) throws SplicingPositionException {
		for (Isoform sElement : s) {
			IsoformType isoformType = sElement.getIsoformType();
			int ASStartPos = sElement.getStrPoint();
			int ASEndPos = sElement.getEndPoint();
			int seqLength = sElement.getIsoformType() == IsoformType.REF ? sElement.getReferenceIsoformLength() : sElement.getVariantIsoformLength();
			String isoformName = sElement.getIsoformName();
			boolean isOverlap = sElement.isOverlap();
			_countSplicingRegionLocation(isoformType, ASStartPos, ASEndPos, seqLength, isoformName, isOverlap);
		}
	}
	
	public void countSplicingRegionLocation(IsoformType ist, int regionStr, int regionEnd, int regionLen, String isoformName, boolean isOverlap) throws SplicingPositionException {
		_countSplicingRegionLocation(ist, regionStr, regionEnd, regionLen, isoformName, isOverlap);
	}

	private String createKey(String... k) {
		StringBuilder b = new StringBuilder();
		for (String s : k) {
			b.append(s);
			b.append("+");
		}
		return b.toString();
	}
}