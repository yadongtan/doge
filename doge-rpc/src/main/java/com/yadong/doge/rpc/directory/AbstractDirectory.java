package com.yadong.doge.rpc.directory;


import com.yadong.doge.registry.client.RegistryClient;
import com.yadong.doge.registry.config.HostInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractDirectory implements Directory{

   private static final Logger logger = LoggerFactory.getLogger(AbstractDirectory.class);
   protected ConcurrentHashMap<String, List<HostInfo>> hostInfoMap;
   protected RegistryClient registryClient;

}
