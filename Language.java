package com.randommurican.Parsemer;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * The Language class handles most of the functionality
 * 
 *	@author ej Byrne
 */

public class Language {
	private List<Lexeme> lexemes;
	private List<Kind> kinds; // List of patterns we look for
	private boolean wasError;
	private int currentLexeme;

	/**
	 * Declaration of the language that turns the grammar file into
	 * regEx statements
	 * 
	 * @param grammar
	 * @throws FileNotFoundException
	 */
	Language() {
		lexemes = new ArrayList<Lexeme>();
		kinds = new ArrayList<Kind>();
		wasError = false;
		currentLexeme = 0;

		// Hardcoding the grammar for simplicity
			kinds.add(new Kind("lessThan", "<", false));
			kinds.add(new Kind("equal", "=", false));
			kinds.add(new Kind("plus", "\\+", false));
			kinds.add(new Kind("minus", "-", false));
			kinds.add(new Kind("multiply", "\\*", false));
			kinds.add(new Kind("divide", "/", false));
			kinds.add(new Kind("or", "or", false));
			kinds.add(new Kind("and", "and", false));
			kinds.add(new Kind("not", "not", false));
			kinds.add(new Kind("openParentheses", "\\(", false));
			kinds.add(new Kind("closeParentheses", "\\)", false));
			kinds.add(new Kind("true", "(true)", false));
			kinds.add(new Kind("false", "(false)", false));
			kinds.add(new Kind("ID", "[a-zA-Z]([a-zA-Z]|[0-9]|(_))*", true));
			kinds.add(new Kind("NUM", "[0-9]+", true));
	}

	/**
	 * Takes input and turns it into a list of lexemes
	 * 
	 * @param input
	 * @throws FileNotFoundException
	 */
	public void parse(File input) throws FileNotFoundException {
		wasError = false;																			// Reset to default error state
		Scanner inputScanner = new Scanner(input);													// Start reading the input
		lexemes.clear();																			// Delete any data from previous reads
		int line = 1, start = 1;

		while(inputScanner.hasNextLine() && !wasError) {											// While the file has more text
			String lexemeString = "", str = inputScanner.nextLine();
			for(int i = 0; i <= str.length(); i++) {												// traverse the line
				if(lexemeString.equals("/") && str.charAt(i) == '/') {								// If the indexed character is a comment
					i = str.length();																// move to the next line
					lexemeString = "";
				} else if(i < str.length() && str.charAt(i) != ' ' && str.charAt(i) != '\t') {		// If the indexed character isn't white space
					if(	str.charAt(i) == '(' || str.charAt(i) == ')') {								// and we found parentheses 
						if(lexemeString != "") {													// and there was something before the parentheses
							if(!addLexeme(lexemeString, line, start))								// attempt to add the lexeme before the parentheses
								i = str.length();
							lexemeString = "";
						}
						if(!wasError) {																// if no error occurred 
								if(!addLexeme(String.valueOf(str.charAt(i)), line, start))			// attempt to add the parentheses as a lexeme
									i = str.length();
								lexemeString = "";
						}
					} else {																		// if the character wasn't a parentheses
						lexemeString += str.charAt(i);												// add the character to the lexemeString
						if(lexemeString.length() == 1) {											// if that was the first addition to the string
							start = i;																// mark this position as the start of the lexeme
						}
					}
				} else if(lexemeString != "") {														// if the indexed character was a space or end of line and we have a lexemeString
					if(!addLexeme(lexemeString, line, start))										// attempt to add lexeme
						i = str.length();
					lexemeString = "";
				}
			}
			line++;																					// move to the next line and repeat the for loop
		}
		inputScanner.close();
	}
	
	/**
	 * attempts to create a new lexeme and add it to the lexeme list
	 * 
	 * @param lexeme 
	 * @param line
	 * @param pos
	 * @return success 
	 */
	public boolean addLexeme(String lexeme, int line, int pos) {
		lexemes.add(new Lexeme(lexeme, kinds, line, pos+1));
		if(!lexemes.get(lexemes.size()-1).getSuccess())				// if the added lexeme wasn't supported by the grammar
			wasError = true;										// indicate an error
		return !wasError;
	}

	/**
	 * Outputs the lexemes in order in the terminal
	 */
	public void print() {
		if(!wasError && lexemes.size() > 0) {											// Check that there wasn't an error and we actually have lexemes (file wasn't empty)
			do {																		// Since there is at least 1 lexeme, print first ask questions later
				if(!(value() == null))
					System.out.println(position() + ", " + kind() + ", " + value());	// Print with value
				else
					System.out.println(position() + ", " + kind());						// Print without value
			} while ( next() != null );													// attempt to pull the next lexeme and stop if we get an error otherwise loop
		}
	}

	/*
	 * Project Requirements
	 */

	public Lexeme next() {
		Lexeme temp;									// Good practice to avoid sending the real object
		if(currentLexeme < lexemes.size() - 1) {		// If there is a next lexeme
			currentLexeme++;								// select it
			temp = lexemes.get(currentLexeme);				// copy it
			return temp;									// and return it
		} else
			return null;								// otherwise return a null
	}

	public String kind() {
		return lexemes.get(currentLexeme).getKind();	
	}

	public String value() {
		return lexemes.get(currentLexeme).getValue();
	}

	public String position() {
		return lexemes.get(currentLexeme).getPos();
	}

	public int getCurrentLexeme() {return currentLexeme;}
	public int getLexemeCount() {return lexemes.size();}
}
