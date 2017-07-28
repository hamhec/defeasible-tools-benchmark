package fr.lirmm.graphik.defeasible.tools.benchmark.existential;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import fr.lirmm.graphik.defeasible.core.DefeasibleKnowledgeBase;
import fr.lirmm.graphik.defeasible.core.io.DlgpDefeasibleParser;
import fr.lirmm.graphik.defeasible.tools.benchmark.core.BenchDataSet;
import fr.lirmm.graphik.defeasible.tools.benchmark.core.DataSetGenerator;
import fr.lirmm.graphik.graal.api.core.AtomSetException;
import fr.lirmm.graphik.graal.api.io.ParseException;

public class ExistentialBenchDataSet implements BenchDataSet {
	private static final String NAME = "ChainBench";
	
	private int[] sizes;
	
	public ExistentialBenchDataSet(int[] sizes) {
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
			
			
			for(int i=1; i <= n; i++) {
				// Add atom
				String atomString = P + (i) + "(a" + i +").";
				kb.addFact(atomString);
				
				String label = "[r" + i + "] ";
				String body = P + (i) + "(X)";
				String head = S + "(Y)";
				
				String ruleString = label + head + " <= " + body + ".";
				kb.addDefeasibleRule(ruleString);
			}
			
			// Add Query
			String query = S + "(a" + n + ").";
			List<Object> generatedKBandQuery = new LinkedList<Object>();
			generatedKBandQuery.add(kb);
			generatedKBandQuery.add(DlgpDefeasibleParser.parseQuery("?() :- " + query));
			
			return generatedKBandQuery.iterator();
		}
		
	}
}
