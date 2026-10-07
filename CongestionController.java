public class CongestionController {

    private int congestionWindow;

    public CongestionController() {
        congestionWindow = 1;
    }

    public int getCongestionWindow() {
        return congestionWindow;
    }

    public void increaseWindow() {

        congestionWindow++;
    }

    public void increaseWindowBy(int amount) {

        for (int i = 0; i < amount; i++) {

            increaseWindow();
        }
    }

    public void decreaseTo(int newWindow) {

        if (newWindow < 1) {
            newWindow = 1;
        }

        while (congestionWindow > newWindow) {

            congestionWindow--;
        }
    }
}