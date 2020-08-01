package com.bonc.tianjin.guotou.handler;

import com.bonc.tianjin.guotou.config.EsParamConfig;
import com.bonc.tianjin.guotou.config.InitEsConnectionCofnig;
import com.bonc.tianjin.guotou.model.CalcDpStore;
import com.bonc.tianjin.guotou.model.DailyUnionBuyFormula;
import com.bonc.tianjin.guotou.service.DailyUnionBuyFormulaService;
import com.bonc.tianjin.guotou.utils.DateUtil;
import com.bonc.tianjin.guotou.utils.ESUtils;
import com.bonc.tianjin.guotou.utils.MD5Util;
import com.google.gson.Gson;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: DailyTotalYesterdayMaxPowerHandler
 * @Description: 统计各个设备节点上一天最大的值
 * @Author: liujianfu
 * @Date: 2020/07/31 16:52:20
 * @Version: V1.0
 **/
@Component
public class DailyTotalYesterdayMaxPowerHandler {
    private static Logger logger = LoggerFactory.getLogger(DailyUnionNetTotalHandler.class);
    @Autowired
    private DailyUnionBuyFormulaService dailyUnionBuyFormulaService;//数据库
    @Autowired
    private InitEsConnectionCofnig esConnectionCofnig;
    @Autowired
    private EsParamConfig esParamConfig;
    /**
    * @author liujianfu
    * @description       
    * @date 2020/7/31 0031 下午 4:54
    * @param []        
    * @return void
    */
    @Scheduled(cron="0 04 14 * * ?")
    public void dailyTotalLastMaxValueTask(){
        //1.计算要执行的日期
        Date singleDate=new Date();
        //昨天开始的日期
        long startTimeStamps = DateUtil.getYestesdayTime(singleDate, 1,0).getTime();
        //今天开始的日期
        long endTimeStamps = DateUtil.getYestesdayTime(singleDate, 0,0).getTime();
        String startTimeStr = DateUtil.fromatMillisToDate(startTimeStamps);
        String endTimeStr = DateUtil.fromatMillisToDate(endTimeStamps);
        logger.info("统计上一天开始的毫秒数:"+startTimeStamps+ " 统计上一天开始的日期:" + startTimeStr + " 统计今天开始的毫秒数"+endTimeStamps+" 统计今天开始的日期:" + endTimeStr);
        String today01Time=endTimeStr.split(" ")[0]+" 01:00:00";
        long time=DateUtil.getStringToDate(today01Time,"yyyy-MM-dd HH:mm:ss").getTime();
        logger.info("es的入库时间为:time:"+time);
        //2.查询公式列表
        List<DailyUnionBuyFormula> dailyUnionBuyFormulaList= dailyUnionBuyFormulaService.queryDailyUnionBuyFormulaList();
        //Client client= esConnectionCofnig.localEsConnectionByTest();//获取本地的连接
        Client client=esConnectionCofnig.initEsConnectionBySingle(esParamConfig.getTypeName());//线上连接用，值为：CALC_DP_STORE，用于存储统计结果
        for(DailyUnionBuyFormula dubf:dailyUnionBuyFormulaList){
           String devicePoint= dubf.getDeviceNodePoint();
           String deviceNodeEsType=dubf.getDeviceNodeEsType();
           String esIndexName=dubf.getEsIndex();
           String totalEsType=dubf.getTotalEsType();
           String yesterdayMaxName=dubf.getYesterdayMaxFormulaName();
           logger.info(".....正在查询的设备节点devicePoint为: "+devicePoint);
           logger.info(".....正在查询的设备节点的es索引为: "+esIndexName+" es的类型为: "+deviceNodeEsType);
           //3.查询上一天的最大值
           Client transportSingleClient= esConnectionCofnig.initEsConnectionBySingle(deviceNodeEsType);
           double lastMaxValue=totalYesterdayMaxValue(transportSingleClient,devicePoint, startTimeStamps,endTimeStamps,esIndexName,deviceNodeEsType);
           //4.入到存储结果的es索引库中
            int flag= addEsData(client, yesterdayMaxName,lastMaxValue, time,esIndexName,totalEsType);
            //5.记录一下执行时间
            dubf.setTotalYesterdayMaxPowerTime(new Date());//设置更新时间
            dailyUnionBuyFormulaService.updateYesterdayMaxPowerTime(dubf);
            //每一次使用完，就关闭连接
            if(transportSingleClient!=null){
               transportSingleClient.close();
            }
            logger.info("============设备节点devicePoint: "+devicePoint+" 统计完毕!!!!!=======");
    }
        //每一次使用完，就关闭连接
        if(client!=null){
            client.close();
        }
        logger.info(endTimeStr+"=======================统计上一天节点最大值程序统计完毕!!!!!============");
}
/**
* @author liujianfu
* @description       求出每个节点上一天的最大值
* @date 2020/8/1 0001 上午 9:13
* @param [transportClient, pointName, startTime, endTime, indexName, typeName]
* @return double
*/
    public double totalYesterdayMaxValue(Client transportClient,String pointName,long startTime,long endTime,String indexName,String typeName){
        //设置查询条件
        double value=0.0d;
        BoolQueryBuilder builder = QueryBuilders.boolQuery()
                .must(QueryBuilders.termQuery("METERNAME",pointName))
                .must(QueryBuilders.rangeQuery("DATETIME").gte(startTime).lte(endTime));
        System.out.println("sql:"+builder.toString());
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
             value = (Double) content.get("READING");
           // System.out.println("METERNAME:"+meterName+"  >>>>:value:"+value);
        }
        return value;
    }
    /**
    * @author liujianfu
    * @description       向es添加数据   主键生成策略为：permetername+MD5Util.getMD5String(time+"").substring(0,11)
    * @date 2020/8/1 0001 上午 9:44
    * @param [client, meterName, value, time, indexName, typeName]
    * @return void
    */

    public static int addEsData( Client client,String meterName,double value,long time,String indexName,String typeName) {
        CalcDpStore cp = new CalcDpStore();
        cp.setMETERNAME(meterName);
        cp.setREADING(value);
        cp.setDATETIME(time);
        String jsonData = toJson(cp);
        try {
            IndexResponse res = client.prepareIndex().setIndex(indexName).setType(typeName)
                    .setId(meterName + MD5Util.getMD5String(time + "").substring(0, 11)).setRefresh(true)
                    .setSource(jsonData).execute().actionGet();
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("json串为:" + jsonData + " 添加失败!!!");
            return -1;
        }
        logger.info("json串为:" + jsonData + " 添加完成!!!");
        return 1;
    }
    /**
    * @author liujianfu
    * @description   转换成json串
    * @date 2020/8/1 0001 上午 9:44
    * @param [o]
    * @return java.lang.String
    */

    public static String toJson(Object o){
        try {
            Gson gson=new Gson();
            String json=   gson.toJson(o);
            return json;
            // return objectMapper.writeValueAsString(o);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
