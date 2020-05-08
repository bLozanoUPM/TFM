package io;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ParallelExecutor {

    private static final Logger LOG = LoggerFactory.getLogger(ParallelExecutor.class);

    private ExecutorService pool;

    public ParallelExecutor(){
        int cpus = Runtime.getRuntime().availableProcessors();
        int maxThreads = (cpus > 1)? cpus - 1 : cpus;
        LOG.debug("Creating pool with {} threads",maxThreads);
        pool = new ThreadPoolExecutor(
                maxThreads, // core thread pool size
                maxThreads, // maximum thread pool size
                1, // time to wait before resizing pool
                TimeUnit.MINUTES,
                new ArrayBlockingQueue<Runnable>(maxThreads, true),
                new ThreadPoolExecutor.CallerRunsPolicy());
    }

    public ParallelExecutor(int threads){
        int cpus = Runtime.getRuntime().availableProcessors();
        int maxThreads = Math.max(Math.min(cpus, threads)-1,1);
        LOG.debug("Creating pool with {} threads",maxThreads);
        pool = new ThreadPoolExecutor(
                maxThreads, // core thread pool size
                maxThreads, // maximum thread pool size
                1, // time to wait before resizing pool
                TimeUnit.MINUTES,
                new ArrayBlockingQueue<Runnable>(maxThreads, true),
                new ThreadPoolExecutor.CallerRunsPolicy());
    }


    public void execute(Runnable task){
        pool.execute(task);
    }

    public void submit(Runnable task) {
        pool.submit(task);
    }

    public void shutdown(){
        pool.shutdown();
    }

    public void awaitTermination(){
        pool.shutdown();
        while(!pool.isTerminated());
    }

    boolean awaitTermination(long time, TimeUnit unit) {
        pool.shutdown();
        try {
            return pool.awaitTermination(time,unit);
        } catch (InterruptedException e) {
            LOG.debug("Interruption", e);
        }
        return true;
    }
}