package org.example.demo2.entity.dto;


public class UserDTO {
    private Integer id;
    private String userName;
    private String password;
    private String email;
    private String phone;
    private String verifyCode;

    public UserDTO() {
    }

    public UserDTO(Integer id, String userName, String password, String email, String phone, String verifyCode) {
        this.id = id;
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.verifyCode = verifyCode;
    }

    public Integer getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getVerifyCode() {
        return verifyCode;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setVerifyCode(String verifyCode) {
        this.verifyCode = verifyCode;
    }
}
