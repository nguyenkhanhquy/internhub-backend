package com.internhub.backend.util;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.springframework.web.multipart.MultipartFile;

public final class ExcelUtils {

    private ExcelUtils() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static boolean isNotExcelFile(MultipartFile file) {
        return file == null ||
                !"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet".equalsIgnoreCase(file.getContentType());
    }

    public static String getCellValueAsString(Cell cell) {
        if (cell == null) return "";
        return switch (cell.getCellType()) {
            case CellType.STRING -> cell.getStringCellValue().trim();
            case CellType.NUMERIC -> String.valueOf((int) cell.getNumericCellValue());
            case CellType.BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
            default -> "";
        };
    }

    public static Double getCellValueAsDouble(Cell cell) {
        if (cell == null || cell.getCellType() != CellType.NUMERIC) {
            return 0.0;
        }
        return cell.getNumericCellValue();
    }
}
