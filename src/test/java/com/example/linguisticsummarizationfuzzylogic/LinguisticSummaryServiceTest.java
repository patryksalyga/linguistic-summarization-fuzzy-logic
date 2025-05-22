package com.example.linguisticsummarizationfuzzylogic;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class LinguisticSummaryServiceTest {
    private static QuantifiersRepository quantifiersRepository;
    private static Path tempFile1;
    private static Path tempFile2;
    private static LinguisticRepository linguisticRepository;
    private static Path tempFile3;
    private static ElectoralDistricts electoralDistricts;
    private static ExcelDataController excelDataController;

    @BeforeAll
    public static void setUp() {
        linguisticRepository = new LinguisticRepository();
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

            tempFile3 = Files.createTempFile("test-data3", ".csv");
            Files.write(tempFile3, List.of(
                    "featureName,termLabel,functionType,a,b,c,d",
                    "Stopień przygotowania komisji,Niedostateczny,trapezoidal,0.0,0.0,0.95,1.0",
                    "Stopień przygotowania komisji,Odpowiedni,triangular,1.0,1.03,1.06",
                    "Stopień przygotowania komisji,Nadmiarowy,triangular,1.05,1.1,1.15",
                    "Stopień przygotowania komisji,Przesadzony,trapezoidal,1.15,1.2,INF,INF",
                    "Nadmiar kart,Minimalny,trapezoidal,0.0,0.0,0.02,0.03",
                    "Nadmiar kart,Akceptowalny,triangular,0.02,0.05,0.08",
                    "Nadmiar kart,Wysoki,triangular,0.07,0.11,0.15",
                    "Nadmiar kart,Niepokojący,trapezoidal,0.15,0.17,1.0,1.0",
                    "Poparcie dla kandydata A,Marginalne,trapezoidal,0.0,0.0,0.1,0.11",
                    "Poparcie dla kandydata A,Umiarkowane,triangular,0.1,0.2,0.3",
                    "Poparcie dla kandydata A,Silne,triangular,0.3,0.45,0.7",
                    "Poparcie dla kandydata A,Dominujące,trapezoidal,0.6,0.7,1.0,1.0",
                    "Poparcie dla kandydata B,Marginalne,trapezoidal,0.0,0.0,0.1,0.11",
                    "Poparcie dla kandydata B,Umiarkowane,triangular,0.1,0.2,0.3",
                    "Poparcie dla kandydata B,Silne,triangular,0.3,0.45,0.7",
                    "Poparcie dla kandydata B,Dominujące,trapezoidal,0.6,0.7,1.0,1.0"
            ));

            linguisticRepository.loadFromCSV(tempFile3.toString());

            quantifiersRepository.loadFromCSV(tempFile1.toString(), tempFile2.toString());

        } catch (IOException e) {
            throw new RuntimeException("Błąd przy tworzeniu lub zapisie do tymczasowego pliku CSV", e);
        } catch (Exception e) {
            throw new RuntimeException("Błąd przy ładowaniu CSV do LinguisticRepository", e);
        }

        electoralDistricts = new ElectoralDistricts();
        excelDataController = new ExcelDataController(electoralDistricts);
        excelDataController.loadDataFromExcel("src/main/resources/com/example/linguisticsummarizationfuzzylogic/wybory2020.xlsx");
    }

    @Test
    public void testLinguisticSummary() {
        //linguisticRepository.getLinguisticVariables().get(0).getTerms().get(1).toggle();
        linguisticRepository.getLinguisticVariables().get(2).getTerms().get(2).toggle();
        linguisticRepository.getLinguisticVariables().get(3).getTerms().get(2).toggle();

        //quantifiersRepository.getAbsoluteQuantifiers().get(0).toggle();
        //quantifiersRepository.getAbsoluteQuantifiers().get(1).toggle();

        quantifiersRepository.getRelativeQuantifiers().get(1).toggle();
        quantifiersRepository.getRelativeQuantifiers().get(2).toggle();
        quantifiersRepository.getRelativeQuantifiers().get(3).toggle();

        // Test if the linguistic summary is generated correctly
        LinguisticSummaryService linguisticSummaryService = new LinguisticSummaryService(electoralDistricts, linguisticRepository, quantifiersRepository);
        linguisticSummaryService.prepareData();
        linguisticSummaryService.generateZadeh();
        linguisticSummaryService.generateYager();

        for (LinguisticSummary summary : linguisticSummaryService.getLinguisticSummaries()) {
            System.out.println(summary.getText());
        }
    }


    @AfterAll
    public static void tearDown() {
        try {
            Files.deleteIfExists(tempFile1);
            Files.deleteIfExists(tempFile2);
            Files.deleteIfExists(tempFile3);
        } catch (IOException e) {
            throw new RuntimeException("Błąd przy usuwaniu tymczasowych plików CSV", e);
        }
    }
}
