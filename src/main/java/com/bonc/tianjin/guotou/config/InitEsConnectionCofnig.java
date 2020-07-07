package com.bonc.tianjin.guotou.config;

import com.bonc.tianjin.guotou.handler.DailyElectricDataHandler;
import com.bonc.tianjin.guotou.utils.ESUtils;
import org.elasticsearch.client.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class InitEsConnectionCofnig {
    private static Logger logger = LoggerFactory.getLogger(InitEsConnectionCofnig.class);
    @Autowired
    private EsParamConfig esParamConfig;

    /**
     * 加载配置文件的内容，初始化es的连接
     * @return
     */
    public   Client initEsConnection(){
     String indexName=   esParamConfig.getIndexName();
      String typeName=  esParamConfig.getTypeName();
       String clusterName= esParamConfig.getClusterName();
      String nodes=  esParamConfig.getClusterNodes();
      logger.info("获取配置文件es的索引名称为: "+indexName+" type名称: "+typeName+"   集群名称:"+clusterName+" 节点地址: "+nodes);
        Client transportClient = ESUtils.getESClientConnection(indexName,typeName,clusterName,nodes);
        return transportClient;
    }
}
