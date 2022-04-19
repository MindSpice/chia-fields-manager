package data.tail;

public class CachedPoint extends DateTime {

    int signagePointIndex;

    public CachedPoint(String dateTime, int signagePointIndex) {
        super(dateTime);
        this.signagePointIndex = signagePointIndex;
    }

    public int getSignagePointIndex() {
        return signagePointIndex;
    }

}
