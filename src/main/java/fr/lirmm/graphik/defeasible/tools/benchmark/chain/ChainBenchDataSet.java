package fr.lirmm.graphik.defeasible.tools.benchmark.chain;

import fr.lirmm.graphik.defeasible.core.DefeasibleKnowledgeBase;
import fr.lirmm.graphik.defeasible.core.io.DlgpDefeasibleParser;
import fr.lirmm.graphik.defeasible.tools.benchmark.core.BenchDataSet;
import fr.lirmm.graphik.defeasible.tools.benchmark.core.DataSetGenerator;
import fr.lirmm.graphik.graal.api.core.Atom;
import fr.lirmm.graphik.graal.api.core.AtomSetException;
import fr.lirmm.graphik.graal.api.core.Rule;
import fr.lirmm.graphik.graal.api.io.ParseException;

public class ChainBenchDataSet implements BenchDataSet {
	private static final String NAME = "ChainBench";
	
	private int[] sizes;
	private int nbrAtoms, nbrTerms;
	
	public ChainBenchDataSet(int[] sizes, int nbrAtoms, int nbrTerms) {
		this.sizes = sizes;
		this.nbrAtoms = nbrAtoms;
		this.nbrTerms = nbrTerms;
	}
	
	public ChainBenchDataSet(int[] sizes) {
		this(sizes, 1, 1);
	}
	
	public void init() {
		// Nothing to Initialize
	}

	public String getName() {
		return NAME;
	}

	public DataSetGenerator getDataSets() {
		return new ChainDataSetGenerator(this.sizes, this.nbrAtoms, this.nbrTerms);
	}
	
	/**
	 * 
	 * @author hamhec
	 *
	 */
	class ChainDataSetGenerator extends DataSetGenerator {

		public ChainDataSetGenerator(int[] sizes, int nbrAtoms, int nbrTerms) {
			super(sizes, nbrAtoms, nbrTerms);
		}

		@Override
		protected DefeasibleKnowledgeBase generate(int n) throws ParseException, AtomSetException {
			DefeasibleKnowledgeBase kb = new DefeasibleKnowledgeBase();
			
			//Add first ground atoms
			for(int j=0; j < this.getNbrAtoms(); j++) {
				String atomString = P + "0" + "_" + j + this.getTermsString("a") + ".";
				kb.addFact(atomString);
			}
			
			// Add rules
			for(int i=1; i <= n; i++) {
				String label = "[r" + i + "] ";
				String body = P + (i-1) + "_0" + this.getTermsString("X");
				for(int j=1; j < this.getNbrAtoms(); j++) {// Add more atoms to the body if nmore than one
					body += ", ";
					body += P + (i-1) + "_" + j + this.getTermsString("X");
				}
				
				String head = P + (i) + "_0" + this.getTermsString("X");
				for(int j=1; j < this.getNbrAtoms(); j++) { // Add more atoms to the head if more than one
					head += ", ";
					head += P + (i) + "_" + j + this.getTermsString("X");
				}
				
				String ruleString = label + head + " <= " + body + ".";
				kb.addDefeasibleRule(ruleString);
			}
			
			return kb;
		}
		
	}
}
