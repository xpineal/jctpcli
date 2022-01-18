package org.kcr.jctpcli;

public class DemoOrderArg {
    public String instrumentID;
    public char direction;
    public char offsetFlag;
    public int volume;
    public double price;

    @Override
    public String toString() {
        return "OrderArg{" +
                "instrumentID='" + instrumentID + '\'' +
                ", direction=" + direction +
                ", offsetFlag=" + offsetFlag +
                ", volume=" + volume +
                ", price=" + price +
                '}';
    }
}
