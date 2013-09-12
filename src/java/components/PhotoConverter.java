package components;

import java.io.File;
import java.io.IOException;

import org.im4java.core.ConvertCmd;
import org.im4java.core.IM4JavaException;
import org.im4java.core.IMOperation;

public class PhotoConverter {
	
	private static void convertPhoto(String original, String converted) {
		ConvertCmd cmd = new ConvertCmd();
		IMOperation op = new IMOperation();
		op.resize(1920, 1080);
		op.colorspace("RGB");
		op.addImage(original);
		op.addImage(converted);
		try {
			cmd.run(op);
		} catch (IOException | InterruptedException | IM4JavaException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static  void main(String[] args) {
		File f = new File(args[0]);
		convertPhoto(args[0], "/tmp/" + f.getName());
	}
}
