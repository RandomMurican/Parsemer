package com.randommurican.Parsemer;

import java.util.List;
import java.util.regex.Matcher;

/**
 * The Lexeme class is used to store the information
 * on each lexeme
 * 
 *	@author ej Byrne
 */

public class Lexeme {
	private String kind;		// the kind of lexeme we have
	private int line;			// which line the lexeme is on
	private int pos;			// position on the line
	private String value;		// optional value
	private boolean matched;	// If the passed lexeme doesn't match a grammar rule, this will be false
	
	Lexeme(String lexemeString, List<Kind> patterns, int line, int pos) {
		this.line = line;
		this.pos = pos;
		matched = false;
		boolean hasValue = false; // We assume false and change it when we know true
		
		/*
		 * Here we cycle through all the known patterns checking each one
		 * until we have a match. 
		 */
		for(int i = 0; i < patterns.size(); i++) {
			Matcher patternMatcher = patterns.get(i).getPattern().matcher(lexemeString);
	        if(patternMatcher.matches()) { 						// When we have a match
	        	this.kind = patterns.get(i).getKind();			// We pull the name of the kind
	        	if(patterns.get(i).hasValue()) {				// Check if we need a value
	        		this.value = lexemeString;					// Add the value if true
	        		hasValue = true;							// And mark down that it has a value
	        	}
	        	i = patterns.size();							// Then we skip the rest of the kinds
	        	matched = true;									// and lazily inform the program there were no errors
	        }
		} 
		if(!matched)											// Error printout if we didn't find a match
			System.out.println("Unexpected character at Line: " + line + " char: " + pos);
		if(hasValue)											// If the lexeme supports a value
			this.value = lexemeString;							// set the value
	}
	
	public boolean getSuccess() {return matched;}
	public String getKind() {return kind;}
	public String getValue() {return value;}
	public String getPos() {return line + ", " + pos;}
	
	public boolean hasValue() {
		if(value.equals(null))
			return false;
		return true;
	}
	
	public void print() {
		if(!value.equals(null))
			System.out.print("<" + line + "-" + pos + ", " + kind + ", \"" + value + "\"> ");	// Printed with value
		else
			System.out.print("<" + line + "-" + pos + ", " + kind + "> ");						// Printed without value
	}
	
}
