import java.util.Hashtable;


public class SymbolTable {
	
	private static class SymbolDetails {
		private String type;
		private KIND kind;
		private int indexof;
		private SymbolDetails(String type, KIND kind, int indexof) {
			this.type = type;
			this.kind = kind;
			this.indexof = indexof;
		}
	}
	
	private Hashtable<String, SymbolDetails> classTable;
	private Hashtable<String, SymbolDetails> subroutineTable;
	private int nStatic, nField, nArg, nVar;

	
	private SymbolDetails getSymbolTable(String name) {
		if (this.subroutineTable.containsKey(name)) {
			return this.subroutineTable.get(name);
		}
		else if (this.classTable.containsKey(name)) {
			return this.classTable.get(name);
		}	
		return null;
	}
	
	public SymbolTable() {
		this.classTable = new Hashtable<>();
		this.nStatic = 0;
		this.nField = 0;
	}
	
	public void incNArgs() {
		++nArg;
	}
	
	public void startSubroutine() {
		this.subroutineTable = new Hashtable<>();
		this.nArg = 0;
		this.nVar = 0;
	}
	
	public void define(String name, String type, KIND kind) {
		switch(kind) {
			case STATIC:
				this.classTable.put(name, new SymbolDetails(type, kind, nStatic++));
				break;
			case FIELD:
				this.classTable.put(name, new SymbolDetails(type, kind, nField++));
				break;
			case ARG:
				this.subroutineTable.put(name, new SymbolDetails(type, kind, nArg++));
				break;
			case VAR:
				this.subroutineTable.put(name, new SymbolDetails(type, kind, nVar++));
				break;
			default:
				break;
		}
	}
	
	public int varCount(KIND kind) {
		switch(kind) {
			case STATIC:
				return nStatic;
			case FIELD:
				return nField;
			case ARG:
				return nArg;
			case VAR:
				return nVar;
			default:
				return 0;
		}
	}
	
	public KIND kindOf(String name) {
		SymbolDetails symbolDetails = getSymbolTable(name);
		
		if (symbolDetails == null) {
			return KIND.NONE;
		}
		else {
			return symbolDetails.kind;
		}
	}
	
	public String typeOf(String name) {
		return getSymbolTable(name).type;
	}
	
	public int indexOf(String name) {
		return getSymbolTable(name).indexof;
	}
	
	public SEGMENT segmentOf(String name) {
		switch (kindOf(name)) {
			case ARG:
				return SEGMENT.ARG;
			case FIELD:
				return SEGMENT.THIS;
			case STATIC:
				return SEGMENT.STATIC;
			case VAR:
				return SEGMENT.LOCAL;
			default:				
				return null;
		}
	}
	
}
