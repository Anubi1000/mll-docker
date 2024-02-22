package mll;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;

public class Neg extends UnOp {
    Neg(Op arg) {
        super(arg);
    }

    // Smart constructor
    public static Op c(Op arg) {
        var dag = arg.dag();

        if (dag.doRewrite) {
            if (arg instanceof Lit argLit) {
                var value = -argLit.get();
                return dag.lit(value);
            }

            if (arg instanceof Neg argNeg) {
                return dag.unify(argNeg.arg());
            }
        }

        return dag.unify(new Neg(arg));
    }

    @Override
    double eval_(double[] inVals) {
        return -inVals[0];
    }

    @Override
    protected Op diff(int inputIdx) {
        return dag().lit(-1);
    }

    @Override
    protected String llvm_(HashMap<Op, String> cache, Writer writer) throws IOException {
        throw new UnsupportedOperationException();
    }
}
