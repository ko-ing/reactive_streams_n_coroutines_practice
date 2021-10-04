package com.reactive.main;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;


// intPub -> [Data1] -> map -> [Data2] -> intSub
//                  map도 intSub 입장에선 publisher
//                  <- subscribe(subA)
//                  -> onSubscribe(s)
//                  -> onNext
//                  -> onNext
//                  -> onComplete

public class Operators {
    public static void main(String[] args) {
        Publisher<Integer> intPub = intPub();
        Publisher<Integer> mapPub = mapPub(intPub, s -> s * 10);

        mapPub.subscribe(new DelegateSub());
    }

    private static Publisher<Integer> mapPub(Publisher<Integer> pub, Function<Integer, Integer> func) {
        return new Publisher<>() {
            @Override
            public void subscribe(Subscriber<? super Integer> s) {
                pub.subscribe(new DelegateSub() {
                    @Override
                    public void onNext(Integer i) {
                        s.onNext(func.apply(i));
                    }
                });
            }
        };
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
