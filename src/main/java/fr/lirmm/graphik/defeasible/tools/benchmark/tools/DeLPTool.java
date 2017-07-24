package fr.lirmm.graphik.defeasible.tools.benchmark.tools;

import fr.lirmm.graphik.defeasible.core.preference.Preference;
import fr.lirmm.graphik.defeasible.core.rules.DefeasibleRule;
import fr.lirmm.graphik.defeasible.core.rules.DefeaterRule;
import fr.lirmm.graphik.graal.api.core.Atom;
import fr.lirmm.graphik.graal.api.core.ConjunctiveQuery;
import fr.lirmm.graphik.graal.api.core.NegativeConstraint;
import fr.lirmm.graphik.graal.api.core.Rule;
import fr.lirmm.graphik.util.stream.CloseableIteratorWithoutException;

public class DeLPTool extends AbstractTool {

	public void run() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String formatStrictRule(Rule rule) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String formatDefeasibleRule(DefeasibleRule rule) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String formatDefeaterRule(DefeaterRule rule) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String formatFact(Atom atom) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String formatNegativeConstraint(NegativeConstraint nc) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String formatPreference(Preference pref) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String formatQuery(ConjunctiveQuery query) {
		// TODO Auto-generated method stub
		return null;
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
