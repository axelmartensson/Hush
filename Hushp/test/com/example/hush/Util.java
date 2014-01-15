package com.example.hush;

import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Util {
	public static String fileContentToString(String fileName) {
		StringBuilder content = new StringBuilder();
		try {
			BufferedReader buf = new BufferedReader(new FileReader(fileName));
			String line = "";
			while ((line = buf.readLine()) != null) {
				content.append(line);
			}
		} catch (FileNotFoundException e1) {
			assertTrue("FileNotFound: "+fileName, false);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return content.toString();
	}
}
