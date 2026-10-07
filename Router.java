import java.util.Random;

public class Router {

    private double packetLossRate;
    private double bandwidth;
    private double baseDelay;

    private Receiver receiver;

    private Random random;

    private int lostPackets;
    private long totalSimulatedDelay;

    private double queueOccupancy;
    private double queueCapacity;

    public Router(
            double packetLossRate,
            double bandwidth,
            double baseDelay,
            Receiver receiver,
            long seed) {

        this.packetLossRate =
                packetLossRate;

        this.bandwidth =
                bandwidth;

        this.baseDelay =
                baseDelay;

        this.receiver =
                receiver;

        random =
                new Random(seed);

        lostPackets = 0;
        totalSimulatedDelay = 0;

        double bdp =
                bandwidth
                * (baseDelay / 1000.0);

        queueCapacity =
                Math.max(
                        2,
                        Math.ceil(bdp * 1.5)
                );

        queueOccupancy = 0;
    }

    public TransmissionResult transmitWindow(
            Packet[] packets,
            int count) {

        boolean[] lost =
                new boolean[count];

        int acceptedPackets = 0;
        int lostPacketCount = 0;

        for (int i = 0;
                i < count;
                i++) {

            /*
             * Queue overflow.
             */
            if (queueOccupancy
                    + acceptedPackets
                    >= queueCapacity) {

                lost[i] = true;
                lostPacketCount++;
                lostPackets++;

                continue;
            }

            /*
             * Random network loss.
             */
            double randomValue =
                    random.nextDouble();

            if (randomValue
                    < packetLossRate) {

                lost[i] = true;
                lostPacketCount++;
                lostPackets++;

            } else {

                lost[i] = false;
                acceptedPackets++;
            }
        }

        queueOccupancy +=
                acceptedPackets;

        /*
         * Calculate queue utilization.
         */
        double queueUtilization =
                queueOccupancy
                / queueCapacity;

        /*
         * Queueing delay increases
         * as the queue becomes full.
         */
        double queueDelay =
                baseDelay
                * queueUtilization;

        /*
         * Transmission time depends
         * on bandwidth.
         */
        double transmissionDelay =
                (count / bandwidth)
                * 1000.0;

        double measuredDelay =
                baseDelay
                + queueDelay
                + transmissionDelay;

        totalSimulatedDelay +=
                Math.round(measuredDelay);

        /*
         * Packets served by the link.
         */
        double packetsServed =
                bandwidth
                * (measuredDelay / 1000.0);

        queueOccupancy =
                Math.max(
                        0,
                        queueOccupancy
                        - packetsServed
                );

        return new TransmissionResult(
                lost,
                measuredDelay,
                queueUtilization,
                acceptedPackets,
                lostPacketCount
        );
    }

    public int getLostPackets() {
        return lostPackets;
    }

    public long getTotalSimulatedDelay() {
        return totalSimulatedDelay;
    }

    public double getQueueOccupancy() {
        return queueOccupancy;
    }

    public double getQueueCapacity() {
        return queueCapacity;
    }
}
