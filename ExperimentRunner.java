import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ExperimentRunner {

    private int totalPackets;
    private int repetitions;
    private NetworkCondition[] conditions;

    public ExperimentRunner(
            int totalPackets,
            int repetitions,
            NetworkCondition[] conditions) {

        this.totalPackets = totalPackets;
        this.repetitions = repetitions;
        this.conditions = conditions;
    }

    public void runExperiments() {

        List<SimulationResult> results =
                new ArrayList<>();

        System.out.println();
        System.out.println(
                "========================================");

        System.out.println(
                "       RESEARCH EXPERIMENT");

        System.out.println(
                "========================================");

        for (int c = 0;
                c < conditions.length;
                c++) {

            NetworkCondition condition =
                    conditions[c];

            System.out.println();
            System.out.println(
                    "Condition: "
                    + condition.getName());

            System.out.println(
                    "Loss Rate: "
                    + (condition.getPacketLossRate() * 100)
                    + "%");

            System.out.println(
                    "Bandwidth: "
                    + condition.getBandwidth()
                    + " packets/sec");

            System.out.println(
                    "Delay: "
                    + condition.getDelay()
                    + " ms");

            for (int r = 1;
                    r <= repetitions;
                    r++) {

                long seed = 100 + r;

                AIMDController aimd =
                        new AIMDController();

                results.add(
                        SimulationRunner.runSimulation(
                                "AIMD",
                                aimd,
                                condition,
                                totalPackets,
                                seed
                        )
                );

                RenoController reno =
                        new RenoController();

                results.add(
                        SimulationRunner.runSimulation(
                                "TCP Reno",
                                reno,
                                condition,
                                totalPackets,
                                seed
                        )
                );

                AdaptiveController adaptive =
                        new AdaptiveController(
                                condition.getPacketLossRate(),
                                condition.getDelay()
                        );

                results.add(
                        SimulationRunner.runSimulation(
                                "Adaptive",
                                adaptive,
                                condition,
                                totalPackets,
                                seed
                        )
                );
            }
        }

        displayConditionResults(results);

        saveCSV(results);
    }

    private void displayConditionResults(
            List<SimulationResult> results) {

        System.out.println();
        System.out.println(
                "========================================");

        System.out.println(
                "     CONDITION-WISE AVERAGE RESULTS");

        System.out.println(
                "========================================");

        String[] algorithms = {
                "AIMD",
                "TCP Reno",
                "Adaptive"
        };

        for (int c = 0;
                c < conditions.length;
                c++) {

            String conditionName =
                    conditions[c].getName();

            System.out.println();
            System.out.println(
                    "----------------------------------------");

            System.out.println(
                    "Condition: "
                    + conditionName);

            System.out.println(
                    "----------------------------------------");

            for (int a = 0;
                    a < algorithms.length;
                    a++) {

                String algorithm =
                        algorithms[a];

                double throughput = 0;
                double packetLossRate = 0;
                double time = 0;
                double averageWindow = 0;
                double finalWindow = 0;

                int count = 0;

                for (int i = 0;
                        i < results.size();
                        i++) {

                    SimulationResult result =
                            results.get(i);

                    if (result.getConditionName()
                            .equals(conditionName)
                            && result.getAlgorithmName()
                            .equals(algorithm)) {

                        throughput +=
                                result.getThroughput();

                        packetLossRate +=
                                result.getPacketLossRate();

                        time +=
                                result.getTotalTime();

                        averageWindow +=
                                result.getAverageCongestionWindow();

                        finalWindow +=
                                result.getFinalCongestionWindow();

                        count++;
                    }
                }

                if (count > 0) {

                    throughput /= count;
                    packetLossRate /= count;
                    time /= count;
                    averageWindow /= count;
                    finalWindow /= count;
                }

                System.out.println();
                System.out.println(
                        "Algorithm: "
                        + algorithm);

                System.out.printf(
                        "Average Throughput: %.2f packets/sec%n",
                        throughput
                );

                System.out.printf(
                        "Average Packet Loss Rate: %.2f%%%n",
                        packetLossRate * 100
                );

                System.out.printf(
                        "Average Completion Time: %.2f ms%n",
                        time
                );

                System.out.printf(
                        "Average Congestion Window: %.2f%n",
                        averageWindow
                );

                System.out.printf(
                        "Average Final Window: %.2f%n",
                        finalWindow
                );
            }
        }
    }

    private void saveCSV(
            List<SimulationResult> results) {

        try {

            FileWriter writer =
                    new FileWriter(
                            "experiment_results.csv"
                    );

            writer.write(
                    "Algorithm,Condition,"
                    + "TotalPackets,ReceivedPackets,"
                    + "TotalAttempts,LostPackets,"
                    + "PacketLossRate,TimeMs,"
                    + "Throughput,AverageCWND,"
                    + "FinalCWND\n"
            );

            for (int i = 0;
                    i < results.size();
                    i++) {

                SimulationResult result =
                        results.get(i);

                writer.write(
                        result.getAlgorithmName()
                        + ","
                        + result.getConditionName()
                        + ","
                        + result.getTotalPackets()
                        + ","
                        + result.getReceivedPackets()
                        + ","
                        + result.getTotalAttempts()
                        + ","
                        + result.getLostPackets()
                        + ","
                        + result.getPacketLossRate()
                        + ","
                        + result.getTotalTime()
                        + ","
                        + result.getThroughput()
                        + ","
                        + result.getAverageCongestionWindow()
                        + ","
                        + result.getFinalCongestionWindow()
                        + "\n"
                );
            }

            writer.close();

            System.out.println();
            System.out.println(
                    "CSV file created:");

            System.out.println(
                    "experiment_results.csv");

        } catch (IOException e) {

            System.out.println(
                    "Unable to create CSV file.");

            System.out.println(
                    e.getMessage());
        }
    }
}
