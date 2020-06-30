package Handler;


import KafkaUtil.KafkaSend;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.otter.canal.protocol.CanalEntry;
import com.google.common.base.CaseFormat;
import com.study.common.Constant;

import java.util.List;

/**
 * canal行集数据处理工具类
 */
public class CanalHandler {

    /**
     * 处理方法
     * @param tableName  数据库表明
     * @param eventType  动作类型
     * @param rowDatasList  行集数据
     */
    public static void Handler(String tableName, CanalEntry.EventType eventType, List<CanalEntry.RowData> rowDatasList){
        //处理订单表数据
        if ("order_info".equals(tableName) && CanalEntry.EventType.INSERT.equals(eventType)){
            //表示下单
            for (CanalEntry.RowData rowData : rowDatasList) {
                //获取更新后的数据
                List<CanalEntry.Column> afterColumnsList = rowData.getAfterColumnsList();

                JSONObject jsonObject = new JSONObject();
                //列集数据展开
                for (CanalEntry.Column column : afterColumnsList) {
                    //System.out.println(column.getName()+":::"+column.getValue());
                    //驼峰转换
                    String s = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, column.getName());
                    jsonObject.put(s,column.getValue());
                }

                KafkaSend.send(Constant.KAFKA_TOPIC_ORDER,jsonObject.toJSONString());
            }
        }
    }
}
