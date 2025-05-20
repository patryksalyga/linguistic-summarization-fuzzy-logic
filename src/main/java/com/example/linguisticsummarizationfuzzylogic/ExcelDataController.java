package com.example.linguisticsummarizationfuzzylogic;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.File;
import java.io.FileInputStream;
import java.util.List;
import java.util.Objects;

public class ExcelDataController {
    // This class is responsible for loading data from an Excel file
    // and providing it to the application.
    private ElectoralDistricts electoralDistricts;

    // Constructor
    public ExcelDataController(ElectoralDistricts electoralDistricts) {
        this.electoralDistricts = electoralDistricts;
    }

    // Method to load data from an Excel file
    public void loadDataFromExcel(String filePath) {
        try (FileInputStream fis = new FileInputStream(new File(filePath));
             Workbook workbook = new XSSFWorkbook(fis)) {
            Sheet sheet = workbook.getSheetAt(0); // Assuming data is in the first sheet
            Row headerRow = sheet.getRow(0); // First row as headers

            for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
                Row row = sheet.getRow(rowIndex);
                if (row == null) continue; // Skip empty
                if(Objects.isNull(row.getCell(10))) continue;
                String areaTypeRaw = row.getCell(5).getStringCellValue();
                String areaType = areaTypeRaw.equals("dzielnica w m.st. Warszawa") ? "miasto" : areaTypeRaw;

                String voivodeship;
                if (areaTypeRaw.equals("zagranica")) {
                    voivodeship = "zagranica";
                } else if (areaTypeRaw.equals("statek")) {
                    voivodeship = "statek";
                } else {
                    voivodeship = row.getCell(10).getStringCellValue();
                }

                double commissionPreparationLevel = safeDivide(row.getCell(11).getNumericCellValue(), row.getCell(12).getNumericCellValue(), Double.POSITIVE_INFINITY);
                double surplusBallots = safeDivide(row.getCell(13).getNumericCellValue(), row.getCell(11).getNumericCellValue(), 0.0);
                double voterTurnout = safeDivide(row.getCell(14).getNumericCellValue(), row.getCell(12).getNumericCellValue(), 0.0);
                double voterMobilization = safeDivide(row.getCell(16).getNumericCellValue(), row.getCell(14).getNumericCellValue(), 0.0);
                double ballotBoxConsistency = safeDivide(row.getCell(24).getNumericCellValue(), row.getCell(14).getNumericCellValue(), 0.0);
                double postalVoteShare = safeDivide(row.getCell(25).getNumericCellValue(), row.getCell(24).getNumericCellValue(), 0.0);
                double invalidBallotsRate = safeDivide(row.getCell(26).getNumericCellValue(), row.getCell(24).getNumericCellValue(), 0.0);
                double votingEffectiveness = safeDivide(row.getCell(27).getNumericCellValue(), row.getCell(24).getNumericCellValue(), 0.0);
                int proxyVotersCount = (int) row.getCell(15).getNumericCellValue();
                double candidateASupport = safeDivide(row.getCell(35).getNumericCellValue(), row.getCell(32).getNumericCellValue(), 0.0);
                double candidateBSupport = safeDivide(row.getCell(41).getNumericCellValue(), row.getCell(32).getNumericCellValue(), 0.0);

                electoralDistricts.addDistrict(new ElectoralDistrict(
                        areaType,
                        voivodeship,
                        commissionPreparationLevel,
                        surplusBallots,
                        voterTurnout,
                        voterMobilization,
                        ballotBoxConsistency,
                        postalVoteShare,
                        invalidBallotsRate,
                        votingEffectiveness,
                        proxyVotersCount,
                        candidateASupport,
                        candidateBSupport
                ));

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private double safeDivide(double numerator, double denominator, double defaultValue) {
        if (denominator == 0) {
            return defaultValue;
        }
        return numerator / denominator;
    }
}
