package mll;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;

public class Log extends UnOp {
    Log(Op arg) {
        super(arg);
    }

    // Smart constructor
    public static Op c(Op arg) {
        var dag = arg.dag();

       if (dag.doRewrite) {
           if (arg instanceof Lit argLit) {
               var value = Math.log(argLit.get());
               return dag.lit(value);
           }
       }

        return dag.unify(new Log(arg));
    }

    @Override
    double eval_(double[] inVals) {
        return Math.log(inVals[0]);
    }

    @Override
    protected Op diff(int inputIdx) {
        return arg().pow(dag().lit(-1));
    }

    @Override
    protected String llvm_(HashMap<Op, String> cache, Writer writer) throws IOException {
        throw new UnsupportedOperationException();
    }
}
