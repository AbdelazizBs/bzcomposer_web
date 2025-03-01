package com.nxsol.bizcomposer.common;

import java.util.Calendar;
import java.util.Date;

public class TblLineofCreditTerm {

	  //overdue?
    private boolean overDue = false;

    //term ID
    private int creditTermId = -1;

    // days
    private int days = 0;

    // Name
    private String name = "";

    // overdue days
    private long overdue_days = 0;

    // due date
    private java.util.Date dueDate = null;

    private int isDefault = 0;


    /** Creates a new instance of Term */
    public TblLineofCreditTerm() {
    }   

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private void overDueDays(java.util.Date dueDate) {

        //today - due date =past days
        java.util.Date today  = new java.util.Date();
        setOverdue_days(DaysBetween(today,dueDate));
        setOverDue(true);

    }

    private long DaysBetween(java.util.Date in1, java.util.Date in2) {

        long days = 0;

        try {

            long milis1 = in1.getTime();
            long milis2 = in2.getTime();
            float diff = milis1 - milis2;
            days = (int)java.lang.Math.round(diff / (86400 * 1000));

        } catch (Exception e) {

            //System.out.println("DateToDay:: error: " + e.getMessage());
            return 0;
        }
        return days;
    }


    public long getOverdue_days() {
        return overdue_days;
    }

    public void setOverdue_days(long overdue_days) {
        this.overdue_days = overdue_days;
    }

    public boolean isOverDue() {
        return overDue;
    }

    public void setOverDue(boolean overDue) {
        this.overDue = overDue;
    }

    public java.util.Date getDueDate() {
        return dueDate;
    }

    public void setDueDateOf(java.util.Date dateAdded) {

        if (dateAdded==null || getName().equalsIgnoreCase("paid")) {
            setOverDue(false);
            return;
        }

        Calendar cal = Calendar.getInstance();
        cal.setTime(dateAdded);
        cal.add(Calendar.DAY_OF_MONTH,this.getDays());
        if (!cal.getTime().after(new Date()) ) {

            overDueDays(cal.getTime() );

        } else {

            setOverDue(false);
        }
        this.dueDate = cal.getTime();
    }

    public boolean isDueAfter(java.util.Date from) {
        if (getDueDate()==null)
            return false;

        return getDueDate().after(from) || ConstValue.getSimpleDateFormat().format(from).equals(
                ConstValue.getSimpleDateFormat().format(getDueDate()));

    }

    public boolean isDueBefore(java.util.Date to) {
        if (getDueDate()==null)
            return false;

        return getDueDate().before(to) || ConstValue.getSimpleDateFormat().format(to).equals(
                ConstValue.getSimpleDateFormat().format(getDueDate()));
    }

     public boolean isOverDueAfter(java.util.Date from) {
        if (getDueDate()==null)
            return false;

        return getDueDate().after(from) ;

    }

    public boolean isOverDueBefore(java.util.Date to) {
        if (getDueDate()==null)
            return false;

        return getDueDate().before(to);
    }

    public String toString() { return getName();}

    public boolean equals(Object obj) {
        //check for self-comparison
        if ( this == obj ) return true;
        if ( !(obj instanceof TblLineofCreditTerm) ) return false;

        TblLineofCreditTerm other = (TblLineofCreditTerm)obj;
        if (this.getCreditTermId()!=other.getCreditTermId()) return false;

        return true;

    }

    /**
     * @return the creditTermId
     */
    public int getCreditTermId() {
        return creditTermId;
    }

    /**
     * @param creditTermId the creditTermId to set
     */
    public void setCreditTermId(int creditTermId) {
        this.creditTermId = creditTermId;
    }

    /**
     * @return the isDefault
     */
    public int getIsDefault() {
        return isDefault;
    }

    /**
     * @param isDefault the isDefault to set
     */
    public void setIsDefault(int isDefault) {
        this.isDefault = isDefault;
    }

}
