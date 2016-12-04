import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import yzf.java.excel.annotation.ExportField;

import java.util.Date;

/**
 * Created by yangzefeng on 2016/11/19
 */
public class Order {

    private final static DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");

    @ExportField(translation = "订单id")
    private Long id;

    @ExportField(translation = "商品名称")
    private String name;

    @ExportField(translation = "下单时间", handler = "dateTimeHandler")
    private Date createAt;

    public Order(Long id, String name) {
        this.id = id;
        this.name = name;
        this.createAt = new Date();
    }

    private String dateTimeHandler() {
        return DATE_TIME_FORMATTER.print(new DateTime(createAt));
    }
}
