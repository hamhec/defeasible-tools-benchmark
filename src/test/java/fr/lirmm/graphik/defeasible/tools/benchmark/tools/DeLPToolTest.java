package fr.lirmm.graphik.defeasible.tools.benchmark.tools;

import org.junit.Assert;
import org.junit.Test;

import fr.lirmm.graphik.defeasible.core.io.DlgpDefeasibleParser;
import fr.lirmm.graphik.defeasible.core.rules.DefeasibleRule;
import fr.lirmm.graphik.graal.api.core.Atom;
import fr.lirmm.graphik.graal.api.core.NegativeConstraint;
import fr.lirmm.graphik.graal.api.core.Rule;
import fr.lirmm.graphik.graal.api.io.ParseException;

public class DeLPToolTest {
	public static DeLPTool tool = new DeLPTool();
	
	@Test
	public void shouldFormatGroundAtom() throws ParseException {
		Atom atom = DlgpDefeasibleParser.parseAtom("p(a).");
		Assert.assertTrue((tool.formatFact(atom).equals("p(a).")));
	}
	@Test
	public void shouldFormatGroundAtomWithArity() throws ParseException {
		Atom atom = DlgpDefeasibleParser.parseAtom("p(a,b).");
		Assert.assertTrue((tool.formatFact(atom).equals("p(a,b).")));
	}
	
	@Test
	public void shouldFormatStrictRuleWithLabel() throws ParseException {
		Rule rule = DlgpDefeasibleParser.parseRule("[r1] p(X) <- q(X).");
		Assert.assertTrue((tool.formatStrictRule(rule).equals("p(X) <- q(X).\n")));
	}
	@Test
	public void shouldFormatStrictRuleWithoutLabel() throws ParseException {
		Rule rule = DlgpDefeasibleParser.parseRule("p(X) <- q(X).");
		Assert.assertTrue((tool.formatStrictRule(rule).equals("p(X) <- q(X).\n")));
	}
	@Test
	public void shouldFormatDefeasibleRuleWithLabel() throws ParseException {
		DefeasibleRule rule = DlgpDefeasibleParser.parseDefeasibleRule("[r1] p(X) <= q(X).");
		System.out.println(tool.formatDefeasibleRule(rule));
		Assert.assertTrue((tool.formatDefeasibleRule(rule).equals("p(X) -< q(X).\n")));
	}
	
	@Test
	public void shouldFormatNegativeConstraint() throws ParseException {
		NegativeConstraint nc = DlgpDefeasibleParser.parseNegativeConstraint("! :- p(X), q(X).");
		Assert.assertTrue((tool.formatNegativeConstraint(nc).equals("~p(X) <- q(X).\n~q(X) <- p(X).")));
	}
}
