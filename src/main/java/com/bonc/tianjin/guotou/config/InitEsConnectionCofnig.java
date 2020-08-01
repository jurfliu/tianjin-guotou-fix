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
    public   Client initEsConnection(String tableName){
        String clusterName= esParamConfig.getClusterName();
        String nodes=  esParamConfig.getClusterNodes();
        String indexName=   esParamConfig.getIndexName();
        String typeName=  esParamConfig.getTypeName();//默认走配置文件
        if(tableName!=null&&"".equals(tableName)){
            typeName=tableName;
        }
      logger.info("获取配置文件es的索引名称为: "+indexName+" type名称: "+typeName+"   集群名称:"+clusterName+" 节点地址: "+nodes);
        Client transportClient = ESUtils.getESClientConnection(indexName,typeName,clusterName,nodes);
        return transportClient;
    }
    /**
     * d单个线程加载配置文件的内容，初始化es的连接
     * @return
     */
    public   Client initEsConnectionBySingle(String tableName){
        String clusterName= esParamConfig.getClusterName();
        String nodes=  esParamConfig.getClusterNodes();
        String indexName=   esParamConfig.getIndexName();
        String typeName=  esParamConfig.getTypeName();//默认走配置文件
        if(tableName!=null&&!"".equals(tableName)){
            typeName=tableName;
        }
        logger.info("获取配置文件es的索引名称为: "+indexName+" type名称: "+typeName+"   集群名称:"+clusterName+" 节点地址: "+nodes);
        Client transportClient = ESUtils.getESClientConnectionByCluster(indexName,typeName,clusterName,nodes);
        return transportClient;
    }
    /**
    * @author liujianfu
    * @description       本地测试专用
    * @date 2020/8/1 0001 上午 11:57
    * @param []
    * @return org.elasticsearch.client.Client
    */
    public   Client  localEsConnectionByTest(){
        String clusterName="elasticsearch";
        String nodes="127.0.0.1:9300";
        String indexName=   esParamConfig.getIndexName();
        String typeName=  esParamConfig.getTypeName();//默认走配置文件
        logger.info("获取配置文件es的索引名称为: "+indexName+" type名称: "+typeName+"   集群名称:"+clusterName+" 节点地址: "+nodes);
        Client client = ESUtils.getESClientConnection(indexName,typeName,clusterName,nodes);
        return client;
    }
}
