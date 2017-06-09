package com.xjh.rxjava.class2;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import java.util.Arrays;
import java.util.List;

/**
 * Created by olyls on 2017/6/9.
 */
//http://blog.csdn.net/qq_35064774/article/details/53057332
public class Rxjava02 {


    //Hello RxJava 2
    public static void helloRxjava01()
    {
        Flowable<String> flowable = Flowable.create(flowableEmitter -> {
            flowableEmitter.onNext("hello rxjava2");
            flowableEmitter.onComplete();
        }, BackpressureStrategy.BUFFER);


        Subscriber<String> subscriber = new Subscriber<String>() {
            @Override
            public void onSubscribe(Subscription subscription) {
                System.out.println("onSubscribe");
               subscription.request(Long.MAX_VALUE);
            }

            @Override
            public void onNext(String s) {
                System.out.println(s);
            }

            @Override
            public void onError(Throwable throwable) {
                System.out.println("onError");
            }

            @Override
            public void onComplete() {
                System.out.println("onComplete");
            }
        };
        flowable.subscribe(subscriber);
    }

    //hello rxjava 简洁代码
    public static void helloRxjava02()
    {
        Flowable.just("hello rxjava 02").subscribe(s -> System.out.print(s));
    }

    //map String 处理后返回String
    public static void map()
    {
        Flowable.just("map")
                .map(s -> s+"---> xjh")
                .subscribe(s -> System.out.print(s));
    }

    //map 操作符进阶  Integer 转换为 String
    public static void mapIntToString()
    {
        Flowable.just(1)
                .map(integer -> String.valueOf(integer))
                .subscribe(s -> System.out.print(s+"：我是String类型的哦"));
    }

    //输出数组,这种写法是不好的
    public static void printArrays01()
    {
        List<Integer> list = Arrays.asList(1,2,3,4,5,6,7,8,9,0);
        Flowable.just(list)
                .subscribe(integers -> {
                    for (int i = 0; i < integers.size(); i++) {
                        System.out.println(integers.get(i));
                    }
                });
    }

    //输出数组,这样操作的 每一次onNext 就是数组中的一个元素
    public static void printArrays02()
    {
        List<Integer> list = Arrays.asList(1,2,3,4,5,6,7,8,9,0);
        Flowable.fromIterable(list)
                .subscribe(integer -> System.out.println(integer));
    }


    //flatMap Flowable.flatMap 可以把一个 Flowable 转换成另一个 Flowable
    public static void printArrays03()
    {   List<Integer> list = Arrays.asList(1,2,3,4,5,6,7,8,9,0);
        Flowable.just(list)
                .flatMap(integers -> Flowable.fromIterable(integers))
                .subscribe(integer -> System.out.println(integer));
    }


    //filter   过滤小于5的数字
    public static void fliterTest01()
    {
        List<Integer> list = Arrays.asList(1,2,3,4,5,6,7,8,9,0);
        Flowable.fromIterable(list)
                .filter(integer -> integer<5) //filter 是用于过滤数据的，返回false表示拦截此数据。
                .subscribe(integer -> System.out.println(integer));
    }


    //take
    public static void takeTest()
    {
        List<Integer> list = Arrays.asList(1,2,3,4,5,6,7,8,9,0);
        Flowable.fromIterable(list)
                .take(2) //take 用于指定订阅者最多收到多少数据。
                .subscribe(integer -> System.out.println(integer));
    }

    //如果我们想在订阅者接收到数据前干点事情，比如记录日志:
    public static void doOther()
    {
        List<Integer> list = Arrays.asList(1,2,3,4,5,6,7,8,9,0);
        Flowable.fromIterable(list)
                .doOnNext(integer -> System.out.println("搞点其他的："+integer))//接收到数据处理一些其他事情
                .subscribe(integer -> System.out.println(integer));
    }


    //Subscriber

    public static void SubscriberTest01()
    {
        Flowable.create((FlowableOnSubscribe<String>) flowableEmitter -> {
            flowableEmitter.onNext("exception:"+1/0);
            flowableEmitter.onComplete();
        },BackpressureStrategy.BUFFER)
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onSubscribe(Subscription subscription) {
                        System.out.println("onSubscribe");
                        subscription.request(1);
                    }

                    @Override
                    public void onNext(String s) {
                        System.out.println("onNext"+s);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        System.out.println("onError :-----> "+throwable.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        System.out.println("onComplete");
                    }
                });
    }


    //test 完整的来一波

    public static void test()
    {
        List<Integer> list = Arrays.asList(1,2,3,4,5,6,7,8,9,0);
        Flowable.just(list)
                .flatMap(integers -> Flowable.fromIterable(integers))
                .filter(integer -> integer<5)
                .doOnNext(integer -> System.out.println("doOnNext："+integer))
                .map(integer -> "map："+String.valueOf(integer))
                .take(3)
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onSubscribe(Subscription subscription) {
                        System.out.println("onSubscribe");
                        subscription.request(Long.MAX_VALUE);
                    }

                    @Override
                    public void onNext(String s) {
                        System.out.println("onNext:"+s);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        System.out.println("onError:"+throwable.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        System.out.println("onComplete");
                    }
                });



    }

    //以不订阅的方式获取数据
    public static void test2()
    {
        List<String> strings = Flowable.range(1, 100)
                .map(integer -> "id:" + integer)
                .toList().blockingGet();
        System.out.print(strings);
    }


    public static void main(String[] args) throws InterruptedException {
        test2();
    }







}
