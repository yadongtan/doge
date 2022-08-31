package com.yadong.doge.rpc.invoker;

public class InvokerAndResultMap {

    Invoker invoker;
    InvokedResult invokedResult;

    private InvokerAndResultMap(){

    }

    public InvokerAndResultMap(Invoker invoker){
        this.invoker = invoker;
    }

    public InvokerAndResultMap(InvokedResult invokedResult){
        this.invokedResult = invokedResult;
    }

    public Invoker getInvoker() {
        return invoker;
    }

    public void setInvoker(Invoker invoker) {
        this.invoker = invoker;
    }

    public InvokedResult getInvokedResult() {
        return invokedResult;
    }

    public void setInvokedResult(InvokedResult invokedResult) {
        this.invokedResult = invokedResult;
    }
}
