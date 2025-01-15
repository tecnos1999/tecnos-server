package com.example.tecnosserver.utils;

public class Utils {
    public static final String USERNAME_ALREADY_EXISTS = "Username already exists";
    public static final String EMAIL_ALREADY_EXISTS = "Email already exists";
    public static final String NO_USER_FOUND_BY_USERNAME = "No user found by username: ";
    public static final String FOUND_USER_BY_USERNAME = "Returning found user by username: ";
    public static final String NO_USER_FOUND_BY_EMAIL = "No user found for email: ";
    public static final long EXPIRATION_TIME = 432_000_000;
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String JWT_TOKEN_HEADER = "Jwt-Token";
    public static final String JWT_TOKEN_EXPIRATION_HEADER = "Jwt-Token expiration time";
    public static final String TOKEN_CANNOT_BE_VERIFIED = "Token cannot be verified";
    public static final String MY_CODE = "MyCode";
    public static final String GET_MY_CODE_LLC = "MyCode, LLC";
    public static final String ADMINISTRATION = "Library Manager ";
    public static final String AUTHORITIES = "authorities";
    public static final String FORBIDDEN_MESSAGE = "You need to log in to access this page";
    public static final String ACCESS_DENIED_MESSAGE = "You do not have permission to access this page";
    public static final String OPTIONS_HTTP_METHOD = "OPTIONS";
    public static final String[] PUBLIC_URLS = {"/swagger-ui.html", "/swagger-ui/**",
            "/v3/**", "/server/api/v1/login", "/server/api/v1/register",
            "server/api/v1/category/find**", "/server/api/v1/category/findAll**",
            "/server/api/v1/subcategory/find**", "/server/api/v1/itemcategory/find**",
            "/server/api/v1/subcategory/all**", "/server/api/v1/product/all**",
            "/server/api/v1/product/{sku}", "/server/api/v1/itemcategory/all**",
            "/server/api/v1/product/category/**", "/server/api/v1/partners",
            "/server/api/v1/partners/{name}", "/server/api/v1/product/partner/{partnerName}",
            "/server/api/v1/events", "/server/api/v1/events/{eventCode}",
            "/server/api/v1/webinars", "/server/api/v1/webinars/{webCode}",
            "/server/api/v1/testimonials", "/server/api/v1/testimonials/{code}",
            "/server/api/v1/tags", "/server/api/v1/tags/{name}",
            "/server/api/v1/product/tag/{tagName}", "/server/api/v1/product/tags",
            "/server/api/v1/news", "/server/api/v1/news/{uniqueCode} ",
            "/server/api/v1/carousel" , "/server/api/v1/carousel/videos",
            "/server/api/v1/carousel/images" , "/server/api/v1/carousel/orderd",
            "/server/api/v1/motto", "/server/api/v1/motto/{code}",
            "/server/api/v1/captions", "/server/api/v1/captions/{code}", "/server/api/v1/captions/codes**",
            "/server/api/v1/blogs", "/server/api/v1/blogs/{blogCode}",
            "/server/api/v1/series", "/server/api/v1/series/{code}",
            "/server/api/v1/series/getSeriesByName**" , "/server/api/v1/product/sku**",
            "server/api/v1/infocard", "server/api/v1/infocard/{code}"

    };
}
