package yzf.java.excel.storage;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.meta.InsertOnly;
import com.qcloud.cos.request.UploadFileRequest;
import com.qcloud.cos.sign.Credentials;
import yzf.java.excel.exception.ExportException;
import yzf.java.excel.utils.FileUtils;
import yzf.java.excel.utils.PathUtils;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * Created by yangzefeng on 2016/11/21
 */
public class CosStorage implements StorageService {

    private static final Logger log = LoggerFactory.getLogger(CosStorage.class);
    private final static ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final TypeReference<Map<String, Object>> MAP_OF_STRING = new TypeReference<Map<String,Object>>(){};

    public void flushToFile(SXSSFWorkbook wb, Map<String, Object> storageConfig) {
        if (storageConfig == null) {
            log.error("storage config can not be null");
            throw new ExportException("storage.config.empty");
        }
        if (storageConfig.get("appId") == null || storageConfig.get("appId") == "") {
            log.error("app.id.empty");
            throw new ExportException("app.id.empty");
        }
        Long appId = Long.valueOf(storageConfig.get("appId").toString());
        if (storageConfig.get("secretId") == null || storageConfig.get("secretId") == "") {
            log.error("secret.id.empty");
            throw new ExportException("secret.id.empty");
        }
        String secretId = storageConfig.get("secretId").toString();

        if (storageConfig.get("secretKey") == null || storageConfig.get("secretKey") == "") {
            log.error("secret.key.empty");
            throw new ExportException("secret.key.empty");
        }
        String secretKey = storageConfig.get("secretKey").toString();

        if (storageConfig.get("bucketName") == null || storageConfig.get("bucketName") == "") {
            log.error("bucket.name.empty");
            throw new ExportException("bucket.name.empty");
        }
        String bucketName = storageConfig.get("bucketName").toString();

        if (storageConfig.get("dir") == null || storageConfig.get("dir") == "") {
            log.error("dir.empty");
            throw new ExportException("dir.empty");
        }
        String dir = storageConfig.get("dir").toString();

        String clazz = storageConfig.get("clazz").toString();

        String preDicPath = PathUtils.generateFilePrefix(dir, clazz);
        String fileName = PathUtils.generateFileName();

        //这里为了方便就把filename 当成是cospath
        String localPath = preDicPath + File.separator + fileName;

        File preDic = new File(preDicPath);
        File tmp = new File(localPath);

        try {
            //先上传到本地
            FileUtils.writeToExcel(preDic, tmp, wb);

            //上传到腾讯云
            Credentials cred = new Credentials(appId, secretId, secretKey);
            ClientConfig clientConfig = new ClientConfig();
            clientConfig.setRegion("gz");
            COSClient cosClient = new COSClient(clientConfig, cred);

            UploadFileRequest uploadFileRequest = new UploadFileRequest(
                    bucketName, File.separator + fileName, localPath);
            uploadFileRequest.setInsertOnly(InsertOnly.OVER_WRITE);
            String result = cosClient.uploadFile(uploadFileRequest);
            Map<String, Object> response = getResultMap(result);
            Integer code = getResultCode(response);
            String msg = getResultMsg(response);
            if (code != 0) {
                log.error("fail to upload excel(localPath={}) to cos path(filename={}), code:{} cause:{}",
                        localPath, fileName, code, msg);
            }

            //删除本地文件
            FileUtils.delete(tmp);
        } catch (Exception e) {
            log.error("fail to flush excel to file with config {}, cause:{}",
                    storageConfig, e.getMessage());
        }
    }

    private Map<String, Object> getResultMap(String result) throws IOException {
        return OBJECT_MAPPER.readValue(result, MAP_OF_STRING);
    }

    private Integer getResultCode(Map<String, Object> result) {
        return Integer.valueOf(result.get("code").toString());
    }

    private String getResultMsg(Map<String, Object> result) {
        return result.get("message").toString();
    }
}
