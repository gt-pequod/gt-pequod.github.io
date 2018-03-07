package leetCodeTest;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import com.microsoft.z3.BoolExpr;
import com.microsoft.z3.InterpolationContext;
import com.microsoft.z3.Model;

import equivalence.PairDAG;
import soot.Body;
import soot.BodyTransformer;
import soot.PackManager;
import soot.SootMethod;
import soot.Transform;
import soot.options.Options;
import soot.toolkits.graph.ExceptionalUnitGraph;

public class addDigits1 {
	static Map<String, Body> stores = new HashMap<String, Body>();

	public static void main(String[] args) throws IOException {
		Options.v().set_src_prec(Options.src_prec_c);
		Options.v().set_output_format(Options.output_format_jimple);
		Options.v().set_allow_phantom_refs(true);
        //specific the class file
		String input=args[0];
		String output=args[1];
		String outputFolder=input+"/output";
		String[] sootArgs = new String[] { "-process-dir", input,
				"-output-dir", outputFolder };
		PackManager.v().getPack("jtp").add(new Transform("jtp.sim-itps1", new BodyTransformer() {

			@Override
			protected void internalTransform(Body body, String phaseName, Map<String, String> options) {
				// hack here
				SootMethod method = body.getMethod();
				String methodSig = method.getSignature();
				System.out.println(methodSig);
				/* System.out.println(method.getName()); */
				stores.put(methodSig, body);

			}
		}));
		soot.Main.main(sootArgs);
		System.out.println(stores.size());
		String theOutPutfile=output+"/result.txt";
		FileWriter writeFile = new FileWriter(theOutPutfile, true);
		PrintWriter outputWrite = new PrintWriter(writeFile);
        //specific the signature of two program
		ExceptionalUnitGraph cfgLeft = new ExceptionalUnitGraph(
				stores.get("<toy_benchmark.addDigits: int addDigits1(int)>"));
		ExceptionalUnitGraph cfgRight = new ExceptionalUnitGraph(
				stores.get("<toy_benchmark.addDigits: int addDigits2(int)>"));
		PairDAG theSolver = new PairDAG(cfgLeft, cfgRight, "test",false);
		InterpolationContext ictx = theSolver.getIctx();
        //specific the extra policy that input is greater than one
		BoolExpr extraPolicy = ictx.mkGt(ictx.mkIntConst("i0Path0Copy1Level0index0"), ictx.mkInt("0"));
		theSolver.addExtraPrePolicy(extraPolicy);
		if (theSolver.isEquivalent()) {
			outputWrite.println("addDigits1 and addDigits2 are equivalent");
		} else {
			outputWrite.println("addDigits1 and addDigits2 are not equivalent");
		}
		outputWrite.close();
		//System.out.println("this two program should be equivalent");
	}
}
