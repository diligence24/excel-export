# 目标


    1.根据用户定义dto,生成对应的excel文件
    2.可上传至本地服务器, 也可自定义扩展，如oss
    3.保证对应10w+的导出量内存不会溢出
    4.支持excel表头的中文翻译
    5.可以为每个字段定制处理函数
   
# 待完成目标

    1.记录所有的生成文件名，提供文件列表功能
    
    
# 使用说明

    1. 首先定义需要导出的dto对象
    
        ```
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
        ```
       
    2. 定义StorageService,这里已本地存储为例
    
        ```
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
        ```
    
    3. 使用ExportClient导出
    
        ```
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
        ```