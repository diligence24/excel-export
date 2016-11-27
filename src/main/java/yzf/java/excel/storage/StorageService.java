package yzf.java.excel.storage;

import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import java.util.Map;

/**
 * Created by yangzefeng on 2016/11/21
 */
public interface StorageService {

    void flushToFile(SXSSFWorkbook wb, Map<String, Object> storageConfig);
}
