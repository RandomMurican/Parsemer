package com.randommurican.Parsemer;

/**
 * I apologize for this mess that is mostly hard coded. It hurt to type,
 * but I spent way to much time studying for physics and it hurt me 
 * time-wise for this project. At least, I passed the physics test!
 * 
 * I'm sad to say I didn't implement error catching or an AST. I was
 * really looking forward to that :/ 
 * 
 * @author ej Byrne
 *
 */

public class Parser {
	
	private Language lang = new Language();
	private boolean result;
	private int currentLexeme;

	Parser(Language lang) {
		this.lang = lang;
		result = true;
		currentLexeme = 0;
		while(result && currentLexeme < lang.getLexemeCount()) {
			result = isExpression();
			next();
		}
	}

	public boolean isExpression() {						/******* Expression *******/
		switch(lang.kind()) { 								// No terminals. Is the lexeme part of first(Expression)?
		case "not":
		case "true":
		case "false":
		case "NUM":
		case "ID":
		case "openParentheses":
			return isBooleanExpression();					// If yes, then pass it on for further testing
		default:
			return false;									// If not then it can't be anything else
		}
	}

	public boolean isBooleanExpression() {				/******* BooleanExpression *******/
		boolean result = false;
		switch(lang.kind()) { 								// No terminals. Is the lexeme part of first(BooleanExpression)?
		case "not":
		case "true":
		case "false":
		case "NUM":
		case "ID":
		case "openParentheses":
			result = isBooleanTerm();						// If yes, then pass it on for further testing
			break;
		default:
			return false;									// If not then kill the process, it can't be anything else
		}
		while(	lang.kind().equals("or") &&					// While we have an "or" chain going
				result == true) { 							// and the checks are passing
			next();
			result = isBooleanTerm();						// Check that a BoolTerm follows
		}
		return result;
	}

	public boolean isBooleanTerm() {					/******* BooleanTerm *******/
		boolean result = false;
		switch(lang.kind()) { 								// No terminals. Is the lexeme part of first(BooleanTerm)?
		case "not":
		case "true":
		case "false":
		case "NUM":
		case "ID":
		case "openParentheses":
			result = isBooleanFactor();						// If yes, then pass it on for further testing
			break;
		default:
			return false;									// If not then it can't be anything else
		}
		while(	lang.kind().equals("and") &&				// While we have an "and" chain going
				result == true) { 							// and the checks are passing
			next();
			result = isBooleanFactor();						// Check that a BoolFactor follows
		}
		return result;
	}

	public boolean isBooleanFactor() {					/******* BooleanFactor *******/
		boolean result = false;
		switch(lang.kind()) {
		case "not":											// Not is our only terminal and can simply be ignored
			next();
		case "true":										// Is the next lexeme part of first(BooleanFactor) excluding not?
		case "false":
		case "NUM":
		case "ID":
		case "openParentheses":
			result = isArithmeticExpression();				// If yes, then pass it on for further testing
			break;
		default:
			return false;									// If not then it can't be anything else
		}
		while((	lang.kind().equals("equal") ||				// While we have an "equal" chain
				lang.kind().equals("lessThan")) && 			// or a "lessThan" chain going
				result == true) { 							// and the checks are passing
			next();
			result = isArithmeticExpression();				// Check that a ArithmeticExpr follows
		}
		return result;
	}

	public boolean isArithmeticExpression() {			/******* ArithmeticExpression *******/
		switch(lang.kind()) { 								// No terminals. Is the lexeme part of first(ArithmeticExpression)?
		case "true":
		case "false":
		case "NUM":
		case "ID":
		case "openParentheses":
			result = isTerm();								// If yes, then pass it on for further testing
			break;
		default:
			return false;									// If not then it can't be anything else
		}
		while((	lang.kind().equals("plus") ||				// While we have an "plus" chain
				lang.kind().equals("minus")) && 			// or a "minus" chain going
				result == true) { 							// and the checks are passing
			next();
			result = isTerm();								// Check that a Term follows
		}
		return result;
	}

	public boolean isTerm() {							/******* Term *******/
		switch(lang.kind()) { 								// No terminals. Is the lexeme
		case "true":										//  part of first(Term)?
		case "false":
		case "NUM":
		case "ID":
		case "openParentheses":
			result = isFactor();							// If yes, then pass it on for further testing
			break;
		default:
			return false;									// If not then it can't be anything else
		}
		while((	lang.kind().equals("multiply") ||			// While we have an "multiply" chain
				lang.kind().equals("divide")) && 			// or a "divide" chain going
				result == true) { 							// and the checks are passing
			next();
			result = isFactor();							// Check that a Factor follows
		}
		return result;
	}

	public boolean isFactor() {							/******* Factor *******/
		switch(lang.kind()) {
		case "ID":											// Is the lexeme the ID terminal?
			next();												// then move onto the next lexeme
			return true;										// and return true
		case "openParentheses":								// If the lexeme is "("
			next();												// Check the next lexeme
			result = isExpression();							// to see if it's an Expression
			break;
		case "true":										// Otherwise see if the lexeme is part of first(Factor)
		case "false":
		case "NUM":
			result = isLiteral();							// If yes, then pass it on for further testing
			break;
		default:
			return false;									// If not then it can't be anything else
		}
		if(lang.kind().equals("closeParentheses"))			// Just picking up the straggling parentheses
			next();
		return result;
	}

	public boolean isLiteral() {						/******* Literal *******/
		switch(lang.kind()) {
		case "NUM":											// Is the lexeme the NUM terminal
			next();												// then move on
			return true;										// and return true
		case "true":										// otherwise is the lexeme part of first(Literal)?
		case "false":
			return isBooleanLiteral();						// If so, check if its a BooleanLiteral even though it definitely is...
		default:
			return false;									// Otherwise it can't be anything else
		}

	}

	public boolean isBooleanLiteral() {					/******* BooleanLiteral *******/
		switch(lang.kind()) {
		case "true":										// Is the lexeme a terminal?
		case "false":
			next();											// then move on
			return true;									// and return true
		default:
			return false;									// this shouldn't ever run...
		}
	}
	
	public boolean getResult() {return result;}				// Just because...
	
	// I didn't feel like writing this out 100 times in my code
	private void next() {
		currentLexeme++;
		lang.next();
	}
}
