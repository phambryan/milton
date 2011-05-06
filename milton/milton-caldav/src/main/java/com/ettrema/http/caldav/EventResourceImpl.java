package com.ettrema.http.caldav;

import java.util.Date;

/**
 *
 * @author brad
 */
public class EventResourceImpl implements EventResource {

    private Date start;
    private Date end;
    private String summary;

    public Date getStart() {
        return start;
    }

    public void setStart(Date d) {
        this.start = d;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date d) {
        this.end = d;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String s) {
        this.summary = s;
    }

}
