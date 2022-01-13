package org.erasmusoffice;

import java.util.Random;

public class ApplicationModel {
    private int appID;
    private int studentID;
    private String universityName;
    private int universityId;
    private int priority;
    private String term;
    private Boolean result;
    private String resultString;
    private float fund;

    public ApplicationModel(){
        Random rnd = new Random();
        this.fund = rnd.nextInt(200) + 200;
    }

    public String getUniversityName() {
        return universityName;
    }

    public void setUniversityName(String universityName) {
        this.universityName = universityName;
    }

    public int getAppID() {
        return appID;
    }

    public void setAppID(int appID) {
        this.appID = appID;
    }

    public float getFund() {
        return fund;
    }

    public void setFund(float fund) {
        this.fund = fund;
    }

    public int getStudentID() {
        return studentID;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public void setStudentID(int studentID) {
        this.studentID = studentID;
    }

    public int getUniversityId() {
        return universityId;
    }

    public void setUniversityId(int universityId) {
        this.universityId = universityId;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public Boolean getResult() {
        return result;
    }

    public String getResultString(){
        return resultString;
    }

    public void setResult(Boolean result) {
        this.result = result;
        if(result){
            resultString = "Approved";
        }else{
            resultString = "Denied";
        }
    }
}
