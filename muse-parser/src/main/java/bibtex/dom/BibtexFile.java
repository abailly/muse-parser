/*
 * Created on Mar 17, 2003
 *
 * @author henkel@cs.colorado.edu
 * 
 */
package bibtex.dom;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * This is the root of a bibtex DOM tree and the factory for any bibtex model -
 * the only way to create nodes. For an example, check out the documentation for the constructor of BibtexNode.
 * 
 * @author henkel
 */
public class BibtexFile extends BibtexNode {

	

	private final ArrayList entries = new ArrayList();

	public BibtexFile() {
		super(null);
	}

	public void addEntry(BibtexAbstractEntry entry) {
		this.entries.add(entry);
	}
	
	public void removeEntry(BibtexAbstractEntry entry){
		this.entries.remove(entry);
	}

	/**
	 * returns an unmodifiable view of the entries.
	 * 
	 * @return List
	 */
	public List getEntries() {
		return Collections.unmodifiableList((List)entries.clone());
	}

	public BibtexConcatenatedValue makeConcatenatedValue(
		BibtexAbstractValue left,
		BibtexAbstractValue right) {
		return new BibtexConcatenatedValue(this,left, right);
	}
	
	public BibtexEntry makeEntry(String entryType, String entryKey){
		return new BibtexEntry(this,entryType,entryKey);
	}
	
	public BibtexPersonList makePersonList(){
		return new BibtexPersonList(this);
	}
	
	public BibtexPerson makePerson(	String first,
	String preLast,
	String last,
	String lineage,
	boolean isOthers){
		return new BibtexPerson(this,first,preLast,last,lineage,isOthers);
	}

	public BibtexPreamble makePreamble(BibtexAbstractValue content){
		return new BibtexPreamble(this,content);
	}
	
	/* content does not include the quotes or curly braces around the string! */
	public BibtexString makeString(String content){
		return new BibtexString(this,content);
	}
	
	public BibtexMacroDefinition makeMacroDefinition(String key, BibtexAbstractValue value){
		return new BibtexMacroDefinition(this,key,value);
	}
	
	public BibtexMacroReference makeMacroReference(String key){
		return new BibtexMacroReference(this,key);
	}
	
	public BibtexToplevelComment makeToplevelComment(String content){
		return new BibtexToplevelComment(this,content);
	}

	public void printBibtex(PrintWriter writer) {
		for (Iterator iter = this.entries.iterator(); iter.hasNext();) {
			BibtexNode node = (BibtexNode) iter.next();
			node.printBibtex(writer);
		}
		writer.flush();
	}

}
