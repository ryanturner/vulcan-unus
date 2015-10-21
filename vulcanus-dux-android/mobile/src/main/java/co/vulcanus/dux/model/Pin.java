package co.vulcanus.dux.model;

/**
 * Created by ryan_turner on 10/17/15.
 */
public class Pin {
    private int number;
    private boolean isHigh;

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public boolean isHigh() {
        return isHigh;
    }

    public void setIsHigh(boolean high) {
        this.isHigh = high;
    }
}
