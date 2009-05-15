/* This file is part of the Joshua Machine Translation System.
 * 
 * Joshua is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1
 * of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free
 * Software Foundation, Inc., 59 Temple Place, Suite 330, Boston,
 * MA 02111-1307 USA
 */
package joshua.corpus;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

/**
 * Partial basic implementation of a symbol table.
 * 
 * @author Lane Schwartz
 * @author Zhifei Li
 * @version $LastChangedDate$
 */
public abstract class AbstractSymbolTable implements SymbolTable {
	
	/** 
	 * Character set encoding used when 
	 * exporting and importing binary files to and from disk. 
	 */
	public String characterEncoding = "UTF-8";
	
	
	/* See Javadoc for SymbolTable interface. */
	final public int[] addTerminals(String sentence){
		return addTerminals(sentence.split("\\s+"));
	}	
	
	/* See Javadoc for SymbolTable interface. */
    final public int[] addTerminals(String[] words){
		int[] res =new int[words.length];
		for(int t=0; t<words.length; t++)
			res[t]=addTerminal(words[t]);
		return res;
	}	
    
    /* See Javadoc for SymbolTable interface. */
	final public int getTargetNonterminalIndex(int id) {
		if (! isNonterminal(id)) {
			return -1;
		} else {
			// TODO: get rid of this expensive interim object
			String symbol = getWord(id);
			
			return getTargetNonterminalIndex(symbol);
		}
	}
	
	/* See Javadoc for SymbolTable interface. */
	final public int getTargetNonterminalIndex(String wrd) {
		// Assumes the last character is a digit
		// and extracts it, starting from one.
		// Assumes the whole prefix is the
		// nonterminal-ID portion of the string
		return Integer.parseInt( wrd.substring(wrd.length() - 2,	wrd.length() - 1) ) - 1;
	}

	/* See Javadoc for SymbolTable interface. */
	public String getUnknownWord() {
		return SymbolTable.UNKNOWN_WORD_STRING;
	}

	/* See Javadoc for SymbolTable interface. */
	public int getUnknownWordID() {
		return SymbolTable.UNKNOWN_WORD;
	}
	
	/* See Javadoc for SymbolTable interface. */
	public String getWords(int[] wordIDs, boolean ntIndexIncrements) {
		StringBuilder s = new StringBuilder();
		
		int nextNTIndex = 1;
		for(int t=0; t<wordIDs.length; t++){
			if(t>0) {
				s.append(' ');
			}
			
			int wordID = wordIDs[t];
			
//			if (wordID >= vocabList.size()) { 
//				s.append(UNKNOWN_WORD_STRING);
//			} else 
			if (wordID < 0) {
				s.append("[X,"); //XXX This should NOT be hardcoded here!
				if (ntIndexIncrements) {
					s.append(nextNTIndex++);
				} else {
					s.append(-1*wordID);
				}
				s.append(']');
			} else {
				s.append(getWord(wordID));
			}

		}
		
		return s.toString();
	}
	
	/* See Javadoc for SymbolTable interface. */
	public boolean isNonterminal(int id) {
		if (id < 0) 
			return true;
		else
			return false;
	}
	
	/* See Javadoc for SymbolTable interface. */
	public void setExternalizableEncoding(String charsetName) {
		characterEncoding = charsetName;
	}
	
	/**
	 * Writes a binary form of the vocabulary to disk.
	 * 
	 * @param out
	 * @param charsetName
	 * @throws IOException
	 */
	public void write(FileOutputStream out, String charsetName) 
			throws IOException {
		
		setExternalizableEncoding(charsetName);
		
		writeExternal(new ObjectOutputStream(out));
	}
	
	/** This method throws an <code>UnsupportedOperationException</code>. */
	public void readExternal(ObjectInput in) throws IOException,
	ClassNotFoundException {
		throw new UnsupportedOperationException();
	}

	/** This method throws an <code>UnsupportedOperationException</code>. */
	public void writeExternal(ObjectOutput out) throws IOException {
		throw new UnsupportedOperationException();
	}
	
}
