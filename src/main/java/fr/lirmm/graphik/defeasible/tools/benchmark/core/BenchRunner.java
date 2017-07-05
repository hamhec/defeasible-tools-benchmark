package fr.lirmm.graphik.defeasible.tools.benchmark.core;

import java.io.FileNotFoundException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import fr.lirmm.graphik.defeasible.core.DefeasibleKnowledgeBase;

public class BenchRunner {
	private final BenchDataSet BENCH;
	private final Iterable<Approach> APPROACHES;
	private final OutputStream OUT;
	private final int NB_ITERATION;
	
	private final long TIMEOUT;
	
	// ------------------------------------------------------------------------
	// CONSTRUCTORS
	// ------------------------------------------------------------------------
	public BenchRunner(BenchDataSet bench, Iterable<Approach> approaches, OutputStream out) {
		this(bench, approaches, out, 1, 60000);
	}
	
	public BenchRunner(BenchDataSet bench, Iterable<Approach> approaches, OutputStream out, int nbIteration, long timeout) {
		this.BENCH = bench;
		this.APPROACHES = approaches;
		this.OUT = out;
		this.NB_ITERATION = nbIteration;
		this.TIMEOUT = timeout;
	}
	
	// ------------------------------------------------------------------------
	// PUBLIC METHODS
	// ------------------------------------------------------------------------
	public void run() throws FileNotFoundException {
		System.out.println("===Start the execusion of the Bench===");
		this.BENCH.init();
		PrintStream writer = new PrintStream(OUT);
		writer.print("approach,bench,size,iteration,loadingTime,executionTime,answer\n");
		
		// Loop for different KBs
		Iterator<DefeasibleKnowledgeBase> itDataSet = this.BENCH.getDataSets().iterator();
		
			while(itDataSet.hasNext()) {
				KBStructure kb = (KBStructure) itBenchParam.next();
				
				// Loop for Approaches
				Iterator<Approach> itApproaches = this.APPROACHES.iterator();
				while(itApproaches.hasNext()) {
					Approach approach = itApproaches.next();
					approach.initialize();
					List<Pair<String, ? extends Object>> kbPair = new LinkedList<Pair<String, ? extends Object>>();
					kbPair.add(new ImmutablePair<String, KBStructure>("KB", kb));
					
					// Loop for Iterations
					for(int iteration = 0; iteration < this.NB_ITERATION; ++iteration) {
						StringBuilder output = new StringBuilder();
						output.append(approach.getName());
						output.append(',');
						output.append(BENCH.getName());
						output.append(',');
						output.append(itBenchParam.getCurrentSize());
						output.append(',');
						output.append(iteration);
						
						if (LOGGER.isInfoEnabled()) {
							LOGGER.info(output.toString());
						}
						// call prepare function of the approach
						approach.prepare(kbPair.iterator());
						// run the approach
						Thread thread = new Thread(approach);
						thread.start();
						// If the thread takes too much time log it and kill it
						try {
							thread.join(timeout);
						} catch (InterruptedException e1) {

						}
						
						String loadingTime = "TO";
						String executionTime = "TO";
						String answer = "TO";
						
						if(thread.isAlive()) {
							if(LOGGER.isWarnEnabled()) {
								LOGGER.warn("TIMEOUT: ", output.toString());
							}
							thread.stop();
						} else {
							Iterator<Pair<String, ? extends Object>> data = approach.getResults();
							while (data.hasNext()) {
								Pair<String, ? extends Object> pair = data.next();
								if(pair.getKey().equals(Approach.LOADING_TIME)) {
									loadingTime = String.valueOf(pair.getValue());
								} else if(pair.getKey().equals(Approach.EXE_TIME)) {
									executionTime = String.valueOf(pair.getValue());
								} else if(pair.getKey().equals(Approach.ANSWER)) {
									answer = String.valueOf(pair.getValue());
								}
							}
						}
						output.append(',');
						output.append(loadingTime);
						output.append(',');
						output.append(executionTime);
						output.append(',');
						output.append(answer);
						output.append('\n');
						writer.print(output.toString());
						System.out.println(output.toString());
						writer.flush();
					}
				}
			}
		}
		writer.close();
	}
}
