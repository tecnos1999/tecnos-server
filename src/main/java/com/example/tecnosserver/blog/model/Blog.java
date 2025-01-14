package com.example.tecnosserver.blog.model;

import com.example.tecnosserver.caption.model.Caption;
import com.example.tecnosserver.products.model.Product;
import com.example.tecnosserver.series.model.Series;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "blogs")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Blog {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "blog_sequence")
    @SequenceGenerator(name = "blog_sequence", sequenceName = "blog_sequence", allocationSize = 1)
    private Long id;

    @Column(name = "code", nullable = false, unique = true, updatable = false)
    private String code;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", nullable = false, length = 5000)
    private String description;

    @Column(name = "main_photo_url", nullable = false)
    private String mainPhotoUrl;

    @Column(name = "broschure_url")
    private String broschureUrl;

    @Column(name = "view_url")
    private String viewUrl;

    @ManyToOne
    @JoinColumn(name = "series_id")
    private Series series;

    @OneToMany(mappedBy = "blog", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Caption> captions = new ArrayList<>();

    @Column(name = "active", nullable = false)
    private boolean active = false;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JoinTable(
            name = "blog_products",
            joinColumns = @JoinColumn(name = "blog_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    @JsonManagedReference
    private List<Product> products = new ArrayList<>();
    public void addProduct(Product product) {
        this.products.add(product);
        product.getBlogs().add(this);
    }

    public void removeProduct(Product product) {
        this.products.remove(product);
        product.getBlogs().remove(this);
    }


    @PrePersist
    protected void generateCode() {
        if (this.code == null || this.code.isBlank()) {
            this.code = UUID.randomUUID().toString();
        }
    }
}
