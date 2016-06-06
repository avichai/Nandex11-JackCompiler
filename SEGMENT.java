
public enum SEGMENT {

	CONST("constant"),
	ARG("argument"),
	LOCAL("local"),
	STATIC("static"),
	THIS("this"),
	THAT("that"),
	POINTER("pointer"),
	TEMP("temp");

	
	private String segment;
	
	private SEGMENT(String segment) {
		this.segment = segment;
	}
	
	public String toString() {
		return this.segment;
	}
	
}
