package fr.lirmm.graphik.defeasible.tools.benchmark.tools;

import java.io.StringReader;
import java.util.Map.Entry;

import fr.lirmm.graphik.DEFT.core.DefeasibleKB;
import fr.lirmm.graphik.defeasible.core.preference.Preference;
import fr.lirmm.graphik.defeasible.core.rules.DefeasibleRule;
import fr.lirmm.graphik.defeasible.core.rules.DefeaterRule;
import fr.lirmm.graphik.defeasible.tools.benchmark.core.Approach;
import fr.lirmm.graphik.graal.api.core.Atom;
import fr.lirmm.graphik.graal.api.core.ConjunctiveQuery;
import fr.lirmm.graphik.graal.api.core.NegativeConstraint;
import fr.lirmm.graphik.graal.api.core.Rule;

public class DEFTTool extends AbstractTool {
	public static final String NAME = "DEFT";
	
	public void run() {
String KBString = this.getKBString();
		
		System.out.println(KBString);
		System.out.println(this.getQuery());
		try {
			// I- Prepare Phase
			this.getProfiler().clear();
			
			this.getProfiler().start(Approach.LOADING_TIME);
			DefeasibleKB kb = new DefeasibleKB(new StringReader(KBString));
			kb.saturate();
			Atom query = kb.getAtomsSatisfiyingAtomicQuery(this.getQuery()).iterator().next();
			this.getProfiler().stop(Approach.LOADING_TIME);
			
			// II- Query Answering Phase
			this.getProfiler().start(Approach.EXE_TIME);
			String entailement = this.getAnswer(kb.EntailmentStatus(query));
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

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public String formatStrictRule(Rule rule) {
		return this.formatRule(rule, null);
	}

	@Override
	public String formatDefeasibleRule(DefeasibleRule rule) {
		return this.formatRule(rule, "DEFT");
	}

	@Override
	public String formatDefeaterRule(DefeaterRule rule) {
		return null; // DEFT does not support defeaters.
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
		String result = "[DEFT" + pref.getSuperior() + "]";
		result += " > ";
		result += "[DEFT" + pref.getInferior() + "].";
		return result;
	}

	@Override
	public String formatQuery(ConjunctiveQuery query) {
		return "?() :- " + this.formatAtom(query.getAtomSet().iterator().next()) + ".";
	}
	
	protected String formatRule(Rule rule, String label) {
		String result = "";
		// Parse the label if it was defined
		if(label != null || (rule.getLabel() != null && !rule.getLabel().isEmpty())) {
			result += "[" + label + rule.getLabel() + "] ";
		}
		// Parse the head of the rule
		result += this.formatConjunctionOfAtoms(rule.getHead().iterator());
		result += " :- ";
		// Parse the body of the rule
		result += this.formatConjunctionOfAtoms(rule.getBody().iterator());
		result += ".";
		return result;
	}
	
	private String getAnswer(int ans) {
		String answerString = "";
		
		switch(ans) {
			case DefeasibleKB.NOT_ENTAILED: answerString = "No"; break;
			case DefeasibleKB.DEFEASIBLY_ENTAILED: answerString = "Yes"; break;
			case DefeasibleKB.STRICTLY_ENTAILED: answerString = "Yes";
		}

		return answerString;
	}
}
