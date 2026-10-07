public class RenoController
        extends CongestionController {

    private int slowStartThreshold;

    public RenoController() {
        super();

        slowStartThreshold = 16;
    }

    public void onSuccessfulTransmission() {

        int currentWindow =
                getCongestionWindow();

        if (currentWindow < slowStartThreshold) {

            // Slow Start
            increaseWindowBy(currentWindow);

        } else {

            // Congestion Avoidance
            increaseWindow();
        }
    }

    public void onPacketLoss() {

        int currentWindow =
                getCongestionWindow();

        // Multiplicative decrease
        slowStartThreshold =
                currentWindow / 2;

        if (slowStartThreshold < 1) {
            slowStartThreshold = 1;
        }

        decreaseTo(slowStartThreshold);
    }

    public int getSlowStartThreshold() {
        return slowStartThreshold;
    }
}
