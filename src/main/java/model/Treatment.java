package model;

import utils.DateConverter;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Treatment for patient.
 */
public class Treatment {
    private long tid;
    private long pid;
    private long cgid;
    private LocalDate date;
    private LocalTime begin;
    private LocalTime end;
    private String description;
    private String remarks;
    private String locked;

    /**
     * constructs a treatment from the given params.
     * @param pid
     * @param cgid
     * @param date
     * @param begin
     * @param end
     * @param description
     * @param remarks
     * @param locked
     */
    public Treatment(long pid, long cgid, LocalDate date, LocalTime begin,
                     LocalTime end, String description, String remarks, String locked) {
        this.pid = pid;
        this.cgid = cgid;
        this.date = date;
        this.begin = begin;
        this.end = end;
        this.description = description;
        this.remarks = remarks;
        this.locked = locked;
    }

    /**
     * constructs a treatment from the given params.
     * @param tid
     * @param pid
     * @param cgid
     * @param date
     * @param begin
     * @param end
     * @param description
     * @param remarks
     * @param remarks
     */
    public Treatment(long tid, long pid, long cgid, LocalDate date, LocalTime begin,
                     LocalTime end, String description, String remarks, String locked) {
        this.tid = tid;
        this.pid = pid;
        this.cgid = cgid;
        this.date = date;
        this.begin = begin;
        this.end = end;
        this.description = description;
        this.remarks = remarks;
        this.locked = locked;
    }
    /**
     *
     * @return Treatment id
     */
    public long getTid() {
        return tid;
    }

    /**
     *
     * @return Patient id
     */
    public long getPid() {
        return this.pid;
    }

    /**
     *
     * @return Pfleger id
     */
    public long getCgid() { return this.cgid; }

    /**
     *
     * @return Treatment Date
     */
    public String getDate() {
        return date.toString();
    }

    /**
     *
     * @return Date Begin of Treatment
     */
    public String getBegin() {
        return begin.toString();
    }

    /**
     *
     * @return Date end of Treatment
     */
    public String getEnd() {
        return end.toString();
    }

    /**
     * @param s_date new date of treatment
     */
    public void setDate(String s_date) {
        LocalDate date = DateConverter.convertStringToLocalDate(s_date);
        this.date = date;
    }

    /**
     * @param begin new date  begin of treatment
     */
    public void setBegin(String begin) {
        LocalTime time = DateConverter.convertStringToLocalTime(begin);
        this.begin = time;
    }

    /**
     * @param end new date end of treatment
     */
    public void setEnd(String end) {
        LocalTime time = DateConverter.convertStringToLocalTime(end);
        this.end = time;
    }

    /**
     *
     * @return description of Treatment
     */
    public String getDescription() {
        return description;
    }

    /**
     *
     * @param description set description of Treatment
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     *
     * @return remarks of Treatment
     */
    public String getRemarks() {
        return remarks;
    }

    /**
     *
     * @param remarks set remarks of Treatment
     */
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    /**
     *
     * @return string-representation of the treatment
     */
    public String toString() {
        return "\nBehandlung" + "\nTID: " + this.tid +
                "\nPID: " + this.pid +
                "\nDate: " + this.date +
                "\nBegin: " + this.begin +
                "\nEnd: " + this.end +
                "\nDescription: " + this.description +
                "\nRemarks: " + this.remarks + "\n";
    }
}