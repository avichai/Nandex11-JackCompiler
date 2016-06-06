
public enum FUNC_T {

	CONSTRUCTOR, METHOD, FUNCTION;
	
	
	public static FUNC_T getFuncType(KEYWORD_T funcType) { 
		if (funcType == KEYWORD_T.METHOD) {
			return METHOD;
		}
		else if (funcType == KEYWORD_T.FUNCTION) {
			return FUNCTION;
		}
		else {
			return FUNC_T.CONSTRUCTOR;
		}
	}
}
