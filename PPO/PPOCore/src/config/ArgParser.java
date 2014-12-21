package config;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.NotDirectoryException;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.io.FilenameUtils;

public final class ArgParser {
	private static File isoformPath;
	private static File peptidePath;
	private static File sequencePath;
	private static File outputPath;
	private static File ppoPath;
	private static File statPath;
	private static int peptideCompareLength = 5;
	private static int numThread = 8;
	
	private ArgParser() {
		// prevent create new object
	}
	
	@Override
	protected Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException();
	}
	
	private static File parseDirectory(String path) throws NotDirectoryException, FileNotFoundException {
		path = FilenameUtils.normalizeNoEndSeparator(path, true);
		File file = new File(path);
		if (path.contains("~"))
			throw (new FileNotFoundException("PLEASE use only ABSOLUTE path, DO NOT use RELATIVE path: " + path));
		if (!file.exists())
			throw (new FileNotFoundException(path));
		if (!file.isDirectory())
			throw (new NotDirectoryException(path));
		return file;
	}
	
	@SuppressWarnings("static-access")
	public static void parseArg(String[] args) throws ParseException, NotDirectoryException, FileNotFoundException {
		Options options = new Options();
		
		options.addOption(OptionBuilder.withLongOpt("altseq")
				.withDescription("directory of the alternative splicing")
				.hasArg()
				.isRequired()
				.withArgName("dir")
				.create("a"));
		
		options.addOption(OptionBuilder.withLongOpt("peptide")
				.withDescription("directory of peptide sequence, which was performed by BlastP program")
				.hasArg()
				.isRequired()
				.withArgName("dir")
				.create("p"));

		options.addOption(OptionBuilder.withLongOpt("seq")
				.withDescription("file of amino sequence (both reference and variant corresponding with -a,--altseq) in fasta format")
				.hasArg()
				.isRequired()
				.withArgName("file")
				.create("s"));
		
		options.addOption(OptionBuilder.withLongOpt("out")
				.withDescription("output directory")
				.hasArg()
				.isRequired()
				.withArgName("dir")
				.create("o"));
	
		options.addOption(OptionBuilder.withLongOpt("np")
				.withDescription("number of thread (default is 8)")
				.hasArg()
				.withArgName("value")
				.create("n"));

		options.addOption(OptionBuilder.withLongOpt("region")
				.withDescription("number of overlapping region between peptide and AS region (default is 5)")
				.hasArg()
				.withArgName("size")
				.create("r"));

		options.addOption(OptionBuilder.withLongOpt("help")
				.withDescription("print this message")
				.hasArg(false)
				.create("h"));

		CommandLineParser parser = new BasicParser();
		HelpFormatter formatter = new HelpFormatter();
		
		try {
			CommandLine cmd = parser.parse(options, args);
			for (Option o : cmd.getOptions()) {
				switch(o.getOpt()) {
					case "altseq":
					case "a":
						isoformPath = parseDirectory(o.getValue());
					break;
					case "peptide":
					case "p":
						peptidePath = parseDirectory(o.getValue());
					break;
					case "seq":
					case "s":
						sequencePath = parseDirectory(o.getValue());
					break;
					case "out":
					case "o":
						outputPath = parseDirectory(o.getValue());
						ppoPath = parseDirectory(outputPath.toString().concat("/ppo"));
						statPath = parseDirectory(outputPath.toString().concat("/stat"));
					break;
					case "region":
					case "r":
						peptideCompareLength = Integer.parseInt(o.getValue(Integer.toString(peptideCompareLength)));
					break;
					case "np":
					case "n":
						numThread = Integer.parseInt(o.getValue(Integer.toString(numThread)));
					break;
				}
			}
		} catch (ParseException e) {
			System.err.println("PPO.jar: " + e.getMessage());
			formatter.printHelp("PPO.jar", options, true);
			throw (e);
		}
	}

	public static final File getIsoformPath() {
		return isoformPath;
	}

	public static final File getPeptidePath() {
		return peptidePath;
	}

	public static final File getSequencePath() {
		return sequencePath;
	}

	public static final File getOutputPath() {
		return outputPath;
	}

	public static final File getPpoPath() {
		return ppoPath;
	}

	public static final File getStatPath() {
		return statPath;
	}

	public static final int getPeptideCompareLength() {
		return peptideCompareLength;
	}

	public static final int getNumThread() {
		return numThread;
	}
}