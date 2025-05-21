package com.example.linguisticsummarizationfuzzylogic;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LinguisticRepositoryTest {
    private static LinguisticRepository linguisticRepository;
    private static Path tempFile;


    @BeforeAll
    public static void setUp() {
        linguisticRepository = new LinguisticRepository();

        try {
            tempFile = Files.createTempFile("test-data", ".csv");
            Files.write(tempFile, List.of(
                    "featureName,termLabel,functionType,a,b,c,d",
                    "Stopień przygotowania komisji,Niedostateczny,trapezoidal,0.0,0.0,0.95,1.0",
                    "Stopień przygotowania komisji,Odpowiedni,triangular,1.0,1.03,1.06",
                    "Stopień przygotowania komisji,Nadmiarowy,triangular,1.05,1.1,1.15",
                    "Stopień przygotowania komisji,Przesadzony,trapezoidal,1.15,1.2,INF,INF",
                    "Nadmiar kart,Minimalny,trapezoidal,0.0,0.0,0.02,0.03",
                    "Nadmiar kart,Akceptowalny,triangular,0.02,0.05,0.08",
                    "Nadmiar kart,Wysoki,triangular,0.07,0.11,0.15",
                    "Nadmiar kart,Niepokojący,trapezoidal,0.15,0.17,1.0,1.0"
            ));

            linguisticRepository.loadFromCSV(tempFile.toString());

        } catch (IOException e) {
            throw new RuntimeException("Błąd przy tworzeniu lub zapisie do tymczasowego pliku CSV", e);
        } catch (Exception e) {
            throw new RuntimeException("Błąd przy ładowaniu CSV do LinguisticRepository", e);
        }
    }


    @Test
    public void testLoadLinguisticTerms() {
        // Test if the linguistic terms are loaded correctly
        assertEquals(2, linguisticRepository.getLinguisticVariables().size());

        // Test if the first term is loaded correctly
        assertEquals(4, linguisticRepository.getLinguisticVariables().get(0).getTerms().size());
        assertEquals("Stopień przygotowania komisji", linguisticRepository.getLinguisticVariables().get(0).getName());

        // Test if the second term is loaded correctly
        assertEquals(4, linguisticRepository.getLinguisticVariables().get(1).getTerms().size());
        assertEquals("Nadmiar kart", linguisticRepository.getLinguisticVariables().get(1).getName());

        // Test if the first term of the first variable is loaded correctly
        assertEquals("Niedostateczny", linguisticRepository.getLinguisticVariables().get(0).getTerms().get(0).getLabel());
        assertEquals(1.0, linguisticRepository.getLinguisticVariables().get(0).getTerms().get(0).getMembershipFunction().getMembership(0.0), 0.0001);
        assertEquals(1.0, linguisticRepository.getLinguisticVariables().get(0).getTerms().get(0).getMembershipFunction().getMembership(0.95), 0.0001);
        assertEquals(0.0, linguisticRepository.getLinguisticVariables().get(0).getTerms().get(0).getMembershipFunction().getMembership(1.0), 0.0001);

        // Test if the last term of the second variable is loaded correctly
        assertEquals("Niepokojący", linguisticRepository.getLinguisticVariables().get(1).getTerms().get(3).getLabel());
        assertEquals(0.0, linguisticRepository.getLinguisticVariables().get(1).getTerms().get(3).getMembershipFunction().getMembership(0.15), 0.0001);
        assertEquals(1.0, linguisticRepository.getLinguisticVariables().get(1).getTerms().get(3).getMembershipFunction().getMembership(0.17), 0.0001);
        assertEquals(1.0, linguisticRepository.getLinguisticVariables().get(1).getTerms().get(3).getMembershipFunction().getMembership(1.0), 0.0001);
    }

    @AfterAll
    public static void tearDown() {
        try {
            if (tempFile != null) {
                Files.deleteIfExists(tempFile);
            }
        } catch (IOException e) {
            System.err.println("Błąd przy usuwaniu pliku tymczasowego: " + e.getMessage());
        }
    }
}
