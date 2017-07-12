package com.rencare.pay.utils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

public class FileUtil {
	
	private static final int BUFFER_SIZE = 1024;

	public static void redirectOut(String fileName) throws FileNotFoundException {
		FileOutputStream fos = new FileOutputStream(fileName);
		PrintStream ps = new PrintStream(fos);
		System.setOut(ps);
	}

	public static String getFileContent(String fileName) throws IOException {
		StringBuffer sb = new StringBuffer();
		File f = new File(fileName);
		FileReader r = new FileReader(f);
		BufferedReader br = new BufferedReader(r);
		String line = br.readLine();
		while (line != null) {
			sb.append(line).append("\n");
			line = br.readLine();
		}
		r.close();
		br.close();
		return sb.toString();
	}

	public static void mkdirs(String path) {
		File folder = new File(path);
		if (!folder.exists())
			folder.mkdirs();
	}

	public static void mkdir(File file) {
		if (file.getParentFile().exists()) {
			file.mkdir();
		} else {
			mkdir(file.getParentFile());
			file.mkdir();
		}
	}

	/**
	 * 读输入流到输出流中，并返回输出流字节数组
	 * @param inStream
	 * @return
	 * @throws IOException
	 */
	public static byte[] readInputStream(InputStream inStream) throws IOException {
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[BUFFER_SIZE];
		int len = 0;
		while ((len = inStream.read(buffer)) != -1) {
			outStream.write(buffer, 0, len);
		}
		inStream.close();
		return outStream.toByteArray();
	}

	/**
	 * 读取输入流，并输出字符串
	 * @param in
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws IOException
	 */
	public static final String readInputStream2String(InputStream in) throws UnsupportedEncodingException, IOException {
		if (in == null)
			return "";
		StringBuffer out = new StringBuffer();
		byte[] b = new byte[BUFFER_SIZE];
		for (int n; (n = in.read(b)) != -1;) {
			out.append(new String(b, 0, n, "UTF-8"));
		}
		return out.toString();
	}

}
