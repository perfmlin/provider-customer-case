package zeus.producerCustomerCase.impl;

import zeus.producerCustomerCase.*;
import zeus.producerCustomerCase.exception.ReduceException;

import java.util.*;
import java.util.concurrent.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by AdminPerfmlin on 2016/10/12.
 */
public class SimpleDispatchService implements IDispatchService {
    private static Logger logger = Logger.getLogger(SimpleDispatchService.class.getName());
    //共享数据，阻塞队列，如要实现自己的阻塞队列；参考关键词： synchronized，java.util.concurrent.locks.*
    private final BlockingQueue m_sharedQueue;
    //调度者工作线程
    private final Thread m_threadProducer, m_threadReducer;
    private final IProducerDispatcher m_producer;
    private final IDispatcher  m_reducer;


    /**
     * 初始化样例任务调度器
     *
     * @param queueSize 队列大小，2-99，超出范围，默认10
     */
    public SimpleDispatchService(int queueSize) {
        //producer.bind(this);
        if (queueSize < 1 || queueSize > 100) queueSize = 10;
        m_sharedQueue = new ArrayBlockingQueue(queueSize);
        m_producer = new ProduceDispatcher(this);
        m_reducer = new ReduceDispatcher(this);
        m_threadProducer = new Thread(m_producer);
        m_threadReducer = new Thread(m_reducer);
    }

    private boolean m_running;
    private boolean m_stopped;

    @Override
    public boolean start() {
        if (!m_running) {
            m_running = true;
            m_threadProducer.start();
            m_threadReducer.start();
            logger.info("简单调度器服务器开启.....");
        }
        return m_running;
    }

    @Override
    public boolean stop() {
        if (!m_stopped) {
            m_stopped = true;
            if (!m_running) {
                m_running = true;
            }

            try {

                //通知生产者队列结束生产
                if(m_write_blocking)
                {
                    m_threadProducer.interrupt();
                }else {
                    m_threadProducer.join();
                }
                //通知消费者队列结束工作
                if(m_read_blocking) {
                    m_threadReducer.join();
                }else{
                    m_threadReducer.join();
                }
            } catch (Exception ex) {
                // TODO: 2016/10/15
            }
            //等待工作尚未完成的消费者工作
            waitingCustomers();
        }
        report();
        logger.info("简单调度器服务器结束.....");
        return !m_running;
    }

    @Override
    public void pause() {
        if (m_running) {
            m_running = false;
        }
    }


    @Override
    public boolean isRunning() {
        return m_running;
    }

    @Override
    public boolean isStopped() {
        return m_stopped;
    }


    private final List<ICustomer> m_customerList = new ArrayList<>();

    @Override
    public boolean registerCustomer(ICustomer customer) {
        m_customerList.add(customer);
        return true;
    }


    @Override
    public boolean registerProducer(IProducer producer) {
        return  m_producer.registerProducer(producer);

    }

    private boolean m_write_blocking;
    @Override
    public void pushMessage(Object message) {
        try {
            m_write_blocking= true;
            m_sharedQueue.put(message);
            m_write_blocking =false;
            logger.info(String.format("结束生产:%s", message));
        } catch (InterruptedException ex) {
            m_write_blocking =false;
            logger.log(Level.SEVERE, null, ex);
        }
    }
    private boolean m_read_blocking;
    @Override
    public Object popMessage() {
        try {
            m_read_blocking=true;
            Object data = m_sharedQueue.take();
            m_read_blocking =false;
            return data;
        } catch (InterruptedException ex) {
            m_read_blocking =false;
            logger.log(Level.SEVERE, null, ex);
            return null;
        }
    }


    private final ExecutorService m_reduceService = Executors.newCachedThreadPool();

    private final class ReducerRunner<TMessage,TOutput> implements Callable<TOutput>{
        private final ICustomer<TMessage,TOutput> _customer;
        private final TMessage _message;
        private final UUID _id;
        public ReducerRunner(ICustomer customer,TMessage message) {
            this._id = UUID.randomUUID();
            this._customer = customer;
            this._message = message;
        }

        public UUID getId() {
            return _id;
        }

        @Override
        public TOutput call() throws Exception {
            TOutput out = _customer.reduce(_message);

            m_customerFutureList.remove(getId());
            return out;
        }
    }
    private final Hashtable<UUID,Future> m_customerFutureList = new Hashtable<>();
    @Override
    public void notifyCustomer(Object message) throws ReduceException {
        for (int i = 0; i < m_customerList.size(); i++) {
            ReducerRunner runner = new ReducerRunner<>(m_customerList.get(i), message);
            m_customerFutureList.put(
                    runner.getId(),
                    m_reduceService.submit(runner)
            );

        }
    }
    private void waitingCustomers() {
        for (Map.Entry<UUID, Future> entry : m_customerFutureList.entrySet()) {
            try {
                entry.getValue().get();
            }catch (Exception ex){

            }
        }
    }
    @Override
    public void report() {
        m_producer.report();
        m_reducer.report();
    }
}
