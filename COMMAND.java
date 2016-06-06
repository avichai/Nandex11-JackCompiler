
public enum COMMAND {

	ADD("add"),
	SUB("sub"),
	NEG("neg"),
	EQ("eq"),
	GT("gt"),
	LT("lt"),
	AND("and"),
	OR("or"),
	NOT("not"),
	MULT(""),
	DIV("");
	
	
	private String command;
	
	private COMMAND(String command) {
		this.command = command;
	}
	
	public String toString() {
		return this.command;
	}

	
}
