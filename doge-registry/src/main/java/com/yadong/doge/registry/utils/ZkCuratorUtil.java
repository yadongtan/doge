package com.yadong.doge.registry.utils;




import org.apache.curator.framework.CuratorFramework;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * zk工具类 curator
 */
public class ZkCuratorUtil {
    private static final Logger logger = LoggerFactory.getLogger(ZkCuratorUtil.class);
    public CuratorFramework client;

    public static String CHARSET_NAME = "utf-8";

    public ZkCuratorUtil(CuratorFramework curatorFramework){
        this.client = curatorFramework;
    }

    //创建节点
    public void createNodeSimple(String path){
        try {
            if (null == client.checkExists().forPath(path)) {
                client.create().creatingParentsIfNeeded().forPath(path);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //删除节点
    public void deleteNodeSimple(String path){
        try {
            if (null != client.checkExists().forPath(path)) {
                client.delete().deletingChildrenIfNeeded().forPath(path);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //设置数据
    public void setData(String path, String data){
        try {
            if(null == client.checkExists().forPath(path)) return;
            client.setData().forPath(path, data.getBytes(ZkCuratorUtil.CHARSET_NAME));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //获取数据
    public byte[] getData(String path){
        try {
            return client.getData().forPath(path);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    //删除数据
    public void deleteDataRetainNode(String path){
        try {
            if (null != client.checkExists().forPath(path)) {
                client.delete().forPath(path);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    // 递归删除节点
    public void deletingChildrenIfNeeded(String path){
        try {
            if (null == client.checkExists().forPath(path)) return;
            client.delete().deletingChildrenIfNeeded().forPath(path);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
