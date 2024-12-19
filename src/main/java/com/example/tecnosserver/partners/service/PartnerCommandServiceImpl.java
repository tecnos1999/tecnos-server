package com.example.tecnosserver.partners.service;

import com.example.tecnosserver.exceptions.exception.AlreadyExistsException;
import com.example.tecnosserver.exceptions.exception.AppException;
import com.example.tecnosserver.exceptions.exception.NotFoundException;
import com.example.tecnosserver.intercom.CloudAdapter;
import com.example.tecnosserver.partners.dto.PartnerDTO;
import com.example.tecnosserver.partners.mapper.PartnerMapper;
import com.example.tecnosserver.partners.model.Partner;
import com.example.tecnosserver.partners.repo.PartnerRepo;
import com.example.tecnosserver.image.dto.ImageDTO;
import com.example.tecnosserver.image.mapper.ImageMapper;
import com.example.tecnosserver.products.model.Product;
import com.example.tecnosserver.products.repo.ProductRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class PartnerCommandServiceImpl implements PartnerCommandService {

    private final ProductRepo productRepository;
    private final PartnerRepo partnerRepository;
    private final PartnerMapper partnerMapper;
    private final CloudAdapter cloudAdapter;

    @Override
    public void addPartner(PartnerDTO partnerDTO, MultipartFile image, MultipartFile catalogFile) {
        validatePartnerDTO(partnerDTO);

        if (partnerRepository.findByName(partnerDTO.name()).isPresent()) {
            throw new AlreadyExistsException("Partner with name '" + partnerDTO.name() + "' already exists.");
        }

        String imageUrl = uploadFile(image, "Failed to upload image: ");
        String catalogFileUrl = uploadFile(catalogFile, "Failed to upload catalog file: ");

        Partner partner = partnerMapper.fromDTO(partnerDTO);
        if (imageUrl != null) {
            partner.setImage(ImageMapper.mapDTOToImage(new ImageDTO(imageUrl, image.getContentType())));
        }
        partner.setCatalogFile(catalogFileUrl);

        partnerRepository.save(partner);
    }

    @Override
    public void deletePartner(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new AppException("Partner name cannot be null or empty.");
        }

        Partner partner = partnerRepository.findByName(name.trim())
                .orElseThrow(() -> new NotFoundException("Partner with name '" + name + "' not found."));

        Optional<List<Product>> products = productRepository.findAllByPartnerName(partner.getName());
        if (products.isPresent()){
            for (Product product : products.get()) {
                product.setPartner(null);
            }
            productRepository.saveAll(products.get());
        }
        safeDeleteFile(partner.getImage() != null ? partner.getImage().getUrl() : null, "Failed to delete associated image: ");
        safeDeleteFile(partner.getCatalogFile(), "Failed to delete associated catalog file: ");
        partnerRepository.delete(partner);
    }



    @Override
    public void updatePartner(String name, PartnerDTO partnerDTO, MultipartFile image, MultipartFile catalogFile) {
        validatePartnerDTO(partnerDTO);

        Partner partner = partnerRepository.findByName(name.trim())
                .orElseThrow(() -> new NotFoundException("Partner with name '" + name + "' not found."));

        if (image != null && !image.isEmpty()) {
            safeDeleteFile(partner.getImage() != null ? partner.getImage().getUrl() : null, "Failed to delete old image: ");
            String imageUrl = uploadFile(image, "Failed to upload new image: ");
            partner.setImage(ImageMapper.mapDTOToImage(new ImageDTO(imageUrl, image.getContentType())));
        }

        if (catalogFile != null && !catalogFile.isEmpty()) {
            safeDeleteFile(partner.getCatalogFile(), "Failed to delete old catalog file: ");
            String catalogFileUrl = uploadFile(catalogFile, "Failed to upload new catalog file: ");
            partner.setCatalogFile(catalogFileUrl);
        }

        partner.setDescription(partnerDTO.description());

        partnerRepository.save(partner);
    }



    private void validatePartnerDTO(PartnerDTO partnerDTO) {
        if (partnerDTO == null) {
            throw new AppException("Partner data cannot be null.");
        }
        if (partnerDTO.name() == null || partnerDTO.name().trim().isEmpty()) {
            throw new AppException("Partner name cannot be null or empty.");
        }
        if (partnerDTO.description() == null || partnerDTO.description().trim().isEmpty()) {
            throw new AppException("Description cannot be null or empty.");
        }
    }

    private String uploadFile(MultipartFile file, String errorMessage) {
        if (file != null && !file.isEmpty()) {
            try {
                return cloudAdapter.uploadFile(file);
            } catch (Exception e) {
                throw new AppException(errorMessage + e.getMessage());
            }
        }
        return null;
    }

    private void safeDeleteFile(String fileUrl, String errorMessage) {
        if (fileUrl != null && !fileUrl.isBlank()) {
            try {
                cloudAdapter.deleteFile(fileUrl);
            } catch (Exception e) {
                System.err.println(errorMessage);
            }
        }
    }

}
