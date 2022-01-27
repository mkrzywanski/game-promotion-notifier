package io.mkrzywanski.pn.testcontainers;

import org.awaitility.Awaitility;
import org.testcontainers.containers.Container;
import org.testcontainers.containers.wait.strategy.AbstractWaitStrategy;

public class ContainerCommandWaitStrategy extends AbstractWaitStrategy {

    private final String[] command;
    private final String expectedOutput;

    ContainerCommandWaitStrategy(final String[] command, final String expectedOutput) {
        this.command = command;
        this.expectedOutput = expectedOutput;
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    protected void waitUntilReady() {
        System.out.println("xDDDDD " + waitStrategyTarget.getContainerId());
        Awaitility.await().atMost(startupTimeout).until(() -> {
            Container.ExecResult result = waitStrategyTarget.execInContainer(command);
            return result.getStdout().equals(expectedOutput);
        });
    }

    public static class Builder {
        private String[] command = {};
        private String expectedOutput = "";

        public Builder command(String... command) {
            this.command = command;
            return this;
        }

        public Builder expectedOutput(String expectedOutput) {
            this.expectedOutput = expectedOutput;
            return this;
        }

        public ContainerCommandWaitStrategy build() {
            return new ContainerCommandWaitStrategy(command, expectedOutput);
        }
    }

}
