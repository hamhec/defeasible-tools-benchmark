package fr.lirmm.graphik.defeasible.tools.benchmark;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;

import com.beust.jcommander.IStringConverter;
import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;

import fr.lirmm.graphik.defeasible.tools.benchmark.ambiguity.AmbiguityBenchDataSet;
import fr.lirmm.graphik.defeasible.tools.benchmark.core.Approach;
import fr.lirmm.graphik.defeasible.tools.benchmark.core.BenchDataSet;
import fr.lirmm.graphik.defeasible.tools.benchmark.core.BenchRunner;
import fr.lirmm.graphik.defeasible.tools.benchmark.existential.ExistentialBenchDataSet;
import fr.lirmm.graphik.defeasible.tools.benchmark.existential.fes.TransitiveChainBenchDataSet;
import fr.lirmm.graphik.defeasible.tools.benchmark.grd.ChainBenchDataSet;
import fr.lirmm.graphik.defeasible.tools.benchmark.grd.CircleBenchDataSet;
import fr.lirmm.graphik.defeasible.tools.benchmark.tools.ASPICTool;
import fr.lirmm.graphik.defeasible.tools.benchmark.tools.DEFTTool;
import fr.lirmm.graphik.defeasible.tools.benchmark.tools.DeLPTool;
import fr.lirmm.graphik.defeasible.tools.benchmark.tools.ELDRTool;
import fr.lirmm.graphik.util.stream.IteratorException;

public class BenchLauncher {
	public static final String BENCH_CHAIN = "CHAIN";
	public static final String BENCH_CIRCLE = "CIRCLE";

	public static final String BENCH_AMBIGUITY = "AMBIGUITY_TEST";
	
	public static final String BENCH_EXISTENTIAL = "EXISTENTIAL_TEST";
	public static final String BENCH_TRANSITIVE_CHAIN = "TRANSITIVE_CHAIN";
	
	public static final String BENCH_SIMPLE_CHAIN_FES = "SIMPLE_CHAIN_FES";
	public static final String BENCH_CHAIN_FES = "CHAIN_FES";
	public static final String BENCH_CHAIN_PREF = "CHAIN_PREF";
	public static final String BENCH_LEVELS = "LEVELS";
	public static final String BENCH_TEAMS = "TEAMS";
	public static final String BENCH_TEAMS_GAD = "TEAMS_GAD";
	public static final String BENCH_TREES = "TEES";
	public static final String BENCH_TREES_CONFLICT = "TEES_CONFLICT";
	
	
	@Parameter(names = { "-h", "--help" }, description = "Print this message", help = true)
	private boolean            help;
	
	@Parameter(names = { "-n", "--size" }, converter = IntArrayConverter.class, description = "Comma-separated list of sizes of the generated knowledge bases")
	private int[]              sizes          = new int[] {1};
	
	@Parameter(names = { "-b", "--bench" }, description = BENCH_CHAIN+"|"+BENCH_CHAIN_FES +"|"+BENCH_CIRCLE+"|...")
	private String             benchType             = this.BENCH_EXISTENTIAL;
	
	@Parameter(names = { "-o", "--output-file" }, description = "Output file (use '-' for stdout)")
	private String             outputFilePath = "-"; //"chain.csv"
	
	@Parameter(names = { "-t", "--timeout" }, description = "Timeout in ms")
	private long               timeout        = 3000000;
	
	@Parameter(names = { "-i", "--iterations" }, description = "Number of iterations for each Bench size")
	private int 			   iterations     = 1;
	
	@Parameter(names = { "-a", "--n-atoms" }, description = "Number of atoms per rule")
	private int 			   nbrAtoms          = 1;
	
	@Parameter(names = { "-x", "--n-terms" }, description = "Number of terms per rule")
	private int 			   nbrTerms          = 1;
	
	public static void main(String args[]) throws FileNotFoundException, IteratorException {
		System.out.println("===Start the execusion===");
		
		BenchLauncher options = new BenchLauncher();

		JCommander commander = new JCommander(options, args);

		if (options.help) {
			commander.usage();
			System.exit(0);
		}
		
		OutputStream outputStream = null;
		if (options.outputFilePath.equals("-")) {
			outputStream = System.out;
		} else {
			try {
				outputStream = new FileOutputStream(options.outputFilePath);
			} catch (Exception e) {
				System.err.println("Could not open file: " + options.outputFilePath);
				System.err.println(e);
				e.printStackTrace();
				System.exit(1);
			}
		}
		
		List<Approach> approaches = new LinkedList<Approach>();
		approaches.add(new ELDRTool());
		approaches.add(new DeLPTool());
		approaches.add(new ASPICTool());
		approaches.add(new DEFTTool());
		//approaches.add(new DRDeviceTool());
		//approaches.add(new RuleLogTool());
		
		BenchDataSet bench = null;
		if(options.benchType.equals(BENCH_CHAIN)) {
			bench = new ChainBenchDataSet(options.sizes, options.nbrAtoms, options.nbrTerms);
		} else if(options.benchType.equals(options.BENCH_CIRCLE)) {
			bench = new CircleBenchDataSet(options.sizes, options.nbrAtoms, options.nbrTerms);
		} else if(options.benchType.equals(options.BENCH_AMBIGUITY)) {
			bench = new AmbiguityBenchDataSet(options.sizes, options.nbrTerms);
		} else if(options.benchType.equals(options.BENCH_EXISTENTIAL)) {
			bench = new ExistentialBenchDataSet(options.sizes);
		} else if(options.benchType.equals(options.BENCH_TRANSITIVE_CHAIN)) {
			bench = new TransitiveChainBenchDataSet(options.sizes);
		}
		
		new BenchRunner(bench, approaches, outputStream, options.iterations, options.timeout).run();
		
		System.out.println("===End the execusion===");
	}
	
	
	
	
	
	
	
	public class IntArrayConverter implements IStringConverter<int[]> {
		public int[] convert(String value) {
			String[] strArray = value.split(",");
			int[] intArray = new int[strArray.length];
			for(int i = 0; i < strArray.length; i++) {
			    intArray[i] = Integer.parseInt(strArray[i]);
			}
			return intArray;
		}
	}

}
