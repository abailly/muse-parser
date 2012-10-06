/*
 * Created on Mar 17, 2003
 *
 * @author henkel@cs.colorado.edu
 * 
 */
package bibtex.dom;

import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * An entry is something that can be referenced by a latex file using the \cite
 * command. E.g.
 * <pre>
 * &#x0040inproceedings{diwan98typebased,
 *    year=1998,
 *    pages={106-117},
 *    title={Type-Based Alias Analysis},
 *    url={citeseer.nj.nec.com/diwan98typebased.html},
 *    booktitle={SIGPLAN Conference on Programming Language Design and Implementation},
 *    author={Amer Diwan and Kathryn S. McKinley and J. Eliot B. Moss},
 * }
 * </pre>
 * For an example of how to create BibtexEntry objects, check out the documentation for the constructor of BibtexNode.
 * @author henkel
 */
public class BibtexEntry extends BibtexAbstractEntry {

	protected BibtexEntry(BibtexFile file,String entryType, String entryKey){
		super(file);
		this.entryKey=entryKey;
		// we intern the entry type for space optimization.
		this.entryType=entryType.toLowerCase().intern();
	}

	private String entryType;
	private String entryKey;
	private HashMap fields = new HashMap();

	/**
	 * @return String
	 */
	public String getEntryKey() {
		return entryKey;
	}
	
	public BibtexAbstractValue getFieldValue(String name){
		return (BibtexAbstractValue) fields.get(name);
	}

	/**
	 * @return String
	 */
	public String getEntryType() {
		return entryType;
	}

	/**
	 * Returns a read only view of the field map.
	 * This is a mapping from java.lang.String instances (field names) to instances of
	 * BibtexAbstractValue.
	 * 
	 * @return HashMap
	 */
	public Map getFields() {
		return Collections.unmodifiableMap((Map)fields.clone());
	}
	
	
	public void setField(String fieldName, BibtexAbstractValue fieldValue){
		// we intern fieldName for space optimization.
		fields.put(fieldName.toLowerCase().intern(),fieldValue);
	}

	/**
	 * Sets the entryKey.
	 * @param entryKey The entryKey to set
	 */
	public void setEntryKey(String entryKey) {
//		assert entryKey!=null: "BibtexEntry.setEntryKey(String entryKey): encountered entryKey==null.";
		this.entryKey = entryKey.toLowerCase();
	}

	/**
	 * Sets the entryType.
	 * @param entryType The entryType to set
	 */
	public void setEntryType(String entryType) {
//		assert entryType!=null : "BibtexEntry.setEntryType(String entryType): encountered entryType==null";
		this.entryType = entryType.toLowerCase().intern();
	}

	/* (non-Javadoc)
	 * @see bibtex.dom.BibtexNode#printBibtex(java.io.PrintWriter)
	 */
	public void printBibtex(PrintWriter writer) {
		writer.print('@');
		writer.print(this.entryType);
		writer.print('{');
		writer.print(this.entryKey);
		writer.println(',');
		String keys [] = new String[fields.keySet().size()];
		fields.keySet().toArray(keys);
		Arrays.sort(keys);
		for(int i=0;i<keys.length;i++){
			String key = keys[i];
			BibtexNode value = (BibtexNode) this.fields.get(key);
			writer.print('\t');
			writer.print(key);
			writer.print('=');
			value.printBibtex(writer);
			writer.println(',');
		}

		writer.println('}');
	}

	/**
	 * @param fieldName
	 */
	public void undefineField(String fieldName) {
		this.fields.remove(fieldName);
	}

}
