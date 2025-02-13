package br.com.wevertontsousa.spring.entity;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Table(name = "categories")
@Entity
public class CategoryJpaEntity {

    @Setter(AccessLevel.NONE) // Não criará um setter
    @Id
    private UUID id;

    @Column(nullable = false, unique = true)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_category_id")
    private CategoryJpaEntity parentCategoryJpaEntity;

    protected CategoryJpaEntity() {}

    public CategoryJpaEntity(UUID id, String name, CategoryJpaEntity parentCategoryJpaEntity) {
        this.id = BusinessRuleValidator.validateNotNull(id);
        this.name = this.validateName(name);
        this.parentCategoryJpaEntity = parentCategoryJpaEntity;
    }

    private String validateName(String name) {
        return BusinessRuleValidator.validateString(name, 2, 30, "[^\\d\\p{L} ]");
    }

}
