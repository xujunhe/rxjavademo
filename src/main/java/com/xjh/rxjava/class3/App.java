package com.xjh.rxjava.class3;

import io.reactivex.*;
import io.reactivex.schedulers.Schedulers;

import java.util.concurrent.Executors;

/**
 * 线程切换
 * Created by olyls on 2017/6/16.
 */
public class App
{
    public static void main(String[] args)
    {
        Flowable.just("登录")
                .map(s -> {
                    threadInfo("登录 map");
                    return  s+"map";
                })
                .subscribeOn(getNamedScheduler("io"))
                .doOnSubscribe( subscription -> {
                    threadInfo("提醒：正在登录...doOnSubscribe");
                })
                .subscribeOn(getNamedScheduler("ui"))
                .observeOn(getNamedScheduler("ui"))
                .doOnNext(s -> {
                    threadInfo("提醒：正在加载用户数据  doOnNext");
                })
                .observeOn(getNamedScheduler("io"))
                .flatMap( s -> {
                    threadInfo("请求用户数据 flatMap");
                    return Flowable.just(" 请求用户数据");
                })
               .map( s -> {
                   threadInfo("转换用户数据");
                   return s;
               })
                .observeOn(getNamedScheduler("ui"))
                .doOnNext(stringFlowable -> {
                    threadInfo("提醒：正在加载部门数据");
                } )
                .observeOn(getNamedScheduler("io"))
                .flatMap( s -> {
                    threadInfo("加载部门数据  flatMap");
                    return Flowable.just("dept");
                })
                .observeOn(getNamedScheduler("ui"))
                .doOnNext(s -> {
                    threadInfo("提醒：正在加载考勤设置");

                })
                .observeOn(getNamedScheduler("io"))
                .flatMap( s -> {

                    threadInfo("加载考勤设置");
                    return Flowable.just("考勤设置");
                })
                .observeOn(getNamedScheduler("ui"))
                .subscribe(s -> {
                    threadInfo("结果："+s);
                } );
    }

    private static void subscribeOn()
    {
        Observable.create((ObservableOnSubscribe<String>) observableEmitter -> {
            threadInfo("OnSubscribe.call()");
            observableEmitter.onNext("RxJava");
        }).subscribeOn(getNamedScheduler("create 之后的subscribeOn"))
                .doOnSubscribe( disposable -> {
                    threadInfo(".doOnSubscribe()-1");
                })
                .subscribeOn(getNamedScheduler("doOnSubscribe1之后的subscribeOn"))
                .doOnSubscribe( disposable -> {
                    threadInfo(".doOnSubscribe()-2");
                })
                .subscribe( s -> {
                    threadInfo(".onNext()");
                    System.out.println(s + "-onNext");
                });
    }
    
    private static  void observeOn()
    {
        Observable.just("Rxjava")
                .observeOn(getNamedScheduler("map 之前的 observeOn" ))
                .map(s -> {
                    threadInfo(".map() 1");
                    return s + "-map1";
                }).map(s -> {
            threadInfo(".map() 2");
            return s + "-map2";
        }).observeOn(getNamedScheduler("subscribe之前的observeOn"))
                .subscribe(s -> {
                    threadInfo(".onNext()");
                    System.out.println(s + "-onNext");
                });
    }
    
    
    public static Scheduler getNamedScheduler(String name) {
        return Schedulers.from(Executors.newCachedThreadPool(r -> new Thread(r, name)));
    }

    public static void threadInfo(String caller) {
        System.out.println(caller + " => " + Thread.currentThread().getName());
    }
}
