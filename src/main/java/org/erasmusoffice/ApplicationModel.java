package org.erasmusoffice;

import java.time.LocalDate;

public class ApplicationModel {
    private int appID;
    private int studentID;
    private String universityName;
    private String term;

    public ApplicationModel(int appID, int studentID, String universityName, String term) {
        this.appID = appID;
        this.studentID = studentID;
        this.universityName = universityName;
        this.term = term;
    }

    public int getAppID() {
        return appID;
    }

    public void setAppID(int appID) {
        this.appID = appID;
    }

    public int getStudentID() {
        return studentID;
    }

    public void setStudentID(int studentID) {
        this.studentID = studentID;
    }

    public String getUniversityName() {
        return universityName;
    }

    public void setUniversityName(String universityName) {
        this.universityName = universityName;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }
}
