package org.com.springboot.hardy.core.datasource;

/**
 * @ClassName DataBase
 * @Description 数据源枚举类
 * @author Hardy
 * @Date 2018年12月20日 下午3:11:49
 * @version 1.0.0
 */
public enum DataBase {
    MASTAR_DATA("master_data", "主库数据源"),
    SLAVE_DATA("slave_data", "从库数据源"),;

    String type;

    String description;

    private DataBase(String type, String description) {
        this.type = type;
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
