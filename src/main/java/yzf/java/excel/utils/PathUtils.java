package yzf.java.excel.utils;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.File;

/**
 * Created by yangzefeng on 2016/11/21
 */
public class PathUtils {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormat.forPattern("yyyyMMddHHmmss");

    /**
     * 生成完整的文件路径, 格式为 /dir/className
     */
    public static String generateFilePrefix(String dir, String clazz) {
        String fullFileName = dir;

        fullFileName += File.separator
                + clazz;

        return fullFileName;
    }

    /**
     * 生成文件名
     */
    public static String generateFileName() {
        return DATE_TIME_FORMATTER.print(DateTime.now()) + ".xlsx";
    }
}
