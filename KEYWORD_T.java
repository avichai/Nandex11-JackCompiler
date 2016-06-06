
public enum KEYWORD_T {
	CLASS("class"),
	METHOD("method"),
	FUNCTION("function"),
	CONSTRUCTOR("constructor"),
	INT("int"),
	BOOLEAN("boolean"),
	CHAR("char"),
	VOID("void"),
	VAR("var"),
	STATIC("static"),
	FIELD("field"),
	LET("let"),
	DO("do"), 
	IF("if"), 
	ELSE("else"), 
	WHILE("while"), 
	RETURN("return"), 
	TRUE("true"), 
	FALSE("false"), 
	NULL("null"), 
	THIS("this");
	
	
	private final String keyWord;
	
	private KEYWORD_T(String keyWord) {
		this.keyWord = keyWord;
	}
	
	public String toString() {
		return this.keyWord;
	}
	
	/**
	 * Converting the string to the a valid key from the enum.
	 * If the string doesn't match any key return null.
	 * @param keyString 
	 * @return a KeyWord from the enum and if the string is not valid return null.
	 */
	public static KEYWORD_T convertToKey(String keyString){
		if (keyString.equals(CLASS.toString())){
			return CLASS;
		}
		if (keyString.equals(METHOD.toString())){
			return METHOD;
		}
		if (keyString.equals(FUNCTION.toString())){
			return FUNCTION;
		}
		if (keyString.equals(CONSTRUCTOR.toString())){
			return CONSTRUCTOR;
		}
		if (keyString.equals(INT.toString())){
			return INT;
		}
		if (keyString.equals(BOOLEAN.toString())){
			return BOOLEAN;
		}
		if (keyString.equals(CHAR.toString())){
			return CHAR;
		}
		if (keyString.equals(VOID.toString())){
			return VOID;
		}
		if (keyString.equals(VAR.toString())){
			return VAR;
		}
		if (keyString.equals(STATIC.toString())){
			return STATIC;
		}
		if (keyString.equals(FIELD.toString())){
			return FIELD;
		}
		if (keyString.equals(LET.toString())){
			return LET;
		}
		if (keyString.equals(DO.toString())){
			return DO;
		}
		if (keyString.equals(IF.toString())){
			return IF;
		}
		if (keyString.equals(ELSE.toString())){
			return ELSE;
		}
		if (keyString.equals(WHILE.toString())){
			return WHILE;
		}
		if (keyString.equals(RETURN.toString())){
			return RETURN;
		}
		if (keyString.equals(TRUE.toString())){
			return TRUE;
		}
		if (keyString.equals(FALSE.toString())){
			return FALSE;
		}
		if (keyString.equals(NULL.toString())){
			return NULL;
		}
		if (keyString.equals(THIS.toString())){
			return THIS;
		}
		return null;
	}

}
