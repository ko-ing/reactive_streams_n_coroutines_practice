package com.reactive.main;

import lombok.extern.slf4j.Slf4j;

import java.util.Objects;
import java.util.concurrent.*;

@Slf4j
public class AsyncFuture {
    // 비동기
    // Future
    // Callback
    // Spring에서는 @EnableAsync + @Async로
    // 보통 배치작업 등 오래 걸리는 작업에 async를 많이 씀
    interface SuccessCallback {
        void onSuccess(String result);
    }
    public static class CallbackFutureTask extends FutureTask<String> {
        SuccessCallback callback;
        public CallbackFutureTask(Callable<String> callable, SuccessCallback callback) {
            super(callable);
            this.callback = Objects.requireNonNull(callback);
        }
    }

    public static void main(String[] args) {
        ExecutorService es = Executors.newSingleThreadExecutor();

        FutureTask<String> f = new FutureTask<String>(() -> {
            Thread.sleep(2000);
            log.debug("Async");
            return "Hello";
        }) {
            @Override
            protected void done() {
                try {
                    log.debug(get());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        };

        es.execute(f);
        es.shutdown();

    }
}
