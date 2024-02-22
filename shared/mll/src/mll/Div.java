package mll;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;

import static java.lang.String.format;

public class Div extends BinOp {
    Div(Op lhs, Op rhs) {
        super(lhs, rhs);
    }

    // Smart constructor
    public static Op c(Op x, Op y) {
        var dag = x.dag();

        if (dag.doRewrite) {
            if (x instanceof Lit xLit && y instanceof Lit yLit) {
                var value = xLit.get() / yLit.get();
                return dag.lit(value);
            }

            if (x instanceof Lit xLit && xLit.is(0)) {
                return dag.lit0();
            }
        }

        return dag.unify(new Div(x, y));
    }

    // Custom operator string
    @Override public String opString() { return "/"; }

    @Override
    double eval_(double[] inVals) {
        return inVals[0] / inVals[1];
    }

    @Override
    protected Op diff(int inputIdx) {
        if (inputIdx == 0) {
            return dag().lit1().div(rhs());
        } else {
            return lhs().div(rhs().pow(dag().lit2())).neg();
        }
    }

    @Override
    protected String llvm_(HashMap<Op, String> map, Writer writer) throws IOException {
        var l = lhs().llvm(map, writer);
        var r = rhs().llvm(map, writer);
        var x = format("%%_%d", id());
        writer.append(format("\t%s = fdiv double %s, %s\n", x, l, r));
        return x;
    }
}
