package fr.lirmm.graphik.defeasible.tools.benchmark.preference;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import fr.lirmm.graphik.defeasible.core.DefeasibleKnowledgeBase;
import fr.lirmm.graphik.defeasible.core.io.DlgpDefeasibleParser;
import fr.lirmm.graphik.defeasible.core.preference.Preference;
import fr.lirmm.graphik.defeasible.tools.benchmark.core.BenchDataSet;
import fr.lirmm.graphik.defeasible.tools.benchmark.core.DataSetGenerator;
import fr.lirmm.graphik.graal.api.core.AtomSetException;
import fr.lirmm.graphik.graal.api.io.ParseException;

public class LevelsBenchDataSet implements BenchDataSet {
	private static final String NAME = "ChainBench";
	
	private int[] sizes;
	private int nbrAtoms, nbrTerms;
	
	public LevelsBenchDataSet(int[] sizes, int nbrAtoms, int nbrTerms) {
		this.sizes = sizes;
		this.nbrAtoms = nbrAtoms;
		this.nbrTerms = nbrTerms;
	}
	
	public LevelsBenchDataSet(int[] sizes) {
		this(sizes, 1, 1);
	}
	
	public void init() {
		// Nothing to Initialize
	}

	public String getName() {
		return NAME;
	}

	public DataSetGenerator getDataSets() {
		return new LocalDataSetGenerator(this.sizes, this.nbrAtoms, this.nbrTerms);
	}
	
	/**
	 * 
	 * @author hamhec
	 *
	 */
	class LocalDataSetGenerator extends DataSetGenerator {

		public LocalDataSetGenerator(int[] sizes, int nbrAtoms, int nbrTerms) {
			super(sizes, nbrAtoms, nbrTerms);
		}

		@Override
		protected Iterator<? extends Object> generate(int n) throws ParseException, AtomSetException {
			DefeasibleKnowledgeBase kb = new DefeasibleKnowledgeBase();
			
			int size = (2*n) +2;
			
			// Add first ground atoms
			for(int j=0; j <= size; j++) {
				String atomString = S + j + this.getTermsString("a") + ".";
				kb.addFact(atomString);
			}
			
			String label, body, head, ruleString;
			// Add rules
			for(int i=0; i < size; i++) {
				label = "[r" + i + "] ";
				body = S + i + this.getTermsString("X");
				head = P + i + this.getTermsString("X");
				
				ruleString = label + head + " <= " + body + ".";
				kb.addDefeasibleRule(ruleString);
				
				label = "[r" + NP + i + "] ";
				body = P + (i+1) + this.getTermsString("X");
				head = NP + i + this.getTermsString("X");
				ruleString = label + head + " <- " + body + ".";
				kb.addStrictRule(ruleString);
				
				kb.addNegativeConstraint("! :- " + P + i + this.getTermsString("X") + ", " + NP + i + this.getTermsString("X") + ".");
				
				// Add preference
				if((i % 2) != 0) { // i Is odd
					kb.addPreference(new Preference("r" + NP + i, "r" + i));
				}
			}
			
			label = "[r" + size + "] ";
			kb.addStrictRule(label + P + size + this.getTermsString("X") + " <- " + S + size + this.getTermsString("X") + ".");
			
			String query = P + "0" + this.getTermsString("a") + ".";
			List<Object> generatedKBandQuery = new LinkedList<Object>();
			generatedKBandQuery.add(kb);
			generatedKBandQuery.add(DlgpDefeasibleParser.parseQuery("?() :- " + query));
			
			return generatedKBandQuery.iterator();
		}
		
	}
}
