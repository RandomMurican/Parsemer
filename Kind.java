package com.randommurican.Parsemer;

import java.util.regex.Pattern;

/**
 * The Kind class is used to store the regEx patterns that
 * make up the grammar we were given. The kind in this
 * context is the name of the pattern for example ID, 
 * DIGIT, or EXPRESSION. 
 * 
 *	@author ej Byrne
 */

public class Kind {
	private String kind;	// name of the lexeme kind
	private Pattern pat;	// RegEx pattern for the kind
	private boolean bool;	// whether the pattern gets a value or not
	
	// if no boolean value is passed we assume it's false
	Kind(String kind, String inputPat) {
		this.kind = kind;
		this.pat = Pattern.compile(inputPat);
		bool = false;
	}
	
	Kind(String kind, String inputPat, boolean bool) {
		this.kind = kind;
		this.pat = Pattern.compile(inputPat);
		this.bool = bool;
	}
	
	public String getKind() {return kind;}
	public Pattern getPattern() {return pat;}
	public boolean hasValue() {return bool;}
}
