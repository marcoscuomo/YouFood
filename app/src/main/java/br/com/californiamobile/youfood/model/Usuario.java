package br.com.californiamobile.youfood.model;

public class Usuario {

    private String Name;
    private String Password;
    private String Phone;
    private String isStaff;

    public Usuario() {
    }

    public Usuario(String name, String password) {
        Name = name;
        Password = password;
        isStaff = "false";
        //Phone = phone;
    }

    public String getIsStaff() {
        return isStaff;
    }

    public void setIsStaff(String isStaff) {
        this.isStaff = isStaff;
    }

    public String getPhone() {
        return this.Phone;
    }

    public void setPhone(String phone) {
        this.Phone = phone;
    }

    public String getName() {
        return this.Name;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        this.Password = password;
    }
}
