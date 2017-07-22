package fr.lirmm.graphik.defeasible.tools.benchmark.tools;

import fr.lirmm.graphik.defeasible.core.preference.Preference;
import fr.lirmm.graphik.defeasible.core.rules.DefeasibleRule;
import fr.lirmm.graphik.defeasible.core.rules.DefeaterRule;
import fr.lirmm.graphik.graal.api.core.Atom;
import fr.lirmm.graphik.graal.api.core.ConjunctiveQuery;
import fr.lirmm.graphik.graal.api.core.NegativeConstraint;
import fr.lirmm.graphik.graal.api.core.Rule;
import fr.lirmm.graphik.util.stream.CloseableIteratorWithoutException;

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
		return this.formatConjunctionOfAtoms(query.iterator());
	}
	
	public void run() {
		// TODO Auto-generated method stub
		
	}
	
	private String formatConjunctionOfAtoms(CloseableIteratorWithoutException<Atom> itConjunct) {
		String result = "";
		if(itConjunct.hasNext()) result += this.formatAtom(itConjunct.next());
		while(itConjunct.hasNext()) {
			result += ", " + this.formatAtom(itConjunct.next());
		}
		return result;
	}
	
	private String formatRule(Rule rule, String implication) {
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
