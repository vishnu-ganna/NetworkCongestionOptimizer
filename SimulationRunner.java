public class SimulationRunner {

    public static SimulationResult runSimulation(
            String algorithmName,
            CongestionController controller,
            NetworkCondition condition,
            int totalPackets,
            long seed) {

        Receiver receiver =
                new Receiver();

        Router router =
                new Router(
                        condition.getPacketLossRate(),
                        condition.getBandwidth(),
                        condition.getDelay(),
                        receiver,
                        seed
                );

        Sender sender =
                new Sender(
                        controller,
                        router,
                        receiver,
                        seed
                );

        sender.sendPackets(totalPackets);

        long totalTime =
                router.getTotalSimulatedDelay();

        int receivedPackets =
                receiver.getReceivedPackets();

        int lostPackets =
                router.getLostPackets();

        int totalAttempts =
                receivedPackets + lostPackets;

        double throughput = 0;

        if (totalTime > 0) {

            throughput =
                    (double) receivedPackets
                    / (totalTime / 1000.0);
        }

        double packetLossRate = 0;

        if (totalAttempts > 0) {

            packetLossRate =
                    (double) lostPackets
                    / totalAttempts;
        }

        double averageCongestionWindow =
                sender.getAverageCongestionWindow();

        return new SimulationResult(
                algorithmName,
                condition.getName(),
                totalPackets,
                receivedPackets,
                totalAttempts,
                lostPackets,
                totalTime,
                throughput,
                packetLossRate,
                controller.getCongestionWindow(),
                averageCongestionWindow
        );
    }
}
