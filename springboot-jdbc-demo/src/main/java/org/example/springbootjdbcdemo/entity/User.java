package org.example.springbootjdbcdemo.entity;

public class User {
    private Integer userId;

    private String userName;

    private Long incrementId;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
    }

    public Long getIncrementId() {
        return incrementId;
    }

    public void setIncrementId(Long incrementId) {
        this.incrementId = incrementId;
    }
}