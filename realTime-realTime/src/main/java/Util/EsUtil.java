package Util;

import Bean.StartUpLog;
import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.config.HttpClientConfig;
import io.searchbox.core.Bulk;
import io.searchbox.core.BulkResult;
import io.searchbox.core.Index;

import java.io.IOException;
import java.util.List;

/**
 * es工具类
 */
public class EsUtil {
    private static String ES_HOST="http://192.168.30.67";
    private static String ES_PORT="9200";
    private static JestClientFactory factory = null;

    /**
     * 获取客户端
     * @return
     */
    public static JestClient getJestClient(){
        if (factory == null)
            build();
        return factory.getObject();
    }

    /**
     * 关闭客户端
     * @param client
     */
    public static void Close(JestClient client){
        if (client != null){
            try {
                client.close();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * 建立连接
     */
    private static void build(){
        factory = new JestClientFactory();
        factory.setHttpClientConfig(new HttpClientConfig.Builder(ES_HOST+":"+ES_PORT).multiThreaded(true)
                //设置最大链接数         连接超时时间          读取超时时间
            .maxTotalConnection(20).connTimeout(10000).readTimeout(10000).build());
    }

    /**
     * 批量插入es
     * @param indexName
     * @param list
     */
    public static void indexBulk(String indexName, List<StartUpLog> list){
        JestClient client = getJestClient();
        Bulk.Builder builder = new Bulk.Builder().defaultIndex(indexName).defaultType("_doc");
        for (Object doc : list) {
            Index index = new Index.Builder(doc).build();
            builder.addAction(index);
        }

        try {
            List<BulkResult.BulkResultItem> items = client.execute(builder.build()).getItems();
            System.out.println("保存:"+items.size());
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            //关闭资源
            Close(client);
        }

    }

}
