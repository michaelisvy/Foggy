package org.beijingair.batch;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.beijingair.service.AirService;
import org.springframework.context.support.GenericXmlApplicationContext;

/**
 * script used to import tweets that were copied and pasted manually from Google
 * @author misvy
 * 
 */
public class ImportDataBatch {
	
	private static String BEIJINGAIR_FOLDER="/Users/misvy/perso/side-projects/beijingair/";

	public static void main(String[] args) throws Exception {
		//cleanUpData("data-jan-june-2010.txt", "data-jan-june-2010-clean.txt");
		//cleanUpData("data-july-august-2010.txt", "data-july-august-2010-clean.txt");
		//cleanUpData("data-jan-july-2011.txt", "data-jan-july-2011-clean.txt");
		importData("2011-data-jan-july.txt");
		//cleanUpData("2010-data-june-december.txt", "2010-data-june-december-copy.txt");
		
	}

	private static void importData(String file) throws IOException {
		GenericXmlApplicationContext applicationContext = new GenericXmlApplicationContext();
		applicationContext.getEnvironment().setActiveProfiles("production");
		applicationContext.load("classpath:/infrastructure-config.xml");
		applicationContext.refresh();
		AirService airService = (AirService) applicationContext.getBean(AirService.class);
		airService.importData(BEIJINGAIR_FOLDER+file); // does
																									// the
																									// job
																									// for
																									// both
																									// cities
	}
	/**
	 * read and cleanup file from http://twapperkeeper.com
	 */
	private static void cleanUpData(String input, String output) throws IOException {
		File fread = new File(BEIJINGAIR_FOLDER+input);
		BufferedReader bufferedReader = new BufferedReader(new FileReader(fread));

		File fwrite = new File(BEIJINGAIR_FOLDER+output);
		BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(fwrite));

		String line ="";
		while (line != null) {
			line = bufferedReader.readLine();
			if ((line!=null && !line.equals("") && Character.isDigit(line.charAt(0)))) {
				bufferedWriter.write(line+"\n");
			}
		}
		bufferedWriter.close();
		bufferedReader.close();

	}

}
