package com.bonc.tianjin.guotou.utils;

import com.bonc.tianjin.guotou.model.CalcDpStore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.sort.SortOrder;

import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class TestEs {
    public static Client client=null;

    public static void main(String args[]){
        String indexName="data_store";
        String typeName="CALC_DP_STORE";
       /**
        String clusterName="tianjin-es";
        String nodes="192.168.168.160:32016";
**/

          String clusterName="elasticsearch";
        String nodes="127.0.0.1:9300";


        client = ESUtils.getESClientConnection(indexName,typeName,clusterName,nodes);
       // createEsIndex(indexName);
       // addEsData( indexName, typeName);
      //  String name="港狮光伏电站日发电量总和new_gszygglh";
       // addEsData(indexName,typeName,name);
        //updateIndexData(indexName,typeName,"AXMnCp8y578bR0-tYb3g");
        deleteById(indexName,typeName,"AXNS9p-tE4opSbZV1Osv");
       // String pointName="佰达昨日并网电量最大值_佰达昨日并网电量最大值";
        //queryEsData(client,pointName,indexName, typeName);
        System.out.println("over!!!!!!!!!!!!");

    }
    public static void queryEsData(Client transportClient,String pointName,String indexName,String typeName){
        //设置查询条件
        BoolQueryBuilder builder = QueryBuilders.boolQuery()
                .must(QueryBuilders.termQuery("METERNAME",pointName));
        // .must(QueryBuilders.rangeQuery("DATETIME").gte(startTime).lte(endTime));
        //System.out.println("sql:"+builder.toString());
        //执行查询
        SearchResponse searchResponse = transportClient.prepareSearch(indexName)  //设置索引
                .setTypes(typeName)                     //设置类型
                .setQuery(builder)                             //设置查询条件
                .setSize(1)
                .addSort("DATETIME", SortOrder.DESC)
                .execute().actionGet();
        //遍历结果
        SearchHit[] hits = searchResponse.getHits().getHits();
        for(SearchHit hit : hits){
            Map content = hit.getSource();
            String meterName = (String) content.get("METERNAME");
            long dateTime = (Long) content.get("DATETIME");
            double value = (Double) content.get("READING");
            System.out.println("METERNAME:"+meterName+"  >>>>:value:"+value);
        }
    }
    /**
     *
     * 方法简要描述信息.
     * <p> 描述 : 方法的主要功能和使用场合</p>
     * <p> 备注 : 其他对方法的说明信息</p>
     * @return void
     */
    public static  void createEsIndex(String indexName){
        if(!isExistsIndex(indexName)){
            client.admin().indices().prepareCreate(indexName).execute().actionGet();


        }
        else{
            System.out.println("创建的数据库已经存在");
        }

    }
    /**
     *
     * 判断索引库是否存在
     * <p> 描述 : 方法的主要功能和使用场合</p>
     * <p> 备注 : 其他对方法的说明信息</p>
     * @return boolean
     */
    public static  boolean isExistsIndex(String indexName){
        IndicesExistsResponse response =
                client.admin().indices().exists(
                        new IndicesExistsRequest().indices(new String[]{indexName})).actionGet();
        return response.isExists();
    }

    public static void addEsData(String indexName,String typeName){
        CalcDpStore cp=new CalcDpStore();
        cp.setMETERNAME("ceshi_天丰");
        cp.setREADING(6762.72);
       // cp.setDATETIME(new Date());
        cp.setDATETIME(1564536960000l);
        String jsondata = toJson(cp);
        System.out.println("json串为:"+jsondata);
        IndexResponse res = client.prepareIndex().setIndex(indexName).setType(typeName)
                .setId("ceshi_天丰"+MD5Util.getMD5String("1564536960000").substring(0,11)).setRefresh(true)
             .setSource(jsondata).execute().actionGet();
        System.out.println("添加完成!!!");
    }

    public static void addEsData(String indexName,String typeName,String name){
        CalcDpStore cp=new CalcDpStore();
        cp.setMETERNAME(name);
        cp.setREADING(6762.72);
       // cp.setDATETIME(1564536960000l);
        String jsondata = toJson(cp);
        System.out.println("json串为:"+jsondata);
        IndexResponse res = client.prepareIndex().setIndex(indexName).setType(typeName)
                .setSource(jsondata).execute().actionGet();
        System.out.println("添加完成!!!");
    }
    /**
     * 通过主键id进行修改
     * @param indexName
     * @param typeName
     * @param id
     */
    public static void updateIndexData(String indexName,String typeName,String id){
        UpdateRequest updateRequest;
        try {
            updateRequest = new UpdateRequest(indexName, typeName, id)
                    .doc(XContentFactory.jsonBuilder()
                            .startObject()
                            .field("reading", 666.666)
                            .endObject());
            try {
                client.update(updateRequest).get();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
    private static void deleteById(String indexName,String typeName,String id){
       // String id = "AWb23S95Hj0kMOI7Sqr8";
        DeleteRequest deleteRequest = new DeleteRequest(indexName, typeName, id);
        DeleteResponse response = client.delete(deleteRequest).actionGet();
        System.out.println(response);

    }
    private static ObjectMapper objectMapper = new ObjectMapper();
    public static String toJson(Object o){
        try {
            Gson gson=new Gson();
         String json=   gson.toJson(o);
         System.out.println("json:"+json);
         return json;
           // return objectMapper.writeValueAsString(o);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
