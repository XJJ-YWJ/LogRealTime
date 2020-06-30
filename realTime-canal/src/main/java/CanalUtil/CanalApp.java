package CanalUtil;

import Handler.CanalHandler;
import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.client.CanalConnectors;
import com.alibaba.otter.canal.protocol.CanalEntry;
import com.alibaba.otter.canal.protocol.Message;
import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * canal客户端连接工具
 */
public class CanalApp {

    public static void main(String[] args) {
        CanalConnector canalConnector = CanalConnectors.
                newSingleConnector(new InetSocketAddress("192.168.30.67", 11111),
                        "example", "canal", "canal");

        while(true){
            //获取连接
            canalConnector.connect();
            //设置订阅的库和表
            canalConnector.subscribe("realtime.order_info");
            //获取100个消息
            Message message = canalConnector.get(100);
            //获取实体对象的个数
            int size = message.getEntries().size();

            if (size == 0){
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }else{
                for (CanalEntry.Entry entry : message.getEntries()) {
                    //判断事件类型,只处理行变化
                    if(entry.getEntryType().equals(CanalEntry.EntryType.ROWDATA)){
                        //反序列化数据集
                        ByteString storeValue = entry.getStoreValue();
                        CanalEntry.RowChange rowChange = null;

                        try {
                            rowChange = CanalEntry.RowChange.parseFrom(storeValue);
                        } catch (InvalidProtocolBufferException e) {
                            e.printStackTrace();
                        }

                        //获取行集信息
                        List<CanalEntry.RowData> rowDatasList = rowChange.getRowDatasList();
                        //获取动作信息
                        CanalEntry.EventType eventType = rowChange.getEventType();
                        //获取数据库表名
                        String tableName = entry.getHeader().getTableName();

                        CanalHandler.Handler(tableName,eventType,rowDatasList);
                    }
                }
            }
        }
    }
}
