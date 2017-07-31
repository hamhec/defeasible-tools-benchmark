package fr.lirmm.graphik.defeasible.tools.benchmark.tools;

import java.util.Map.Entry;

import fr.lirmm.graphik.defeasible.core.DefeasibleKnowledgeBase;
import fr.lirmm.graphik.defeasible.core.preference.Preference;
import fr.lirmm.graphik.defeasible.core.rules.DefeasibleRule;
import fr.lirmm.graphik.defeasible.core.rules.DefeaterRule;
import fr.lirmm.graphik.defeasible.tools.benchmark.core.Approach;
import fr.lirmm.graphik.eldr.core.ExtendedDialecticalGraph;
import fr.lirmm.graphik.graal.api.core.Atom;
import fr.lirmm.graphik.graal.api.core.ConjunctiveQuery;
import fr.lirmm.graphik.graal.api.core.NegativeConstraint;
import fr.lirmm.graphik.graal.api.core.Rule;

public class ELDRTool extends AbstractTool {
	public static final String NAME = "ELDR";
	
	private boolean isGroundQuery;
	
	public ELDRTool() {
		super();
		this.isGroundQuery = false;
	}
	
	@Override
	public String getName() {
		return NAME;
	}
	
	@Override
	public String formatStrictRule(Rule rule) {
		return this.formatRule(rule, " <- ");
	}

	@Override
	public String formatDefeasibleRule(DefeasibleRule rule) {
		return this.formatRule(rule, " <= ");
	}

	@Override
	public String formatDefeaterRule(DefeaterRule rule) {
		return this.formatRule(rule, " <~ ");
	}

	@Override
	public String formatFact(Atom atom) {
		return this.formatAtom(atom) + ".";
	}

	@Override
	public String formatNegativeConstraint(NegativeConstraint nc) {
		String result = "! :- ";
		result += this.formatConjunctionOfAtoms(nc.getBody().iterator());
		result += ".";
		return result;
	}

	@Override
	public String formatPreference(Preference pref) {
		String result = "[" + pref.getSuperior() + "]";
		result += " > ";
		result += "[" + pref.getInferior() + "].";
		return result;
	}

	@Override
	public String formatQuery(ConjunctiveQuery query) {
		if(query.isBoolean()) { // The query is ground (given my on-the-fly convention)
			this.isGroundQuery = true;
		}
		return this.formatConjunctionOfAtoms(query.iterator()) + ".";
	}
	
	public void run() {
		String KBString = this.getKBString();
		
		System.out.println(KBString);
		System.out.println(this.getQuery());
		try {
			// I- Prepare Phase
			this.getProfiler().clear();
			this.getProfiler().start(Approach.LOADING_TIME);
			
			DefeasibleKnowledgeBase kb = new DefeasibleKnowledgeBase(KBString);
			ExtendedDialecticalGraph edg = new ExtendedDialecticalGraph(kb);
			edg.build();
			this.getProfiler().stop(Approach.LOADING_TIME);
			
			// II- Query Answering Phase
			String entailement = "";
			this.getProfiler().start(Approach.EXE_TIME);
			if(this.isGroundQuery) {
				entailement = edg.groundQuery(this.getQuery());
			} else {
				// TODO Non ground queries
			}
			this.getProfiler().stop(Approach.EXE_TIME);
			
			// Submit Results
			this.addResult(Approach.ANSWER, entailement);
			for(Entry<String, Object> entry: this.getProfiler().entrySet()) {
				this.addResult(entry.getKey(), entry.getValue());
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	protected String formatRule(Rule rule, String implication) {
		String result = "";
		// Parse the label if it was defined
		if(rule.getLabel() != null && !rule.getLabel().isEmpty()) {
			result += "[" + rule.getLabel() + "] ";
		}
		// Parse the head of the rule
		result += this.formatConjunctionOfAtoms(rule.getHead().iterator());
		result += implication;
		// Parse the body of the rule
		result += this.formatConjunctionOfAtoms(rule.getBody().iterator());
		result += ".";
		return result;
	}
}
