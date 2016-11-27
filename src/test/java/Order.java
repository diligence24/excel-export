import yzf.java.excel.annotation.ExportField;

import java.util.Date;

/**
 * Created by yangzefeng on 2016/11/19
 */
public class Order {

    @ExportField(translation = "订单id")
    private Long id;

    @ExportField(translation = "商品名称")
    private String name;

    @ExportField(translation = "下单时间")
    private Date createAt;

    public Order(Long id, String name) {
        this.id = id;
        this.name = name;
        this.createAt = new Date();
    }
}
