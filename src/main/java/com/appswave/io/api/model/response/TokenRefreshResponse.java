package com.appswave.io.api.model.response;

public record TokenRefreshResponse(
        String accessToken,
        String refreshToken) {
}