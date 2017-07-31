package fr.lirmm.graphik.defeasible.tools.benchmark.preformance;

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

public class DirectedAcyclicGraphBenchDataSet implements BenchDataSet {
	private static final String NAME = "ChainBench";
	
	private int[] sizes;
	private int k;
	
	public DirectedAcyclicGraphBenchDataSet(int[] sizes, int k) {
		this.sizes = sizes;
		this.k = k;
	}
	
	public void init() {
		// Nothing to Initialize
	}

	public String getName() {
		return NAME;
	}

	public DataSetGenerator getDataSets() {
		return new LocalDataSetGenerator(this.sizes, this.k);
	}
	
	/**
	 * 
	 * @author hamhec
	 *
	 */
	class LocalDataSetGenerator extends DataSetGenerator {
		private int k;
		
		public LocalDataSetGenerator(int[] sizes, int k) {
			super(sizes, 1, 1);
			this.k = k;
		}
		
		
		@Override
		protected Iterator<? extends Object> generate(int n) throws ParseException, AtomSetException {
			DefeasibleKnowledgeBase kb = new DefeasibleKnowledgeBase();
			
			//  Create basic Atom
			for(int i = (2*k+1); i <= (2*k+k); i++) {
				kb.addFact(P+i+this.getTermsString("a") + ".");
			}
			// Create rules
			for(int i = 0; i <= (2*k); i++) {
				String label = "[r" + i + "] ";
				String head  = P + i + this.getTermsString("X");
				String body = P + (i+1) + this.getTermsString("X");
				for(int j = 1; j <= k; j++) {
					body += ", " + P + (i+j) + this.getTermsString("X");
				}
				kb.addDefeasibleRule(label + head + " <= " + body + ".");
			}
			
			String query = P + "0" + this.getTermsString("a") + ".";
			List<Object> generatedKBandQuery = new LinkedList<Object>();
			generatedKBandQuery.add(kb);
			generatedKBandQuery.add(DlgpDefeasibleParser.parseQuery("?() :- " + query));
			
			return generatedKBandQuery.iterator();
		}
		
	}
}
