package com.example.harumeonglog.global.discord.service;

import jakarta.servlet.http.HttpServletRequest;

public interface DiscordService {
    void sendErrorMessage(HttpServletRequest request, Exception exception, String... message);
}
