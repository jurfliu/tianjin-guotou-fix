package com.bonc.tianjin.guotou.handler;

import com.bonc.tianjin.guotou.config.EsParamConfig;
import com.bonc.tianjin.guotou.config.InitEsConnectionCofnig;
import com.bonc.tianjin.guotou.model.DailyUnionBuyFormula;
import com.bonc.tianjin.guotou.model.OpsCjyFormula;
import com.bonc.tianjin.guotou.service.DailyUnionBuyFormulaService;
import com.bonc.tianjin.guotou.service.OpsCjyFormulaService;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//java -jar tianjin-guotou-fix-1.0-SNAPSHOT.jar com.bonc.tianjin.guotou.handler.DailyUnionNetTotalHandler
//
@Component
public class DailyUnionNetTotalHandler implements Executable{
    private static Logger logger = LoggerFactory.getLogger(DailyUnionNetTotalHandler.class);
    @Autowired
    private DailyUnionBuyFormulaService dailyUnionBuyFormulaService;//数据库
    @Autowired
    private EsParamConfig esParamConfig;
    @Autowired
    private InitEsConnectionCofnig esConnectionCofnig;
    @Override
    public void execute(String[] args) throws ParseException, InterruptedException {
        //1.链接es
        logger.info("日并网程序初始化es连接客户端.....");
      //  Client transportClient= esConnectionCofnig.initEsConnection("CALC_DP_STORE");//CALC_DP_STORE为es的type名称

        //2.查询公式列表
         List<DailyUnionBuyFormula> dailyUnionBuyFormulaList= dailyUnionBuyFormulaService.queryDailyUnionBuyFormulaList();
        for(DailyUnionBuyFormula dubf:dailyUnionBuyFormulaList){







        }

    }
      //3.获取要分析的公式


    public void queryEsByFormula(Client transportClient,String pointName,long startTime,long endTime){
        //设置查询条件
        BoolQueryBuilder builder = QueryBuilders.boolQuery()
                .must(QueryBuilders.termQuery("METERNAME",pointName))
                .must(QueryBuilders.rangeQuery("DATETIME").gte(startTime).lte(endTime));
        //System.out.println("sql:"+builder.toString());
        //执行查询
        SearchResponse searchResponse = transportClient.prepareSearch(esParamConfig.getIndexName())  //设置索引
                .setTypes(esParamConfig.getTypeName())                     //设置类型
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
     * 正则匹配所要的数据
     * @param line
     * @return
     */
    public List<String>  formulaRegexMatch(String line){
        List<String> list=new ArrayList<String>();
        line=line.replace("\r","").replace("\n","");
        String pattern="\\$\\{(.*?)\\}";
        // 创建 Pattern 对象
        Pattern r = Pattern.compile(pattern);
        // 现在创建 matcher 对象
        Matcher m = r.matcher(line);
        while(m.find( )) {
            //   System.out.println("Found value: " + m.group(0) );
            list.add(m.group(0));
        }
        return  list;
    }
}
