package com.reactive.main;

import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;


// intPub -> [Data1] -> map -> [Data2] -> intSub
//                  map도 intSub 입장에선 publisher
//                  <- subscribe(subA)
//                  -> onSubscribe(s)
//                  -> onNext
//                  -> onNext
//                  -> onComplete

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

        schedularOnSub.subscribe(intSub);
        log.debug("exit");
    }

    private static Publisher<Integer> intPub() {
        return new Publisher<>() {
            List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 6, 7);

            @Override
            public void subscribe(Subscriber<? super Integer> s) {
                s.onSubscribe(new Subscription() {
                    @Override
                    public void request(long n) {
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
