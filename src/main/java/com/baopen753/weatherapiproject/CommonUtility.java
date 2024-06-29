package com.baopen753.weatherapiproject;

import com.baopen753.weatherapiproject.global.HttpResponseException;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommonUtility {
    private static Logger LOGGER = LoggerFactory.getLogger(CommonUtility.class);

    public static String getIPAddress(HttpServletRequest request) {
        String ip = request.getHeader("X-FORWARDED-FOR");

        if (ip == null || ip.isEmpty()) {
            ip = request.getRemoteAddr();
        }

        //    LOGGER.info("Client's IP Address: " + ip);
        LOGGER.info("Local address: " + request.getLocalAddr());   // server machine
        LOGGER.info("Remote address: " + request.getRemoteAddr()); // client ip
        return ip;
    }

    public static Integer getCurrentHour(HttpServletRequest request) {
        String currentHour = request.getHeader("X-CURRENT-HOUR");
        if (currentHour == null || currentHour.isEmpty())
            throw new HttpResponseException("Missing or empty 'X-Current-Hour' header");
        return Integer.parseInt(request.getHeader("X-CURRENT-HOUR"));
    }
}