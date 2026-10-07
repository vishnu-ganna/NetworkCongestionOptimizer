public class SimulationResult {

    private String algorithmName;
    private String conditionName;

    private int totalPackets;
    private int receivedPackets;

    private int totalAttempts;
    private int lostPackets;

    private long totalTime;

    private double throughput;
    private double packetLossRate;

    private int finalCongestionWindow;
    private double averageCongestionWindow;

    public SimulationResult(
            String algorithmName,
            String conditionName,
            int totalPackets,
            int receivedPackets,
            int totalAttempts,
            int lostPackets,
            long totalTime,
            double throughput,
            double packetLossRate,
            int finalCongestionWindow,
            double averageCongestionWindow) {

        this.algorithmName = algorithmName;
        this.conditionName = conditionName;

        this.totalPackets = totalPackets;
        this.receivedPackets = receivedPackets;

        this.totalAttempts = totalAttempts;
        this.lostPackets = lostPackets;

        this.totalTime = totalTime;

        this.throughput = throughput;
        this.packetLossRate = packetLossRate;

        this.finalCongestionWindow =
                finalCongestionWindow;

        this.averageCongestionWindow =
                averageCongestionWindow;
    }

    public String getAlgorithmName() {
        return algorithmName;
    }

    public String getConditionName() {
        return conditionName;
    }

    public int getTotalPackets() {
        return totalPackets;
    }

    public int getReceivedPackets() {
        return receivedPackets;
    }

    public int getTotalAttempts() {
        return totalAttempts;
    }

    public int getLostPackets() {
        return lostPackets;
    }

    public long getTotalTime() {
        return totalTime;
    }

    public double getThroughput() {
        return throughput;
    }

    public double getPacketLossRate() {
        return packetLossRate;
    }

    public int getFinalCongestionWindow() {
        return finalCongestionWindow;
    }

    public double getAverageCongestionWindow() {
        return averageCongestionWindow;
    }
}
