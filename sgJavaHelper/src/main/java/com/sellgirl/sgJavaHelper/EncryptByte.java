package com.sellgirl.sgJavaHelper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * @deprecated 此类有问题，修改后移到com.sellgirl.sgJavaHelper.file.SGEncryptByte
 */
@Deprecated
public class EncryptByte {

	private static final int numOfEncAndDec = 0x123456;
	private static int dataOfFile = 0; // 文件字节内容
	public static final int DEFAULT_BUFFER_SIZE = 4096;

	public static void EncFile(File srcFile, File encFile) throws Exception {

		if (!srcFile.exists()) {

			System.out.println("source file not exixt");

			return;

		}

		if (!encFile.exists()) {

			System.out.println("encrypt file created");

			encFile.createNewFile();

		}

		FileInputStream fis = new FileInputStream(srcFile);

		FileOutputStream fos = new FileOutputStream(encFile);

		// int dataOfFile=0;
		int b=0;

		while ((dataOfFile = fis.read()) > -1) {

			fos.write(0==b?dataOfFile ^ numOfEncAndDec:dataOfFile);
			b++;
			if(b>3) {b=0;}
		}

		fis.close();

		fos.flush();

		fos.close();

	}

	public static void DecFile(File encFile, File decFile) throws Exception {

		if (!encFile.exists()) {

			System.out.println("encrypt file not exixt");

			return;

		}

		if (!decFile.exists()) {

			System.out.println("decrypt file created");

			decFile.createNewFile();

		}

		FileInputStream fis = new FileInputStream(encFile);

		FileOutputStream fos = new FileOutputStream(decFile);

//int dataOfFile=0;

		int b=0;
		while ((dataOfFile = fis.read()) > -1) {

			fos.write(0==b?dataOfFile ^ numOfEncAndDec:dataOfFile);
			b++;
			if(b>3) {b=0;}

		}

		fis.close();

		fos.flush();

		fos.close();

	}
}
