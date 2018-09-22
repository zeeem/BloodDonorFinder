package finder2.donor.bllod.zeeem.blooddonorfinder;


public class Donor {

    public String name, age, area, blood, gender, lon, lat, history, mobile;

    public Donor(String name, String age, String area, String blood, String gender, String lon, String lat, String history, String mobile) {

        this.name = name;
        this.age = age;
        this.area = area;
        this.blood = blood;
        this.gender = gender;
        this.lon = lon;
        this.lat = lat;
        this.history = history;
        this.mobile = mobile;
    }

    public String getName() {
        return name;
    }

    public String getAge() {
        return age;
    }

    public String getArea() {
        return area;
    }

    public String getBlood() {
        return blood;
    }

    public String getGender() {
        return gender;
    }

    public String getLon() {
        return lon;
    }

    public String getLat() {
        return lat;
    }

    public String getHistory() {
        return history;
    }

    public String getMobile() {
        return mobile;
    }
}
