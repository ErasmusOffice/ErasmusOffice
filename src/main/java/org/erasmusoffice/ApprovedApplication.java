package org.erasmusoffice;

public class ApprovedApplication {
    private int std_id;
    private String std_name;
    private String country;
    private String uni_name;
    private String term;

    public int getStd_id() {
        return std_id;
    }

    public void setStd_id(int std_id) {
        this.std_id = std_id;
    }

    public String getStd_name() {
        return std_name;
    }

    public void setStd_name(String std_name) {
        this.std_name = std_name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getUni_name() {
        return uni_name;
    }

    public void setUni_name(String uni_name) {
        this.uni_name = uni_name;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }
}
