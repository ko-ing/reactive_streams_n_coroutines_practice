package com.reactive.main;

import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class Schedulers {
    public static void main(String[] args) {
        Publisher<Integer> intPub = intPub();
        Subscriber<Integer> intSub = new DelegateSub();

        Publisher<Integer> schedularOnSub = new Publisher<Integer>() {
            @Override
            public void subscribe(Subscriber<? super Integer> s) {
                ExecutorService es = Executors.newSingleThreadExecutor();
                es.execute(() -> intPub.subscribe(s));
                es.shutdown();
            }
        };

        Publisher<Integer> schedularOnPub = new Publisher<Integer>() {
            @Override
            public void subscribe(Subscriber<? super Integer> s) {
                schedularOnSub.subscribe(new Subscriber<Integer>() {
                    ExecutorService es = Executors.newSingleThreadExecutor();

                    @Override
                    public void onSubscribe(Subscription s) {
                        intSub.onSubscribe(s);
                    }

                    @Override
                    public void onNext(Integer integer) {
                        es.execute(() -> intSub.onNext(integer));
                    }

                    @Override
                    public void onError(Throwable t) {
                        es.execute(() -> intSub.onError(t));
                        es.shutdown();
                    }

                    @Override
                    public void onComplete() {
                        es.execute(() -> intSub.onComplete());
                        es.shutdown();
                    }
                });
            }
        };

        schedularOnPub.subscribe(intSub);
        log.debug("exit");
    }

    private static Publisher<Integer> intPub() {
        return new Publisher() {
            List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 6, 7);

            @Override
            public void subscribe(Subscriber s) {
                s.onSubscribe(new Subscription() {
                    @Override
                    public void request(long n) {
                        log.debug("request");
                        list.forEach(s::onNext);
                        s.onComplete();
                    }

                    @Override
                    public void cancel() {

                    }
                });
            }
        };
    }
}
