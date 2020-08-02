package com.bonc.tianjin.guotou.handler;

import com.bonc.tianjin.guotou.config.EsParamConfig;
import com.bonc.tianjin.guotou.config.InitEsConnectionCofnig;
import com.bonc.tianjin.guotou.model.CalcDpStore;
import com.bonc.tianjin.guotou.model.DailyUnionBuyFormula;
import com.bonc.tianjin.guotou.service.DailyBuyNetService;
import com.bonc.tianjin.guotou.service.DailyUnionNetService;
import com.bonc.tianjin.guotou.utils.DateUtil;
import com.bonc.tianjin.guotou.utils.MD5Util;
import com.google.gson.Gson;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.NoNodeAvailableException;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @ClassName: DailyBuyNetHandler
 * @Description: 日购网分析程序
 * @Author: liujianfu
 * @Date: 2020/08/02 16:49:57
 * @Version: V1.0
 **/
@Component
public class DailyBuyNetHandler {
    private static Logger logger = LoggerFactory.getLogger(DailyBuyNetHandler.class);
    public ExecutorService executor=null;//定义线程
    private  static final int  THREAD_NUM=10;//执行的线程数
    public static int count=0;
    @Autowired
    private InitEsConnectionCofnig esConnectionCofnig;
    @Autowired
    private EsParamConfig esParamConfig;
    @Autowired
    private DailyBuyNetService dailyBuyNetService;//数据库
    //@Scheduled(cron="0 12 09 * * ?")  //测试用
    @Scheduled(cron = "0 */3 * * * ?")   //每一分钟执行一次
    public void analysisUnionNetData(){
        Long startTime=System.currentTimeMillis();
        logger.info("==========日购网分析程序开始执行===========");
        executor = Executors.newFixedThreadPool(THREAD_NUM);//初始化fixed线程池
        //2.查询公式列表
        List<DailyUnionBuyFormula> dailyUnionFormulaList= dailyBuyNetService.queryDailyBuyDataList();
        //3.任务分发
        dispatcherTask(dailyUnionFormulaList, executor, startTime);
        //4.线程池销毁
        executor.shutdown();
        logger.info("=================================日购网分析程序 本次执行完成，线程池已经销毁==========");
    }
    /**
     * 任务分发
     * @param taskList
     */
    public void dispatcherTask(List<DailyUnionBuyFormula> taskList, ExecutorService executor, Long startTime){
        int totalSize = taskList.size();
        int ts = THREAD_NUM;//线程数目
        if (ts > totalSize) {
            ts = totalSize;
        }
        //CountDownLatch是一个同步辅助类也可以使用AtomicInteger替代
        final CountDownLatch doneSignal = new CountDownLatch(ts);
        int m = totalSize / ts;
        for (int k = 0; k < ts; k++) {
            int startIndex = k * m;
            int endIndex = (k + 1) * m;
            if (k== ts - 1) {
                endIndex = totalSize;
            }
            List<DailyUnionBuyFormula> ducsList = taskList.subList(startIndex, endIndex);
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    Long startTime=System.currentTimeMillis();
                    totalPerFormualResult(ducsList);//统计执行
                    //任务执行完毕递减锁存器的计数
                    doneSignal.countDown();
                    long endTime=System.currentTimeMillis();
                    logger.info("日购网分析程序 线程名字:" + Thread.currentThread().getName() + " 线程id:" + Thread.currentThread().getId() + "本次业务代码逻辑执行完成，进行countdown--;耗时:"+(endTime-startTime));
                }
            });
        }
        try {
            logger.info("日购网分析程序 线程名字:" + Thread.currentThread().getName() + " 线程id:" + Thread.currentThread().getId() +"进入等待状态......");
            doneSignal.await();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //子线程执行完毕，可以开始后续任务处理了
        count++;
        Long endTime=System.currentTimeMillis();
        logger.info("**************日购网分析程序 所有子任务执行完毕,这是第"+count+"次,执行完成,耗时："+(endTime-startTime)+":*******************");

    }
    /**
     * @author liujianfu
     * @description       实时计算每个公式的值
     * @date 2020/8/1 0001 下午 10:29
     * @param [ducsList]
     * @return void
     */
    public  void  totalPerFormualResult(List<DailyUnionBuyFormula> ducsList){
        //1.统计结果表es的链接初始化
        Client client=esConnectionCofnig.initEsConnectionBySingle(esParamConfig.getTypeName());//线上连接用，值为：CALC_DP_STORE，用于存储统计结果
        //Client client2= esConnectionCofnig.localEsConnectionByTest();//获取本地的连接
        //2.遍历要计算的模板公式
        for(DailyUnionBuyFormula dubf:ducsList){
            String devicePoint= dubf.getDeviceNodePoint();
            String deviceNodeEsType=dubf.getDeviceNodeEsType();
            String esIndexName=dubf.getEsIndex();
            String totalEsType=dubf.getTotalEsType();
            String yesterdayMaxName=dubf.getYesterdayMaxFormulaName();
            int  constantWeight=dubf.getConstantWeight();
            String calculateFormulaName=dubf.getCalculateFormulaName();
            // logger.info("线程名字:" + Thread.currentThread().getName() + " 线程id:" + Thread.currentThread().getId()+".....正在查询的设备节点devicePoint为: "+devicePoint);
            //logger.info("线程名字:" + Thread.currentThread().getName() + " 线程id:" + Thread.currentThread().getId()+".....正在查询的设备节点的es索引为: "+esIndexName+" es的类型为: "+deviceNodeEsType);
            //3.实时查询设备节点的信息，查询上一次执行时间，到当前时间的数据
            Client transportSingleClient= esConnectionCofnig.initEsConnectionBySingle(deviceNodeEsType);
            Date singleDate=new Date();
            //dubf.getLastCalculateFromulaTime() 为null，向前逆推1分钟
            long startTime= dubf.getLastCalculateFromulaTime()==null?singleDate.getTime()-60000: dubf.getLastCalculateFromulaTime().getTime();
            long endTime=singleDate.getTime();
            List<CalcDpStore> calcDpStoreList= queryDeviceNodeDataByRealTime(transportSingleClient, devicePoint,  startTime,endTime, esIndexName, deviceNodeEsType);
            logger.info("日购网分析程序 线程名字:" + Thread.currentThread().getName() + " 线程id:" + Thread.currentThread().getId()+" 查询规定时间内设备节点devicePoint:"+ devicePoint+"库的数据量为:"+calcDpStoreList.size());
            //4.查询es中该节点昨日最大值，昨天00：00：00 到23：59：59
            //昨天开始的日期
            long startTimeStamps = DateUtil.getYestesdayTime(singleDate, 1,0).getTime();
            //昨天结束的日期
            long endTimeStamps = DateUtil.getYestesdayTime(singleDate, 1,1).getTime();
            String startTimeStr = DateUtil.fromatMillisToDate(startTimeStamps);
            String endTimeStr = DateUtil.fromatMillisToDate(endTimeStamps);
            //logger.info("线程名字:" + Thread.currentThread().getName() + " 线程id:" + Thread.currentThread().getId()+"统计上一天开始的毫秒数:"+startTimeStamps+ " 统计上一天开始的日期:" + startTimeStr + " 统计今天开始的毫秒数"+endTimeStamps+" 统计今天开始的日期:" + endTimeStr);
            double  yeterdayMaxValue=queryYesterdayMaxValue( client,yesterdayMaxName,startTimeStamps,endTimeStamps,esIndexName,totalEsType);
            //5.按照公式逻辑进行计算
            for(CalcDpStore calcDpStore:calcDpStoreList){
                long exeTime=calcDpStore.getDATETIME();
                double realTimeValue=calcDpStore.getREADING();
                double currentValue=(realTimeValue-yeterdayMaxValue)*constantWeight;//按照公式进行计算
                int errorCount=1;
                 //addEsData(errorCount,client2,calculateFormulaName,currentValue,exeTime,esIndexName,totalEsType);//本地测试专用
                addEsData(errorCount,client,calculateFormulaName,currentValue,exeTime,esIndexName,totalEsType);
                // logger.info("线程名字:" + Thread.currentThread().getName() + " 线程id:" + Thread.currentThread().getId()+" 计算公式:"+calculateFormulaName+"  成功！！！");

            }
            //6.记录本次执行的最后一次时间
            Date calculateDate=DateUtil.longToDate(endTime);
            dubf.setLastCalculateFromulaTime(calculateDate);
            dailyBuyNetService.updateLastBuyNetCalulateTime(dubf);
            logger.info("日购网分析程序 线程名字:" + Thread.currentThread().getName() + " 线程id:" + Thread.currentThread().getId()+" 计算公式: "+calculateFormulaName+" 记录本次最后执行时间成功");
            //每一次使用完，就关闭连接
            if(transportSingleClient!=null){
                transportSingleClient.close();
            }
            logger.info("日购网分析程序 线程名字:" + Thread.currentThread().getName() + " 线程id:" + Thread.currentThread().getId()+"============设备节点devicePoint: "+devicePoint+" 计算公式: "+calculateFormulaName+" 统计完毕!!!!!=======");
        }
        //每一次使用完，就关闭连接

        if(client!=null){
            client.close();
        }
       /**
         if(client2!=null){
         client2.close();
         }
**/
        logger.info("日购网分析程序 线程名字:" + Thread.currentThread().getName() + " 线程id:" + Thread.currentThread().getId()+"=======================本次统计公式完毕!!!!!============");
    }
    /**
     * @author liujianfu
     * @description       实时查询设备节点的信息
     * @date 2020/8/1 0001 下午 7:33
     * @param [transportClient, pointName, startTime, endTime, indexName, typeName]
     * @return java.util.List<com.bonc.tianjin.guotou.model.CalcDpStore>
     */
    public List<CalcDpStore> queryDeviceNodeDataByRealTime(Client transportClient, String pointName, long startTime, long endTime, String indexName, String typeName){
        List<CalcDpStore> CalcDpStoreList=new ArrayList<CalcDpStore>();
        //设置查询条件
        BoolQueryBuilder builder = QueryBuilders.boolQuery()
                .must(QueryBuilders.termQuery("METERNAME",pointName))
                .must(QueryBuilders.rangeQuery("DATETIME").gte(startTime).lte(endTime));
        String prefix="{\"sort\": [{\"DATETIME\": \"desc\"}],\"query\":";
        System.out.println("sql:"+prefix+builder.toString()+" }");
        //执行查询
        SearchResponse searchResponse = transportClient.prepareSearch(indexName)  //设置索引
                .setTypes(typeName)                     //设置类型
                .setQuery(builder)                             //设置查询条件
                .setSize(5)
                .addSort("DATETIME", SortOrder.DESC)
                .execute().actionGet();
        //遍历结果
        SearchHit[] hits = searchResponse.getHits().getHits();
        for(SearchHit hit : hits){
            Map content = hit.getSource();
            String meterName = (String) content.get("METERNAME");
            long dateTime = (Long) content.get("DATETIME");
            double value = (Double) content.get("READING");
            CalcDpStore cds=new CalcDpStore();
            cds.setMETERNAME(meterName);
            cds.setREADING(value);
            cds.setDATETIME(dateTime);
            CalcDpStoreList.add(cds);
            // System.out.println("METERNAME:"+meterName+"  >>>>:value:"+value);
        }
        return CalcDpStoreList;
    }
    /**
     * @author liujianfu
     * @description       去索引库：data_store    type表：CALC_DP_STORE 中查询统计好的给设备的最大值
     * @date 2020/8/1 0001 上午 9:44
     * @param [client, meterName, value, time, indexName, typeName]
     * @return void
     */
    public static double queryYesterdayMaxValue( Client transportClient,String pointName,long startTime,long endTime,String indexName,String typeName) {
        //设置查询条件
        double value=0.0d;
        BoolQueryBuilder builder = QueryBuilders.boolQuery()
                .must(QueryBuilders.termQuery("METERNAME",pointName))
                .must(QueryBuilders.rangeQuery("DATETIME").gte(startTime).lte(endTime));
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
    public  int addEsData(int errorCount, Client client,String meterName,double value,long time,String indexName,String typeName) {
        System.out.println("client:"+client);
        if(errorCount==3){
            return -1;
        }
        CalcDpStore cp = new CalcDpStore();
        cp.setMETERNAME(meterName);
        cp.setREADING(value);
        cp.setDATETIME(time);
        String jsonData = toJson(cp);
        try {
            IndexResponse res = client.prepareIndex().setIndex(indexName).setType(typeName)
                    .setId(meterName + MD5Util.getMD5String(time + "").substring(0, 11)).setRefresh(true)
                    .setSource(jsonData).execute().actionGet();
        } catch (NoNodeAvailableException e) {
            e.printStackTrace();
            logger.info("日购网分析程序 索引名称为: "+indexName+" type名称: "+typeName+" json串为:" + jsonData + " 添加失败!!!,第 "+errorCount+"次");
            errorCount++;
            //  Client client3= esConnectionCofnig.localEsConnectionByTest();//获取本地的连接
            Client client3=esConnectionCofnig.initEsConnectionBySingle(esParamConfig.getTypeName());//线上连接用，值为：CALC_DP_STORE，用于存储统计结果
            System.out.println("client3："+client3);
            logger.info("日购网分析程序 重新初始化客户端连接!!!!!1");
            addEsData(errorCount, client3, meterName, value,time,indexName,typeName);
            return -1;
        }
        logger.info("日购网分析程序 json串为:" + jsonData + " 添加完成!!!");
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
