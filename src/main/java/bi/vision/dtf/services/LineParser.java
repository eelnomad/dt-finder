package bi.vision.dtf.services;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LineParser {
	private Pattern pattern;

	public LineParser(String delim, String quote) {
		this.pattern = Pattern.compile(delim + "|" + quote + "([^" + quote + "]*)" + quote);
		System.out.println(this.pattern);
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
