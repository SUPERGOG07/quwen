package com.quwen.entity.business;

import com.quwen.entity.base.BaseEntity;

import javax.validation.constraints.NotBlank;

public class Token extends BaseEntity<Token> {

    private static final long serialVersionUID = 1L;

    /**
     * 用户标识码
     */
    @NotBlank
    private String userId;

    /**
     * 用户角色：小程序或手表
     */
    @NotBlank
    private String role;

    /**
     * accessTokenId
     */
    @NotBlank
    private String accessTokenId;

    /**
     * refreshTokenId
     */
    @NotBlank
    private String refreshTokenId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getAccessTokenId() {
        return accessTokenId;
    }

    public void setAccessTokenId(String accessTokenId) {
        this.accessTokenId = accessTokenId;
    }

    public String getRefreshTokenId() {
        return refreshTokenId;
    }

    public void setRefreshTokenId(String refreshTokenId) {
        this.refreshTokenId = refreshTokenId;
    }
}
