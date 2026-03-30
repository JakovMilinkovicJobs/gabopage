package proj.gabopage.util;

import org.owasp.html.HtmlPolicyBuilder;
import org.owasp.html.PolicyFactory;

public class HtmlSanitizer {

    private static final PolicyFactory POLICY = new HtmlPolicyBuilder()
            // Allow common text formatting
            .allowElements("p", "br", "strong", "em", "u", "s", "h1", "h2", "h3", "h4", "h5", "h6",
                    "blockquote", "pre", "code", "ul", "ol", "li", "hr", "sub", "sup")
            // Allow links with safe attributes
            .allowElements("a")
            .allowAttributes("href").onElements("a")
            .allowUrlProtocols("http", "https", "mailto")
            // Allow images with safe attributes
            .allowElements("img")
            .allowAttributes("src", "alt", "title", "width", "height").onElements("img")
            .allowUrlProtocols("http", "https", "data")
            // Allow text styling
            .allowElements("span", "div")
            .allowAttributes("style").onElements("span", "div", "p", "h1", "h2", "h3", "h4", "h5", "h6")
            // Allow safe CSS properties for styling
            .allowStyling()
            .toFactory();

    /**
     * Sanitizes HTML content to prevent XSS attacks while preserving safe formatting
     * @param html Raw HTML content
     * @return Sanitized HTML safe for rendering
     */
    public static String sanitize(String html) {
        if (html == null) {
            return "";
        }
        return POLICY.sanitize(html);
    }
}
