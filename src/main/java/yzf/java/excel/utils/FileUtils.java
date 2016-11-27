package yzf.java.excel.utils;

import yzf.java.excel.exception.ExportException;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by yangzefeng on 2016/11/21
 */
public class FileUtils {
    private static final Logger log = LoggerFactory.getLogger(FileUtils.class);

    public static void mkdirs(File file) {
        if (file.exists()) {
            log.debug("file {} exists", file);
            return;
        }
        boolean suc = file.mkdirs();
        if (!suc) {
            log.error("fail to create parent path {}", file);
            throw new ExportException("make.export.dir.fail");
        }
    }

    public static void createFile(File file) throws IOException {
        boolean suc = file.createNewFile();
        if (!suc) {
            log.error("fail to create file {}", file);
            throw new ExportException("create.export.file.fail");
        }
    }

    public static void writeToExcel(File preDic, File excel, SXSSFWorkbook wb) {
        OutputStream outputStream = null;
        try {
            mkdirs(preDic);
            createFile(excel);
            outputStream = new FileOutputStream(excel);
            wb.write(outputStream);
        } catch (Exception e) {
            log.error("fail to flush workbook to file {}, cause:{}",
                    excel, e.getMessage());
            throw new ExportException("create.excel.fail");
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException e) {
                //ignore this
            }
        }
    }

    public static void delete(File file) {
        boolean delSuc = file.delete();
        if (!delSuc) {
            log.error("fail to delete file {}", file);
        }
    }
}
