package yzf.java.excel;

import yzf.java.excel.annotation.ExportField;
import yzf.java.excel.exception.ExportException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yangzefeng on 2016/11/19
 */
class ExcelFactory {
    private static final Logger log = LoggerFactory.getLogger(ExcelFactory.class);

    <T> void createExcel(List<T> data,
                         Field[] fields,
                         ExportClient exportClient) {
        SXSSFWorkbook wb = new SXSSFWorkbook(exportClient.getRowAccessWindowSize());
        Sheet sheet = wb.createSheet();
        Row title = sheet.createRow(0);
        for (int i = 0; i < fields.length; i++) {
            Cell cell = title.createCell(i);
            cell.setCellValue(getFieldTranslation(fields[i]));
        }
        int contentRowIndex = 1;
        for (T d : data) {
            Row content = sheet.createRow(contentRowIndex);
            for (int i = 0; i < fields.length; i++) {
                Cell cell = content.createCell(i);
                cell.setCellValue(getFieldValue(d, fields[i]));
            }
            contentRowIndex++;
        }
        flushToFile(exportClient, wb);
    }

    private void flushToFile(ExportClient exportClient, SXSSFWorkbook wb)  {
        Map<String, Object> exportConfig = exportClient.getStorageConfig();
        String clazz = exportClient.getClazz().getSimpleName().toLowerCase();
        if (exportConfig != null) {
            exportConfig.put("clazz", clazz);
        } else {
            exportConfig = new HashMap<String, Object>();
            exportConfig.put("clazz", clazz);
        }
        exportClient.getStorageService().flushToFile(wb, exportClient.getStorageConfig());
    }

    private String getFieldValue(Object d, Field field) {
        try {
            field.setAccessible(Boolean.TRUE);
            return field.get(d).toString();
        } catch (IllegalAccessException e) {
            log.error("fail to get field {} value by object {}, cause:{}",
                    field, d, e.getMessage());
            throw new ExportException("get.field.value.fail");
        }
    }

    private String getFieldTranslation(Field field) {
        field.setAccessible(Boolean.TRUE);
        ExportField exportField = field.getAnnotation(ExportField.class);
        if (exportField == null) {
            return field.getName();
        }
        String translation = exportField.translation();
        if (translation.equals("")) {
            return field.getName();
        }
        return translation;
    }
}
