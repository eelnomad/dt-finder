package bi.vision.dtf.services;

import java.util.Map;

import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import bi.vision.dtf.DataType;
import bi.vision.dtf.JobInfo;
import bi.vision.exceptions.ColNumException;

public class DTFUtils {
	private static Logger logger = LoggerFactory.getLogger("generalLog");

	public static DataType[] processLine(DataType[] columnInfo, CSVRecord line) throws ColNumException {
		// Countermeasure for lines that do not match the required number of columns
		if (line.size() != columnInfo.length) {
			// System.out.println("Line ignored");
			// return columnInfo;
			throw new ColNumException();
		}

		for (int i = 0; i != columnInfo.length; i++) {
			columnInfo[i] = processValue(columnInfo[i], line.get(i));
			//For debugging the value at indicee i
			if (7 == i)
				logger.debug("Value of line " + i + ": " + line.get(i));
		}
		return columnInfo;
	}

	public static DataType[] createHeader(CSVParser cp, JobInfo jobInfo) throws ColNumException {
		DataType[] header = new DataType[jobInfo.getColCount()];
		if (!jobInfo.isNoHead()) {
			Map<String, Integer> line = cp.getHeaderMap();
			if (jobInfo.getColCount() != line.size()) {
				System.out.printf("Col Count: %d\nLine: %s", line.size(), line);
				throw new ColNumException();
			}
			String[] temp = line.keySet().toArray(new String[jobInfo.getColCount()]);
			for (int i = 0; i != jobInfo.getColCount(); i++) {
				header[i] = new DataType(temp[i]);
			}
		} else {
			for (int i = 0; i != jobInfo.getColCount(); i++) {
				header[i] = new DataType("Col " + i);
			}
		}
		return header;
	}

	// 1: numeric
	// 2: int
	// 3: varchar
	// 4: timestamp
	// 5: date later
	private static DataType processValue(DataType column, String value) {
		if (StringUtils.isBlank(value))
			return column;
		switch (column.getDatatype()) {
		// Check if varchar
		case 3:
			if (column.getLength() < value.length())
				column.setLength(value.length());
			column.setDatatype(3);
			return column;
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
				if (column.getLength() < value.length())
					column.setLength(value.length());
				column.setDatatype(2);
				return column;
			} catch (Exception e) {
				// Do nothing
			}

			// Check if float
		case 1:
			try {
				Float.parseFloat(value);
				if (column.getLength() < String.valueOf(Math.floor(Float.parseFloat(value))).length() - 2)
					column.setLength(String.valueOf(Math.floor(Float.parseFloat(value))).length() - 2);
				if (value.indexOf(".") != -1)
					if (column.getPrecision() < value.substring(value.indexOf(".")).length())
						column.setPrecision(value.substring(value.indexOf(".")).length() - 1);
				column.setDatatype(1);
				return column;
			} catch (Exception e) {
				// Do nothing
			}
			// Did not pass any parsing, setting to varchar
		default:
			if (column.getLength() < value.length())
				column.setLength(value.length());
			column.setDatatype(3);
			return column;

		}
	}

	public static void printResults(DataType[] columns) {
		for (int i = 0; i != columns.length; i++) {
			System.out.println(columns[i]);
		}
	}
}