package org.erasmusoffice;

public class ApplicationModel {
    private int appID;
    private int studentID;
    private String universityName;
    private String term;
    private Boolean result;
    private String resultString;

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
