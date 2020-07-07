package com.bonc.tianjin.guotou.utils;

import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.net.InetAddress;

/**
* @Description:    es数据库工具类
* @Author:         ypy
* @Date:           2019/3/4 17:44
*/
public class ESUtils {
    public static Client client=null;

    /**
     * 方法描述：    获取连接
     * @auther:    ypy
     * @data:      2019/3/4 17:44
     * @param:
     * @return:
     **/
    public static Client getESClientConnection(String indexName,String typeName,String clusterName,String nodes) {
        if (client == null) {
            System.err.println("-------连接es----------------------------");
            System.setProperty("es.set.netty.runtime.available.processors", "false");
            try {
                //设置集群名称
                Settings settings = Settings.builder().put("cluster.name", clusterName)
                        .put("client.transport.sniff", true).build();

                   String esNodes[]=nodes.split(",");
                //创建client
                String host=esNodes[0].split(":")[0];
                int port=Integer.parseInt(esNodes[0].split(":")[1]);
                client = TransportClient.builder().settings(settings).build()
                        .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(host), port));

            } catch (Exception ex) {
                ex.printStackTrace();
                System.out.println(ex.getMessage());
                if (client != null) {
                    client.close();
                }
            }
        }
        return client;
    }
}
