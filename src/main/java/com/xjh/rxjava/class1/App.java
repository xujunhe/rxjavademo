package com.xjh.rxjava.class1;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Single;
import io.reactivex.functions.Consumer;

import java.util.concurrent.TimeUnit;

/**
 * Created by olyls on 2017/6/1.
 */
public class App {


    public static void main (String[] args)
    {
       // helloRxJava("a","b","c","d");
        //test2();

        Single();

    }


    public static void Single()
    {
        Single.just("666")
                .subscribe(s -> System.out.print(s));
    }

    public static void customObservableBlocking()
    {
        Observable.create(observableEmitter -> {
            observableEmitter.onNext("xxx");
            observableEmitter.onNext("ccc");
        }).replay(10, TimeUnit.SECONDS)
                .subscribe(o -> System.out.print(o));
    }



    public static void test2()
    {
        Observable.just("hello Rxjava").subscribe(s -> System.out.print(s));
    }

    //第一个Rxjava例子

    public static void helloRxJava(String... names)
    {
        Observable.fromArray(names).subscribe(s -> System.out.println(s));

    }


}
