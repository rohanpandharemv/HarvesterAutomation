package com.sprinklr.harvester.bulkRunner;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Class for executing the JAVA program from command line.
 *
 */
public class CommandExecutor {

	public static void exec(String command) {
		System.out.println("Inside CommandExecutor.exec() : " + command);
		ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", command);
		builder.redirectErrorStream(true);
		try {
			Process bulkRunnerProcess = builder.start();

			BufferedReader br = new BufferedReader(new InputStreamReader(bulkRunnerProcess.getInputStream()));
			String line;

			while (true) {
				line = br.readLine();
				if (line == null) {
					break;
				}
				System.out.println(line);
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
