package com.reactive.main;

import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

@Slf4j
public class DelegateSub implements Subscriber<Integer> {
    @Override
    public void onSubscribe(Subscription s) {
        log.debug("onSubscribe");
        s.request(Long.MAX_VALUE);
    }

    @Override
    public void onNext(Integer i) {
        log.debug("onNext " + i);
    }

    @Override
    public void onError(Throwable t) {
        log.debug("onError");
    }

    @Override
    public void onComplete() {
        log.debug("onComplete");
    }
}

