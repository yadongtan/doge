package com.yadong.doge.rpc.directory;

import com.yadong.doge.registry.config.HostInfo;
import com.yadong.doge.rpc.invoker.Invoker;
import com.yadong.doge.utils.NameGenerateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
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

}
