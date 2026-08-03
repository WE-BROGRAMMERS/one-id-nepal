package com.oneidnepal.auth_server.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Redirects from /oauth2/authorization/{provider} to /oauth2/authorize
 * This is needed for Angular apps or clients that expect the Spring OAuth2 pattern
 */
@Controller
@RequestMapping("/oauth2/authorization")
public class OAuth2RedirectController {

    @GetMapping("/{provider}")
    public String redirectToAuthorize(
            @PathVariable String provider,
            HttpServletRequest request) {

        // Get all query parameters and remove the 'continue' parameter added by HttpSessionRequestCache
        String queryString = request.getQueryString();
        String redirectUrl = "/oauth2/authorize";

        if (queryString != null && !queryString.isEmpty()) {
            // Remove the 'continue' parameter which breaks OAuth2 validation
            queryString = queryString.replaceAll("[&?]continue$", "")
                                     .replaceAll("[&?]continue&", "&");

            if (!queryString.isEmpty()) {
                redirectUrl += "?" + queryString;
            }
        }

        return "redirect:" + redirectUrl;
    }
}

