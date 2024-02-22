package mll;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;

import static java.lang.String.format;

public class Pow extends BinOp {
    Pow(Op lhs, Op rhs) {
        super(lhs, rhs);
    }

    // Smart constructor
    public static Op c(Op x, Op y) {
        var dag = x.dag();

        if (dag.doRewrite) {
            if (x instanceof Lit xLit && y instanceof Lit yLit) {
                var value = Math.pow(xLit.get(), yLit.get());
                return dag.lit(value);
            }
        }

        return dag.unify(new Pow(x, y));
    }

    @Override
    double eval_(double[] inVals) {
        return Math.pow(inVals[0], inVals[1]);
    }

    @Override
    protected Op diff(int inputIdx) {
        if (inputIdx == 0) {
            return rhs().mul(lhs().pow(rhs().sub(dag().lit1())));
        } else {
            return lhs().pow(rhs()).mul(lhs().log());
        }
    }

    @Override
    protected String llvm_(HashMap<Op, String> map, Writer writer) throws IOException {
        var l = lhs().llvm(map, writer);
        var r = rhs().llvm(map, writer);
        var x = format("%%_%d", id());
        writer.append(format("\t%s = llvm.pow.f64(double %s, double %s)\n", x, l, r));
        return x;
    }
}
