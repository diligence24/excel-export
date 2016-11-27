package yzf.java.excel;

import yzf.java.excel.storage.StorageService;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Map;

/**
 * Created by yangzefeng on 2016/11/19
 */
public abstract class ExportClient<T> {
    /**
     * excel文件在内容中的最大行数,默认5000,超过这个数量之后,之前在内存中的行就会被flush到硬盘中.
     * 这种方式对于大量的数据同时导出也不会导致内存溢出,但是会在服务器上生成一个excel文件,如果最后希望在云服务器上存放excel,
     * 本地服务器的excel文件将会删除
     */
    private Integer rowAccessWindowSize;

    /**
     * 泛型类
     */
    private Class<T> clazz;

    /**
     * 文件存放服务
     */
    private StorageService storageService;

    /**
     * 文件存放服务配置
     */
    private Map<String, Object> storageConfig;


    private ExcelFactory excelFactory = new ExcelFactory();

    public Integer getRowAccessWindowSize() {
        return rowAccessWindowSize;
    }

    public Class<T> getClazz() {return clazz;}

    public Map<String, Object> getStorageConfig() {
        return storageConfig;
    }

    public StorageService getStorageService() {
        return storageService;
    }

    public ExportClient(Integer rowAccessWindowSize,
                        StorageService storageService,
                        Map<String, Object> storageConfig) {
        this.clazz = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        this.rowAccessWindowSize = rowAccessWindowSize;
        this.storageService = storageService;
        this.storageConfig = storageConfig;
    }

    public void export(List<T> data) {
        Field[] fields = getFields();
        excelFactory.createExcel(data, fields, this);
    }

    private Field[] getFields() {
        return clazz.getDeclaredFields();
    }
}
