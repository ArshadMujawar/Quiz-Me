package com.asm.quizme;


import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class WithdrawRequest {
    private String userId;
    private String emailAddress;
    private String requestedBy;
    private String coins;

    @ServerTimestamp
    private Date createdAt;

    public WithdrawRequest() {
    }

    public WithdrawRequest(String userId, String emailAddress, String requestedBy, String coins) {
        this.userId = userId;
        this.emailAddress = emailAddress;
        this.requestedBy = requestedBy;
        this.coins = coins;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getRequestedBy() {
        return requestedBy;
    }

    public void setRequestedBy(String requestedBy) {
        this.requestedBy = requestedBy;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getCoins() {
        return coins;
    }

    public void setCoins(String coins) {
        this.coins = coins;
    }
}
