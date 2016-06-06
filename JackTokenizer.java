import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.regex.Pattern;



/**
 * class for tokenizer a jack file.
 */
public class JackTokenizer {
	
	private static final char EOF = (char) -1;
	private static final String INTEGER_VAL_REG = "\\d+";
	private static final String VAR_NAME_REG = "[_A-Za-z]\\w*";
	private static final String STR_PREF = "\"";
	
	private BufferedReader buf;
	private HashSet<Character> symbolSet;
	private char nextChar;
	private char nextSymbol;
	private TOKEN_T nextTokenType;
	private String nextStr;
	private int nextIntConst;
	private String nextIdentifier;
	private String nextStrConst;
	private KEYWORD_T nextKeyword;
	
	/*
	 * filling the symbol set with all availible chars.
	 */
	private void fillSymbolSet() {
		this.symbolSet.add('{');
		this.symbolSet.add('}');
		this.symbolSet.add('(');
		this.symbolSet.add(')');
		this.symbolSet.add('[');
		this.symbolSet.add(']');
		this.symbolSet.add('.');
		this.symbolSet.add(',');
		this.symbolSet.add(';');
		this.symbolSet.add('+');
		this.symbolSet.add('-');
		this.symbolSet.add('*');
		this.symbolSet.add('/');
		this.symbolSet.add('&');
		this.symbolSet.add('|');
		this.symbolSet.add('<');
		this.symbolSet.add('>');
		this.symbolSet.add('=');
		this.symbolSet.add('~');
	}
	
	/*
	 * get the next token from the input buffer. 
	 */
	private void getNextToken() {
		try {
			this.nextChar = (char) this.buf.read();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * dismiss multiple lines comment
	 */
	private void dismissMultipleLinesComent() {
		if (this.nextSymbol == '/' && this.nextChar == '*'){
			getNextToken();
			ignoreMultLineComment();
			getNextToken();
			advance();
		}	
	}
	
	/*
	 * ignore multiple line of comment.
	 */
	private void ignoreMultLineComment() {
		//if its a comment of the form /** <comment> */
		if (this.nextChar == '*'){
			getNextToken();
			// if there wasn't any real comment
			if (this.nextChar == '/'){
				return;
			}
		}
		while (this.nextChar != '*') {
			getNextToken();
		}
		if (this.nextChar != '/') {
			ignoreMultLineComment();
		}
		
	}
	
	
	/*
	 * ignore one line of comment.
	 */
	private void dismissOneLineComent() {
		// if it's a comment
		if (this.nextSymbol == '/' && this.nextChar == '/' ){
			while (this.nextChar != '\n'){
				//assumption : the file is valid and therfore there must be more chars
				getNextToken();
			}
			getNextToken();
			advance(); // reading another line.
		}
	}
	
	/*
	 * find the token type and associated word.
	 */
	private void getTokenTypeOfWord() {
		if (isKeyword()) {
			return;
		}
		else if (isIntConst()) {
			return;
		}
		else if (isIdentifier()) {
			return;		
		}
		else if (isStrConst()) {
			return;		
		}
		else if (Character.isWhitespace(this.nextChar)){
			getNextToken();
			advance();
		}
	}
	/*
	 * checks if the word is a keyword
	 */
	private boolean isKeyword() {
		this.nextKeyword = KEYWORD_T.convertToKey(this.nextStr);
		if (this.nextKeyword == null) {
			return false;
		}
		else {
			this.nextTokenType = TOKEN_T.KEYWORD;
			return true;
		}
	}
	
	/*
	 * checks if the word is a integer const
	 */
	private boolean isIntConst() {
		if (!Pattern.compile(INTEGER_VAL_REG).matcher(this.nextStr).matches()) {
			return false;
		}
		else {
			this.nextTokenType = TOKEN_T.INT_CONST;
			this.nextIntConst = Integer.parseInt(this.nextStr);
			return true;
		}
	}
	
	/*
	 * checks if the word is an identifier
	 */
	private boolean isIdentifier() {
		if (!Pattern.compile(VAR_NAME_REG).matcher(this.nextStr).matches()) {
			return false;
		}
		else {
			this.nextTokenType = TOKEN_T.IDENTIFIER;
			this.nextIdentifier = this.nextStr;
			return true;
		}
		
	}
	
	/*
	 * checks if the word is a string const
	 */
	private boolean isStrConst() {
		// string start and ends at the same line.
		if (this.nextStr.startsWith((STR_PREF)) && this.nextStr.endsWith(STR_PREF)) {
			this.nextTokenType = TOKEN_T.STRING_CONST;
			this.nextStrConst = this.nextStr.substring(1, this.nextStr.length()-1);
			return true;
		}
		// string spread over more than one line.
		else if (this.nextStr.startsWith((STR_PREF))) {
			this.nextTokenType = TOKEN_T.STRING_CONST;
			this.nextStrConst = this.nextStr.substring(1);
			while (this.nextChar != '"') {
				this.nextStrConst += this.nextChar;
				getNextToken();
			}
			getNextToken();
			return true;
		}
		return false;
	}
	
	
	
	
	/**
	 * Opens the input file/stream and gets ready
	 * to tokenize it.
	 * @param jackFile file to tokenizer
	 * 
	 * @throws IOException when failed to read char from buf.
	 */
	public JackTokenizer(File jackFile) {
		try {
			this.buf = new BufferedReader(new FileReader(jackFile));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		this.symbolSet = new HashSet<Character>();
		fillSymbolSet();
		getNextToken();
		while (Character.isWhitespace(this.nextChar)) {
			getNextToken();
		}
	}
	

	/**
	 * @return true if we have more tokens in the input
	 */
	public boolean hasMoreTokens() {
		return this.nextChar != (char)EOF;
	}
	
	
	/**
	 * Gets the next token from the input and
	 * makes it the current token. This method
	 * should only be called if hasMoreTokens()
	 * is true. Initially there is no current token.
	 * @throws IOException when failed to read char from buf.
	 * 
	 */
	public void advance() {
		if (this.symbolSet.contains(this.nextChar)) {
			this.nextSymbol = this.nextChar;
			this.nextTokenType = TOKEN_T.SYMBOL;
			getNextToken();
			
			
			dismissOneLineComent();
			dismissMultipleLinesComent();
		}
		else {
			this.nextStr = "";
			while (!this.symbolSet.contains(this.nextChar) &&  !Character.isWhitespace(this.nextChar)){
				this.nextStr += this.nextChar;
				getNextToken();
			}
			if((this.nextStr.startsWith(STR_PREF) && !this.nextStr.endsWith(STR_PREF)) ||
					(this.nextStr.startsWith(STR_PREF) && this.nextStr.length() == 1)){
				while (this.nextChar != '"') {
					this.nextStr += this.nextChar;
					getNextToken();
				}
				this.nextStr += this.nextChar;
				getNextToken();
			}
			getTokenTypeOfWord();
		}
		while (Character.isWhitespace(this.nextChar)) {
			getNextToken();
		}
	}	
	
	/**
	 * @return the type of the current token. 
	 */
	public TOKEN_T tokenType() {
		return this.nextTokenType;
	}
	
	
	/**
	 * @return the keyword which is the current 
	 * token. Should be called only when 
	 * tokenType() is KEYWORD.
	 */
	public KEYWORD_T keyWord() {
		return this.nextKeyword;
	}
	
	/**
	 * @return  the character which is the current 
	 * token. Should be called only when
	 * tokenType() is SYMBOL. 
	 */
	public char symbol() {
		return this.nextSymbol;
	}
	
	/**
	 * @return  the identifier which is the current
	 * token. Should be called only when
	 * tokenType() is IDENTIFIER
	 */
	public String identifier() {
		return this.nextIdentifier;
	}
	
	/**
	 * @return  the integer value of the current
	 * token. Should be called only when
	 * tokenType() is INT_CONST
	 */
	public int intVal() {
		return this.nextIntConst;
	}
	
	/**
	 * @return  the string value of the current
	 * token, without the double quotes. Should
	 * be called only when tokenType() is
	 * STRING_CONST. 
	 */
	public String stringVal() {
		return this.nextStrConst;
	}
	
	
	
	
}