package com.realtime.publish.Service.Impl;

import com.realtime.publish.Bean.OrderLog;
import com.realtime.publish.Bean.StartUpLog;
import com.realtime.publish.Service.OrderService;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.BucketOrder;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.Sum;
import org.elasticsearch.search.aggregations.metrics.SumAggregationBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    ElasticsearchOperations elasticsearchOperations;

    @Override
    public Double getOrderAmount(String date) {
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();

        SumAggregationBuilder sumAggregationBuilder = AggregationBuilders.sum("sum_totalAmount").field("totalAmount");
        NativeSearchQuery query = queryBuilder.withQuery(QueryBuilders.matchQuery("createDate", date))
                // .withFilter(QueryBuilders.termQuery("logDate", "2020-05-31"))
                .addAggregation(sumAggregationBuilder)
                .build();

        SearchHits<OrderLog> search = elasticsearchOperations.search(query, OrderLog.class, IndexCoordinates.of("real_time_order"));
        //获取总额
        Sum sum = search.getAggregations().get("sum_totalAmount");


        return sum.getValue();
    }

    @Override
    public Map<Integer, Double> getOrderAmontHourMap(String date) {

        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();

        TermsAggregationBuilder termsAggregationBuilder = AggregationBuilders.terms("groupByCreateHour").field("createHour").size(24);
        SumAggregationBuilder sumAggregationBuilder = AggregationBuilders.sum("sum_totalAmount").field("totalAmount");
        TermsAggregationBuilder aggregationBuilder = termsAggregationBuilder.subAggregation(sumAggregationBuilder);
        NativeSearchQuery query = queryBuilder.withQuery(QueryBuilders.matchQuery("createDate", date))
                // .withFilter(QueryBuilders.termQuery("logDate", "2020-05-31"))
                .addAggregation(aggregationBuilder)
                .build();

        SearchHits<OrderLog> search = elasticsearchOperations.search(query, OrderLog.class, IndexCoordinates.of("real_time_order"));

        Terms terms = search.getAggregations().get("groupByCreateHour");
        //获取所有桶
        List<? extends Terms.Bucket> buckets = terms.getBuckets();

        Map<String,Double> map = new HashMap<>();
        for (Terms.Bucket bucket : buckets) {
            //获取时间,作为Key
            String key = bucket.getKeyAsString();
            //获取子聚合函数
            Sum sum = bucket.getAggregations().get("sum_totalAmount");
            map.put(key,sum.getValue());
        }

        //方便key进行排序
        Map<Integer,Double> treeMap = new TreeMap<>();
        for (Map.Entry<String,Double> x : map.entrySet()) {
            treeMap.put(Integer.parseInt(x.getKey()),x.getValue());
        }
        return treeMap;
    }
}
