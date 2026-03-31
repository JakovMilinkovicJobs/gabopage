package proj.gabopage.config;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
public class IpWhitelistFilter implements Filter {

    private static final Logger log = LoggerFactory.getLogger(IpWhitelistFilter.class);

    @Value("${admin.allowed.ips:127.0.0.1,0:0:0:0:0:0:0:1}")
    private String allowedIpsConfig;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String requestUri = httpRequest.getRequestURI();

        // Only apply IP filtering to admin login and admin routes
        if (requestUri.startsWith("/login") || requestUri.startsWith("/admin")) {
            String clientIp = getClientIp(httpRequest);
            
            // Check if environment variable override exists
            String allowedIpsStr = System.getenv("ALLOWED_ADMIN_IPS");
            if (allowedIpsStr == null || allowedIpsStr.isBlank()) {
                allowedIpsStr = allowedIpsConfig;
            }

            List<String> allowedIps = Arrays.asList(allowedIpsStr.split(","));

            if (!isIpAllowed(clientIp, allowedIps)) {
                log.warn("Access denied from IP: {} to {}", clientIp, requestUri);
                httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied");
                return;
            }

            log.debug("Access granted from IP: {} to {}", clientIp, requestUri);
        }

        chain.doFilter(request, response);
    }

    private String getClientIp(HttpServletRequest request) {
        // Check common headers for real IP (in case of proxy/load balancer)
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        // If X-Forwarded-For contains multiple IPs, take the first one
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }

        return ip;
    }

    private boolean isIpAllowed(String clientIp, List<String> allowedIps) {
        for (String allowedIp : allowedIps) {
            String trimmedIp = allowedIp.trim();
            if (trimmedIp.equals(clientIp)) {
                return true;
            }
            // Support for IPv4 wildcards like 192.168.1.*
            if (trimmedIp.endsWith("*")) {
                String prefix = trimmedIp.substring(0, trimmedIp.length() - 1);
                if (clientIp.startsWith(prefix)) {
                    return true;
                }
            }
        }
        return false;
    }
}
