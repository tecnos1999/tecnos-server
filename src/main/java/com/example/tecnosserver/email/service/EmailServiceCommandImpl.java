package com.example.tecnosserver.email.service;

import com.example.tecnosserver.email.model.Email;
import com.example.tecnosserver.utils.AdvancedStructuredLogger;
import com.example.tecnosserver.products.model.Product;
import com.example.tecnosserver.products.repo.ProductRepo;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class EmailServiceCommandImpl implements EmailServiceCommand {

    private final JavaMailSender mailSender;
    private final ProductRepo productRepo;


    private final AdvancedStructuredLogger advancedStructuredLogger;


    public EmailServiceCommandImpl(JavaMailSender mailSender, ProductRepo productRepo, AdvancedStructuredLogger advancedStructuredLogger) {
        this.mailSender = mailSender;
        this.productRepo = productRepo;
        this.advancedStructuredLogger = advancedStructuredLogger;
    }

    @Override
    public void sendEmail(Email email) {
        AdvancedStructuredLogger.ensureCorrelationId();

        advancedStructuredLogger.logBuilder()
                .withEventType("EMAIL_SEND_START")
                .withMessage("Start sending email")
                .withField("customerName", email.getFullName())
                .withField("customerEmail", email.getEmail())
                .withLevel("INFO")
                .log();
        List<Product> products = productRepo.findBySkuIn(email.getProducts());
        if (products.isEmpty()) {
            advancedStructuredLogger.logBuilder()
                    .withEventType("EMAIL_PRODUCTS_NOT_FOUND")
                    .withMessage("No products found for SKUs")
                    .withField("SKUs", email.getProducts())
                    .withLevel("WARN")
                    .log();
        } else {
            advancedStructuredLogger.logBuilder()
                    .withEventType("EMAIL_PRODUCTS_FOUND")
                    .withMessage("Products retrieved successfully")
                    .withField("SKUs", email.getProducts())
                    .withField("productCount", products.size())
                    .withLevel("INFO")
                    .log();
        }
        MimeMessagePreparator preparator = mimeMessage -> {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

            helper.setFrom("pathpilot116@gmail.com");
            helper.setTo("floredenis907@gmail.com");
            helper.setSubject("Cerere de ofertă - " + email.getFullName());
            String emailContent = buildEmailContent(email, products);
            helper.setText(emailContent, true);
        };
        advancedStructuredLogger.logBuilder()
                .withEventType("EMAIL_SEND_ATTEMPT")
                .withMessage("Attempting to send the email")
                .withField("recipient", "floredenis907@gmail.com")
                .withLevel("INFO")
                .log();
        mailSender.send(preparator);
        advancedStructuredLogger.logBuilder()
                .withEventType("EMAIL_SEND_SUCCESS")
                .withMessage("Email sent successfully")
                .withField("customerName", email.getFullName())
                .withField("recipient", "floredenis907@gmail.com")
                .withLevel("INFO")
                .log();
    }

    private String buildEmailContent(Email email, List<Product> products) {
        advancedStructuredLogger.logBuilder()
                .withEventType("EMAIL_CONTENT_BUILD")
                .withMessage("Building HTML content for email")
                .withField("customerName", email.getFullName())
                .withField("numberOfProducts", products.size())
                .withLevel("DEBUG")
                .log();
        StringBuilder content = new StringBuilder();

        content.append("<!DOCTYPE html>")
                .append("<html lang='en'>")
                .append("<head>")
                .append("<meta charset='UTF-8'>")
                .append("<meta name='viewport' content='width=device-width, initial-scale=1.0'>")
                .append("<style>")
                .append("body { font-family: Arial, sans-serif; line-height: 1.6; background-color: #f9f9f9; color: #333; margin: 0; padding: 0; }")
                .append(".container { max-width: 600px; margin: 20px auto; background: #fff; border-radius: 10px; overflow: hidden; box-shadow: 0 4px 8px rgba(0,0,0,0.1); }")
                .append(".header { background: #e63946; color: #fff; padding: 20px; text-align: center; }")
                .append(".header h1 { margin: 0; font-size: 24px; }")
                .append(".content { padding: 20px; }")
                .append(".content h2 { color: #e63946; margin-bottom: 10px; }")
                .append(".product-card { border: 1px solid #ddd; margin-bottom: 10px; border-radius: 5px; overflow: hidden; display: flex; }")
                .append(".product-card img { max-width: 120px; object-fit: cover; }")
                .append(".product-card-details { padding: 10px; }")
                .append(".footer { text-align: center; font-size: 12px; color: #666; padding: 20px; }")
                .append("</style>")
                .append("</head>")
                .append("<body>")
                .append("<div class='container'>")
                .append("<div class='header'>")
                .append("<h1>Cerere de ofertă</h1>")
                .append("</div>")
                .append("<div class='content'>")
                .append("<h2>Informații client</h2>")
                .append("<p><strong>Nume:</strong> ").append(email.getFullName()).append("</p>")
                .append("<p><strong>Companie:</strong> ").append(email.getCompany()).append("</p>")
                .append("<p><strong>Email:</strong> ").append(email.getEmail()).append("</p>")
                .append("<p><strong>Telefon:</strong> ").append(email.getPhone()).append("</p>")
                .append("<p><strong>Județ:</strong> ").append(email.getJudet()).append("</p>")
                .append("<p><strong>Mesaj:</strong> ").append(email.getMessage()).append("</p>");

        for (Product product : products) {
            content.append("<div class='product-card'>")
                    .append("<img src='").append(product.getImages().get(0).getImageUrl()).append("' alt='").append(product.getName()).append("' />")
                    .append("<div class='product-card-details'>")
                    .append("<h3>").append(product.getName()).append("</h3>")
                    .append("<p><strong>SKU:</strong> ").append(product.getSku()).append("</p>")
                    .append("</div>")
                    .append("</div>");
        }

        content.append("</div>")
                .append("<div class='footer'>")
                .append("<p>Acesta este un email generat automat. Vă mulțumim!</p>")
                .append("</div>")
                .append("</div>")
                .append("</body>")
                .append("</html>");

        return content.toString();
    }

}
