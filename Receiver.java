public class Receiver {

    private int receivedPackets;

    public Receiver() {
        receivedPackets = 0;
    }

    public Acknowledgement receivePacket(Packet packet) {

        receivedPackets++;

        return new Acknowledgement(
                packet.getPacketId()
        );
    }

    public int getReceivedPackets() {
        return receivedPackets;
    }
}