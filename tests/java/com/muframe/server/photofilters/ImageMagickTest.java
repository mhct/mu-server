package com.muframe.server.photofilters;

import java.io.IOException;

import org.im4java.core.ConvertCmd;
import org.im4java.core.IM4JavaException;
import org.im4java.core.IMOperation;
import org.junit.Test;

public class ImageMagickTest {

	@Test
	public void test() {
		ConvertCmd cmd = new ConvertCmd();
		IMOperation op = new IMOperation();
		op.resize(320,200);
		op.colorspace("RGB");
		op.addImage();
		op.addImage();
		try {
			cmd.run(op, "/tmp/28b6fdfd-5571-41b8-8f28-d2479f4f8c55.jpg", "/tmp/reduced.jpg");
		} catch (IOException | InterruptedException | IM4JavaException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
