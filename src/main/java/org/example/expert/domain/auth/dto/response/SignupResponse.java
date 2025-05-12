package org.example.expert.domain.auth.dto.response;

import lombok.Getter;

@Getter
public class SignupResponse {

    private final String bearerToken;
    private final String profileImageUrl;

    public SignupResponse(String bearerToken, String profileImageUrl) {
        this.bearerToken = bearerToken;
        this.profileImageUrl = profileImageUrl;
    }
}
