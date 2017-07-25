package fr.lirmm.graphik.defeasible.tools.benchmark.tools;

import java.util.Iterator;
import java.util.Map.Entry;

import fr.lirmm.graphik.defeasible.core.preference.Preference;
import fr.lirmm.graphik.defeasible.core.rules.DefeasibleRule;
import fr.lirmm.graphik.defeasible.core.rules.DefeaterRule;
import fr.lirmm.graphik.defeasible.tools.benchmark.core.Approach;
import fr.lirmm.graphik.graal.api.core.Atom;
import fr.lirmm.graphik.graal.api.core.ConjunctiveQuery;
import fr.lirmm.graphik.graal.api.core.NegativeConstraint;
import fr.lirmm.graphik.graal.api.core.Rule;
import fr.lirmm.graphik.graal.core.Rules;
import fr.lirmm.graphik.util.stream.CloseableIteratorWithoutException;
import net.sf.tweety.arg.delp.DefeasibleLogicProgram;
import net.sf.tweety.arg.delp.DelpReasoner;
import net.sf.tweety.arg.delp.parser.DelpParser;
import net.sf.tweety.arg.delp.semantics.GeneralizedSpecificity;
import net.sf.tweety.commons.Answer;
import net.sf.tweety.commons.Formula;
import net.sf.tweety.logics.fol.parser.FolParser;
import net.sf.tweety.logics.fol.syntax.FolFormula;
import net.sf.tweety.logics.fol.syntax.Negation;

public class DeLPTool extends AbstractTool {
	public static final String NAME = "DeLP";
	
	public void run() {
		String KBString = this.getKBString();
		
		System.out.println(KBString);
		System.out.println(this.getQuery());
		try {
			// I- Prepare Phase
			this.getProfiler().clear();
			this.getProfiler().start(Approach.LOADING_TIME);
			
			DelpParser parser = new DelpParser();
			DefeasibleLogicProgram delp = parser.parseBeliefBase(KBString);
			GeneralizedSpecificity comp = new GeneralizedSpecificity();
			
			DelpReasoner reasoner = new DelpReasoner(delp,comp);
			
			FolParser folParser = new FolParser();
			folParser.setSignature(parser.getSignature());
			
			this.getProfiler().stop(Approach.LOADING_TIME);
			
			// II- Query Answering Phase
			this.getProfiler().start(Approach.EXE_TIME);
			
			Formula query;
			if(this.getQuery().startsWith("~"))
				query = new Negation((FolFormula)folParser.parseFormula(this.getQuery().substring(1)));
			else query = folParser.parseFormula(this.getQuery());
			
			Answer ans = reasoner.query(query);
			String entailement = String.valueOf(ans.getAnswerBoolean());
			
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
		return this.formatRule(rule, " <- ");
	}

	@Override
	public String formatDefeasibleRule(DefeasibleRule rule) {
		return this.formatRule(rule, " -< ");
	}

	@Override
	public String formatDefeaterRule(DefeaterRule rule) {
		return null; // DeLP does not support Defeaters.
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
		return null; // DeLP does not support preferences
	}

	@Override
	public String formatQuery(ConjunctiveQuery query) {
		return this.formatAtom(query.getAtomSet().iterator().next());
	}
	
	protected String formatRule(Rule rule, String implication) {
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
			result += rString + ".\n";
		}
		
		return result;
	}
}
