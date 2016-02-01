package bi.vision.dtf;

import org.apache.commons.cli.CommandLine;

public class JobInfo {
	private String fileName;
	private char delim;
	private char quotes;
	private int colCount;
	private int sampSize;
	private boolean noHead;

	public JobInfo(CommandLine c1) {
		this.fileName = c1.getOptionValue("fileName");
		this.delim = c1.getOptionValue("delim").charAt(0);
		if (c1.getOptionValue("quotes") != null)
			this.quotes = c1.getOptionValue("quotes").charAt(0);
		this.colCount = Integer.parseInt(c1.getOptionValue("colCount"));
		this.setSampSize(-1);
		if (c1.getOptionValue("sampSize") != null)
			this.sampSize = Integer.parseInt(c1.getOptionValue("sampSize"));
		if (c1.hasOption("noHead"))
			this.noHead = true;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public char getDelim() {
		return delim;
	}

	public void setDelim(char delim) {
		this.delim = delim;
	}

	public char getQuotes() {
		return quotes;
	}

	public void setQuotes(char quotes) {
		this.quotes = quotes;
	}

	public int getColCount() {
		return colCount;
	}

	public boolean isNoHead() {
		return noHead;
	}

	public void setNoHead(boolean noHead) {
		this.noHead = noHead;
	}

	public void setColCount(int colCount) {
		this.colCount = colCount;
	}

	public int getSampSize() {
		return sampSize;
	}

	public void setSampSize(int sampSize) {
		this.sampSize = sampSize;
	}

}
