package com.baopen753.weatherapiproject;

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
        return Integer.parseInt(request.getHeader("X-Current-Hour"));
    }
}
