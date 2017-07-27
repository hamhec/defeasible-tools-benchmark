package fr.lirmm.graphik.defeasible.tools.benchmark.core;

import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import fr.lirmm.graphik.defeasible.core.DefeasibleKnowledgeBase;
import fr.lirmm.graphik.graal.api.core.AtomSetException;
import fr.lirmm.graphik.graal.api.io.ParseException;

public abstract class DataSetGenerator implements Iterator<Iterator<? extends Object>> {
	public static final int QUERY = 0, KB = 1;
	public static final String P = "p", NP="np", Q = "q", NQ="nq", S = "s", NS="ns";
	
	private int[] sizes;
	private int position;
	private int nbrAtoms;
	private int nbrTerms;
	
	private boolean hasNextCallDone;
	
	public DataSetGenerator(int[] sizes, int nbrAtoms, int nbrTerms) {
		this.sizes = sizes;
		this.nbrTerms = nbrTerms;
		this.nbrAtoms = nbrAtoms;
		this.position = -1;
		this.hasNextCallDone = false;
	}

	public boolean hasNext() {
		if(!this.hasNextCallDone) {
			++this.position;
			this.hasNextCallDone = true;
		}
		return this.position < sizes.length;
	}

	public Iterator<? extends Object> next() {
		if(!this.hasNextCallDone) {
			this.hasNext();
		}
		this.hasNextCallDone = false;
		try {
			return generate(sizes[position]);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public int getCurrentSize() {
		return this.sizes[this.position];
	}
	
	protected abstract Iterator<? extends Object> generate(int i) throws ParseException, AtomSetException;
	
	protected String getTermsString(String param) {
		String str = "(" + param + "0";
		
		for(int i =1; i < this.nbrTerms; i++) {
			str += "," + param + i;
		}
		str += ")";
		return str;
	}
	
	protected int getNbrAtoms() {
		return this.nbrAtoms;
	}
}
