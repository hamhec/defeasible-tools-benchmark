package fr.lirmm.graphik.defeasible.tools.benchmark.tools;

import java.util.Iterator;

import fr.lirmm.graphik.defeasible.core.preference.Preference;
import fr.lirmm.graphik.defeasible.core.rules.DefeasibleRule;
import fr.lirmm.graphik.defeasible.core.rules.DefeaterRule;
import fr.lirmm.graphik.graal.api.core.Atom;
import fr.lirmm.graphik.graal.api.core.ConjunctiveQuery;
import fr.lirmm.graphik.graal.api.core.NegativeConstraint;
import fr.lirmm.graphik.graal.api.core.Rule;
import fr.lirmm.graphik.graal.core.Rules;
import fr.lirmm.graphik.util.stream.CloseableIteratorWithoutException;

public class ASPICTool extends AbstractTool {
	public static final String NAME = "ASPIC",
			DEFEASIBLE = "0.5", SUPERIOR = "0.8", INFERIOR = "0.2";
	
	public void run() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public String formatStrictRule(Rule rule) {
		return this.formatRule(rule, " <- ", "");
	}

	@Override
	public String formatDefeasibleRule(DefeasibleRule rule) {
		return this.formatRule(rule, " <- ", DEFEASIBLE);
	}

	@Override
	public String formatDefeaterRule(DefeaterRule rule) {
		return null; // ASPIC does not support Defeaters.
	}

	@Override
	public String formatFact(Atom atom) {
		return this.formatAtom(atom) + ".";
	}

	@Override
	public String formatNegativeConstraint(NegativeConstraint nc) {
		String result = "";
		CloseableIteratorWithoutException<Atom> itBody = nc.getBody().iterator();
		String firstAtom = this.formatAtom(itBody.next());
		String secondAtom = this.formatAtom(itBody.next());
		// the first atom implies the negation and the second the vice-versa
		result += "~" + firstAtom + " <- " + secondAtom + ".\n"; 
		result += "~" + secondAtom + " <- " + firstAtom + ".";
		return result;
	}

	@Override
	public String formatPreference(Preference pref) {
		String updatedRule = "";
		for(Rule rule: this.getKB().getDefeasibleRules()) {
			if(rule.getLabel().equals(pref.getSuperior()) || rule.getLabel().equals(pref.getInferior())) {
				String r = this.formatDefeasibleRule((DefeasibleRule)rule);
				int index = this.getKBStringBuilder().indexOf(r);
				this.getKBStringBuilder().delete(index, index + r.length()); // delete the old version
				if(rule.getLabel().equals(pref.getSuperior())) {
					updatedRule += this.formatRule(rule, " <- ", SUPERIOR);
				} else {
					updatedRule += this.formatRule(rule, " <- ", INFERIOR);
				}
			}
		}
		return updatedRule; // return the updated version of the rule or rules affected by this preference
	}

	@Override
	public String formatQuery(ConjunctiveQuery query) {
		return this.formatAtom(query.getAtomSet().iterator().next());
	}


	protected String formatRule(Rule rule, String implication, String defeasibleValue) {
		String result = "";
		
		// In case the head is a conjunction and not atomic
		Iterator<Rule> itAtomicHeadRule = Rules.computeAtomicHead(rule).iterator();
		
		while(itAtomicHeadRule.hasNext()) {
			String rString = "";
			Rule r = itAtomicHeadRule.next();
			// Format Head
			rString += this.formatAtom(r.getHead().iterator().next());
			rString += implication;
			// Format Body
			rString += this.formatConjunctionOfAtoms(r.getBody().iterator());
			result += rString + " " + defeasibleValue + ".\n";
		}
		
		return result;
	}

}
