package core;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.cli.ParseException;

import sequence.compare.CompareSequenceType;
import config.ArgParser;
import core.subprocess.Worker;

public class PPOCore {
	public static void main(String[] args) {
		try {
			ArgParser.parseArg(args);
			CompareSequenceType.initialFromFile(ArgParser.getSequencePath());
			System.out.println("START");
			File[] listOfFiles = ArgParser.getIsoformPath().listFiles();
			int i = 0;
			ExecutorService executor = Executors.newFixedThreadPool(8);
			for (; i < listOfFiles.length; i++) {
				//for (; i < 100; i++) {
				Runnable worker = new Worker(listOfFiles[i]);
				executor.execute(worker);
			}
			executor.shutdown();
			while (!executor.isTerminated());
			System.out.println("END");
		} catch (ParseException e) {
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}