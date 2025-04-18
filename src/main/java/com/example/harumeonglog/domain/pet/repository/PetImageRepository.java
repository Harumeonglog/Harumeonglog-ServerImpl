package com.example.harumeonglog.domain.pet.repository;

import com.example.harumeonglog.domain.pet.entity.PetImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PetImageRepository extends JpaRepository<PetImage, Long> {
}
