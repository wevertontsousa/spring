package br.com.wevertontsousa.spring.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import br.com.wevertontsousa.spring.Application;
import br.com.wevertontsousa.spring.error.InvalidResourceException;
import jakarta.transaction.Transactional;

@ActiveProfiles("test")
@SpringBootTest(classes = Application.class)
public class CategoryServiceTest {

    @Autowired
    private CategoryService categoryService;

    @Transactional
    @Test
    public void shouldSaveCategoryWithoutParentCategorySuccessfully() {
        SaveCategoryInput input = new SaveCategoryInput("Categoria 1", null);
        SaveCategoryOutput output = this.categoryService.save(input);
        assertNotNull(output);
        assertDoesNotThrow(() -> UUID.fromString(output.id().toString()));
        assertNull(output.parentCategory());
    }

    @Transactional
    @Test
    public void shouldSaveCategoryWithExistingParentCategorySuccessfully() {
        SaveCategoryInput parentCategoryInput = new SaveCategoryInput("Categoria Pai", null);
        SaveCategoryOutput parentCategoryOutput = this.categoryService.save(parentCategoryInput);
        SaveCategoryInput categoryInput = new SaveCategoryInput("Categoria Filha", parentCategoryOutput.id());
        SaveCategoryOutput categoryOutput = this.categoryService.save(categoryInput);
        assertNotNull(categoryOutput);
        assertEquals("Categoria Pai", categoryOutput.parentCategory().name());
    }

    @Test
    public void shouldThrowExceptionWhenCategoryAlreadyExists() {
        SaveCategoryInput input = new SaveCategoryInput("Categoria 1", null);
        this.categoryService.save(input);
        SaveCategoryInput duplicateInput = new SaveCategoryInput("Categoria 1", null);
        assertEquals(
            assertThrows(
                InvalidResourceException.class,
                () -> this.categoryService.save(duplicateInput)
            ).getMessage(),
            "Categoria já existe"
        );
    }

    @Test
    public void shouldThrowExceptionWhenCategoryNameIsNull() {
        SaveCategoryInput input = new SaveCategoryInput(null, null);
        assertEquals(
            assertThrows(
                InvalidResourceException.class,
                () -> this.categoryService.save(input)
            ).getMessage(),
            "Recurso inválido"
        );
    }

    @Test
    public void shouldThrowExceptionWhenCategoryNameHasLessThanTwoCharacters() {
        SaveCategoryInput input = new SaveCategoryInput("C", null);
        assertEquals(
            assertThrows(
                InvalidResourceException.class,
                () -> this.categoryService.save(input)
            ).getMessage(),
            "Recurso inválido"
        );
    }

    @Test
    public void shouldThrowExceptionWhenCategoryNameHasMoreThanThirtyCharacters() {
        SaveCategoryInput input = new SaveCategoryInput("C".repeat(31), null);
        assertEquals(
            assertThrows(
                InvalidResourceException.class,
                () -> this.categoryService.save(input)
            ).getMessage(),
            "Recurso inválido"
        );
    }

    @Test
    public void shouldThrowExceptionWhenCategoryNameHasInvalidCharacters() {
        SaveCategoryInput input = new SaveCategoryInput("Categoria #", null);
        assertEquals(
            assertThrows(
                InvalidResourceException.class,
                () -> this.categoryService.save(input)
            ).getMessage(),
            "Recurso inválido"
        );
    }

    @Test
    public void shouldThrowExceptionWhenParentCategoryDoesNotExist() {
        SaveCategoryInput input = new SaveCategoryInput("Categoria", UUID.randomUUID());
        assertEquals(
            assertThrows(
                InvalidResourceException.class,
                () -> this.categoryService.save(input)
            ).getMessage(),
            "Categoria pai não existe"
        );
    }

}
