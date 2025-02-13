package br.com.wevertontsousa.spring.service;

import java.util.UUID;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import br.com.wevertontsousa.spring.entity.CategoryJpaEntity;
import br.com.wevertontsousa.spring.error.InvalidResourceException;
import br.com.wevertontsousa.spring.repository.CategoryJpaRepository;

@Service
public class CategoryService {

    private final CategoryJpaRepository categoryJpaRepository;

    public CategoryService(CategoryJpaRepository categoryJpaRepository) {
        this.categoryJpaRepository = categoryJpaRepository;
    }

    public SaveCategoryOutput save(SaveCategoryInput input) {
        UUID parentCategoryId = input.parentCategoryId();
        CategoryJpaEntity parentCategoryJpaEntity = null;
        SaveCategoryOutput parentCategoryOutput = null;

        if (parentCategoryId != null) {
            parentCategoryJpaEntity = categoryJpaRepository
                .findById(parentCategoryId)
                .orElseThrow(() -> new InvalidResourceException("Categoria pai não existe"));

            parentCategoryOutput = new SaveCategoryOutput(
                parentCategoryJpaEntity.getId(),
                parentCategoryJpaEntity.getName(),
                null
            );
        }

        CategoryJpaEntity categoryJpaEntity = new CategoryJpaEntity(
            UUID.randomUUID(),
            input.name(),
            parentCategoryJpaEntity
        );

        try {
            this.categoryJpaRepository.save(categoryJpaEntity);
        } catch (DataIntegrityViolationException e) {
            throw new InvalidResourceException("Categoria já existe");
        }

        return new SaveCategoryOutput(categoryJpaEntity.getId(), categoryJpaEntity.getName(), parentCategoryOutput);
    }
}

record SaveCategoryInput(String name, UUID parentCategoryId) {}
record SaveCategoryOutput(UUID id, String name, SaveCategoryOutput parentCategory) {}
