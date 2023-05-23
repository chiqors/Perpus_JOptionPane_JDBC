package models;

public class Member {
    private int id;
    private String name;
    private String email;
    private String phone;
    private String registered_at;

//    public Member(int id, String name, String email, String phone, String registered_at) {
//        this.id = id;
//        this.name = name;
//        this.email = email;
//        this.phone = phone;
//        this.registered_at = registered_at;
//    }
//
//    public Member(String name, String email, String phone, String registered_at) {
//        this.name = name;
//        this.email = email;
//        this.phone = phone;
//        this.registered_at = registered_at;
//    }

    @Override
    public String toString() {
        return name + " - " + email + " - " + phone + " - " + registered_at;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRegistered_at() {
        return registered_at;
    }

    public void setRegistered_at(String registered_at) {
        this.registered_at = registered_at;
    }
}
