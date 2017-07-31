package fr.lirmm.graphik.defeasible.tools.benchmark.defeater;

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

public class DefeaterBenchDataSet implements BenchDataSet {
	private static final String NAME = "ChainBench";
	
	private int[] sizes;
	private int k;
	
	public DefeaterBenchDataSet(int[] sizes, int k) {
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
			
			// Create rules for P
			for(int i = 1; i <= n; i++) {
				// Add atoms for this rule to fire
				for(int j = 1; j <= k; j++) {
					kb.addFact(P+j+"_"+i+this.getTermsString("a")+".");
				}
				
				String label = "[r" + i + "] ";
				String head  = P + this.getTermsString("X");
				String body = P + "1_" + i + this.getTermsString("X");
				for(int j = 2; j <= k; j++) {
					body += ", " + P + j + "_" + i + this.getTermsString("X");
				}
				kb.addDefeasibleRule(label + head + " <= " + body + ".");
			}
			
			// Create defeaters for NP
			for(int i = 1; i <= n; i++) {
				// Add atoms for this rule to fire
				for(int j = 1; j <= k; j++) {
					kb.addFact(Q+j+"_"+i+this.getTermsString("a")+".");
				}
				
				String label = "[r" + i + "] ";
				String head  = NP + this.getTermsString("X");
				String body = Q + "1_" + i + this.getTermsString("X");
				for(int j = 2; j <= k; j++) {
					body += ", " + Q + j + "_" + i + this.getTermsString("X");
				}
				kb.addDefeaterRule(label + head + " <~ " + body + ".");
			}
			
			kb.addNegativeConstraint("! :- " + P + this.getTermsString("X") + ", " + NP + this.getTermsString("X") + ".");
			
			String query = P + this.getTermsString("a") + ".";
			List<Object> generatedKBandQuery = new LinkedList<Object>();
			generatedKBandQuery.add(kb);
			generatedKBandQuery.add(DlgpDefeasibleParser.parseQuery("?() :- " + query));
			
			return generatedKBandQuery.iterator();
		}
		
	}
}
