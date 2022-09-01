package com.yadong.doge.rpc.loadbalance;

import com.yadong.doge.registry.config.HostInfo;
import com.yadong.doge.rpc.invoker.Invoker;

import java.util.List;


/**
* @author YadongTan
* @date 2022/9/1 15:33
* @Description 简易的一致性hash实现
*/
public class ConsistentHashLoadBalance extends AbstractLoadBalance{

    @Override
    public HostInfo doSelect(List<HostInfo> hostInfos, Invoker invoker) {
        int hash = hash(invoker);
        int index = getIndex(hash, hostInfos.size());
        return hostInfos.get(index);
    }

    private int getIndex(int num, int length) {
        int n = length - 1;
        n |= n >>> 1;
        n |= n >>> 2;
        n |= n >>> 4;
        n |= n >>> 8;
        n |= n >>> 16;
        n = n << 1;
        n = (n < 0) ? 1 : (n >= 1 << 30) ? 1 << 30 : n + 1;
        int index = num & n;
        return index >= length ? index - length : index;
    }

    private int hash(Object key) {
        int h;
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    }
}
