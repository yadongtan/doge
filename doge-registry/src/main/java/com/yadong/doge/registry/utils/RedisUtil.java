package com.yadong.doge.registry.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
* @author YadongTan
* @date 2022/4/28 23:48
* @Description 操作Redis工具类
*/
public class RedisUtil {

    private static final Logger logger = LoggerFactory.getLogger(RedisUtil.class);

    private RedisTemplate<String,Object> redisTemplate;

    private RedisOperations operations;

    public RedisUtil(RedisTemplate<String,Object> redisTemplate){
        this.redisTemplate = redisTemplate;

    }

    /**
     * 指定缓存失效时间
     * @param key 键
     * @param time 时间(秒)
     * @return 是否设置成功
     */
    public boolean expire(String key,long time){
        try{
            if(time>0){
                redisTemplate.expire(key, time, TimeUnit.SECONDS);
            }
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 获取过期时间
     * @param key 键,不能为null
     * @return  返回0代表永久有效
     */
    public Long getExpire(String key){
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    /**
     * 判断key是否存在
     * @param key 键
     * @return 存在true,不存在false
     */
    public Boolean hasKey(String key){
        try{
            return redisTemplate.hasKey(key);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 删除缓存
     * @param key 键,可以多个
     */
    public void del(String ...key){
        if(key != null && key.length > 0){
            if(key.length == 1){
                redisTemplate.delete(key[0]);
            }else{
                redisTemplate.delete(Arrays.asList(key));
            }
        }
    }

    /**
     * 普通缓存获取
     * @param key 键
     * @return 值
     */
    public Object get(String key){
        return key == null ? null : redisTemplate.opsForValue().get(key);
    }

    /**
     *普通缓存放入
     * @param key 键
     * @param value 值
     * @return  true成功,false失败
     */
    public boolean set(String key,Object value){
        try{
            redisTemplate.opsForValue().set(key, value);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 普通缓存放入并设置时间
     * @param key 键
     * @param value 值
     * @param time 时间(秒),要大于0,如果小于等于0,设置为无限时间
     * @return true成功,false失败
     */
    public boolean set(String key,Object value, long time){
        try{
            if(time>0){
                redisTemplate.opsForValue().set(key, value, time);
            }else{
                set(key,value);
            }
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 递增
     * @param key 键
     * @param delta 要增加几(大于0)
     * @return 返回递增结果
     */
    public Long incr(String key,long delta){
        if(delta < 0){
            throw new RuntimeException("递增因子必须大于0");
        }
        return redisTemplate.opsForValue().increment(key,delta);
    }

    /**
     * 递减
     * @param key 键
     * @param delta 要减少的数
     * @return 返回递减结果
     */
    public Long decr(String key,long delta){
        if(delta < 0){
            throw new RuntimeException("递增因子必须大于0");
        }
        return redisTemplate.opsForValue().increment(key,-delta);
    }

    /**
     * HashGet
     * @param key 键,不能为null
     * @param item 项,不能为null
     * @return 值
     */
    public Object hget(String key,String item){
        return redisTemplate.opsForHash().get(key,item);
    }

    /**
     * 获取hashKey对应的所有键值
     * @param key 键
     * @return 对应的多个键值
     */
    public Map<Object,Object> hmget(String key){
        return redisTemplate.opsForHash().entries(key);
    }

    /**
     * HashSet
     * @param key 键
     * @param map 对应多个键值
     * @return 成功true,失败false
     */
    public boolean hmset(String key,Map<String,Object> map){
        try{
            redisTemplate.opsForHash().putAll(key,map);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * HashSet并设置时间
     * @param key 键
     * @param map 对应多个键值
     * @param time 时间(秒)
     * @return 成功true,失败false
     */
    public boolean hmset(String key,Map<String,Object> map,long time){
        try{
            redisTemplate.opsForHash().putAll(key, map);
            if(time > 0){
                expire(key, time);
            }
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 向一张hash表中放入数据,如果不存在将创建
     * @param key 键
     * @param item 项
     * @param value 值
     * @return 成功true,失败false
     */
    public boolean hset(String key,String item,Object value){
        try{
            redisTemplate.opsForHash().put(key, item, value);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 向一张hash表中放入数据,如果不存在将创建
     * @param key 键
     * @param item 项
     * @param value 值
     * @param time 时间(秒),如果已存在的hash表有时间,将替代原有时间
     * @return 成功true,失败false
     */
    public boolean hset(String key, String item, Object value, long time) {
        try{
            redisTemplate.opsForHash().put(key, item, value);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }



    /**
     * 删除hash表中的值
     * @param key 键 不能为null
     * @param item 项 可以多个,不能为null
     */
    public void hdel(String key,Object ...item){
        redisTemplate.opsForHash().delete(key, item);
    }

    /**
     * 判断hash表中是否有该项的值
     * @param key 键 不能为null
     * @param item 项 不能为null
     * @return 存在true false 不存在
     */
    public boolean hHashKey(String key,String item){
        return redisTemplate.opsForHash().hasKey(key,item);
    }

    /**
     * hash递增 如果不存在,就会创建一个,并把新增后的值返回
     * @param key 键
     * @param item 项
     * @param by 要增加几(大于0)
     * @return 处理后的值
     */
    public double hincr(String key,String item,double by){
        return redisTemplate.opsForHash().increment(key, item, by);
    }

    /**
     * hash递减
     * @param key 键
     * @param item 项
     * @param by 要减少的绝对值
     * @return 处理后的值
     */
    public double hdecr(String key,String item,double by){
        return redisTemplate.opsForHash().increment(key, item, -by);
    }

    /**
     * 根据key获取Set中的所有值
     * @param key 键
     * @return 返回集合
     */
    public Set<Object> sGet(String key){
        try{
            return  redisTemplate.opsForSet().members(key);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 根据value从一个set中查询,是否存在
     * @param key 键
     * @param value 值
     * @return true存在,false不存在
     */
    public Boolean sHasKey(String key,Object value){
        try{
            return redisTemplate.opsForSet().isMember(key,value);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 将数据放入set缓存
     * @param key 键
     * @param values 值,可以是多个
     * @return 成功放入个数
     */
    public Long sSet(String key,Object ...values){
        try{
            return redisTemplate.opsForSet().add(key, values);
        }catch (Exception e){
            e.printStackTrace();
            return 0L;
        }
    }

    /**
     * 将数据放入set缓存,并设置时间
     * @param key 键
     * @param time 时间(秒)
     * @param values 值,可以是多个
     * @return 成功个数
     */
    public Long sSetAndTime(String key,long time,Object ...values){
        try{
            Long count = redisTemplate.opsForSet().add(key, values);
            if(time > 0){
                expire(key, time);
            }
            return count;
        }catch (Exception e){
            e.printStackTrace();
            return 0L;
        }
    }

    /**
     * 获取set缓存的长度
     * @param key 键
     * @return 长度
     */
    public Long sGetSetSize(String key){
        try{
            return redisTemplate.opsForSet().size(key);
        }catch (Exception e){
            e.printStackTrace();
            return 0L;
        }
    }

    /**
     * 移除集合中的value值
     * @param key 键
     * @param values 值 可以是多个
     * @return 成功移除的个数
     */
    public Long setRemove(String key,Object ...values){
        try{
            return redisTemplate.opsForSet().remove(key,values);
        }catch (Exception e){
            e.printStackTrace();
            return 0L;
        }
    }

    /**
     * 获取list缓存的内容
     * @param key 键
     * @param start 开始
     * @param end 从0到-1代表所有值
     * @return 返回list集合
     */
    public List<Object> lGet(String key,long start,long end){
        try{
            return redisTemplate.opsForList().range(key,start,end);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取list缓存的长度
     * @param key 键
     * @return 返回长度
     */
    public Long lGetListSize(String key){
        try{
            return redisTemplate.opsForList().size(key);
        }catch (Exception e){
            e.printStackTrace();
            return 0L;
        }
    }

    /**
     * 通过索引获取list中的值
     * @param key 键
     * @param index -1为表尾,-2为倒数第二个元素...
     * @return 返回值
     */
    public Object lGetIndex(String key,long index){
        try{
            return redisTemplate.opsForList().index(key, index);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 放入一个值到list缓存
     * @param key 键
     * @param value 值
     * @return 成功true,失败false
     */
    public boolean lSet(String key,Object value){
        try{
            redisTemplate.opsForList().rightPush(key,value);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 放入值到list缓存,并设置时间
     * @param key 键
     * @param value 值
     * @param time 时间(秒)
     * @return 成功true,失败false
     */
    public boolean lSet(String key,Object value,long time){
        try{
            redisTemplate.opsForList().rightPush(key, value);
            if(time > 0){
                expire(key, time);
            }
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 将一个List放入到Redis的list缓存中
     * @param key 键
     * @param value 值
     * @return 成功true,失败false
     */
    public boolean lSet(String key,List<Object> value){
        try{
            redisTemplate.opsForList().rightPush(key, value);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 将List放入到Redis的list缓存中,并设置时间
     * @param key 键
     * @param value 值
     * @param time 时间(秒)
     * @return 成功true,失败false
     */
    public boolean lSet(String key,List<Object> value,long time){
        try{
            redisTemplate.opsForList().rightPushAll(key, value);
            if(time > 0){
                expire(key, time);
            }
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 根据索引修改list中的某条数据
     * @param key 键
     * @param index 索引
     * @param value 值
     * @return 成功返回true,失败返回false
     */
    public boolean lUpdateIndex(String key,long index,Object value){
        try{
            redisTemplate.opsForList().set(key, index, value);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 移除n个值为value的元素
     * @param key 键
     * @param count 移除多少个
     * @param value 值
     * @return 移除的个数
     */
    public Long lRemove(String key,Long count,Object value){
        try{

            return redisTemplate.opsForList().remove(key, count, value);
        }catch (Exception e){
            e.printStackTrace();
            return 0L;
        }
    }

    /**
     * 添加一个zset数据
     * @param key   键
     * @param score 分数
     * @param value 值
     * @param time  过期时间
     * @return 操作成功与否
     */
    public boolean zSet(String key, Double score, Object value, long time) {
        try{
            redisTemplate.opsForZSet().add(key, value, score);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }


    /**
     * 清空Zset集合
     * @param key 值
     * @return 操作成功与否
     */
    public Long zSetRemove(String key){
        try{
            return redisTemplate.opsForZSet().removeRange(key, 0, Integer.MAX_VALUE);
        }catch (Exception e){
            e.printStackTrace();
            return 0L;
        }
    }
    /**
     * 通过索引获取list中的值
     * @param key 键
     * @return 返回值
     */
    public Set<Object> zSetGetByRange(String key, double start, double end){
        try{
            return redisTemplate.opsForZSet().rangeByScore(key, start, end);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
//    public void watchAndMulti(String watchKey){
//        redisTemplate.watch(watchKey);
//        redisTemplate.multi();
//    }
//
//    public List<Object> exec(){
//        return redisTemplate.exec();
//    }
}