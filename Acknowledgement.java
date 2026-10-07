public class Acknowledgement {

    private int packetId;

    public Acknowledgement(int packetId) {
        this.packetId = packetId;
    }

    public int getPacketId() {
        return packetId;
    }
}