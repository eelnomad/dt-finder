package bi.vision.dtf.services;

import java.util.Map;

import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import bi.vision.dtf.DataType;
import bi.vision.dtf.JobInfo;

public class DTFUtils {
	private static Logger logger = LoggerFactory.getLogger(DTFUtils.class);

	public static DataType[] processLine(DataType[] columnInfo, CSVRecord line) {
		// Countermeasure for bad lines
		if (line.size() != columnInfo.length) {
			// System.out.println("Line ignored");
			return columnInfo;
		}

		for (int i = 0; i != columnInfo.length; i++) {
			columnInfo[i] = processValue(columnInfo[i], line.get(i));
			if (16 == i)
				logger.debug(line.get(i));
		}
		return columnInfo;
	}

	public static DataType[] createHeader(Map<String, Integer> line, JobInfo jobInfo) {
		DataType[] header = new DataType[jobInfo.getColCount()];
		String[] temp = line.keySet().toArray(new String[jobInfo.getColCount()]);
		for (int i = 0; i != jobInfo.getColCount(); i++) {
			if (jobInfo.isNoHead())
				header[i] = new DataType("Col " + i);
			else
				header[i] = new DataType(temp[i]);
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
				if (column.getLength() < value.length())
					column.setLength(value.length());
				try {
				if (column.getPrecision() < value.substring(value.indexOf(".")).length())
					column.setPrecision(value.substring(value.indexOf(".")).length() - 1);
				}
				catch (Exception e) {
					// Do nothing, you found a 0
				}
				column.setDatatype(1);
				return column;
			} catch (Exception e) {
				// Do nothing
			}
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