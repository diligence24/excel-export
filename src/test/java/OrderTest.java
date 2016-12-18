import yzf.java.excel.ExportClient;
import yzf.java.excel.storage.LocalStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yangzefeng on 2016/11/19
 */
public class OrderTest {

    public static void main(String[] args) {

        LocalStorage localStorage = new LocalStorage();
        Map<String, Object> localConfig = new HashMap<String, Object>();
        localConfig.put("dir", "/tmp");

        ExportClient<Order> exportClient = new ExportClient<Order>(localStorage, localConfig){};

        List<Order> orderList = new ArrayList<Order>();
        orderList.add(new Order(1L, "order1"));
        orderList.add(new Order(2L, "order2"));
        orderList.add(new Order(3L, "order3"));
        orderList.add(new Order(4L, "order4"));
        exportClient.export(orderList);
    }
}
