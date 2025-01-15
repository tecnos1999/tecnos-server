package com.example.tecnosserver.infocard.web;

import com.example.tecnosserver.infocard.dto.InfoCardDTO;
import com.example.tecnosserver.infocard.service.InfoCardCommandService;
import com.example.tecnosserver.infocard.service.InfoCardQueryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/server/api/v1/infocard")
@RequiredArgsConstructor
@Slf4j
public class InfoCardControllerApi {

    private final InfoCardCommandService infoCardCommandService;
    private final InfoCardQueryService infoCardQueryService;

    @PostMapping("/create")
    public ResponseEntity<InfoCardDTO> addInfoCard(@RequestBody InfoCardDTO infoCardDTO) {
        log.info("Received request to add a new InfoCard with code: {}", infoCardDTO.code());
        InfoCardDTO savedInfoCardDTO = infoCardCommandService.addInfoCard(infoCardDTO);
        log.info("Successfully added InfoCard with code: {}", savedInfoCardDTO.code());
        return ResponseEntity.ok(savedInfoCardDTO);
    }



    @PutMapping("/update/{code}")
    public ResponseEntity<String> updateInfoCard(@PathVariable String code, @RequestBody InfoCardDTO infoCardDTO) {
        log.info("Received request to update InfoCard with code: {}", code);
        infoCardCommandService.updateInfoCard(code, infoCardDTO);
        log.info("Successfully updated InfoCard with code: {}", code);
        return ResponseEntity.ok("InfoCard with code '" + code + "' has been successfully updated.");
    }


    @DeleteMapping("/delete/{code}")
    public ResponseEntity<String> deleteInfoCard(@PathVariable String code) {
        log.info("Received request to delete InfoCard with code: {}", code);
        infoCardCommandService.deleteInfoCard(code);
        log.info("Successfully deleted InfoCard with code: {}", code);
        return ResponseEntity.ok("InfoCard with code '" + code + "' has been successfully deleted.");
    }


    @GetMapping("/{code}")
    public ResponseEntity<InfoCardDTO> getInfoCardByCode(@PathVariable String code) {
        log.info("Received request to fetch InfoCard with code: {}", code);
        InfoCardDTO infoCardDTO = infoCardQueryService.getInfoCardByCode(code);
        log.info("Successfully fetched InfoCard with code: {}", code);
        return ResponseEntity.ok(infoCardDTO);
    }

    @GetMapping
    public ResponseEntity<List<InfoCardDTO>> getAllInfoCards() {
        log.info("Received request to fetch all InfoCards.");
        List<InfoCardDTO> infoCards = infoCardQueryService.getAllInfoCards();
        log.info("Successfully fetched {} InfoCards.", infoCards.size());
        return ResponseEntity.ok(infoCards);
    }
}
