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
                    "Ponad 200,trapezoidal,180,200,30000,30000",
                    "Więcej niż 500,trapezoidal,450,500,30000,30000"
            ));

            tempFile2 = Files.createTempFile("test-data2", ".csv");
            Files.write(tempFile2, List.of(
                    "label,type,a,b,c,d",
                    "Prawie żaden,trapezoidal,0.0, 0.0, 0.05,0.1",
                    "Niewiele,triangular,0.05,0.2,0.35",
                    "Około połowa,triangular,0.3,0.5,0.7",
                    "Większość,triangular,0.6,0.75,0.95",
                    "Prawie wszyscy,trapezoidal,0.9,0.95,1,1"
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
                    "Frekwencja wyborcza,Niska,trapezoidal,0.0,0.0,0.4,0.41",
                    "Frekwencja wyborcza,Przeciętna,triangular,0.4,0.5,0.61",
                    "Frekwencja wyborcza,Wysoka,triangular,0.6,0.7,0.8",
                    "Frekwencja wyborcza,Rekordowa,trapezoidal,0.8,0.85,1,1",
                    "Mobilność wyborcza,Marginalna,trapezoidal,0.0,0.0,0.01,0.011",
                    "Mobilność wyborcza,Zauważalna,triangular,0.01,0.02,0.031",
                    "Mobilność wyborcza,Znaczna,triangular,0.03,0.045,0.06",
                    "Mobilność wyborcza,Wyjątkowo wysoka,trapezoidal,0.06,0.07,1.0,1.0",
                    "Zgodność urny z wydaniami kart,Niska,trapezoidal,0.0,0.0,0.93,0.95",
                    "Zgodność urny z wydaniami kart,Zgodna,triangular,0.95,0.985,1.02",
                    "Zgodność urny z wydaniami kart,Wyższa niż oczekiwano,triangular,1.02,1.035,1.05",
                    "Zgodność urny z wydaniami kart,Podejrzana,trapezoidal,1.05,1.1,INF,INF",
                    "Udział głosów korespondencyjnych,Sporadyczny,trapezoidal,0.0,0.0,0.01,0.011",
                    "Udział głosów korespondencyjnych,Umiarkowany,triangular,0.01,0.03,0.051",
                    "Udział głosów korespondencyjnych,Znaczny,triangular,0.05,0.125,0.20",
                    "Udział głosów korespondencyjnych,Dominujący,trapezoidal,0.2,0.25,1.0,1.0",
                    "Skala nieważnych kart,Minimalna,trapezoidal,0.0,0.0,0.005,0.006",
                    "Skala nieważnych kart,Typowa,triangular,0.005,0.01,0.015",
                    "Skala nieważnych kart,Podwyższona,triangular,0.015,0.0225,0.03",
                    "Skala nieważnych kart,Alarmująca,trapezoidal,0.03,0.04,1.0,1.0",
                    "Skuteczność głosowania,Niska,trapezoidal,0.0,0.0,0.965,0.97",
                    "Skuteczność głosowania,Dobra,triangular,0.97,0.978,0.985",
                    "Skuteczność głosowania,Wysoka,triangular,0.985,0.99,0.995",
                    "Skuteczność głosowania,Bliska idealnej,trapezoidal,0.995,1.0,1.0,1.0",
                    "Liczba głosujących przez pełnomocnika,Brak,trapezoidal,0,0,1,3",
                    "Liczba głosujących przez pełnomocnika,Niewielu,triangular,1,5,10",
                    "Liczba głosujących przez pełnomocnika,Liczni,triangular,8,15,25",
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
//        linguisticRepository.getLinguisticVariables().get(2).getTerms().get(3).toggle();
//        linguisticRepository.getLinguisticVariables().get(9).getTerms().get(2).toggle();
//        linguisticRepository.getLinguisticVariables().get(10).getTerms().get(2).toggle();

        quantifiersRepository.getAbsoluteQuantifiers().get(0).toggle();
        quantifiersRepository.getAbsoluteQuantifiers().get(1).toggle();
        //quantifiersRepository.getAbsoluteQuantifiers().get(3).toggle();

        quantifiersRepository.getRelativeQuantifiers().get(0).toggle();
        quantifiersRepository.getRelativeQuantifiers().get(1).toggle();
        quantifiersRepository.getRelativeQuantifiers().get(2).toggle();
        quantifiersRepository.getRelativeQuantifiers().get(3).toggle();
        quantifiersRepository.getRelativeQuantifiers().get(4).toggle();

        // Test if the linguistic summary is generated correctly
        LinguisticSummaryService linguisticSummaryService = new LinguisticSummaryService(electoralDistricts, linguisticRepository, quantifiersRepository);
        linguisticSummaryService.prepareData();
        linguisticSummaryService.generateZadeh();
        //linguisticSummaryService.generateYager();

        for (LinguisticSummary summary : linguisticSummaryService.getLinguisticSummaries()) {
            if (summary.getSummaryQualityEvaluator().getOverallQuality() > 0.85)
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
