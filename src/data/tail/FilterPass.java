package data.tail;

public class FilterPass extends DateTime {

    public enum Warning {
        FIVE,
        TEN,
        FIFTEEN,
        TWENTY,
        MISSED
    }

    int proofs = -1;
    int eligiblePlots = -1;
    int totalPlots = -1;
    double lookUpTime;
    Warning warning;
    boolean isWarn;

    public FilterPass(String dateTime, int proofs, int eligiblePlots, int totalPlots, double lookUpTime) {
        super(dateTime);
        this.proofs = proofs;
        this.eligiblePlots = eligiblePlots;
        this.totalPlots = totalPlots;
        this.lookUpTime = lookUpTime;

        if (lookUpTime >= 30) {
            isWarn = true;
            warning = Warning.MISSED;
        }
        else if (lookUpTime >= 20) {
            isWarn = true;
            warning = Warning.TWENTY;
        }
        else if (lookUpTime >= 15) {
            isWarn = true;
            warning = Warning.FIFTEEN;
        }
        else if (lookUpTime >= 10) {
            isWarn = true;
            warning = Warning.TEN;
        }
        else if (lookUpTime >= 5) {
            isWarn = true;
            warning = Warning.FIVE;
        }
    }

    public Warning getWarning() {
        return warning;
    }

    public boolean isWarn() {
        return isWarn;
    }

    public int getProofs() {
        return proofs;
    }

    public int getEligiblePlots() {
        return eligiblePlots;
    }

    public int getTotalPlots() {
        return totalPlots;
    }

    public double getLookUpTime() {
        return lookUpTime;
    }


}
