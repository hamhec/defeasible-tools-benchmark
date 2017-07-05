package fr.lirmm.graphik.defeasible.tools.benchmark.core;

import fr.lirmm.graphik.defeasible.core.DefeasibleKnowledgeBase;

public interface BenchDataSet {
	public void init();
	public String getName();
	public DataSetGenerator getDataSets();
}
