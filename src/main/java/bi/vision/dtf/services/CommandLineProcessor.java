package bi.vision.dtf.services;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import bi.vision.dtf.JobInfo;

@Service
public class CommandLineProcessor {

	private static final Logger logger = LoggerFactory.getLogger(CommandLineProcessor.class);
	private Options options;
	private JobInfo jobInfo;

	public CommandLineProcessor() {
		options = new Options();
		Option fileName = Option.builder("f").argName("fileName").hasArg().desc("Path to file").longOpt("fileName")
				.required().build();
		Option delim = Option.builder("d").argName("delim").hasArg().desc("Delimiter in file").longOpt("delim")
				.required().type(char.class).build();
		Option quotes = Option.builder("q").argName("quotes").hasArg().desc("Quotes in file").longOpt("quotes").build();
		Option colCount = Option.builder("c").argName("colCount").hasArg().desc("Number of columns in file")
				.longOpt("colCount").required().build();
		Option sampSize = Option.builder("s").argName("sampSize").hasArg().desc("Amount of rows to be tested")
				.longOpt("sampSize").build();
		Option noHead = Option.builder("h").argName("noHead").desc("File does not have header").longOpt("noHead")
				.build();
		options.addOption(fileName).addOption(delim).addOption(quotes).addOption(colCount).addOption(sampSize)
				.addOption(noHead);
	}

	public CommandLineProcessor parsArgs(String... arg0) {
		CommandLineParser cmdParser = new DefaultParser();
		CommandLine cl = null;
		try {
			cl = cmdParser.parse(options, arg0, false);
			jobInfo = new JobInfo(cl);
		} catch (ParseException e2) {
			logger.error("Failed to parse command line arguments, stack trace follows:", e2);
			System.exit(1);
		}
		return this;
	}

	public JobInfo getJobInfo() {
		return jobInfo;
	}
}
