package com.yadong.doge.rpc.directory;

import com.yadong.doge.registry.config.HostData;
import com.yadong.doge.registry.config.HostInfo;
import com.yadong.doge.rpc.invoker.Invoker;
import com.yadong.doge.utils.NameGenerateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @author YadongTan
 * @date 2022/9/1 1:19
 * @Description dynamic directory pull from registry center
 */
public class DynamicDirectory extends AbstractDirectory {

    private static final Logger logger = LoggerFactory.getLogger(DynamicDirectory.class);

    private static DynamicDirectory _INSTANCE;

    private DynamicDirectory(){}

    public static DynamicDirectory getInstance(){
        if(_INSTANCE == null){
            synchronized (DynamicDirectory.class){
                if(_INSTANCE == null){
                    _INSTANCE = new DynamicDirectory();
                }
            }
        }
        return _INSTANCE;
    }


    @Override
    public List<HostInfo> pull(Method method, Class<?> targetClass) {
        List<HostInfo> newList = registryClient.getHost(method, targetClass);
        List<HostInfo> oldList = hostInfoMap.put(NameGenerateUtils.generateMethodMapKey(method, targetClass), newList);
        logger.info("更新服务提供者主机目录");
        StringBuilder builder = new StringBuilder();
        if(oldList != null){
            for (HostInfo hostInfo : oldList) {
                builder.append(hostInfo).append("/");
            }
        }
        logger.info("原服务提供者者主机目录:[" + builder + "]");
        StringBuilder builder1 = new StringBuilder();
        if(newList != null){
            for (HostInfo hostInfo : newList) {
                builder1.append(hostInfo).append("/");
            }
        }
        logger.info("现服务提供者主机目录:[" + builder1 + "]");
        return newList;
    }

    @Override
    public synchronized List<HostInfo> search(Invoker invoker) {
        List<HostInfo> hostInfos = hostInfoMap.get(invoker.getKey());
        if(hostInfos == null || hostInfos.isEmpty()){
            return pull(invoker.getMethod(), invoker.getTargetClass());
        }
        return hostInfos;
    }


    //更新某个服务的所有主机信息
    @Override
    public void refresh(String key) {
        List<HostInfo> remove = hostInfoMap.remove(key);
        List<HostInfo> hostInfos = registryClient.getHost(NameGenerateUtils.getMethodNameFromKey(key), NameGenerateUtils.getInterfaceNameFromKey(key));
        hostInfoMap.put(key, hostInfos);
    }

    //更新单个主机的信息
    public void refresh(String key, HostInfo hostInfo) {
        List<HostInfo> hostInfos = hostInfoMap.computeIfAbsent(key, (k) -> {
            return new ArrayList<>();
        });
        hostInfos.remove(hostInfo); //移除来
        HostData hostData = registryClient.getHostData(key, hostInfo);  //获取更新后的数据
        hostInfo.setHostData(hostData); //再设置为最新数据
        hostInfos.add(hostInfo);    //再放入list
    }

    @Override
    public void addHostInfo(String key, HostInfo hostInfo) {
        List<HostInfo> hostInfos = hostInfoMap.computeIfAbsent(key, (k) -> {
            return new ArrayList<>();
        });
        hostInfos.remove(hostInfo); //移除原来的
        hostInfos.add(hostInfo);    //加入新的
    }

    // 本地刷新, 而不是从zookeeper上重新拉取, 直接替换为传进来的值
    public void refreshFromLocal(String key, HostInfo hostInfo) {
        List<HostInfo> hostInfos = hostInfoMap.computeIfAbsent(key, (k) -> {
            return new ArrayList<>();
        });
        hostInfos.remove(hostInfo);
        hostInfos.add(hostInfo);
    }

    // 删除本地的某个节点
    public void deleteFromLocal(String key, HostInfo hostInfo) {
        List<HostInfo> hostInfos = hostInfoMap.get(key);
        hostInfos.remove(hostInfo);
    }

}
