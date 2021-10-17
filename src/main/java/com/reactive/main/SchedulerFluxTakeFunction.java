package com.reactive.main;

import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
public class SchedulerFluxTakeFunction {
    public static void main(String[] args) throws InterruptedException {
//        유저가 만든 쓰레드는 main쓰레드가 종료되어도 종료되지 않는다.
//        interval: 타이머쓰레드 -> 데몬쓰레드
//        데몬쓰레드만 남아있으면 데몬쓰레드를 다 죽인다

//        Flux.interval(Duration.ofMillis(500))
//            .take(10)
//            .subscribe(s->log.debug("onNext{}", s));
//
//        TimeUnit.SECONDS.sleep(12);

//        위의 take과 같은 operator 구현

        Publisher<Integer> pub = sub -> {
            sub.onSubscribe(new Subscription() {
                int num = 0;
                boolean done = false;

                @Override
                public void request(long n) {
                    ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
                    exec.scheduleAtFixedRate(() -> {
                        if (done) {
                            exec.shutdown();
                            return;
                        }
                        sub.onNext(num++);
                    }, 0, 300, TimeUnit.MILLISECONDS);
                }

                @Override
                public void cancel() {
                    done = true;
                }
            });
        };

        Publisher<Integer> takeSub = sub -> {
            pub.subscribe(new Subscriber<Integer>() {
                int count = 0;
                Subscription subscription;

                @Override
                public void onSubscribe(Subscription s) {
                    subscription = s;
                    sub.onSubscribe(s);
                }

                @Override
                public void onNext(Integer integer) {
                    sub.onNext(integer);
                    if (++count > 10) {
                        subscription.cancel();
                    }
                }

                @Override
                public void onError(Throwable t) {
                    sub.onError(t);
                }

                @Override
                public void onComplete() {
                    sub.onComplete();
                }
            });
        };

        takeSub.subscribe(new DelegateSub());
    }

}
