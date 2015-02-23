package config;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.NotDirectoryException;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.io.FilenameUtils;

public final class ArgParser {
	private static final int defaultnumThread = 8;
	private static final int defaultPeptideCompareLength = 5;

	private static File isoformPath;
	private static File peptidePath;
	private static File sequencePath;
	private static File outputPath;
	private static File ppoPath;
	private static File statPath;
	private static int peptideCompareLength = defaultPeptideCompareLength;
	private static int numThread = defaultnumThread;
	private static boolean isAlign = false;
	private static boolean isCount = false;
	private static int nOpenFiles = -1;
	
	// Prevent create new object
	private ArgParser() {
	}
	
	// Prevent cloning object
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
		Options helpOp = new Options();
		
		helpOp.addOption(OptionBuilder.withLongOpt("help")
				.withDescription("print this message")
				.hasArg(false)
				.create("h"));
		
		Options options = new Options();
		
		options.addOption(OptionBuilder.withLongOpt("altseq")
				.withDescription("directory contains the splicing information")
				.hasArg()
				.isRequired()
				.withArgName("dir")
				.create("a"));
		
		options.addOption(OptionBuilder.withLongOpt("peptide")
				.withDescription("directory contains peptide identified files, which were performed by BlastP program")
				.hasArg()
				.isRequired()
				.withArgName("dir")
				.create("p"));

		options.addOption(OptionBuilder.withLongOpt("seq")
				.withDescription("file contains all amino sequences (both \"reference\" and \"variant\" isoform corresponding with -a,--altseq) in FASTA format")
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
				.withDescription(String.format("number of thread (default is %d)", defaultnumThread))
				.hasArg()
				.withArgName("value")
				.create("n"));

		options.addOption(OptionBuilder.withLongOpt("region")
				.withDescription(String.format("number of overlapping region between peptide and AS region (default is %d)", defaultPeptideCompareLength))
				.hasArg()
				.withArgName("size")
				.create("r"));
		
		options.addOption(OptionBuilder.withLongOpt("debug")
				.withDescription(String.format("number of overlapping region between peptide and AS region (default is %d)", defaultPeptideCompareLength))
				.hasArg()
				.withArgName("n")
				.create());

		OptionGroup optionGroup = new OptionGroup();
		
		optionGroup.setRequired(true);
		optionGroup.addOption(OptionBuilder.withLongOpt("align-with-peptide")
				.withDescription("Program will start at the state of alignment of Alternatice Splicing and Peptides sequences")
				.hasArg(false)
				.create());
		optionGroup.addOption(OptionBuilder.withLongOpt("count-data")
				.withDescription("Program will start at the state of counting the statistical data of the outputs")
				.hasArg(false)
				.create());
		
		options.addOptionGroup(optionGroup);
		
		CommandLineParser parser = new BasicParser();
		HelpFormatter formatter = new HelpFormatter();
		
		CommandLine helpCmd = parser.parse(helpOp, args, true); 
		if (helpCmd.hasOption("help") || helpCmd.hasOption("h")) {
			formatter.printHelp("PPO.jar", options, true);
			throw new ParseException("");
		}
		
		try {
			CommandLine cmd = parser.parse(options, args);
			for (Option o : cmd.getOptions()) {
				for (String opt : new String[] { o.getOpt(), o.getLongOpt() }) {
					if (opt != null)
						switch (opt) {
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
								sequencePath = new File(o.getValue());
							break;
							case "out":
							case "o":
								outputPath = parseDirectory(o.getValue());
								new File(outputPath.toString().concat("/ppo")).mkdir();
								ppoPath = parseDirectory(outputPath.toString().concat("/ppo"));
								new File(outputPath.toString().concat("/stat")).mkdir();
								statPath = parseDirectory(outputPath.toString().concat("/stat"));
							break;
							case "region":
							case "r":
								peptideCompareLength = Integer.parseInt(o.getValue());
							break;
							case "np":
							case "n":
								numThread = Integer.parseInt(o.getValue());
							break;
							case "align-with-peptide":
								isAlign = true;
							case "count-data":
								isCount = true;
							break;
							case "debug":
								nOpenFiles = Integer.parseInt(o.getValue());
							break;
						}
				}
			}
		} catch (MissingOptionException e) {
			System.err.println("Missing required options\n\nTry `PPO.jar --help' or `PPO.jar -h' for more options.");
			throw e;
		} catch (ParseException e) {
			System.err.println(e.getMessage());
			throw e;
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

	public static final File getPPOPath() {
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

	public static final boolean getAlignState() {
		return isAlign;
	}
	
	public static final boolean getCountingState() {
		return isCount;
	}
	
	public static final int getDebugMode() {
		return nOpenFiles;
	}
	
	public static void main(String[] args) {
		try {
			ArgParser.parseArg("--count-data -a pas/ -o out/ -p as_seq_out/ -s seq.fasta".split(" "));
		} catch (NotDirectoryException | FileNotFoundException | ParseException e) {
			//
		}
	}
}