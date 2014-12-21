package test;

import java.io.IOException;

import report.Reporter;

public class Tester {
	public static void main(String[] args) {
		
		Thread[] t = new Thread[50];
		
		for (int i=0;i<50;i++) {
			t[i] = new Thread() {
				public void run() {
					Reporter.addValue(Reporter.Variable.NUM_OF_REF, 3);
					Reporter.addValue(Reporter.Variable.NUM_OF_VAR, 2);
				}
			};
			t[i].start();
		}
		try {
			Reporter.writeReportFile("/Users/pramote/Desktop/ee");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}