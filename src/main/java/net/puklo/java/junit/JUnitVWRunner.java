package net.puklo.java.junit;

import org.junit.Ignore;
import org.junit.internal.AssumptionViolatedException;
import org.junit.internal.runners.model.EachTestNotifier;
import org.junit.runner.Description;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;

public class JUnitVWRunner extends BlockJUnit4ClassRunner {

    public JUnitVWRunner(final Class<?> klass) throws InitializationError {
        super(klass);
    }

    @Override
    protected void runChild(final FrameworkMethod method,
                            final RunNotifier notifier) {
        final Description description = describeChild(method);
        if (this.isIgnored(method)) {
            notifier.fireTestIgnored(description);
        } else {
            runVwLeaf(super.methodBlock(method), description, notifier);
        }
    }

    @Override
    protected boolean isIgnored(final FrameworkMethod child) {
        return child.getAnnotation(Ignore.class) != null;
    }

    protected final void runVwLeaf(final Statement statement,
                                   final Description description,
                                   final RunNotifier notifier) {
        final EachTestNotifier eachNotifier = new EachTestNotifier(notifier, description);
        eachNotifier.fireTestStarted();
        try {
            statement.evaluate();
        } catch (final AssumptionViolatedException e) {
            eachNotifier.addFailedAssumption(e);
        } catch (final Throwable e) {
            // no no no...
        } finally {
            eachNotifier.fireTestFinished();
        }
    }

}
