package org.kcr.jctpcli.cnf;

import com.alibaba.fastjson.JSON;

import java.io.*;

public class FJson {
	// 读取文件
	public static String readJsonFile(String fileName) {
		String jsonStr = "";
		try {
			File jsonFile = new File(fileName);
			FileReader fileReader = new FileReader(jsonFile);
			Reader reader = new InputStreamReader(new FileInputStream(jsonFile), "utf-8");
			int ch = 0;
			StringBuffer sb = new StringBuffer();
			while ((ch = reader.read()) != -1) {
				sb.append((char) ch);
			}
			fileReader.close();
			reader.close();
			jsonStr = sb.toString();
			return jsonStr;
		} catch (Exception e) {
			//e.printStackTrace();
			return null;
		}
	}

	public static void resetFile(String fileName) {
		try {
			var f = new File(fileName);
			var rw = new FileWriter(f);
			rw.write("");
			rw.close();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Cnf readCnf(String fileName) {
		var content = readJsonFile(fileName);
		var cnf = JSON.parseObject(content, Cnf.class);
		return cnf;
	}

	public static Cmd readCmdThenReset(String fileName) {
		var content = readJsonFile(fileName);
		if (content == null || content == "") {
			return new Cmd();
		}
		resetFile(fileName);
		return JSON.parseObject(content, Cmd.class);
	}
}
