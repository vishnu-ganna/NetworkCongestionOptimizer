public class Sender {

    private CongestionController controller;
    private Router router;
    private Receiver receiver;

    private long totalCongestionWindow;
    private int congestionWindowSamples;

    public Sender(
            CongestionController controller,
            Router router,
            Receiver receiver,
            long seed) {

        this.controller = controller;
        this.router = router;
        this.receiver = receiver;

        totalCongestionWindow = 0;
        congestionWindowSamples = 0;
    }

    public void sendPackets(int totalPackets) {

        boolean[] delivered =
                new boolean[totalPackets];

        int successfullyReceived = 0;

        while (successfullyReceived
                < totalPackets) {

            int window =
                    controller.getCongestionWindow();

            int packetsToSend =
                    Math.min(
                            window,
                            totalPackets
                    );

            Packet[] packets =
                    new Packet[packetsToSend];

            int[] packetIndexes =
                    new int[packetsToSend];

            int selectedPackets = 0;

            for (int i = 0;
                    i < totalPackets
                    && selectedPackets < packetsToSend;
                    i++) {

                if (!delivered[i]) {

                    packets[selectedPackets] =
                            new Packet(i + 1);

                    packetIndexes[selectedPackets] =
                            i;

                    selectedPackets++;
                }
            }

            if (selectedPackets == 0) {
                break;
            }

            totalCongestionWindow +=
                    selectedPackets;

            congestionWindowSamples++;

            /*
             * Router now returns complete
             * network feedback.
             */
            TransmissionResult result =
                    router.transmitWindow(
                            packets,
                            selectedPackets
                    );

            boolean[] lost =
                    result.getLostPackets();

            int successfulThisRound = 0;
            int lostThisRound = 0;

            /*
             * Process packets.
             */
            for (int i = 0;
                    i < selectedPackets;
                    i++) {

                if (lost[i]) {

                    lostThisRound++;

                } else {

                    Acknowledgement acknowledgement =
                            receiver.receivePacket(
                                    packets[i]
                            );

                    if (acknowledgement != null) {

                        int packetIndex =
                                packetIndexes[i];

                        if (!delivered[packetIndex]) {

                            delivered[packetIndex] =
                                    true;

                            successfullyReceived++;

                            successfulThisRound++;
                        }
                    }
                }
            }

            /*
             * Send network feedback to
             * the Adaptive controller.
             */
            if (controller
                    instanceof AdaptiveController) {

                AdaptiveController adaptive =
                        (AdaptiveController)
                        controller;

                for (int i = 0;
                        i < lostThisRound;
                        i++) {

                    adaptive.recordAttempt(
                            true
                    );
                }

                for (int i = 0;
                        i < successfulThisRound;
                        i++) {

                    adaptive.recordAttempt(
                            false
                    );
                }

                adaptive.updateNetworkFeedback(
                        result.getMeasuredDelay(),
                        result.getQueueUtilization()
                );
            }

            /*
             * Congestion control decision.
             */
            if (lostThisRound > 0) {

                if (controller
                        instanceof AIMDController) {

                    AIMDController aimd =
                            (AIMDController)
                            controller;

                    aimd.onPacketLoss();

                } else if (controller
                        instanceof RenoController) {

                    RenoController reno =
                            (RenoController)
                            controller;

                    reno.onPacketLoss();

                } else if (controller
                        instanceof AdaptiveController) {

                    AdaptiveController adaptive =
                            (AdaptiveController)
                            controller;

                    adaptive.onPacketLoss();
                }

            } else {

                if (controller
                        instanceof AIMDController) {

                    AIMDController aimd =
                            (AIMDController)
                            controller;

                    aimd.onSuccessfulTransmission();

                } else if (controller
                        instanceof RenoController) {

                    RenoController reno =
                            (RenoController)
                            controller;

                    reno.onSuccessfulTransmission();

                } else if (controller
                        instanceof AdaptiveController) {

                    AdaptiveController adaptive =
                            (AdaptiveController)
                            controller;

                    adaptive.onSuccessfulTransmission();
                }
            }
        }
    }

    public double getAverageCongestionWindow() {

        if (congestionWindowSamples == 0) {
            return 0;
        }

        return (double)
                totalCongestionWindow
                / congestionWindowSamples;
    }
}
