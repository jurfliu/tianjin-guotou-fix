package com.bonc.tianjin.guotou.utils;

import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;

import java.net.InetAddress;

/**
* @Description:    es数据库工具类
* @Author:         ypy
* @Date:           2019/3/4 17:44
*/
public class ESUtils {
    public static Client client=null;
    private final static Integer PORT = 32016; //java端口为9300
    private final static String ES_NAME = "tianjin-es";  //es名字
    private final static String HOST1 = "192.168.168.162";  //es名字
    private final static String HOST2 = "192.168.168.161";  //es名字
    private final static String HOST3 = "192.168.168.160";  //es名字

    /**
     * 方法描述：    获取连接
     * @auther:    ypy
     * @data:      2019/3/4 17:44
     * @param:
     * @return:
     **/
    public static Client getESClientConnection() {
        if (client == null) {
            System.err.println("连接----------------------------");
            System.setProperty("es.set.netty.runtime.available.processors", "false");
            try {
                //设置集群名称
                Settings settings = Settings.builder().put("cluster.name", ES_NAME)
                        .put("client.transport.sniff", true).build();


                //创建client
                client = TransportClient.builder().settings(settings).build()
                        .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(HOST1), PORT))
                        .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(HOST2), PORT))
                        .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(HOST3), PORT));
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
