package fr.lirmm.graphik.defeasible.tools.benchmark.team;

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

public class TeamBenchDataSet implements BenchDataSet {
	private static final String NAME = "ChainBench";
	
	private int[] sizes;
	private int nbrAtoms, nbrTerms;
	
	public TeamBenchDataSet(int[] sizes, int nbrAtoms, int nbrTerms) {
		this.sizes = sizes;
		this.nbrAtoms = nbrAtoms;
		this.nbrTerms = nbrTerms;
	}
	
	public TeamBenchDataSet(int[] sizes) {
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
		
		private void generateRecursive(DefeasibleKnowledgeBase kb, String numHead, int currentLvl, int maxLvl) throws ParseException, AtomSetException {
			if(currentLvl == maxLvl) {
				kb.addFact(P+numHead+"_"+maxLvl+this.getTermsString("a") + ".");
				//kb.addFact(P+numHead+"_"+maxLvl+this.getTermsString("a") + ".");
				//kb.addFact(P+numHead+"_"+maxLvl+this.getTermsString("a") + ".");
				//kb.addFact(P+numHead+"_"+maxLvl+this.getTermsString("a") + ".");
				
			} else if(currentLvl < maxLvl) {
				this.generateTeamsForAtom(kb, numHead, currentLvl);
				currentLvl++;
				this.generateRecursive(kb, "1_"+numHead, currentLvl, maxLvl);
				this.generateRecursive(kb, "2_"+numHead, currentLvl, maxLvl);
				this.generateRecursive(kb, "3_"+numHead, currentLvl, maxLvl);
				this.generateRecursive(kb, "4_"+numHead, currentLvl, maxLvl);
			}
		}
		
		private void generateTeamsForAtom(DefeasibleKnowledgeBase kb, String numHead, int lvl) throws ParseException {
			String predicate = P;
			for(int i = 1; i <= 4; i++) {
				kb.addDefeasibleRule("[r" + (i) + "_"+numHead+"_"+lvl+"] "+predicate+numHead+"_"+lvl+ this.getTermsString("X") 
				+ " <= " + P+i+"_"+numHead+"_"+(lvl+1) + this.getTermsString("X") + ".");
				if(i == 2) {
					predicate = NP;
				}
			}
			kb.addPreference(new Preference("r1_"+numHead+"_"+lvl, "r3_"+numHead+"_"+lvl));
			kb.addPreference(new Preference("r2_"+numHead+"_"+lvl, "r4_"+numHead+"_"+lvl));
			kb.addNegativeConstraint("!:- " +P+numHead+"_"+lvl+ this.getTermsString("X") + ", " +NP+numHead+"_"+lvl+ this.getTermsString("X") + ".");
		}
		
		@Override
		protected Iterator<? extends Object> generate(int n) throws ParseException, AtomSetException {
			DefeasibleKnowledgeBase kb = new DefeasibleKnowledgeBase();
			
			//  Create basic Atom and rules for lvl 1
			this.generateRecursive(kb, "0", 0, n);
			
			String query = P + "0_0" + this.getTermsString("a") + ".";
			List<Object> generatedKBandQuery = new LinkedList<Object>();
			generatedKBandQuery.add(kb);
			generatedKBandQuery.add(DlgpDefeasibleParser.parseQuery("?() :- " + query));
			
			return generatedKBandQuery.iterator();
		}
		
	}
}
