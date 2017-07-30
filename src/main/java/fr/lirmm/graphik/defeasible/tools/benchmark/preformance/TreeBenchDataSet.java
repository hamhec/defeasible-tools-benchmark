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

public class TreeBenchDataSet implements BenchDataSet {
	private static final String NAME = "ChainBench";
	
	private int[] sizes;
	private int k;
	
	public TreeBenchDataSet(int[] sizes, int k) {
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
		
		private void generateRecursive(DefeasibleKnowledgeBase kb, String numHead, int currentLvl, int maxLvl) throws ParseException, AtomSetException {
			if(currentLvl == maxLvl) {
				kb.addFact(P+numHead+this.getTermsString("a") + ".");
			} else if(currentLvl < maxLvl) {
				this.generateForAtom(kb, numHead, currentLvl);
				currentLvl++;
				for(int i = 1; i <= k; i++) {
					this.generateRecursive(kb, i+"_"+numHead, currentLvl, maxLvl);
				}
			}
		}
		
		private void generateForAtom(DefeasibleKnowledgeBase kb, String numHead, int lvl) throws ParseException {
			String head = P + numHead + this.getTermsString("X");
			String body = P+"1_" + numHead + this.getTermsString("X");
			String label = "[r1_"+numHead+"] ";
			for(int i = 2; i <= k; i++) {
				body += ", "+ P + i + "_" + numHead + this.getTermsString("X");
			}
			kb.addDefeasibleRule(label + head + " <= " + body + ".");
		}
		
		@Override
		protected Iterator<? extends Object> generate(int n) throws ParseException, AtomSetException {
			DefeasibleKnowledgeBase kb = new DefeasibleKnowledgeBase();
			
			//  Create basic Atom and rules for lvl 1
			this.generateRecursive(kb, "0", 0, n);
			
			String query = P + "0" + this.getTermsString("a") + ".";
			List<Object> generatedKBandQuery = new LinkedList<Object>();
			generatedKBandQuery.add(kb);
			generatedKBandQuery.add(DlgpDefeasibleParser.parseQuery("?() :- " + query));
			
			return generatedKBandQuery.iterator();
		}
		
	}
}
