package com.yadong.doge.rpc.directory;


import com.yadong.doge.registry.client.RegistryClient;
import com.yadong.doge.registry.config.HostInfo;
import org.checkerframework.checker.units.qual.C;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractDirectory implements Directory{

   private static final Logger logger = LoggerFactory.getLogger(AbstractDirectory.class);
   protected ConcurrentHashMap<String, List<HostInfo>> hostInfoMap = new ConcurrentHashMap<>(256);
   protected static RegistryClient registryClient;

   public static void setRegistryClient(RegistryClient registryClient) {
      AbstractDirectory.registryClient = registryClient;
   }

   public String show() {
      StringBuilder builder = new StringBuilder();
      builder.append("客户端获取到的服务主机信息如下:");
      hostInfoMap.forEach((key, hostInfos) -> {
         builder.append("<br/>key: ").append(key);
         for (HostInfo hostInfo : hostInfos) {
            builder.append("<br/>&nbsp&nbsp&nbsp&nbsp&nbsp ip:" + hostInfo.getHostAndPort());
            builder.append("<br/>&nbsp&nbsp&nbsp&nbsp&nbsp version:" + hostInfo.getHostData().getVersion());
            builder.append("<br/>&nbsp&nbsp&nbsp&nbsp&nbsp weight:" + hostInfo.getHostData().getWeight());
            builder.append("<br/>&nbsp&nbsp&nbsp&nbsp&nbsp status:" + hostInfo.getHostData().getStatus());
            builder.append("<br/><br/>");
         }
         builder.append("<br/>================================================================");
      });
      return builder.toString();
   }

}
