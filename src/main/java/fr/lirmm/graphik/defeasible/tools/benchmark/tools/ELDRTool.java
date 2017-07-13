package fr.lirmm.graphik.defeasible.tools.benchmark.tools;

import fr.lirmm.graphik.defeasible.core.preference.Preference;
import fr.lirmm.graphik.defeasible.core.rules.DefeasibleRule;
import fr.lirmm.graphik.defeasible.core.rules.DefeaterRule;
import fr.lirmm.graphik.graal.api.core.Atom;
import fr.lirmm.graphik.graal.api.core.Rule;

public class ELDRTool extends AbstractTool {
	public static final String NAME = "ELDR";
	@Override
	public String getName() {
		return NAME;
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
	public String formatNegativeConstraint() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String formatPreference(Preference pref) {
		// TODO Auto-generated method stub
		return null;
	}

}
