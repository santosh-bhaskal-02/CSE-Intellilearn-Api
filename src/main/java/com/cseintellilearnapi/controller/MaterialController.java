package com.cseintellilearnapi.controller;

import com.cseintellilearnapi.entity.Material;
import com.cseintellilearnapi.service.MaterialService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/materials")
public class MaterialController {

    private final MaterialService materialService;

    public MaterialController(MaterialService materialService) {
        this.materialService = materialService;
    }

    @GetMapping("/topic/{topic_id}")
    public ResponseEntity<List<Material>> getMaterialsByTopic(@PathVariable("topic_id") Long topicId) {
        return ResponseEntity.ok(materialService.getMaterialsByTopic(topicId));
    }

    @PostMapping
    public ResponseEntity<?> addMaterial(@RequestBody Material material) {
        Material saved = materialService.addMaterial(material);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "success", true,
                "message", "Material added successfully",
                "material_id", saved.getMaterialId()
        ));
    }
}
