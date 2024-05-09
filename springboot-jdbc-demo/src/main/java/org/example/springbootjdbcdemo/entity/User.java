package org.example.springbootjdbcdemo.entity;

public class User {
    private Integer userId;

    private String userName;

    private Long incrementId;

    private Long incrementId1;

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

    public Long getIncrementId1() {
        return incrementId1;
    }

    public void setIncrementId1(Long incrementId1) {
        this.incrementId1 = incrementId1;
    }
}