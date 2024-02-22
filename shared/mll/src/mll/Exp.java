package mll;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;

public class Exp extends UnOp {
    Exp(Op arg) {
        super(arg);
    }

    // Smart constructor
    public static Op c(Op arg) {
        var dag = arg.dag();
        if (arg instanceof Lit argLit) {
            var value = Math.exp(argLit.get());
            return dag.lit(value);
        }
        return dag.unify(new Exp(arg));
    }

    @Override
    double eval_(double[] inVals) {
        return Math.exp(inVals[0]);
    }

    @Override
    protected Op diff(int inputIdx) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected String llvm_(HashMap<Op, String> cache, Writer writer) throws IOException {
        throw new UnsupportedOperationException();
    }
}
