/*
 * Created on Mar 27, 2003
 *
 * @author henkel@cs.colorado.edu
 * 
 */
package bibtex.expansions;

import java.util.LinkedList;

import bibtex.dom.BibtexFile;
import bibtex.dom.BibtexPerson;
import bibtex.dom.BibtexPersonList;
import bibtex.dom.BibtexString;

/**
 * @author henkel
 */
final class BibtexPersonListParser {

	public static BibtexPersonList parse(BibtexString personList) throws PersonListParserException {
		
		String content = personList.getContent();
		String[] tokens = tokenize(content);
		
		BibtexPersonList result = personList.getOwnerFile().makePersonList();
		if(tokens.length==0){
			return result;
		}
		int begin = 0;
		for (int i = 0; i < tokens.length; i++) {
			if (tokens[i].toLowerCase().equals(AND) && begin<i) {
				result.add(makePerson(tokens, begin, i, content,personList.getOwnerFile()));
				begin = i + 1;
			}
		}
		if(begin<tokens.length)
			result.add(makePerson(tokens, begin, tokens.length, content,personList.getOwnerFile()));
		return result;
	}

	private static BibtexPerson makePerson(
		String[] tokens,
		int begin,
		int end,
		String fullEntry, BibtexFile factory)
		throws PersonListParserException {		
		if (tokens[begin].equals("others")) {
			return factory.makePerson(null, null, null, null, true);
		} else {
			int numberOfCommas = 0;
			for (int i = begin; i < end; i++) {
				if (tokens[i] == COMMA)
					numberOfCommas++;
			}
			if (numberOfCommas == 0) {
				final String lineage;
				if (tokens[end - 1].equals("Jr")) {
					lineage = "Jr";
					end--;
				} else
					lineage = null;

				int lastLowercase = -1;
				for (int i = end - 1; i > begin; i--) {
					if (Character.isLowerCase(tokens[i].charAt(0))) {
						lastLowercase = i;
						break;
					}
				}
				final String last;
				if (lastLowercase != -1) {
					// everything after that is lastname ...
					StringBuffer buffer = new StringBuffer();
					for (int i = lastLowercase + 1; i < end; i++) {
						buffer.append(tokens[i]+" ");
					}
					last = buffer.toString().trim();
					end = lastLowercase + 1;
				} else {
					// the last token is the lastname ...
					last = tokens[end - 1];
					end--;
				}
				final String preLast;
				{
					int beginPreLast = end;
					for (int i = end - 1; i > begin; i--) {
						if (Character.isLowerCase(tokens[i].charAt(0))) {
							beginPreLast = i;
						} else
							break;
					}
					if (end - beginPreLast > 0) {
						StringBuffer buffer = new StringBuffer();
						for (int i = beginPreLast; i < end; i++) {
							buffer.append(tokens[i]+" ");
						}
						preLast = buffer.toString().trim();
					} else
						preLast = null;
					end = beginPreLast;
				}
				final String first;
				if (end - begin > 0) {
					StringBuffer buffer = new StringBuffer();
					for (int i = begin; i < end; i++)
						buffer.append(tokens[i]+" ");
					first = buffer.toString().trim();
				} else
					first = null;
				return factory.makePerson(first, preLast, last, lineage, false);
			} else if (numberOfCommas == 1 || numberOfCommas == 2) {
				final String first, lineage, last, preLast;
				if (numberOfCommas == 1) {
					// either first or lineage is empty ...
					if (tokens[end - 1].equals("Jr")) {
						lineage = "Jr";
						first = null;
						end -= 2; // skip the comma as well.
					} else {
						int beginFirst = end;
						for (int i = end - 1; i >= begin; i--) {
							if (tokens[i] == COMMA) {
								beginFirst = i + 1;
								break;
							}
						}
						StringBuffer buffer = new StringBuffer();
						for (int i = beginFirst; i < end; i++)
							buffer.append(tokens[i]+" ");
						first = buffer.toString().trim();
						lineage = null;
						end = beginFirst - 1; // skip the comma as well...
					}
				} else { // 2 commas ...
					int beginFirst = end;
					for (int i = end - 1; i >= begin; i--) {
						if (tokens[i] == COMMA) {
							beginFirst = i + 1;
							break;
						}
					}
					StringBuffer buffer = new StringBuffer();
					for (int i = beginFirst; i < end; i++)
						buffer.append(tokens[i]+" ");
					first = buffer.toString().trim();
					end = beginFirst - 1;
					if (!tokens[end - 1].equals("Jr"))
						throw new PersonListParserException(
							"Expected 'Jr' instead of '" + tokens[end - 1] + "' in "+fullEntry);
					lineage = "Jr";
					end -= 2;
				}
				{
					// parse preLast ...
					StringBuffer buffer = new StringBuffer();
					while (Character.isLowerCase(tokens[begin].charAt(0))) {
						buffer.append(tokens[begin++]+" ");
					}
					if (buffer.toString().trim().equals("")) {
						preLast = null;
					} else
						preLast = buffer.toString().trim();
				}
				{
					// parse last ...
					StringBuffer buffer = new StringBuffer();
					while (begin < end) {
						buffer.append(tokens[begin++]+" ");
					}
					if (buffer.toString().trim().equals("")) {
						throw new PersonListParserException("Last name empty in '" + fullEntry + "'");
					} else
						last = buffer.toString().trim();
					return factory.makePerson(first, preLast, last, lineage, false);
				}
			} else {
				throw new PersonListParserException("Too many commas in " + fullEntry + ".");
			}
		}
	}

	private static final String COMMA = ",".intern();
	private static final String AND = "and".intern();

	/**
	 * 
	 * 
	 * @param stringContent
	 * @return String[]
	 */
	private static String[] tokenize(String stringContent) {
		int numberOfOpenBraces = 0;
		int tokenBegin = 0;
		stringContent = stringContent + " ";
		// make sure the last character is whitespace ;-)
		LinkedList tokens = new LinkedList(); // just some strings ...
		for (int currentPos = 0; currentPos < stringContent.length(); currentPos++) {
			switch (stringContent.charAt(currentPos)) {
				case '{' :
					numberOfOpenBraces++;
					break;
				case '}' :
					numberOfOpenBraces--;
					break;
				case ',' :
					if (numberOfOpenBraces == 0) {
						if (tokenBegin <= currentPos - 1) {
							String potentialToken =
								stringContent.substring(tokenBegin, currentPos).trim();
							if (!potentialToken.equals("")) {
								tokens.add(potentialToken);
							}
						}
						tokens.add(COMMA);
						tokenBegin = currentPos + 1;
					}
				default :
					char currentChar = stringContent.charAt(currentPos);
					if (Character.isWhitespace(currentChar)||(currentChar=='~')) {
						if (numberOfOpenBraces == 0 && tokenBegin <= currentPos) {
							String potentialToken =
								stringContent.substring(tokenBegin, currentPos).trim();
							if (!potentialToken.equals("")) {
								tokens.add(potentialToken);
							}
							tokenBegin = currentPos + 1;
						}
					}
			}
		}
		String[] result = new String[tokens.size()];
		tokens.toArray(result);
		return result;
	}

	public static void main(String args[]) {
		try {
			for (int i = 0; i < args.length; i++) {
				System.out.println("-> "+args[i]);
				BibtexFile file = new BibtexFile();
				BibtexString string = file.makeString(args[i]);
				System.out.println(""+parse(string));
				System.out.println();
			}
		} catch (PersonListParserException e) {
			e.printStackTrace();
		}
	}
}
