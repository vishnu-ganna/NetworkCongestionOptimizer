public class Main {

    public static void main(String[] args) {

        int totalPackets = 1000;
        int repetitions = 5;

        NetworkCondition[] conditions = {

                new NetworkCondition(
                        "Good Network",
                        0.01,
                        1000,
                        5
                ),

                new NetworkCondition(
                        "Normal Network",
                        0.05,
                        500,
                        15
                ),

                new NetworkCondition(
                        "Congested Network",
                        0.10,
                        200,
                        30
                ),

                new NetworkCondition(
                        "Highly Congested Network",
                        0.20,
                        100,
                        50
                )
        };

        System.out.println(
                "========================================");

        System.out.println(
                " ADAPTIVE NETWORK CONGESTION OPTIMIZER");

        System.out.println(
                "========================================");

        System.out.println();

        System.out.println(
                "Packets per experiment: "
                + totalPackets);

        System.out.println(
                "Repetitions per condition: "
                + repetitions);

        System.out.println(
                "Algorithms: AIMD, TCP Reno, Adaptive");

        System.out.println(
                "Network conditions: "
                + conditions.length);

        ExperimentRunner runner =
                new ExperimentRunner(
                        totalPackets,
                        repetitions,
                        conditions
                );

        runner.runExperiments();

        System.out.println();

        System.out.println(
                "========================================");

        System.out.println(
                "          EXPERIMENT COMPLETE");

        System.out.println(
                "========================================");
    }
}
