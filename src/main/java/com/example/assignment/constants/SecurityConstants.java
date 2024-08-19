package com.example.assignment.constants;

public class SecurityConstants {
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";
    public static final String[] PUBLIC_URLS = {
            "/api/auth/**",
            "/public/**",
            "/h2-console/**",
            "/"
    };
}
