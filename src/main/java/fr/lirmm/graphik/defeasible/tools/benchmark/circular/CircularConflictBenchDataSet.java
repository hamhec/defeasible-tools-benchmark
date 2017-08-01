package fr.lirmm.graphik.defeasible.tools.benchmark.circular;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import fr.lirmm.graphik.defeasible.core.DefeasibleKnowledgeBase;
import fr.lirmm.graphik.defeasible.core.io.DlgpDefeasibleParser;
import fr.lirmm.graphik.defeasible.tools.benchmark.core.BenchDataSet;
import fr.lirmm.graphik.defeasible.tools.benchmark.core.DataSetGenerator;
import fr.lirmm.graphik.graal.api.core.AtomSetException;
import fr.lirmm.graphik.graal.api.io.ParseException;

public class CircularConflictBenchDataSet implements BenchDataSet {
	private static final String NAME = "CircleBench";
	
	private int[] sizes;
	private int nbrAtoms, nbrTerms;
	
	public CircularConflictBenchDataSet(int[] sizes, int nbrAtoms, int nbrTerms) {
		this.sizes = sizes;
		this.nbrAtoms = nbrAtoms;
		this.nbrTerms = nbrTerms;
	}
	
	public CircularConflictBenchDataSet(int[] sizes) {
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

		private void generateCircularConflict(DefeasibleKnowledgeBase kb, int i) throws ParseException {
			int j = ((i-1) * 2) + 1;
			
			String label,head,body;
			String predicate = P;
			String nPredicate = NQ;
			for(int k=0; k < 2; k++) {
				label = "[r" + predicate + j + "] ";
				body = predicate + (j-1) + this.getTermsString("X");
				head = predicate + (j) + this.getTermsString("X");
				kb.addDefeasibleRule(label + head + " <= " + body + ".");
				
				label = "[r" + predicate + (j+1) + "] ";
				body = predicate + (j) + this.getTermsString("X");
				head = predicate + (j+1) + this.getTermsString("X");
				kb.addDefeasibleRule(label + head + " <= " + body + ".");
				
				label = "[rn" + predicate + (j) + "] ";
				body = predicate + (j+1) + this.getTermsString("X");
				head = nPredicate + (j) + this.getTermsString("X");
				kb.addDefeasibleRule(label + head + " <= " + body + ".");
				
				predicate = Q;
				nPredicate = NP;
			}
			kb.addNegativeConstraint("!:- " + P+(j)+this.getTermsString("X")+ ", " + NP+(j)+this.getTermsString("X") + ".");
			kb.addNegativeConstraint("!:- " + Q+(j)+this.getTermsString("X")+ ", " + NQ+(j)+this.getTermsString("X") + ".");
		}
			
	
		@Override
		protected Iterator<? extends Object> generate(int n) throws ParseException, AtomSetException {
			DefeasibleKnowledgeBase kb = new DefeasibleKnowledgeBase();
			
			//Add first ground atoms
			kb.addFact(P + 0 + this.getTermsString("a") + ".");
			kb.addFact(Q + 0 + this.getTermsString("a") + ".");
			
			// Add rules for each circular conflict
			for(int i=1; i <= n; i++) {
				this.generateCircularConflict(kb, i);
			}
			
			String query = P + (n*2) + this.getTermsString("a") + ".";
			List<Object> generatedKBandQuery = new LinkedList<Object>();
			generatedKBandQuery.add(kb);
			generatedKBandQuery.add(DlgpDefeasibleParser.parseQuery("?() :- " + query));
			
			return generatedKBandQuery.iterator();
		}
		
	}
}
