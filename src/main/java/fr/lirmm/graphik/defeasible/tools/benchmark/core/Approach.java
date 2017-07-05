package fr.lirmm.graphik.defeasible.tools.benchmark.core;

import java.util.Iterator;

import org.apache.commons.lang3.tuple.Pair;

import fr.lirmm.graphik.defeasible.core.DefeasibleKnowledgeBase;

public interface Approach extends Runnable {
	public static final String TOTAL_TIME = "total-time";
	public static final String EXE_TIME = "exe-time";
	public static final String LOADING_TIME = "loading-time";
	public static final String ANSWER = "answer";
	
	void prepare(DefeasibleKnowledgeBase kb);
	
	void initialize();
	
	Iterator<Pair<String, ? extends Object>> getResults();
	
	String getName();
}
