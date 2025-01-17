package com.example.tecnosserver.products.model;

import com.example.tecnosserver.blog.model.Blog;
import com.example.tecnosserver.category.model.Category;
import com.example.tecnosserver.itemcategory.model.ItemCategory;
import com.example.tecnosserver.page.model.Page;
import com.example.tecnosserver.partners.model.Partner;
import com.example.tecnosserver.productimage.model.ProductImage;
import com.example.tecnosserver.tags.model.Tag;
import com.example.tecnosserver.subcategory.model.SubCategory;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@ToString
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_sequence")
    @SequenceGenerator(name = "product_sequence", sequenceName = "product_sequence", allocationSize = 1)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "sku", nullable = false, unique = true)
    private String sku;

    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(name = "broschure")
    private String broschure;

    @Column(name = "tehnic")
    private String tehnic;

    @Column(name = "link_video")
    private String linkVideo;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne(optional = false)
    @JoinColumn(name = "category_id", nullable = false)
    @JsonBackReference
    private Category category;

    @ManyToOne(optional = false)
    @JoinColumn(name = "subcategory_id", nullable = false)
    @JsonBackReference
    private SubCategory subCategory;

    @ManyToOne
    @JoinColumn(name = "item_category_id")
    @JsonBackReference
    private ItemCategory itemCategory;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductImage> images = new ArrayList<>();
    public void addImage(String imageUrl) {
        if (this.images == null) {
            this.images = new ArrayList<>();
        }
        ProductImage image = new ProductImage();
        image.setImageUrl(imageUrl);
        image.setProduct(this);
        this.images.add(image);
    }


    public void addImages(List<String> imageUrls) {
        imageUrls.forEach(this::addImage);
    }

    public void removeImages(List<String> imagesToRemove) {
        this.images.removeIf(image -> imagesToRemove.contains(image.getImageUrl()));
    }


    @ManyToOne
    @JoinColumn(name = "partner_id")
    private Partner partner;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JoinTable(
            name = "product_tags",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private List<Tag> tags = new ArrayList<>();


    @ManyToMany(mappedBy = "products", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JsonBackReference
    private List<Blog> blogs = new ArrayList<>();

    @ManyToMany(mappedBy = "products", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JsonBackReference
    private List<Page> pages = new ArrayList<>();

}
