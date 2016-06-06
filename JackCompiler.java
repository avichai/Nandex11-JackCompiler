import java.io.File;
import java.io.FileFilter;


public class JackCompiler {

	private static final int VALID_NARGS = 1;
	private static final String USAGE_ERR_MSG = "Usage: JackCompiler: <jack file or directory>.";
	private static final String JACK_FILE_REGEX = (".*\\.jack");
	private static final String JACK_SUFF = ".jack";
	private static final String VM_SUFF = ".vm";

	// assumes jackFilePath ends with .jack
	private static String getVMFilePath(String jackFilePath) {
		int lastIndexOf = jackFilePath.lastIndexOf(JACK_SUFF);
		return jackFilePath.substring(0, lastIndexOf) + VM_SUFF;
	}
	
	private static void compileJackFile(File jackFile) {
		JackTokenizer tokenizer = new JackTokenizer(jackFile);
		VMWriter vmWriter = new VMWriter(getVMFilePath(jackFile.getAbsolutePath()));
		CompilationEngine compEngine = new CompilationEngine(tokenizer, vmWriter);
		compEngine.compileClass();
	}
	
	public static void main(String[] args) {
		
		if (args.length != VALID_NARGS) {
			System.err.println(USAGE_ERR_MSG);
			System.exit(1);
		}
		
		File file = new File(args[0]);
		if (!file.exists()) {
			System.err.println(USAGE_ERR_MSG);
			System.exit(1);
		}
		
		if (file.isFile()) {
			String fileName = file.getAbsolutePath();
			if (fileName.matches(JACK_FILE_REGEX)) {
				compileJackFile(file);
			} else {
				System.err.println(USAGE_ERR_MSG);
				System.exit(1);
			}
		} else if (file.isDirectory()) {
			FileFilter jackFileFilter = new FileFilter() {
				@Override
				public boolean accept(File pathname) {
					return pathname.getName().matches(JACK_FILE_REGEX) && pathname.isFile();
				}
			};

			for (File jackFile : file.listFiles(jackFileFilter)) {
				compileJackFile(jackFile);
			}
		} else {	
			System.err.println(USAGE_ERR_MSG);
			System.exit(1);
		}
	}

}
