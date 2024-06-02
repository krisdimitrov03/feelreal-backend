package com.feelreal.api.contoller;

import com.feelreal.api.model.WellnessCheck;
import com.feelreal.api.service.wellnessChecks.WellnessCheckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/wellness-checks")
public class WellnessCheckController {

    private final WellnessCheckService wellnessCheckService;

    @Autowired
    public WellnessCheckController(WellnessCheckService wellnessCheckService) {
        this.wellnessCheckService = wellnessCheckService;
    }

    @GetMapping
    public List<WellnessCheck> getAllWellnessChecks() {
        return wellnessCheckService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<WellnessCheck> getWellnessCheckById(@PathVariable UUID id) {
        WellnessCheck wellnessCheck = wellnessCheckService.findById(id);
        if (wellnessCheck == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(wellnessCheck);
    }

    @PostMapping
    public ResponseEntity<WellnessCheck> createWellnessCheck(@RequestBody WellnessCheck wellnessCheck) {
        WellnessCheck savedWellnessCheck = wellnessCheckService.save(wellnessCheck);
        return ResponseEntity.ok(savedWellnessCheck);
    }

    @PutMapping("/{id}")
    public ResponseEntity<WellnessCheck> updateWellnessCheck(@PathVariable UUID id, @RequestBody WellnessCheck wellnessCheck) {
        WellnessCheck updatedWellnessCheck = wellnessCheckService.update(id, wellnessCheck);
        if (updatedWellnessCheck == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedWellnessCheck);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWellnessCheck(@PathVariable UUID id) {
        wellnessCheckService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
