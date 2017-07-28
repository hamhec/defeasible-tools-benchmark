package fr.lirmm.graphik.defeasible.tools.benchmark.ambiguity;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import fr.lirmm.graphik.defeasible.core.DefeasibleKnowledgeBase;
import fr.lirmm.graphik.defeasible.core.io.DlgpDefeasibleParser;
import fr.lirmm.graphik.defeasible.tools.benchmark.core.BenchDataSet;
import fr.lirmm.graphik.defeasible.tools.benchmark.core.DataSetGenerator;
import fr.lirmm.graphik.graal.api.core.AtomSetException;
import fr.lirmm.graphik.graal.api.io.ParseException;

public class AmbiguityBenchDataSet implements BenchDataSet {
	private static final String NAME = "ChainBench";
	
	private int[] sizes;
	private int nbrTerms;
	
	public AmbiguityBenchDataSet(int[] sizes, int nbrTerms) {
		this.sizes = sizes;
		this.nbrTerms = nbrTerms;
	}
	
	public AmbiguityBenchDataSet(int[] sizes) {
		this(sizes, 1);
	}
	
	public void init() {
		// Nothing to Initialize
	}

	public String getName() {
		return NAME;
	}

	public DataSetGenerator getDataSets() {
		return new LocalDataSetGenerator(this.sizes, this.nbrTerms);
	}
	
	/**
	 * 
	 * @author hamhec
	 *
	 */
	class LocalDataSetGenerator extends DataSetGenerator {

		public LocalDataSetGenerator(int[] sizes, int nbrTerms) {
			super(sizes, 1, nbrTerms);
		}

		@Override
		protected Iterator<? extends Object> generate(int n) throws ParseException, AtomSetException {
			DefeasibleKnowledgeBase kb = new DefeasibleKnowledgeBase();
			
			//Add first ground atoms
			String atomString = P + "0" + this.getTermsString("a") + ".";
			kb.addFact(atomString);
			atomString = Q + "0" + this.getTermsString("a") + ".";
			kb.addFact(atomString);
			atomString = "t" + this.getTermsString("a") + ".";
			kb.addFact(atomString);
			
			// Add the linknig rules
			String label = "[r" + P + S + "] ";
			String body = P + n + this.getTermsString("X");
			String head = S + "0" + this.getTermsString("X");
			String ruleString = label + head + " <= " + body + ".";
			kb.addDefeasibleRule(ruleString);
			
			label = "[r" + NP + "] ";
			body = Q + n + this.getTermsString("X");
			head = NP + n  + this.getTermsString("X");
			ruleString = label + head + " <= " + body + ".";
			kb.addDefeasibleRule(ruleString);
			
			label = "[r" + NS + "] ";
			body = S + n + this.getTermsString("X");
			head = NS + this.getTermsString("X");
			ruleString = label + head + " <= " + body + ".";
			kb.addDefeasibleRule(ruleString);
			
			label = "[r" + S + "] ";
			body = "t"  + this.getTermsString("X");
			head = S + this.getTermsString("X");
			ruleString = label + head + " <= " + body + ".";
			kb.addDefeasibleRule(ruleString);
			// Add the rules
			for(int i=1; i <= n; i++) {
				// P chain
				label = "[r" + P + i + "] ";
				body = P + (i-1) + this.getTermsString("X");
				head = P + (i) + this.getTermsString("X");
				ruleString = label + head + " <= " + body + ".";
				kb.addDefeasibleRule(ruleString);
				
				// Q chain
				label = "[r" + Q + i + "] ";
				body = Q + (i-1) + this.getTermsString("X");
				head = Q + (i) + this.getTermsString("X");
				ruleString = label + head + " <= " + body + ".";
				kb.addDefeasibleRule(ruleString);
				
				// S chain
				label = "[r" + S + i + "] ";
				body = S + (i-1) + this.getTermsString("X");
				head = S + (i) + this.getTermsString("X");
				ruleString = label + head + " <= " + body + ".";
				kb.addDefeasibleRule(ruleString);
			}
			
			// Add Negative Constraints
			String ncString = "! :- " + NS + this.getTermsString("X") + ", " + S + this.getTermsString("X") + ".";
			kb.addNegativeConstraint(ncString);
			ncString = "! :- " + NP + n + this.getTermsString("X") + ", " + P + n + this.getTermsString("X") + ".";
			kb.addNegativeConstraint(ncString);
			
			// Query
			String query = S + this.getTermsString("a") + ".";
			List<Object> generatedKBandQuery = new LinkedList<Object>();
			generatedKBandQuery.add(kb);
			generatedKBandQuery.add(DlgpDefeasibleParser.parseQuery("?() :- " + query));
			
			return generatedKBandQuery.iterator();
		}
		
	}
}
