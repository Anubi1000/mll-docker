package mll;

import static java.lang.String.format;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;

/** Operator to adds two inputs. */
public class Add extends BinOp {
    Add(Op lhs, Op rhs) { super(lhs, rhs); }

    // Smart constructor
    public static Op c(Op x, Op y) {
        var dag = x.dag();

        if (dag.doRewrite) {
            if (x instanceof Lit xLit && y instanceof Lit yLit) {
                var value = xLit.get() + yLit.get();
                return dag.lit(value);
            }

            if (x instanceof Lit xLit && xLit.is(0)) {
                return y;
            }
            if (y instanceof Lit yLit && yLit.is(0)) {
                return x;
            }

            if (x == y) {
                return x.mul(dag.lit(2));
            }
        }

        return dag.unify(new Add(x, y));
    }

    // Custom operator string
    @Override public String opString() { return "+"; }


    // Required implementation (see Op#eval_)
    @Override protected double eval_(double[] inVals) {
    	return inVals[0] + inVals[1];
    }

    // Required implementation (see Op#diff)
    @Override protected Op diff(int inputIdx) { return lit1(); }

    // Required implementation (see Op#llvm_)
    @Override protected String llvm_(HashMap<Op, String> map, Writer writer) throws IOException {
        var l = lhs().llvm(map, writer);
        var r = rhs().llvm(map, writer);
        var x = format("%%_%d", id());
        writer.append(format("\t%s = fadd double %s, %s\n", x, l, r));
        return x;
    }
}
