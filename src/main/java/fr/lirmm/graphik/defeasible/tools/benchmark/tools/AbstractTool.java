package fr.lirmm.graphik.defeasible.tools.benchmark.tools;

import java.util.Iterator;

import org.apache.commons.lang3.tuple.Pair;

import fr.lirmm.graphik.defeasible.core.DefeasibleKnowledgeBase;
import fr.lirmm.graphik.defeasible.core.preference.Preference;
import fr.lirmm.graphik.defeasible.core.rules.DefeasibleRule;
import fr.lirmm.graphik.defeasible.core.rules.DefeaterRule;
import fr.lirmm.graphik.defeasible.tools.benchmark.core.Approach;
import fr.lirmm.graphik.graal.api.core.Atom;
import fr.lirmm.graphik.graal.api.core.Rule;
import fr.lirmm.graphik.util.profiler.Profiler;

public abstract class AbstractTool implements Approach {
	
	Profiler profiler;

	public void run() {
		// TODO Auto-generated method stub
		
	}

	public void prepare(DefeasibleKnowledgeBase kb) {
		// TODO Auto-generated method stub
		
	}

	public void initialize() {
		// TODO Auto-generated method stub
		
	}

	public Iterator<Pair<String, ? extends Object>> getResults() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public Profiler getProfiler() {
		return this.profiler;
	}
	
	public abstract String getName();
	public abstract String formatStrictRule(Rule rule);
	public abstract String formatDefeasibleRule(DefeasibleRule rule);
	public abstract String formatDefeaterRule(DefeaterRule rule);
	public abstract String formatFact(Atom atom);
	public abstract String formatNegativeConstraint();
	public abstract String formatPreference(Preference pref);
	
	
}
