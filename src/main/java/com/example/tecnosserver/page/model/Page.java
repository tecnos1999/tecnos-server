package com.example.tecnosserver.page.model;

import com.example.tecnosserver.products.model.Product;
import com.example.tecnosserver.sections.model.Section;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "pages")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Page {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "page_sequence")
    @SequenceGenerator(name = "page_sequence", sequenceName = "page_sequence", allocationSize = 1)
    private Long id;

    @Column(name = "slug", nullable = false, unique = true)
    private String slug;

    @Column(name = "title", nullable = false , unique = true)
    private String title;

    @Column(name = "subtitle")
    private String subtitle;

    @Column(name="content", columnDefinition = "TEXT")
    private String content;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name="link")
    private String link;

    @Column(name="document_url")
    private String documentUrl;

    @OneToMany(mappedBy = "page", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Section> sections;
    public void removeSection(Section section) {
        sections.remove(section);
        section.setPage(null);
    }

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_page_id")
    private List<Page> subPages;
    public void addSubPage(Page subPage) {
        if (this.subPages == null) {
            this.subPages = new ArrayList<>();
        }
        this.subPages.add(subPage);
    }

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JoinTable(
            name = "page_products",
            joinColumns = @JoinColumn(name = "page_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    @JsonManagedReference
    private List<Product> products = new ArrayList<>();
    public void addProduct(Product product) {
        this.products.add(product);
        product.getPages().add(this);
    }

    public void removeProduct(Product product) {
        this.products.remove(product);
        product.getPages().remove(this);
    }

    public void clearProducts() {
        for (Product product : new ArrayList<>(products)) {
            removeProduct(product);
        }
    }

    @PrePersist
    public void prePersist() {
        if (slug == null) {
            slug = title.toLowerCase().replace(" ", "-");
        }
    }

}

