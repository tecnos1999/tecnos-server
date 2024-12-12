package com.example.tecnosserver.partners.web;

import com.example.tecnosserver.partners.dto.PartnerDTO;
import com.example.tecnosserver.partners.service.PartnerCommandService;
import com.example.tecnosserver.partners.service.PartnerQueryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/server/api/v1/partners")
@RequiredArgsConstructor
public class PartnerControllerApi {

    private final PartnerCommandService partnerCommandService;
    private final PartnerQueryService partnerQueryService;

    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> addPartner(
            @RequestPart("partner") @Valid PartnerDTO partnerDTO,
            @RequestPart(value = "image", required = false) MultipartFile image,
            @RequestPart(value = "catalogFile", required = false) MultipartFile catalogFile
    ) {
        partnerCommandService.addPartner(partnerDTO, image, catalogFile);
        return ResponseEntity.status(HttpStatus.CREATED).body("Partner added successfully.");
    }

    @PutMapping(value = "/update/{name}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> updatePartner(
            @PathVariable String name,
            @RequestPart("partner") @Valid PartnerDTO partnerDTO,
            @RequestPart(value = "image", required = false) MultipartFile image,
            @RequestPart(value = "catalogFile", required = false) MultipartFile catalogFile
    ) {
        partnerCommandService.updatePartner(name, partnerDTO, image, catalogFile);
        return ResponseEntity.ok("Partner updated successfully.");
    }

    @DeleteMapping("/delete/{name}")
    public ResponseEntity<String> deletePartner(@PathVariable String name) {
        partnerCommandService.deletePartner(name);
        return ResponseEntity.ok("Partner deleted successfully.");
    }

    @GetMapping("/{name}")
    public ResponseEntity<PartnerDTO> getPartnerByName(@PathVariable String name) {
        return partnerQueryService.findPartnerByName(name)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping
    public ResponseEntity<Optional<List<PartnerDTO>>> getAllPartners() {
        Optional<List<PartnerDTO>> partners = partnerQueryService.findAllPartners();
        return ResponseEntity.ok(partners);
    }
}
