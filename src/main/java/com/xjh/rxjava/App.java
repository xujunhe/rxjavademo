package com.xjh.rxjava;


import io.reactivex.*;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by olyls on 2017/5/9.
 */
public class App {

    public static void main(String[] args)
    {
        test2();
    }

    //第二种写法
    private static void test2() {
        Observable.create((ObservableOnSubscribe<Integer>) e -> {
            e.onNext(1);
            e.onNext(2);
            e.onNext(3);
            e.onComplete();
            print("subscribe");
            System.out.print(Thread.currentThread().getName());
        }).subscribeOn(Schedulers.newThread()).subscribe(new Observer<Integer>() {
            private Disposable disposable;
            public void onSubscribe(Disposable d) {
                print("onSubscribe");
                disposable = d;
                System.out.print(Thread.currentThread().getName());
            }

            public void onNext(Integer integer) {
                print("onNext");
                disposable.dispose();
            }

            public void onError(Throwable e) {
                print("onError");
            }

            public void onComplete() {
                print("onComplete");
            }
        });
    }

    //第一种写法
    private void test1()
    {
        Observable<Integer> observable = Observable.create(emitter -> {
            emitter.onNext(1);
            emitter.onNext(2);
            emitter.onNext(3);
            emitter.onComplete();
        });

        Observer<Integer> observer = new Observer<Integer>() {
            public void onSubscribe(Disposable d) {
                print("onSubscribe");
            }

            public void onNext(Integer integer) {
                print("onNext"+integer);
            }

            public void onError(Throwable e) {
                print("onError");
            }

            public void onComplete() {
                print("onComplete");
            }
        };
        observable.subscribe(observer);
    }
    private static void print(Object obj)
    {
        System.out.println(obj);
    }
}
