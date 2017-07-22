package fr.lirmm.graphik.defeasible.tools.benchmark.tools;

import org.junit.Assert;
import org.junit.Test;

import fr.lirmm.graphik.defeasible.core.io.DlgpDefeasibleParser;
import fr.lirmm.graphik.defeasible.core.preference.Preference;
import fr.lirmm.graphik.defeasible.core.rules.DefeasibleRule;
import fr.lirmm.graphik.defeasible.core.rules.DefeaterRule;
import fr.lirmm.graphik.graal.api.core.Atom;
import fr.lirmm.graphik.graal.api.core.NegativeConstraint;
import fr.lirmm.graphik.graal.api.core.Rule;
import fr.lirmm.graphik.graal.api.io.ParseException;

public class ELDRToolTest {
	public static ELDRTool eldr = new ELDRTool();
	
	@Test
	public void shouldFormatGroundAtom() throws ParseException {
		Atom atom = DlgpDefeasibleParser.parseAtom("p(a).");
		Assert.assertTrue((eldr.formatFact(atom).equals("p(a).")));
	}
	@Test
	public void shouldFormatGroundAtomWithArity() throws ParseException {
		Atom atom = DlgpDefeasibleParser.parseAtom("p(a,b).");
		Assert.assertTrue((eldr.formatFact(atom).equals("p(a,b).")));
	}
	
	@Test
	public void shouldFormatStrictRuleWithLabel() throws ParseException {
		Rule rule = DlgpDefeasibleParser.parseRule("[r1] p(X) <- q(X).");
		Assert.assertTrue((eldr.formatStrictRule(rule).equals("[r1] p(X) <- q(X).")));
	}
	@Test
	public void shouldFormatStrictRuleWithoutLabel() throws ParseException {
		Rule rule = DlgpDefeasibleParser.parseRule("p(X) <- q(X).");
		Assert.assertTrue((eldr.formatStrictRule(rule).equals("p(X) <- q(X).")));
	}
	@Test
	public void shouldFormatDefeasibleRuleWithLabel() throws ParseException {
		DefeasibleRule rule = DlgpDefeasibleParser.parseDefeasibleRule("[r1] p(X) <= q(X).");
		Assert.assertTrue((eldr.formatDefeasibleRule(rule).equals("[r1] p(X) <= q(X).")));
	}
	@Test
	public void shouldFormatDefeaterRuleWithLabel() throws ParseException {
		DefeaterRule rule = DlgpDefeasibleParser.parseDefeaterRule("[r1] p(X) <~ q(X).");
		Assert.assertTrue((eldr.formatDefeaterRule(rule).equals("[r1] p(X) <~ q(X).")));
	}
	
	@Test
	public void shouldFormatNegativeConstraint() throws ParseException {
		NegativeConstraint nc = DlgpDefeasibleParser.parseNegativeConstraint("! :- p(X), q(X).");
		Assert.assertTrue((eldr.formatNegativeConstraint(nc).equals("! :- p(X), q(X).")));
	}
	
	@Test
	public void shouldFormatPreference() throws ParseException {
		Preference pref = DlgpDefeasibleParser.parsePreference("[r1] > [r2].");
		Assert.assertTrue((eldr.formatPreference(pref).equals("[r1] > [r2].")));
	}
}
