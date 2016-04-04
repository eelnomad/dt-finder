package bi.vision.dtf;

public class DataType {
	private String columnName;
	private int datatype;
	private int length;
	private int precision;

	public DataType(String name) {
		this.columnName = name;
	}
	
	public DataType(int datatype, int length, int precision) {
		this.datatype = datatype;
		this.length = length;
		this.precision = precision;
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public void setDatatype(int datatype) {
		this.datatype = datatype;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public void setPrecision(int precision) {
		this.precision = precision;
	}

	public int getDatatype() {
		return datatype;
	}

	public int getLength() {
		return length;
	}

	public int getPrecision() {
		return precision;
	}

	@Override
	public String toString() {

		switch (datatype) {
		case 0:
			return columnName + ": null (Please run with bigger dataset)";
		case 1:
			return columnName + ": numeric(" + (length+precision) + "," + precision + ")";
		case 2:
			return columnName + ": int";
		case 3:
			return columnName + ": varchar(" + length + ")";
		case 4:
			return columnName + ": timestamp";
		default:
			return "";
		}
	}
}
