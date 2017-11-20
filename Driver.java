package com.randommurican.Parsemer;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * The Driver class creates the language and
 * passes a file of input to get parsed.
 * 
 *	@author ej Byrne
 */

public class Driver {
	public static void main(String[] args) {
		
		Language lang = new Language();
		try {
			/*
			 * lang.parse(File); can be repeated as many
			 * times as necessary, but the language only
			 * holds 1 parse at a time in memory to print
			 */
			lang.parse(new File("Test10.txt"));
			Parser parse = new Parser(lang);
			System.out.println(parse.getResult());
		} catch (FileNotFoundException e) {
			System.out.println("Could not find a text file");
		}
	}
}
