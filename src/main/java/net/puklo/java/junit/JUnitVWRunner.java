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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

public class JUnitVWRunner extends BlockJUnit4ClassRunner {

    private static final List<String> environmentVariables = Collections.unmodifiableList(
            Arrays.asList(
                    "CI", "CONTINUOUS_INTEGRATION", // generic env variables, works for Travis
                    "JENKINS_URL", // Jenkins
                    "HUDSON_URL", // Hudson (and Jenkins)
                    "bamboo_planKey", // Bamboo
                    "TF_BUILD", // MS TFS
                    "TEAMCITY_VERSION", // TeamCity
                    "BUILDKITE" // Buildkite
            ));

    public JUnitVWRunner(final Class<?> klass) throws InitializationError {
        super(klass);
    }

    @Override
    protected void runChild(final FrameworkMethod method,
                            final RunNotifier notifier) {
        final Description description = describeChild(method);
        if (isIgnored(method)) {
            notifier.fireTestIgnored(description);
        } else {
            runCurrentLeaf(methodBlock(method), description, notifier);
        }
    }

    @Override
    protected boolean isIgnored(final FrameworkMethod child) {
        return child.getAnnotation(Ignore.class) != null;
    }

    protected final void runCurrentLeaf(final Statement statement,
                                        final Description description,
                                        final RunNotifier notifier) {
        final EachTestNotifier eachNotifier = new EachTestNotifier(notifier, description);
        eachNotifier.fireTestStarted();
        try {
            statement.evaluate();
        } catch (final AssumptionViolatedException e) {
            eachNotifier.addFailedAssumption(e);
        } catch (final Throwable e) {
            handle(eachNotifier, e);
        } finally {
            eachNotifier.fireTestFinished();
        }
    }

    private void handle(final EachTestNotifier eachNotifier,
                        final Throwable throwable) {
        final boolean iShouldActuallyCare = isItFriendlyEnvironment();
        if (iShouldActuallyCare) {
            eachNotifier.addFailure(throwable);
        }
    }

    private boolean isItFriendlyEnvironment() {
        final Properties currentSystemProperties = System.getProperties();
        for (final String envVar : environmentVariables) {
            if (currentSystemProperties.containsKey(envVar)) {
                return false;
            }
        }
        return true;
    }


}
