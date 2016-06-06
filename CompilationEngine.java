
public class CompilationEngine {

	
	private static final String WHILE_EXP = "WHILE_EXP";
	private static final String WHILE_END = "WHILE_END";
	private static final String IF_TRUE = "IF_TRUE";
	private static final String IF_FALSE = "IF_FALSE";
	private static final String IF_END = "IF_END";
	
	
	
	
	private JackTokenizer tokenizer;
	private VMWriter vmWriter;
	private SymbolTable symbolTable;
	private String className;
	private int whileCounter, ifCounter;
	
	
	// returns the current op, if the token isn't op returns null
	private COMMAND getOp() {
		if (tokenizer.tokenType() == TOKEN_T.SYMBOL) {
			char c = tokenizer.symbol();
			switch (c) {
				case '+':
					return COMMAND.ADD;
				case '-':
					return COMMAND.SUB;
				case '*':
					return COMMAND.MULT;
				case '/':
					return COMMAND.DIV;
				case '&':
					return COMMAND.AND;
				case '|':
					return COMMAND.OR;
				case '<':
					return COMMAND.LT;
				case '>':
					return COMMAND.GT;
				case '=':
					return COMMAND.EQ;
				default:
					return null;
			}
		}
		return null;
	}
		
	private boolean isComma() {
		if (tokenizer.tokenType() == TOKEN_T.SYMBOL) {
			return tokenizer.symbol() == ',';
		}
		return false;
	}
	
	private boolean isClassVarDec() {
		if (tokenizer.tokenType() == TOKEN_T.KEYWORD) {
			KEYWORD_T keyword = tokenizer.keyWord();
			return (keyword == KEYWORD_T.STATIC) || (keyword == KEYWORD_T.FIELD); 
		}
		return false;
	}

	private boolean isSubroutineDec() {
		if (tokenizer.tokenType() == TOKEN_T.KEYWORD) {
			KEYWORD_T keyword = tokenizer.keyWord();
			return (keyword == KEYWORD_T.CONSTRUCTOR) || 
				   (keyword == KEYWORD_T.FUNCTION) || 
				   (keyword == KEYWORD_T.METHOD);
		}
		return false;
	}

	private boolean isOpenSquareBracket() {
		if (tokenizer.tokenType() == TOKEN_T.SYMBOL) {
			return tokenizer.symbol() == '[';
		}
		return false;
	}
	
	private boolean isDot() {
		if (tokenizer.tokenType() == TOKEN_T.SYMBOL) {
			return tokenizer.symbol() == '.'; 
		}
		return false;
	}
	
	private boolean isOpenBracket() {
		if (tokenizer.tokenType() == TOKEN_T.SYMBOL) {
			return tokenizer.symbol() == '('; 
		}
		return false;
	}
	
	private boolean isCloseBracket() {
		if (tokenizer.tokenType() == TOKEN_T.SYMBOL) {
			return tokenizer.symbol() == ')';
		}
		return false;
	}
	
	// returns identifier and advances
	private String getIdentifierAd() {
		String identifier = tokenizer.identifier();
		tokenizer.advance();
		return identifier;
	}
	
	private int getNextWhileCounter() {
		return whileCounter++;
	}
	
	private int getNextIfCounter() {
		return ifCounter++;
	}
	
	private String getTypeAd() {
		String type;
		if(tokenizer.tokenType() == TOKEN_T.KEYWORD) {
			type = tokenizer.keyWord().toString();
		}
		else {
			type = tokenizer.identifier();
		}
		tokenizer.advance(); // type
		
		return type;
	}
	
	
	public CompilationEngine(JackTokenizer tokenizer, VMWriter vmWriter) {
		this.tokenizer = tokenizer;
		this.vmWriter = vmWriter;
		this.symbolTable = new SymbolTable();
		this.whileCounter = 0;
		this.ifCounter = 0;
	}

	public void compileClass() {
		tokenizer.advance();  // starts
		tokenizer.advance();  // class
		this.className = getIdentifierAd();  // className
		tokenizer.advance();  // {

		while (isClassVarDec()) {
			compileClassVarDec();
		}
		while (isSubroutineDec()) {
			compileSubroutine();
		}
		
		this.vmWriter.close();
	}

	public void compileClassVarDec() {
		KIND kind = KIND.getKind(tokenizer.keyWord());  // static | field
		tokenizer.advance();  // kind
		String type = getTypeAd();  // type
		String varName = getIdentifierAd();  // varName
		symbolTable.define(varName, type, kind);
		
		while (isComma()) {
			tokenizer.advance();  // ,
			varName = getIdentifierAd();  // varName
			symbolTable.define(varName, type, kind);
		}
		tokenizer.advance();  // ;
	}

	
	public void compileSubroutine() {
		symbolTable.startSubroutine();
		whileCounter = 0;
		ifCounter = 0;
		
		FUNC_T funcType = FUNC_T.getFuncType(tokenizer.keyWord()); // constructor | function | method
		if (funcType == FUNC_T.METHOD) {
			symbolTable.incNArgs();
		}
		tokenizer.advance();
		tokenizer.advance();  // void | type
		String subroutineName = getIdentifierAd();  // subroutineName
		tokenizer.advance();  // {
		compileParameterList();
		tokenizer.advance();  // } 
		compileSubroutineBody(subroutineName, funcType);
	}

	
	public void compileParameterList() {
		if (!isCloseBracket()) {
			String type = getTypeAd();
			
			String varNaem = getIdentifierAd();  // varName
			symbolTable.define(varNaem, type, KIND.ARG);
			
			while (isComma()) {
				tokenizer.advance();  // ,
				type = getTypeAd();  // type
				varNaem = getIdentifierAd();  // varName
				symbolTable.define(varNaem, type, KIND.ARG);
			}
		}
	}

	

	public void compileVarDec() {
		tokenizer.advance();  // var
		String type = getTypeAd();  // type
		String varName = getIdentifierAd();  // varName
		symbolTable.define(varName, type, KIND.VAR);
		while (isComma()) {
			tokenizer.advance();  // ,
			varName = getIdentifierAd();  // varName
			symbolTable.define(varName, type, KIND.VAR);
		}
		tokenizer.advance();  // ;
	}

	
	public void compileStatements() {
		while (tokenizer.tokenType() == TOKEN_T.KEYWORD) {
			switch (tokenizer.keyWord()) {
				case LET:
					compileLet();
					break;
				case IF:
					compileIf();
					break;
				case WHILE:
					compileWhile();
					break;
				case DO:
					compileDo();
					break;
				case RETURN:
					compileReturn();
					break;
				default:
					return;
			}
		}
	}

	public void compileDo() {
		tokenizer.advance();  // do
		compileSubroutineCall(null, false);
		vmWriter.writePop(SEGMENT.TEMP, 0);
		tokenizer.advance();  // ;
	}
	
	public void compileLet() {
		tokenizer.advance();  // let
		
		String varName = getIdentifierAd();  // varName
		boolean isArray = false;
		if (isOpenSquareBracket()) {
			isArray = true;
			compileArray(varName);
		}
		tokenizer.advance();  // =
		compileExpression();
		
		SEGMENT segment;
		int index;
		if (isArray) {
			segment = SEGMENT.THAT;
			index = 0;
			vmWriter.writePop(SEGMENT.TEMP, 0);
			vmWriter.writePop(SEGMENT.POINTER, 1);
			vmWriter.writePush(SEGMENT.TEMP, 0);
		}
		else {
			segment = symbolTable.segmentOf(varName);
			index = symbolTable.indexOf(varName);
		}
		
		vmWriter.writePop(segment, index);
		tokenizer.advance();  // ;
	}

	public void compileWhile() {
		int curWhileCntr = getNextWhileCounter();
		String expLabel = WHILE_EXP + curWhileCntr;
		String endLabel = WHILE_END + curWhileCntr;
		
		tokenizer.advance();  // while
		tokenizer.advance();  // {
		
		vmWriter.writeLabel(expLabel);
		compileExpression();
		vmWriter.writeArithmetic(COMMAND.NOT);
		vmWriter.writeIf(endLabel);
		
		tokenizer.advance();  // )
		tokenizer.advance();  // {
		compileStatements();
		
		vmWriter.writeGoto(expLabel);
		vmWriter.writeLabel(endLabel);
		
		tokenizer.advance();  // }
	}

	public void compileReturn() {
		tokenizer.advance();  // return
		if (!((tokenizer.tokenType() == TOKEN_T.SYMBOL) && (tokenizer.symbol() == ';'))) {
			compileExpression();
		}
		else {
			vmWriter.writePush(SEGMENT.CONST, 0);
		}
		vmWriter.writeReturn();
		tokenizer.advance();  // ;
	}
	
	public void compileIf() {
		int curIfCntr = getNextIfCounter();
		String ifTrueLabel = IF_TRUE + curIfCntr;
		String ifFalseLabel = IF_FALSE + curIfCntr;
		
		tokenizer.advance();  // if
		tokenizer.advance();  // (
		
		compileExpression();
		vmWriter.writeIf(ifTrueLabel);
		vmWriter.writeGoto(ifFalseLabel);
		vmWriter.writeLabel(ifTrueLabel);
		
		tokenizer.advance();  // )
		tokenizer.advance();  // {
		compileStatements();
		tokenizer.advance();  // }
		
		if ((tokenizer.tokenType() == TOKEN_T.KEYWORD) && (tokenizer.keyWord() == KEYWORD_T.ELSE)) {
			String ifEndLabel = IF_END + curIfCntr;
			
			vmWriter.writeGoto(ifEndLabel);
			vmWriter.writeLabel(ifFalseLabel);
			
			tokenizer.advance();  // else  
			tokenizer.advance();  // {
			compileStatements();
			tokenizer.advance();  // }
			
			vmWriter.writeLabel(ifEndLabel);
		} 
		else {
			vmWriter.writeLabel(ifFalseLabel);
		}
	}

	public void compileExpression() {
		compileTerm();
		COMMAND cmnd;
		while ((cmnd = getOp()) != null) {				
			tokenizer.advance();  // op
			compileTerm();
			switch (cmnd) {
				case MULT:
					vmWriter.writeCall("Math.multiply", 2);
					break;
				case DIV:
					vmWriter.writeCall("Math.divide", 2);
					break;
				default:
					vmWriter.writeArithmetic(cmnd);
					break;
					
			}
		}
	}

	public void compileTerm() {
		switch (tokenizer.tokenType()) {
		
			case SYMBOL:
				char symbol = tokenizer.symbol();
				if (symbol == '(') {
					tokenizer.advance();  // (
					compileExpression();
					tokenizer.advance();  // )
				}
				else {			
					tokenizer.advance();  // unaryOp
					compileTerm();
					COMMAND cmnd;
					if (symbol == '~') {
						cmnd = COMMAND.NOT;
					} 
					else {	// == '-'
						cmnd = COMMAND.NEG;
					}
					vmWriter.writeArithmetic(cmnd);
				}
				break;
				
			case IDENTIFIER:
				String name = getIdentifierAd();  // varName | subroutineCall
				if (isDot() || isOpenBracket()) {		// subroutineCall
					compileSubroutineCall(name, true);
				}
				else {	    // varName
					if (isOpenSquareBracket()) {	// varName[expression]
						compileArray(name);
						vmWriter.writePop(SEGMENT.POINTER, 1);
						vmWriter.writePush(SEGMENT.THAT, 0);
					}
					else {							// varName
						vmWriter.writePush(symbolTable.segmentOf(name), symbolTable.indexOf(name));
					}
				}
				break;
				
			case KEYWORD:
				KEYWORD_T keyword = tokenizer.keyWord();
				tokenizer.advance();  // keywordConstant
				if (keyword == KEYWORD_T.THIS) {		// this
					vmWriter.writePush(SEGMENT.POINTER, 0);
				}
				else {		// true | false | null
					vmWriter.writePush(SEGMENT.CONST, 0);
					if (keyword == KEYWORD_T.TRUE) {
						vmWriter.writeArithmetic(COMMAND.NOT);
					}
				}
				break;
				
			case INT_CONST:
				vmWriter.writePush(SEGMENT.CONST, tokenizer.intVal());
				tokenizer.advance();  // integerConstant
				break;
				
			case STRING_CONST:
				String str = tokenizer.stringVal();
				tokenizer.advance();  // stringConstant
				vmWriter.writePush(SEGMENT.CONST, str.length());
				vmWriter.writeCall("String.new", 1);
				for (char c : str.toCharArray()) {
					vmWriter.writePush(SEGMENT.CONST, c);
					vmWriter.writeCall("String.appendChar", 2);
				}
				break;
		}
	}

	// returns the number of expressions
	public int compileExpressionList() {
		int nArgs = 0;
		if (!isCloseBracket()) {
			compileExpression();
			++nArgs;
			while (isComma()) {
				tokenizer.advance();  // ,
				compileExpression();
				++nArgs;
			}
		}
		return nArgs;
	}

	private void compileSubroutineBody(String subroutineName, FUNC_T funcType) {
		tokenizer.advance();  // {
		while ((tokenizer.tokenType() == TOKEN_T.KEYWORD) && (tokenizer.keyWord() == KEYWORD_T.VAR)) {
			compileVarDec();
		}
		vmWriter.writeFunction(className + "." + subroutineName, symbolTable.varCount(KIND.VAR));
		if (funcType == FUNC_T.METHOD) {
			vmWriter.writePush(SEGMENT.ARG, 0);
			vmWriter.writePop(SEGMENT.POINTER, 0);
		}
		else if (funcType == FUNC_T.CONSTRUCTOR) {
			vmWriter.writePush(SEGMENT.CONST, symbolTable.varCount(KIND.FIELD));
			vmWriter.writeCall("Memory.alloc", 1);
			vmWriter.writePop(SEGMENT.POINTER, 0);
		}
		compileStatements();
		tokenizer.advance();  // }
	}

	private void compileSubroutineCall(String name1, boolean isAdvanced) {
		String name;
		String name2;		
		if (!isAdvanced) {
			name1 = getIdentifierAd();  // subroutineName | (className | varName)
		}
		
		boolean isFunction = false;
		boolean isSubroutineName = true;
		if (isDot()) {
			isSubroutineName = false;
			tokenizer.advance();  // .
			name2 = getIdentifierAd();  // subroutineName
			if (symbolTable.kindOf(name1) == KIND.NONE) {		// if a classNme
				isFunction = true;
				name = name1 + "." + name2;
			}
			else {
				name = symbolTable.typeOf(name1) + "." + name2;
			}
		}
		else {
			name = className + "." + name1;
		}
		tokenizer.advance();  // (
		
		if (!isFunction) {			 // method - has to push this to the stack
			SEGMENT segment;
			int index;
			if (isSubroutineName) {
				segment = SEGMENT.POINTER;
				index = 0;
			}
			else {
				segment = symbolTable.segmentOf(name1);
				index = symbolTable.indexOf(name1);
			}
			vmWriter.writePush(segment, index);
		}
		
		int nArgs = compileExpressionList();
		tokenizer.advance();  // )
		if (!isFunction) {
			++nArgs;
		}
		
		vmWriter.writeCall(name, nArgs);
	}

	// push the address of the varName + exp (exp is the expression found within the [] )
	private void compileArray(String varName) {
		tokenizer.advance();  // [
		compileExpression();
		tokenizer.advance();  // ]
		vmWriter.writePush(symbolTable.segmentOf(varName), symbolTable.indexOf(varName));
		vmWriter.writeArithmetic(COMMAND.ADD);
	}
	
	
}
