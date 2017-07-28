package fr.lirmm.graphik.defeasible.tools.benchmark.existential.fes;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import fr.lirmm.graphik.defeasible.core.DefeasibleKnowledgeBase;
import fr.lirmm.graphik.defeasible.core.io.DlgpDefeasibleParser;
import fr.lirmm.graphik.defeasible.tools.benchmark.core.BenchDataSet;
import fr.lirmm.graphik.defeasible.tools.benchmark.core.DataSetGenerator;
import fr.lirmm.graphik.graal.api.core.AtomSetException;
import fr.lirmm.graphik.graal.api.io.ParseException;

public class TransitiveChainBenchDataSet implements BenchDataSet {
	private static final String NAME = "ChainBench";
	
	private int[] sizes;
	
	public TransitiveChainBenchDataSet(int[] sizes) {
		this.sizes = sizes;
	}
	
	public void init() {
		// Nothing to Initialize
	}

	public String getName() {
		return NAME;
	}

	public DataSetGenerator getDataSets() {
		return new LocalDataSetGenerator(this.sizes);
	}
	
	/**
	 * 
	 * @author hamhec
	 *
	 */
	class LocalDataSetGenerator extends DataSetGenerator {

		public LocalDataSetGenerator(int[] sizes) {
			super(sizes,1,1);
		}

		@Override
		protected Iterator<? extends Object> generate(int n) throws ParseException, AtomSetException {
			DefeasibleKnowledgeBase kb = new DefeasibleKnowledgeBase();
			
			String predicate = P;
			for(int pass = 0; pass < 2; pass++) { // Two chains of P and Q
				//Add first ground atoms
				String atomString = predicate + "0(a,b)" + ".";
				kb.addFact(atomString);
				atomString = predicate + "0(b,c)" + ".";
				kb.addFact(atomString);
				
				// Add rules
				for(int i=1; i <= n; i++) {
					String label = "[r" + i + "] ";
					String body = predicate + (i-1) + "(X,Y), " + predicate + (i-1) + "(Y,Z)";
					String head = predicate + (i-1) + "(X,Z), " + predicate + (i) + "(X,Y), " + predicate + (i) + "(Y,Z)";
					
					String ruleString = label + head + " <= " + body + ".";
					kb.addDefeasibleRule(ruleString);
				}
				predicate = Q;
			}
			// Add negative Constraint
			kb.addNegativeConstraint("! :- " + P + n + "(X,Y), " + Q + n + "(X,Y).");
			// Add Query
			String query = P + (n) + "(a,b).";
			List<Object> generatedKBandQuery = new LinkedList<Object>();
			generatedKBandQuery.add(kb);
			generatedKBandQuery.add(DlgpDefeasibleParser.parseQuery("?() :- " + query));
			
			return generatedKBandQuery.iterator();
		}
		
	}
}
