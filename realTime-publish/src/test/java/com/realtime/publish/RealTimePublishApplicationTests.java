package com.realtime.publish;

import com.realtime.publish.Bean.StartUpLog;
import com.realtime.publish.Service.OrderService;
import org.elasticsearch.common.ParseField;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.QueryShardContext;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilterBuilder;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SourceFilter;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@SpringBootTest
class RealTimePublishApplicationTests {

    @Autowired
    ElasticsearchOperations eo;

    @Autowired
    OrderService orderService;

    @Test
    void contextLoads() {
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();

        TermsAggregationBuilder aggregationBuilder = AggregationBuilders.terms("groupByLogHour").field("logHour").size(24);
        NativeSearchQuery query = queryBuilder.withQuery(QueryBuilders.matchQuery("logDate", "2020-05-31"))
               // .withFilter(QueryBuilders.termQuery("logDate", "2020-05-31"))
                .addAggregation(aggregationBuilder)
                .build();


        System.out.println(query.getQuery().toString());
        SearchHits<StartUpLog> search = eo.search(query, StartUpLog.class, IndexCoordinates.of("real_time_dau"));

        System.out.println(search.getTotalHits());
        Terms terms = search.getAggregations().get("groupByLogHour");

        List<? extends Terms.Bucket> buckets = terms.getBuckets();
        for (Terms.Bucket bucket : buckets) {
            System.out.println(bucket.getKeyAsString()+"-"+bucket.getDocCount());
        }

    }

    @Test
    void contextLoads1() {
        Double amount = orderService.getOrderAmount("2020-06-29");
        System.out.println(amount);

        Map<Integer, Double> map = orderService.getOrderAmontHourMap("2020-06-29");

        for (Map.Entry<Integer,Double> x : map.entrySet()) {
            System.out.println(x.getKey()+":::"+x.getValue());
        }
    }


}
