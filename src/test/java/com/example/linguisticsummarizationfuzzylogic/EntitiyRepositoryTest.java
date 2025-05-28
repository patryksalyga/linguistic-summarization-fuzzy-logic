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
    private static ElectoralDistricts electoralDistricts;
    private static ExcelDataController excelDataController;

    @BeforeAll
    public static void setUp() {
        entityRepository = new EntityRepository();
        electoralDistricts = new ElectoralDistricts();
        excelDataController = new ExcelDataController(electoralDistricts);
        excelDataController.loadDataFromExcel("src/main/resources/com/example/linguisticsummarizationfuzzylogic/wybory2020.xlsx");

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

            entityRepository.loadFromCSV(tempFile.toString(), electoralDistricts);

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

        int sum = 0;
        for (EntityValue value : entityRepository.getEntities().get(0).getValues()) {
            sum += value.getElectoralDistricts().size();
        }
        assertEquals(electoralDistricts.getDistricts().size(), sum);

        assertEquals(16, entityRepository.getEntities().get(1).getValues().size());
        assertEquals("Voivodeship", entityRepository.getEntities().get(1).getName());

        sum = 0;
        for (EntityValue value : entityRepository.getEntities().get(1).getValues()) {
            sum += value.getElectoralDistricts().size();
        }
        assertEquals(electoralDistricts.getDistricts().size() - entityRepository.getEntities().get(0).getValues().get(2).getElectoralDistricts().size() - entityRepository.getEntities().get(0).getValues().get(3).getElectoralDistricts().size(), sum);

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
