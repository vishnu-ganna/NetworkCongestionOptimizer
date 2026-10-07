public class AIMDController
        extends CongestionController {

    public void onSuccessfulTransmission() {

        increaseWindow();
    }

    public void onPacketLoss() {

        int currentWindow =
                getCongestionWindow();

        int newWindow =
                currentWindow / 2;

        if (newWindow < 1) {
            newWindow = 1;
        }

        decreaseTo(newWindow);
    }
}