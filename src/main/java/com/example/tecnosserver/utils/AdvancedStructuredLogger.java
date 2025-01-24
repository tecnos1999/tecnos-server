package com.example.tecnosserver.utils;

import net.logstash.logback.marker.LogstashMarker;
import net.logstash.logback.marker.Markers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * A more advanced structured logger that supports:
 * - Correlation IDs via MDC
 * - Event types
 * - Builder pattern for adding log fields
 */
@Component
public class AdvancedStructuredLogger {

    private static final String CORRELATION_ID_KEY = "correlationId";

    /**
     * If you want each class to have its own logger name, you could pass the class.
     * But here we’ll just keep a single logger for demonstration.
     */
    private final Logger logger = LoggerFactory.getLogger(AdvancedStructuredLogger.class);

    /**
     * Ensures there's a correlation ID in MDC if not already present.
     * You can call this at the start of a request (e.g., in a filter or a controller).
     */
    public static void ensureCorrelationId() {
        if (MDC.get(CORRELATION_ID_KEY) == null) {
            MDC.put(CORRELATION_ID_KEY, UUID.randomUUID().toString());
        }
    }

    /**
     * Clears the correlation ID from MDC. Typically called at the end of a request.
     */
    public static void clearCorrelationId() {
        MDC.remove(CORRELATION_ID_KEY);
    }

    /**
     * Creates a new log builder instance.
     */
    public LogEventBuilder logBuilder() {
        return new LogEventBuilder();
    }

    /**
     * A builder that allows you to add fields in a structured way before logging.
     */
    public class LogEventBuilder {
        private String eventType;
        private String message;
        private String level = "INFO";
        private final Map<String, Object> fields = new HashMap<>();

        public LogEventBuilder withEventType(String eventType) {
            this.eventType = eventType;
            return this;
        }

        public LogEventBuilder withMessage(String message) {
            this.message = message;
            return this;
        }

        public LogEventBuilder withLevel(String level) {
            this.level = level;
            return this;
        }

        public LogEventBuilder withField(String key, Object value) {
            this.fields.put(key, value);
            return this;
        }

        /**
         * A convenient method to add multiple fields at once.
         */
        public LogEventBuilder withFields(Map<String, Object> map) {
            this.fields.putAll(map);
            return this;
        }

        /**
         * Final method that actually logs the event.
         */
        public void log() {
            // Add eventType as a field so it’s searchable in Kibana
            if (eventType != null && !eventType.isEmpty()) {
                fields.put("eventType", eventType);
            }

            // Create a combined marker
            LogstashMarker marker = Markers.appendEntries(fields);

            // Use the correlation ID from MDC (if set), ensuring it’s also in the JSON
            String correlationId = MDC.get(CORRELATION_ID_KEY);
            if (correlationId != null) {
                marker = marker.and(Markers.append(CORRELATION_ID_KEY, correlationId));
            }

            // Log at the correct level
            switch (level.toUpperCase()) {
                case "DEBUG":
                    logger.debug(marker, message);
                    break;
                case "WARN":
                    logger.warn(marker, message);
                    break;
                case "ERROR":
                    logger.error(marker, message);
                    break;
                case "INFO":
                default:
                    logger.info(marker, message);
                    break;
            }
        }
    }
}
