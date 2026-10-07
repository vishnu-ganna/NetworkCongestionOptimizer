public class NetworkCondition {

    private String name;

    private double packetLossRate;

    private double bandwidth;

    private double delay;

    public NetworkCondition(
            String name,
            double packetLossRate,
            double bandwidth,
            double delay) {

        this.name = name;

        this.packetLossRate =
                packetLossRate;

        this.bandwidth =
                bandwidth;

        this.delay =
                delay;
    }

    public String getName() {

        return name;
    }

    public double getPacketLossRate() {

        return packetLossRate;
    }

    public double getBandwidth() {

        return bandwidth;
    }

    public double getDelay() {

        return delay;
    }
}
