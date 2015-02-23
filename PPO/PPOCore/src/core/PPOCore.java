package core;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.cli.ParseException;

import report.Counter;
import report.NumberAggregator;
import sequence.compare.CompareSequenceType;
import config.ArgParser;

public class PPOCore {
	
	private static void writeStatFiles() throws IOException {
		// ---- END State ----
		System.out.println("Start writing statistical data...");
		NumberAggregator.writeReportFile(ArgParser.getStatPath());
	}
	
	public static void main(String[] args) {
		try {
			// ---- Preprocessing State ----
			ArgParser.parseArg(args);
			System.out.println("Preprocessing...");
			CompareSequenceType.initialFromFile(ArgParser.getSequencePath());
			
			// ---- Alignment State ----
			if (ArgParser.getAlignState()) {
				System.out.println("Start Alignment...");
				File[] isoformFileList = ArgParser.getIsoformPath().listFiles();
				ExecutorService executor = Executors.newFixedThreadPool(ArgParser.getNumThread());
				int nFiles = isoformFileList.length;
				if (ArgParser.getDebugMode() > 0)
					nFiles = ArgParser.getDebugMode();
				for (int i = 0; i < nFiles; i++) {
					Runnable subCore = new PPOCoreSubprocessUnit(isoformFileList[i]);
					executor.execute(subCore);
				}
				executor.shutdown();
				while (!executor.isTerminated());
				writeStatFiles();
			}
			// ---- Counting State ----
			if (ArgParser.getAlignState() == false && ArgParser.getCountingState()) {
				System.out.println("Start Counting...");
				File[] outputFileList = ArgParser.getPPOPath().listFiles();
				ExecutorService executor2 = Executors.newFixedThreadPool(ArgParser.getNumThread());
				for (int i = 0; i < outputFileList.length; i++) {
					Runnable counter = new Counter(outputFileList[i]);
					executor2.execute(counter);
				}
				executor2.shutdown();
				while (!executor2.isTerminated());
				writeStatFiles();
			}
		} catch (ParseException e) {
			//
		} catch (IOException e) {
			e.printStackTrace();
		}
		// Complete!
		System.out.println("Complete!");
	}
}