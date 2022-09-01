package com.yadong.doge.rpc.invoker;

public class InvokedResult {

    private String className;   //执行返回结果的类型
    private String obj; //  执行返回的结果 json格式
    private Integer lockId;     // 线程id

    public InvokedResult(String className, String obj, Integer lockId){
        this.className = className;
        this.obj = obj;
        this.lockId = lockId;
    }

    public Integer getLockId() {
        return lockId;
    }

    public void setLockId(int lockId) {
        this.lockId = lockId;
    }

    public InvokedResult(){}


    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getObj() {
        return obj;
    }

    public void setObj(String obj) {
        this.obj = obj;
    }

    @Override
    public String toString() {
        return "InvokedResult{" +
                "className='" + className + '\'' +
                ", obj='" + obj + '\'' +
                ", lockId=" + lockId +
                '}';
    }
}
