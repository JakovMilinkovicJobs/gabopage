-- Mock topics for debugging
INSERT INTO TOPIC (title, description, rich_html, created_at, display_order) VALUES
('Understanding Spring Boot Basics', 'A comprehensive guide to getting started with Spring Boot framework', '<h1>Understanding Spring Boot</h1><p>Spring Boot makes it easy to create stand-alone, production-grade Spring applications...</p>', CURRENT_TIMESTAMP - INTERVAL '10' DAY, 0),
('Advanced Java Streams API', 'Deep dive into Java Streams and functional programming', '<h1>Java Streams API</h1><p>The Stream API is a powerful tool for processing collections of data...</p>', CURRENT_TIMESTAMP - INTERVAL '9' DAY, 1),
('Database Design Principles', 'Best practices for designing scalable databases', '<h1>Database Design</h1><p>Good database design is crucial for application performance...</p>', CURRENT_TIMESTAMP - INTERVAL '8' DAY, 2),
('RESTful API Development', 'Building robust REST APIs with Spring', '<h1>RESTful APIs</h1><p>REST is an architectural style for distributed systems...</p>', CURRENT_TIMESTAMP - INTERVAL '7' DAY, 3),
('Frontend Integration with Thymeleaf', 'Modern web development with Thymeleaf templates', '<h1>Thymeleaf Templates</h1><p>Thymeleaf is a modern server-side Java template engine...</p>', CURRENT_TIMESTAMP - INTERVAL '6' DAY, 4),
('Microservices Architecture Patterns', 'Designing and implementing microservices', '<h1>Microservices</h1><p>Microservices architecture breaks down applications into smaller services...</p>', CURRENT_TIMESTAMP - INTERVAL '5' DAY, 5),
('Spring Security Essentials', 'Securing your Spring Boot applications', '<h1>Spring Security</h1><p>Security is a critical aspect of any web application...</p>', CURRENT_TIMESTAMP - INTERVAL '4' DAY, 6),
('Testing Strategies in Java', 'Unit testing and integration testing best practices', '<h1>Testing in Java</h1><p>Comprehensive testing ensures code quality and reliability...</p>', CURRENT_TIMESTAMP - INTERVAL '3' DAY, 7),
('Docker and Containerization', 'Deploying Java applications with Docker', '<h1>Docker Basics</h1><p>Docker simplifies application deployment through containerization...</p>', CURRENT_TIMESTAMP - INTERVAL '2' DAY, 8),
('Performance Optimization Tips', 'Making your Java applications faster', '<h1>Performance Tuning</h1><p>Application performance is key to user satisfaction...</p>', CURRENT_TIMESTAMP - INTERVAL '1' DAY, 9),
('GraphQL vs REST', 'Comparing modern API approaches', '<h1>GraphQL and REST</h1><p>Choosing the right API architecture for your needs...</p>', CURRENT_TIMESTAMP - INTERVAL '12' HOUR, 10),
('Reactive Programming with Spring WebFlux', 'Building reactive applications', '<h1>Spring WebFlux</h1><p>Reactive programming enables non-blocking, event-driven applications...</p>', CURRENT_TIMESTAMP - INTERVAL '6' HOUR, 11),
('Cloud Native Development', 'Building applications for the cloud', '<h1>Cloud Native</h1><p>Cloud-native applications are designed to thrive in cloud environments...</p>', CURRENT_TIMESTAMP - INTERVAL '3' HOUR, 12),
('Kafka Event Streaming', 'Real-time data processing with Apache Kafka', '<h1>Apache Kafka</h1><p>Kafka is a distributed streaming platform...</p>', CURRENT_TIMESTAMP - INTERVAL '2' HOUR, 13),
('Clean Code Principles', 'Writing maintainable and readable code', '<h1>Clean Code</h1><p>Clean code is easy to understand and easy to change...</p>', CURRENT_TIMESTAMP - INTERVAL '1' HOUR, 14);
