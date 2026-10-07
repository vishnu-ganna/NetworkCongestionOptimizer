import java.util.LinkedList;
import java.util.Queue;

public class AdaptiveController
        extends CongestionController {

    private AdaptiveOptimizer optimizer;

    private double networkLossRate;
    private double networkDelay;

    private double observedDelay;
    private double queueUtilization;

    private int totalAttempts;

    private Queue<Boolean> recentResults;
    private int recentLosses;

    private String currentStrategy;

    public AdaptiveController(
            double networkLossRate,
            double networkDelay) {

        super();

        this.networkLossRate =
                networkLossRate;

        this.networkDelay =
                networkDelay;

        observedDelay =
                networkDelay;

        queueUtilization = 0;

        optimizer =
                new AdaptiveOptimizer();

        totalAttempts = 0;

        recentResults =
                new LinkedList<>();

        recentLosses = 0;

        currentStrategy =
                "BALANCED";
    }

    public void recordAttempt(
            boolean lost) {

        totalAttempts++;

        recentResults.add(lost);

        if (lost) {
            recentLosses++;
        }

        /*
         * Keep only the latest 20 results.
         */
        if (recentResults.size() > 20) {

            boolean oldestResult =
                    recentResults.remove();

            if (oldestResult) {
                recentLosses--;
            }
        }
    }

    public void updateNetworkFeedback(
            double measuredDelay,
            double queueUtilization) {

        this.observedDelay =
                measuredDelay;

        this.queueUtilization =
                queueUtilization;
    }

    private double getRecentLossRate() {

        if (recentResults.isEmpty()) {
            return 0;
        }

        return (double) recentLosses
                / recentResults.size();
    }

    private void updateStrategy() {

        double recentLossRate =
                getRecentLossRate();

        double effectiveDelay =
                Math.max(
                        networkDelay,
                        observedDelay
                );

        if (queueUtilization >= 0.80
                || recentLossRate > 0.10
                || effectiveDelay > 50) {

            currentStrategy =
                    "CONSERVATIVE";

            return;
        }

        if (queueUtilization >= 0.50
                || recentLossRate > 0.05
                || effectiveDelay > 25) {

            currentStrategy =
                    "BALANCED";

            return;
        }

        if (recentLossRate <= 0.02
                && effectiveDelay <= 15
                && queueUtilization < 0.50) {

            currentStrategy =
                    "AGGRESSIVE";

            return;
        }

        currentStrategy =
                optimizer.selectStrategy(
                        networkLossRate,
                        effectiveDelay,
                        recentLossRate
                );
    }

    public void onSuccessfulTransmission() {

        updateStrategy();

        int increase =
                optimizer.calculateIncrease(
                        currentStrategy,
                        getCongestionWindow()
                );

        increaseWindowBy(increase);
    }

    public void onPacketLoss() {

        updateStrategy();

        int newWindow =
                optimizer.calculateDecrease(
                        currentStrategy,
                        getCongestionWindow()
                );

        decreaseTo(newWindow);
    }

    public String getCurrentStrategy() {
        return currentStrategy;
    }

    public double getObservedDelay() {
        return observedDelay;
    }

    public double getQueueUtilization() {
        return queueUtilization;
    }

    public double getRecentLossRateValue() {
        return getRecentLossRate();
    }

    public int getTotalAttempts() {
        return totalAttempts;
    }
}