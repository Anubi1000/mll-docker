package mll;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;

import static java.lang.String.format;

public class Mul extends BinOp {
    Mul(Op lhs, Op rhs) {
        super(lhs, rhs);
    }

    // Smart constructor
    public static Op c(Op x, Op y) {
        var dag = x.dag();

        if (dag.doRewrite) {
            if (x instanceof Lit xLit && y instanceof Lit yLit) {
                var value = xLit.get() * yLit.get();
                return dag.lit(value);
            }
        }

        return dag.unify(new Mul(x, y));
    }

    // Custom operator string
    @Override public String opString() { return "*"; }

    @Override
    double eval_(double[] inVals) {
        return inVals[0] * inVals[1];
    }

    @Override
    protected Op diff(int inputIdx) {
        return input((inputIdx+1)%2);
    }

    @Override
    protected String llvm_(HashMap<Op, String> map, Writer writer) throws IOException {
        var l = lhs().llvm(map, writer);
        var r = rhs().llvm(map, writer);
        var x = format("%%_%d", id());
        writer.append(format("\t%s = fmul double %s, %s\n", x, l, r));
        return x;
    }
}
