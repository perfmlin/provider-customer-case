package zeus.producerCustomerCase.impl;

import zeus.producerCustomerCase.ICustomer;
import zeus.producerCustomerCase.Program;
import zeus.producerCustomerCase.exception.ReduceException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Logger;

/**
 * Created by AdminPerfmlin on 2016/10/12.
 */
public class StringCustomer implements ICustomer<String,String> {
    private static Logger logger = Logger.getLogger(StringProducer.class.getName());
    private final int m_numberOfWorker;
    private final ExecutorService m_service = Executors.newCachedThreadPool();
    /**
     * 最大工作子线程数量
     *
     * @param numberOfWorker
     */
    public StringCustomer(int numberOfWorker) {
        if (numberOfWorker < 1 || numberOfWorker > 10) numberOfWorker = 5;
        this.m_numberOfWorker = numberOfWorker;
    }

    /**
     * 场景需要消费产品，分5个线程处理
     *
     * @param message
     */
    @Override
    public String reduce(String message) throws ReduceException {
        if (null == message) {
            return null;
        }
        long startAt = System.currentTimeMillis();
        logger.info(String.format("开始消费: %s", message));
        Future[] workerFutures = new Future[m_numberOfWorker];
        for (int i = 0; i < m_numberOfWorker; i++) {
            workerFutures[i] = m_service.submit(new Worker(message));
        }
        List rstList = new ArrayList();
        try {
            for (int i = 0; i < m_numberOfWorker; i++) {
                rstList.add(workerFutures[i].get());
            }
        } catch (Exception ex) {
            throw new ReduceException("消费错误", ex);
        }
        logger.info(String.format("消费完毕，耗时（%d)！", System.currentTimeMillis() - startAt));
        StringBuilder sb = new StringBuilder();
        rstList.forEach(rst->{
            sb.append(rst);
        });
        return sb.toString();
    }

    private final class Worker implements Callable<String> {
        private final String _message;

        public Worker(String message) {
            this._message = message;
        }

        @Override
        public String call() throws ReduceException {
            String rst = null;
            try {
                //TODO: 消费业务逻辑
                rst = String.format("-appended %d", Thread.currentThread().getId());
                Thread.sleep(Program.RANDOM.nextInt(100));
            } catch (Exception ex) {
                throw new ReduceException(String.format("子业务逻辑错误%d", Thread.currentThread().getId()), ex);
            }
            return rst;
        }
    }
}
