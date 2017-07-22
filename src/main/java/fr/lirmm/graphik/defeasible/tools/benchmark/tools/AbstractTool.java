package fr.lirmm.graphik.defeasible.tools.benchmark.tools;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import fr.lirmm.graphik.defeasible.core.DefeasibleKnowledgeBase;
import fr.lirmm.graphik.defeasible.core.preference.Preference;
import fr.lirmm.graphik.defeasible.core.rules.DefeasibleRule;
import fr.lirmm.graphik.defeasible.core.rules.DefeaterRule;
import fr.lirmm.graphik.defeasible.tools.benchmark.core.Approach;
import fr.lirmm.graphik.graal.api.core.Atom;
import fr.lirmm.graphik.graal.api.core.ConjunctiveQuery;
import fr.lirmm.graphik.graal.api.core.NegativeConstraint;
import fr.lirmm.graphik.graal.api.core.Rule;
import fr.lirmm.graphik.graal.api.core.Term;
import fr.lirmm.graphik.util.profiler.CPUTimeProfiler;
import fr.lirmm.graphik.util.profiler.Profiler;
import fr.lirmm.graphik.util.stream.CloseableIterator;
import fr.lirmm.graphik.util.stream.IteratorException;

public abstract class AbstractTool implements Approach {
	
	private Profiler profiler;
	private StringBuilder KBStringBuilder;
	private String query;
	private List<Pair<String, ? extends Object>> results; 
	
	public abstract String getName();
	public abstract String formatStrictRule(Rule rule);
	public abstract String formatDefeasibleRule(DefeasibleRule rule);
	public abstract String formatDefeaterRule(DefeaterRule rule);
	public abstract String formatFact(Atom atom);
	public abstract String formatNegativeConstraint(NegativeConstraint nc);
	public abstract String formatPreference(Preference pref);
	public abstract String formatQuery(ConjunctiveQuery query);
	
	public void prepare(DefeasibleKnowledgeBase kb, ConjunctiveQuery query) {
		// Format Facts
		try {
			this.formatFacts(kb.getFacts().iterator());
		} catch (IteratorException e1) {
			e1.printStackTrace();
		}
		
		// Format Rules
		this.formatStrictRules(kb.getStrictRules().iterator());
		this.formatDefeasibleRules(kb.getDefeasibleRules().iterator());
		this.formatDefeaterRules(kb.getDefeaterRules().iterator());
	
		// Format Negative Constraints
		this.formatNegativeConstraints(kb.getNegativeConstraints().iterator());
		
		// Format Preferences
		try {
			this.formatPreferences(kb.getPreferences().iterator());
		} catch (IteratorException e) {
			e.printStackTrace();
		}
		
		// Format Query
		this.query = this.formatQuery(query);
	}
	
	// User initialize rather than the constructor because we need to do it for each iteration
	public void initialize() {
		this.profiler = new CPUTimeProfiler();
		this.results = new LinkedList<Pair<String, ? extends Object>>();
		this.KBStringBuilder = new StringBuilder();
	}

	public Iterator<Pair<String, ? extends Object>> getResults() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public Profiler getProfiler() {
		return this.profiler;
	}
	public String getKB() {
		return this.KBStringBuilder.toString();
	}
	public String getQuery() {
		return this.query;
	}
	
	public void formatStrictRules(Iterator<Rule> itRules) {
		while(itRules.hasNext()) {
			this.KBStringBuilder.append(this.formatStrictRule(itRules.next()));
			this.KBStringBuilder.append("\n");
		}
	}
	public void formatDefeasibleRules(Iterator<Rule> itRules) {
		while(itRules.hasNext()) {
			this.KBStringBuilder.append(this.formatDefeasibleRule((DefeasibleRule) itRules.next()));
			this.KBStringBuilder.append("\n");
		}
	}
	public void formatDefeaterRules(Iterator<Rule> itRules) {
		while(itRules.hasNext()) {
			this.KBStringBuilder.append(this.formatDefeaterRule((DefeaterRule) itRules.next()));
			this.KBStringBuilder.append("\n");
		}
	}
	
	public void formatNegativeConstraints(Iterator<Rule> itRules) {
		while(itRules.hasNext()) {
			this.KBStringBuilder.append(this.formatNegativeConstraint((NegativeConstraint) itRules.next()));
			this.KBStringBuilder.append("\n");
		}
	}
	
	public void formatFacts(CloseableIterator<Atom> it) throws IteratorException {
		while(it.hasNext()) {
			this.KBStringBuilder.append(this.formatFact(it.next()));
			this.KBStringBuilder.append("\n");
		}
	}
	
	public void formatPreferences(Iterator<Preference> it) throws IteratorException {
		while(it.hasNext()) {
			this.KBStringBuilder.append(this.formatPreference(it.next()));
			this.KBStringBuilder.append("\n");
		}
	}
	
	public String formatAtom(Atom atom) {
		String result = "";
		result += atom.getPredicate().getIdentifier() + "(";
		Iterator<Term> itTerms = atom.getTerms().iterator();
		result += itTerms.next().getIdentifier();
		while(itTerms.hasNext()) {
			result += "," + itTerms.next().getIdentifier();
		}
		result += ")";
		return result;
	}
}
