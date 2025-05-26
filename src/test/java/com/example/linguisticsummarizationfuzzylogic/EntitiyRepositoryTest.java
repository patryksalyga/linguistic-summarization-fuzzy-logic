package com.example.linguisticsummarizationfuzzylogic;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class EntitiyRepositoryTest {
    private static EntityRepository entityRepository;
    private static Path tempFile;

    @BeforeAll
    public static void setUp() {
        entityRepository = new EntityRepository();

        try {
            tempFile = Files.createTempFile("test-data", ".csv");
            Files.write(tempFile, List.of(
                    "entityName,enityValue",
                    "Area Type,miasto",
                    "Area Type,wieś",
                    "Area Type,statek",
                    "Area Type,zagranica",
                    "Voivodeship,dolnośląskie",
                    "Voivodeship,kujawsko-pomorskie",
                    "Voivodeship,lubelskie",
                    "Voivodeship,lubuskie",
                    "Voivodeship,łódzkie",
                    "Voivodeship,małopolskie",
                    "Voivodeship,mazowieckie",
                    "Voivodeship,opolskie",
                    "Voivodeship,podkarpackie",
                    "Voivodeship,podlaskie",
                    "Voivodeship,pomorskie",
                    "Voivodeship,śląskie",
                    "Voivodeship,świętokrzyskie",
                    "Voivodeship,warmińsko-mazurskie",
                    "Voivodeship,wielkopolskie",
                    "Voivodeship,zachodniopomorskie"

            ));

            entityRepository.loadFromCSV(tempFile.toString());

        } catch (IOException e) {
            throw new RuntimeException("Błąd przy tworzeniu lub zapisie do tymczasowego pliku CSV", e);
        } catch (Exception e) {
            throw new RuntimeException("Błąd przy ładowaniu CSV do LinguisticRepository", e);
        }
    }

    @Test
    public void testLoadEntities(){
        assertEquals(2, entityRepository.getEntities().size());
        assertEquals(4, entityRepository.getEntities().get(0).getValues().size());
        assertEquals("Area Type", entityRepository.getEntities().get(0).getName());
        assertTrue(entityRepository.getEntities().get(0).getValues().containsKey("miasto"));
        assertTrue(entityRepository.getEntities().get(0).getValues().containsKey("zagranica"));
        assertEquals(16, entityRepository.getEntities().get(1).getValues().size());
        assertEquals("Voivodeship", entityRepository.getEntities().get(1).getName());
        assertTrue(entityRepository.getEntities().get(1).getValues().containsKey("dolnośląskie"));
        assertTrue(entityRepository.getEntities().get(1).getValues().containsKey("zachodniopomorskie"));
    }

    @AfterAll
    public static void tearDown() {
        try {
            Files.deleteIfExists(tempFile);
        } catch (IOException e) {
            throw new RuntimeException("Błąd przy usuwaniu tymczasowych plików CSV", e);
        }
    }
}
