package bi.vision.dtf;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.util.StringUtils;

import bi.vision.dtf.services.CommandLineProcessor;
import bi.vision.dtf.services.DTFUtils;
import bi.vision.dtf.services.LineParser;

@SpringBootApplication
public class Application implements CommandLineRunner {
	private static Logger logger = LoggerFactory.getLogger(Application.class);
	private static final int BUFFER = 8192;

	@Autowired
	CommandLineProcessor cli;

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	public void run(String... arg0) {
		// Create variables to be used later on
		String line; // Line in file
		BufferedReader br = null; // BufferedReader reading file

		// Read and process application parameters
		logger.info("Application configuring...");
		JobInfo newJob = cli.parsArgs(arg0).getJobInfo();
		logger.info("Command line arguments parsed successfully");

		// Open file for reading
		File file = new File(newJob.getFileName());

		// Create array for header names
		String[] columnNames = new String[newJob.getColCount()];
		for (int i = 0; i != columnNames.length; i++) {
			columnNames[i] = "Col" + i;
		}

		// Create array for results
		int[] result = new int[newJob.getColCount()];
		
		// Create array to hold values of line
		String[] values =  new String[newJob.getColCount()];

		LineParser lineParser = new LineParser(newJob);
		
		try {
			FileReader fr = new FileReader(file);
			br = new BufferedReader(fr, BUFFER);

			if (!newJob.isNoHead()) {
				columnNames = DTFUtils.createHeader(br.readLine(), newJob);
			}

			int i = 0;
			int j = 0;
			while ((line = br.readLine()) != null && i != newJob.getSampSize()) {
				if (j == 0) {
					System.out.println("Col Count: " + (StringUtils.countOccurrencesOf(line, newJob.getDelim()) + 1));
					j++;
				}
				i++;
				values = lineParser.parseString(values, line);
				result = DTFUtils.processLine(result, values, newJob);
			}
			System.out.println("Rows Read: " + i);
		} catch (Exception e) {
			// TODO
			e.printStackTrace();
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		DTFUtils.printResults(columnNames, result);

	}
}
