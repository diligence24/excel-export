package yzf.java.excel.annotation;

import java.lang.annotation.*;

/**
 * Created by yangzefeng on 2016/11/20
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ExportField {

    public String translation();
}
