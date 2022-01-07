package org.erasmusoffice;

public class UniversityModel {
    private String country;
    private String name;
    private int fallQuota;
    private int springQuota;

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getFallQuota() {
        return fallQuota;
    }

    public void setFallQuota(int fallQuota) {
        this.fallQuota = fallQuota;
    }

    public int getSpringQuota() {
        return springQuota;
    }

    public void setSpringQuota(int springQuota) {
        this.springQuota = springQuota;
    }
}
