package org.kcr.jctpcli.md;

import org.kcr.jctpcli.env.Parameter;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Recorder {

	public BufferedWriter ssWriter = null;
	public BufferedWriter mmWriter = null;

	public Recorder() {
		try {
			ssWriter = new BufferedWriter(new FileWriter(Parameter.tradingDay + ".csv", true));
			// mmWriter = new BufferedWriter(new FileWriter(Prameter.tradingDay +
			// "mmRecorder.csv", true));
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}

	public void writeMD(String str) {
		try {
			ssWriter.write(str + "\r\n");
			ssWriter.flush();
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			try {
				ssWriter.close();
			} catch (IOException e1) {
				// TODO 自动生成的 catch 块
				e1.printStackTrace();
			}
		}
	}

	public void writeMM(String str) {
		try {
			mmWriter.write(str + "\r\n");
			mmWriter.flush();
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			try {
				mmWriter.close();
			} catch (IOException e1) {
				// TODO 自动生成的 catch 块
				e1.printStackTrace();
			}
		}
	}

}
