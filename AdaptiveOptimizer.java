public class AdaptiveOptimizer {

    public String selectStrategy(
            double lossRate,
            double delay,
            double recentLossRate) {

        if (recentLossRate <= 0.02
                && lossRate <= 0.02
                && delay <= 15) {

            return "AGGRESSIVE";
        }

        if (recentLossRate <= 0.08
                && lossRate <= 0.10
                && delay <= 30) {

            return "BALANCED";
        }

        return "CONSERVATIVE";
    }

    public int calculateIncrease(
            String strategy,
            int currentWindow) {

        if (strategy.equals("AGGRESSIVE")) {

            return 2;
        }

        if (strategy.equals("BALANCED")) {

            return 1;
        }

        return 1;
    }

    public int calculateDecrease(
            String strategy,
            int currentWindow) {

        if (currentWindow <= 1) {
            return 1;
        }

        if (strategy.equals("AGGRESSIVE")) {

            return Math.max(
                    1,
                    (int)(currentWindow * 0.70)
            );
        }

        if (strategy.equals("BALANCED")) {

            return Math.max(
                    1,
                    currentWindow / 2
            );
        }

        return Math.max(
                1,
                (int)(currentWindow * 0.60)
        );
    }
}
