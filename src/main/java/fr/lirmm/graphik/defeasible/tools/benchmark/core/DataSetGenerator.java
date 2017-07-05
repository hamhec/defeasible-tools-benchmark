package fr.lirmm.graphik.defeasible.tools.benchmark.core;

import java.util.Iterator;

import fr.lirmm.graphik.defeasible.core.DefeasibleKnowledgeBase;

public abstract class DataSetGenerator implements Iterator<DefeasibleKnowledgeBase> {
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
	
	public DataSetGenerator(int[] sizes) {
		this(sizes, 1, 1);
	}

	public boolean hasNext() {
		if(!this.hasNextCallDone) {
			++this.position;
			this.hasNextCallDone = true;
		}
		return this.position < sizes.length;
	}

	public DefeasibleKnowledgeBase next() {
		if(!this.hasNextCallDone) {
			this.hasNext();
		}
		this.hasNextCallDone = false;
		return generate(sizes[position]);
	}
	
	public int getCurrentSize() {
		return this.sizes[this.position];
	}
	
	protected abstract DefeasibleKnowledgeBase generate(int i);
	
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
