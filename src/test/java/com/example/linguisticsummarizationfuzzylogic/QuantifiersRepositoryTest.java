package com.example.linguisticsummarizationfuzzylogic;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class QuantifiersRepositoryTest {
    private static QuantifiersRepository quantifiersRepository;
    private static Path tempFile1;
    private static Path tempFile2;


    @BeforeAll
    public static void setUp() {
        quantifiersRepository = new QuantifiersRepository();

        try {
            tempFile1 = Files.createTempFile("test-data1", ".csv");
            Files.write(tempFile1, List.of(
                    "label,type,a,b,c,d",
                    "Mniej niż 10,trapezoidal,0,0,5,10",
                    "Około 100,triangular,75,100,125",
                    "Ponad 200,trapezoidal,180,200,INF,INF",
                    "Więcej niż 500,trapezoidal,450,500,INF,INF"
            ));

            tempFile2 = Files.createTempFile("test-data2", ".csv");
            Files.write(tempFile2, List.of(
                    "label,type,a,b,c,d",
                    "Prawie żaden,trapezoidal,-INF,-INF,0.05,0.1",
                    "Niewiele,triangular,0.05,0.2,0.35",
                    "Około połowa,triangular,0.3,0.5,0.7",
                    "Większość,triangular,0.6,0.75,0.95",
                    "Prawie wszyscy,trapezoidal,0.9,0.95,INF,INF"
            ));

            quantifiersRepository.loadFromCSV(tempFile1.toString(), tempFile2.toString());

        } catch (IOException e) {
            throw new RuntimeException("Błąd przy tworzeniu lub zapisie do tymczasowego pliku CSV", e);
        } catch (Exception e) {
            throw new RuntimeException("Błąd przy ładowaniu CSV do LinguisticRepository", e);
        }
    }


    @Test
    public void testLoadQuantifiers() {
        // Test if the linguistic terms are loaded correctly
        assertEquals(4, quantifiersRepository.getAbsoluteQuantifiers().size());
        assertEquals(5, quantifiersRepository.getRelativeQuantifiers().size());

        // Test if the first term is loaded correctly
        assertEquals("Mniej niż 10", quantifiersRepository.getAbsoluteQuantifiers().get(0).getLabel());
        assertEquals(0.0, quantifiersRepository.getAbsoluteQuantifiers().get(0).getMembership(10));
        assertEquals(1.0, quantifiersRepository.getAbsoluteQuantifiers().get(0).getMembership(5));

        assertEquals("Prawie żaden", quantifiersRepository.getRelativeQuantifiers().get(0).getLabel());
        assertEquals(0.0, quantifiersRepository.getRelativeQuantifiers().get(0).getMembership(0.1));
        assertEquals(1.0, quantifiersRepository.getRelativeQuantifiers().get(0).getMembership(0.05));

        // Test if the last term of the second variable is loaded correctly
        assertEquals("Więcej niż 500", quantifiersRepository.getAbsoluteQuantifiers().get(3).getLabel());
        assertEquals(0.0, quantifiersRepository.getAbsoluteQuantifiers().get(3).getMembership(450));
        assertEquals(1.0, quantifiersRepository.getAbsoluteQuantifiers().get(3).getMembership(500));

        assertEquals("Prawie wszyscy", quantifiersRepository.getRelativeQuantifiers().get(4).getLabel());
        assertEquals(0.0, quantifiersRepository.getRelativeQuantifiers().get(4).getMembership(0.9));
        assertEquals(1.0, quantifiersRepository.getRelativeQuantifiers().get(4).getMembership(0.95));
    }

    @Test
    public void testToggleQuantifier() {
        // Test if the quantifier can be toggled
        assertFalse(quantifiersRepository.getAbsoluteQuantifiers().get(0).isEnabled());
        quantifiersRepository.getAbsoluteQuantifiers().get(0).toggle();
        assertTrue(quantifiersRepository.getAbsoluteQuantifiers().get(0).isEnabled());
        quantifiersRepository.getAbsoluteQuantifiers().get(0).toggle();
        assertFalse(quantifiersRepository.getAbsoluteQuantifiers().get(0).isEnabled());
    }

    @AfterAll
    public static void tearDown() {
        try {
            if (tempFile1 != null) {
                Files.deleteIfExists(tempFile1);
            }
            if (tempFile2 != null) {
                Files.deleteIfExists(tempFile2);
            }
        } catch (IOException e) {
            System.err.println("Błąd przy usuwaniu pliku tymczasowego: " + e.getMessage());
        }
    }
}
