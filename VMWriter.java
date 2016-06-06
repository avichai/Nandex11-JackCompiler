import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;


public class VMWriter {

	private BufferedWriter writer;
	
	
	private void writeToVMFile(String buff) {
		try {
			this.writer.write(buff + "\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public VMWriter(String vmFilePath) {
		try {
			this.writer = new BufferedWriter(new FileWriter(vmFilePath));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void writePush(SEGMENT segment, int index) {
		writeToVMFile("push " + segment + " " + index);
	}
	
	public void writePop(SEGMENT segment, int index) {
		writeToVMFile("pop " + segment + " " + index);
	}
	
	public void writeArithmetic(COMMAND command) {
		writeToVMFile(command.toString());
	}
	
	public void writeLabel(String label) {
		writeToVMFile("label " + label);
	}
	
	public void writeGoto(String label) {
		writeToVMFile("goto " + label);
	}
	
	public void writeIf(String label) {
		writeToVMFile("if-goto " + label);
	}
	
	public void writeCall(String name, int nArgs) {
		writeToVMFile("call " + name + " " + nArgs);
	}
	
	public void writeFunction(String name, int nLocals) {
		writeToVMFile("function " + name + " " + nLocals);
	}
	
	public void writeReturn() {
		writeToVMFile("return");
	}
	
	public void close() {
		try {
			this.writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
