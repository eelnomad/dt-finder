package bi.vision.dtf.services;

import java.io.IOException;
import java.util.Map;

import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import bi.vision.dtf.JobInfo;

public class DTFUtils {
	// private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	private static Logger logger = LoggerFactory.getLogger(DTFUtils.class);
	public static int[] processLine(int[] status, CSVRecord line, JobInfo jobInfo) throws IOException {
		// Countermeasure for bad lines
		if (line.size() != status.length) {
			// System.out.println("Line ignored");
			return status;
		}

		for (int i = 0; i != status.length; i++) {
			status[i] = processValue(status[i], line.get(i));
			if (16 == i) logger.debug(line.get(i));
		}
		return status;
	}

	public static String[] createHeader(Map<String, Integer> line, JobInfo jobInfo) {
		String[] temp = new String[jobInfo.getColCount()];
		if (jobInfo.isNoHead())
			for (int i = 0; i != jobInfo.getColCount(); i++) {
				temp[i] = "Col " + i;
			}
		else
			temp = line.keySet().toArray(new String[jobInfo.getColCount()]);
		return temp;
	}

	// 1: numeric
	// 2: int
	// 3: varchar
	// 4: timestamp
	// 5: date later
	private static int processValue(int status, String value) {
		if (StringUtils.isBlank(value))
			return status;
		switch (status) {
		// Check if varchar
		case 3:
			return 3;
		case 0:

			// Check to date time REMOVED UNTIL FURTHER NOTICE
			// case 4:
			// try {
			// sdf.parse(value);
			// return 4;
			// } catch (Exception e) {
			// // Do nothing
			// }
			// Check if int
		case 2:
			try {
				Integer.parseInt(value);
				return 2;
			} catch (Exception e) {
				// Do nothing
			}

			// Check if float
		case 1:
			try {
				Float.parseFloat(value);
				return 1;
			} catch (Exception e) {
				// Do nothing
			}
		default:
			return 3;

		}
	}

	public static void printResults(String[] header, int[] status) {
		for (int i = 0; i != header.length; i++) {
			// System.out.println(header[i]);
			switch (status[i]) {
			case 0:
				System.out.println(header[i] + ": null (Please run with bigger dataset)");
				break;
			case 1:
				System.out.println(header[i] + ": numeric");
				break;
			case 2:
				System.out.println(header[i] + ": int");
				break;
			case 3:
				System.out.println(header[i] + ": varchar");
				break;
			case 4:
				System.out.println(header[i] + ": timestamp");
				break;
			default:
				break;
			}
		}
	}

}