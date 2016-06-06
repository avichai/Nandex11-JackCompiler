
public enum KIND {

	STATIC, FIELD, ARG, VAR, NONE;
	
	public static KIND getKind(KEYWORD_T keywword) {
		switch (keywword) {
			case STATIC:
				return KIND.STATIC;
			case FIELD:
				return KIND.FIELD;
			default:
				System.err.println("!!!getKind!!!");//TODO
				return null;
		}
	}
	
}
