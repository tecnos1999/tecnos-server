package com.example.tecnosserver.partners.web;

import com.example.tecnosserver.partners.dto.PartnerDTO;
import com.example.tecnosserver.partners.service.PartnerCommandService;
import com.example.tecnosserver.partners.service.PartnerQueryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/server/api/v1/partners")
@RequiredArgsConstructor
public class PartnerControllerApi {

    private final PartnerCommandService partnerCommandService;
    private final PartnerQueryService partnerQueryService;

    @PostMapping
    public ResponseEntity<String> addPartner(@Valid @RequestBody PartnerDTO partnerDTO) {
        partnerCommandService.addPartner(partnerDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body("Partner added successfully.");
    }

    @DeleteMapping("/{name}")
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
