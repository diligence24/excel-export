package yzf.java.excel.storage;

import yzf.java.excel.exception.ExportException;
import yzf.java.excel.utils.FileUtils;
import yzf.java.excel.utils.PathUtils;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Map;

/**
 * Created by yangzefeng on 2016/11/21
 */
public class LocalStorage implements StorageService {

    private static final Logger log = LoggerFactory.getLogger(LocalStorage.class);

    public void flushToFile(SXSSFWorkbook wb,
                            Map<String, Object> storageConfig) {

        if (storageConfig.get("dir") == null || storageConfig.get("dir") == "") {
            log.error("dir.empty");
            throw new ExportException("dir.empty");
        }
        String dir = storageConfig.get("dir").toString();

        String clazz = storageConfig.get("clazz").toString();

        String preDicPath = PathUtils.generateFilePrefix(dir, clazz);
        String fileName = PathUtils.generateFileName();

        File file = new File(preDicPath + File.separator + fileName);
        File preDic = new File(preDicPath);

        FileUtils.writeToExcel(preDic, file, wb);
    }

}
