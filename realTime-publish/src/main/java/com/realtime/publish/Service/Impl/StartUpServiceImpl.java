package com.realtime.publish.Service.Impl;

import com.realtime.publish.Bean.StartUpLog;
import com.realtime.publish.Service.StartUpService;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AbstractAggregationBuilder;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
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

@Service
public class StartUpServiceImpl implements StartUpService {

    @Autowired
    ElasticsearchOperations elasticsearchOperations;

    @Override
    public Long getTotal(String date) {

        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        NativeSearchQueryBuilder boolQuery = queryBuilder.withQuery(QueryBuilders.boolQuery());
        NativeSearchQueryBuilder builder = boolQuery.withFilter(QueryBuilders.termQuery("logDate", date));

        NativeSearchQuery query = builder.build();

        SearchHits<StartUpLog> search = elasticsearchOperations.search(query, StartUpLog.class, IndexCoordinates.of("real_time_dau"));

        long totalHits = search.getTotalHits();

        return totalHits;
    }

    @Override
    public Map<String, Long> getHourTotal(String date) {

        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();

        TermsAggregationBuilder aggregationBuilder = AggregationBuilders.terms("groupByLogHour").field("logHour").size(24);
        NativeSearchQuery query = queryBuilder.withQuery(QueryBuilders.matchQuery("logDate", date))
                // .withFilter(QueryBuilders.termQuery("logDate", "2020-05-31"))
                .addAggregation(aggregationBuilder)
                .build();

        SearchHits<StartUpLog> search = elasticsearchOperations.search(query, StartUpLog.class, IndexCoordinates.of("real_time_dau"));

        //获取聚合数据
        Terms terms = search.getAggregations().get("groupByLogHour");

        //获取所有的桶
        List<? extends Terms.Bucket> buckets = terms.getBuckets();
        Map<String , Long>  map = new HashMap<>();

        for (Terms.Bucket bucket : buckets) {
            map.put(bucket.getKeyAsString(),bucket.getDocCount());
        }
        return map;
    }

}
