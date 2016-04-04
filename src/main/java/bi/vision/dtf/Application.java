package bi.vision.dtf;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import bi.vision.dtf.services.CommandLineProcessor;
import bi.vision.dtf.services.DTFUtils;
import bi.vision.exceptions.ColNumException;

@SpringBootApplication
public class Application implements CommandLineRunner {
	private static Logger logger = LoggerFactory.getLogger("generalLog");
	// private static final int BUFFER = 8192;

	@Autowired
	CommandLineProcessor cli;

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(Application.class);
		app.setBannerMode(Banner.Mode.OFF);
		app.setLogStartupInfo(false);
		app.run(args);
	}

	public void run(String... arg0) {
		// Create variables to be used later on
		CSVParser cp = null;

		// Read and process application parameters
		JobInfo newJob = cli.parsArgs(arg0).getJobInfo();

		// Open file for reading
		File file = new File(newJob.getFileName());

		int i = 0;
		try {
			Reader fr = new FileReader(file);

			if (!newJob.isNoHead()) 
				cp = new CSVParser(fr, CSVFormat.newFormat(newJob.getDelim()).withQuote(newJob.getQuotes()).withHeader());
			else 
				cp = new CSVParser(fr, CSVFormat.newFormat(newJob.getDelim()).withQuote(newJob.getQuotes()));
			
			// Create array for results
			DataType[] result = DTFUtils.createHeader(cp, newJob);

			for (CSVRecord record : cp) {
				i++;
				result = DTFUtils.processLine(result, record);
			}

			logger.info("{};Success;;{}",file.getName(), i);
			DTFUtils.printResults(result);
		} catch (ColNumException e) {
			logger.info("{};Failed;InvalidColumnCount;{}",file.getName(), i);
		} catch (IllegalArgumentException e) {
			logger.info("{};Failed;InvalidColumnNames;{}",file.getName(), i);
		} catch (Exception e) {
			// TODO
			e.printStackTrace();
		} finally {
			try {
				cp.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}
}
