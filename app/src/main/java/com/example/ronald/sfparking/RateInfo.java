package com.example.ronald.sfparking;

/**
 * Created by Pedro on 5/2/2015.
 * holds information about price rates for parking.
 */
public class RateInfo {
    private String beg;
    private String end;
    private String rate;
    private String rq;

    public RateInfo() {}

    // getters
    public String getBeg() {
        return beg;
    }

    public String getEnd() {
        return end;
    }

    public String getRate() {
        return rate;
    }

    public String getRq() {
        return rq;
    }

    // setters
    public void setBeg(String start) {
        this.beg = start;
    }

    public void setEnd(String finish) {
        this.end = finish;
    }

    public void setRate(String money) {
        this.rate = money;
    }

    public void setRq(String request) {
        this.rq = request;
    }

    /**
     * builds the string that will be used in the rates field of a location object.
     * @return the representation of rates for the parking space.
     */
    @Override
    public String toString() {
        StringBuilder temp = new StringBuilder();
        temp.append("\t\t");
        temp.append(beg);
        temp.append(" - ");
        temp.append(end);
        temp.append(":\n");

        if(rate.equals("0")) {
            temp.append("\t\t\t");
            temp.append(rq);
            temp.append("\n");
        }
        else {
            temp.append("\t\t\t");
            temp.append("$");
            temp.append(rate);
            temp.append(" ");
            temp.append(rq);
            temp.append("\n");
        }

        return temp.toString();
    }
}