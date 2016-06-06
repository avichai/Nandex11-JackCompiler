
public enum TOKEN_T {
	KEYWORD("keyword"),
	SYMBOL("symbol"),
	IDENTIFIER("identifier"),
	INT_CONST("integerConstant"),
	STRING_CONST("stringConstant");

	
	private final String token;
	
	private TOKEN_T(String token) {
		this.token = token;
	}
	
	public String toString() {
		return this.token;
	}
	
}

