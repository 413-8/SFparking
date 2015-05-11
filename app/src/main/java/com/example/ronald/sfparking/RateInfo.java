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
    private String desc = "";
    private String rr = "";

    public RateInfo() {}

    // getters
    /**
     * get the begin time for this schedule
     * @return the string that contains the begin time
     */
    public String getBeg() {
        return beg;
    }

    /**
     * get the end time for this schedule
     * @return the string that contains the end time
     */
    public String getEnd() {
        return end;
    }

    /**
     * get the applicable rate for this rate schedule
     * @return the string that contains the rate
     */
    public String getRate() {
        return rate;
    }

    /**
     * get the rate qualifier for this rate schedule, e.g. Per Hr
     * @return the string that contains the rate qualifier
     */
    public String getRq() {
        return rq;
    }

    /**
     * get descriptive rate information when it is not possible to specify
     * using beg or end times for this rate schedule
     * @return the string that contains the descriptive rate information
     */
    public String getDesc() {
        return desc;
    }

    /**
     * get rate restriction for this rate schedule if any
     * @return the string that contains the rate restriction
     */
    public String getRr() {
        return rr;
    }

    // setters
    /**
     * set the begin time for this schedule
     * @param start A string containing the begin time
     */
    public void setBeg(String start) {
        this.beg = start;
    }

    /**
     * set the end time for this schedule
     * @param finish A string containing the end time
     */
    public void setEnd(String finish) {
        this.end = finish;
    }

    /**
     * set the applicable rate for this rate schedule
     * @param money A string containing the rate
     */
    public void setRate(String money) {
        this.rate = money;
    }

    /**
     * set the rate qualifier for this rate schedule, e.g. Per Hr
     * @param request A string containing the rate qualifier
     */
    public void setRq(String request) {
        this.rq = request;
    }

    /**
     * set descriptive rate information when it is not possible to specify
     * using beg or end times for this rate schedule
     * @param description A string containing the descriptive rate information
     */
    public void setDesc(String description) {
        this.desc = description;
    }

    /**
     * set rate restriction for this rate schedule if any
     * @param restriction A string containing the rate restriction
     */
    public void setRr(String restriction) {
        this.rr = restriction;
    }

    /**
     * builds the string that will be used in the rates field of a location object.
     * @return the representation of rates for the parking space.
     */
    @Override
    public String toString() {
        StringBuilder temp = new StringBuilder();

        if(!rr.equals("")) {
            temp.append("\t\t");
            temp.append(desc);
            temp.append(":\n");
            temp.append("\t\t\t");
            temp.append("$");
            temp.append(rate);
            temp.append(" ");
            temp.append(rq);
            temp.append("\n");
            temp.append("\t\t\t");
            temp.append(rr);
            temp.append("\n");
        } else if (!desc.equals("")) {
            temp.append("\t\t");
            temp.append(desc);
            temp.append(":\n");
            temp.append("\t\t\t");
            temp.append("$");
            temp.append(rate);
            temp.append(" ");
            temp.append(rq);
            temp.append("\n");
        } else {
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
        }
        return temp.toString();
    }
}