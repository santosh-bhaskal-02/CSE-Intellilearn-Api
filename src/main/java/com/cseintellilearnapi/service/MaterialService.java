package com.cseintellilearnapi.service;

import com.cseintellilearnapi.entity.Material;
import com.cseintellilearnapi.exception.BadRequestException;
import com.cseintellilearnapi.repository.MaterialRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class MaterialService {

    private final MaterialRepository materialRepository;

    public MaterialService(MaterialRepository materialRepository) {
        this.materialRepository = materialRepository;
    }

    @Transactional(readOnly = true)
    public List<Material> getMaterialsByTopic(Long topicId) {
        return materialRepository.findByTopicIdOrderByUploadedAtDesc(topicId);
    }

    public Material addMaterial(Material material) {
        if (material.getTopicId() == null || material.getTitle() == null || material.getTitle().trim().isEmpty()) {
            throw new BadRequestException("Topic and title are required");
        }
        return materialRepository.save(material);
    }
}
