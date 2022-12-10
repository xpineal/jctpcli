package test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Test {

	public static void main(String[] args) throws ParseException, InterruptedException {
		// TODO 自动生成的方法存根

		// SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		SimpleDateFormat sdf = new SimpleDateFormat("MMdd");

		System.out.println(sdf.format(new Date()));
	}
}
