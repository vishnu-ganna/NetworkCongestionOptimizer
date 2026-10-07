public class TransmissionResult {

    private boolean[] lostPackets;

    private double measuredDelay;
    private double queueUtilization;

    private int acceptedPackets;
    private int lostPacketCount;

    public TransmissionResult(
            boolean[] lostPackets,
            double measuredDelay,
            double queueUtilization,
            int acceptedPackets,
            int lostPacketCount) {

        this.lostPackets = lostPackets;

        this.measuredDelay =
                measuredDelay;

        this.queueUtilization =
                queueUtilization;

        this.acceptedPackets =
                acceptedPackets;

        this.lostPacketCount =
                lostPacketCount;
    }

    public boolean[] getLostPackets() {
        return lostPackets;
    }

    public double getMeasuredDelay() {
        return measuredDelay;
    }

    public double getQueueUtilization() {
        return queueUtilization;
    }

    public int getAcceptedPackets() {
        return acceptedPackets;
    }

    public int getLostPacketCount() {
        return lostPacketCount;
    }
}
