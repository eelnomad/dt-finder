package bi.vision.dtf.services;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import bi.vision.dtf.JobInfo;

public class LineParser {
	private static JobInfo jobInfo;
	private static Pattern pattern;

	public LineParser(JobInfo newInfo) {
		this.jobInfo = newInfo;
		this.pattern = Pattern.compile(jobInfo.getDelim() + "|" + jobInfo.getQuotes() + "([^" + jobInfo.getQuotes()
				+ "]*)" + jobInfo.getQuotes());
	}
	
	public String[] parseString(String[] values, String line) {
		Matcher regexMatcher = pattern.matcher(line);
		for (int i = 0; i != values.length; i++) {
			regexMatcher.find();
		    if (regexMatcher.group(1) != null) {
		        // Add double-quoted string without the quotes
		        values[i] = regexMatcher.group(1);
		    } else {
		        // Add unquoted word
		    	values[i] = regexMatcher.group();
		    }
		} 
		return values;
	}
}
