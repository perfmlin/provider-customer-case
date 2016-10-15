package zeus.producerCustomerCase.impl;

import zeus.producerCustomerCase.ICustomer;
import zeus.producerCustomerCase.IDispatcher;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Logger;

/**
 * Created by AdminPerfmlin on 2016/10/12.
 */
public class DataCustomer implements ICustomer {
    private static Logger logger = Logger.getLogger(DataProducer.class.getName());
    private IDispatcher m_dispatcher;
    private final int m_numberOfWorker;
    private final ExecutorService m_service = Executors.newCachedThreadPool();
    private final Hashtable<Integer,Future> m_workFutures = new Hashtable();
    private int m_id;
    public DataCustomer(int numberOfWorker) {
        if (numberOfWorker < 1 || numberOfWorker > 10) numberOfWorker = 5;
        this.m_numberOfWorker = numberOfWorker;
    }

    @Override
    public void bind(IDispatcher dispatcher) {
        m_dispatcher = dispatcher;
    }

    @Override
    public void run() {
        while (m_dispatcher.isOnline()) {
            Object data = m_dispatcher.pop();

            if (null != data) {
                doWork(data, m_id++);
            }
        }
        try {
            for (Map.Entry<Integer, Future> entry :
                    m_workFutures.entrySet()) {
                entry.getValue().get();
            }
        } catch (Exception ex) {

        }
    }


    private void doWork(final Object data, int id) {
        final class WorkStarter implements Runnable {
            private int _id;

            public WorkStarter(int id) {
                this._id = id;
            }

            @Override
            public void run() {
                Long tid = Thread.currentThread().getId();
                logger.info(String.format("开始消费: %s by thread %d", data, tid));
                final class Worker implements Runnable {
                    private String _data;
                    private final List<Exception> _errList = new ArrayList();
                    private final ReadWriteLock _lock = new ReentrantReadWriteLock(false);

                    public Worker(Object data) {
                        this._data = data.toString();
                    }

                    @Override
                    public void run() {
                        _lock.writeLock().lock();
                        try {

                            //TODO: 消费业务逻辑
                            _data = String.format("%s-appended %d", this._data, Thread.currentThread().getId());
                            Thread.sleep(100);

                        } catch (Exception ex) {
                            _errList.add(ex);
                        } finally {
                            _lock.writeLock().unlock();
                        }
                    }
                }
                Worker worker = new Worker(data);
                Future[] workerFutures = new Future[m_numberOfWorker];
                for (int i = 0; i < m_numberOfWorker; i++) {
                    workerFutures[i] = m_service.submit(worker);
                }
                try {
                    for (int i = 0; i < m_numberOfWorker; i++) {
                        workerFutures[i].get();
                    }
                } catch (Exception ex) {
                    // TODO: 2016/10/12  异常处理

                    m_workFutures.remove(tid);
                    return;
                }

                // TODO: 2016/10/12 所有子线程工作完毕（可能其中某个业务错误）
                logger.info(String.format("消费完毕: %s by thread %d", worker._data, tid));
                if (worker._errList.size() > 0) {
                    // TODO: 2016/10/12  工作异常处理
                } else {
                    // TODO: 2016/10/12  落地
                }
                m_workFutures.remove(this._id);
            }
        }
        ;

        m_workFutures.put(id, m_service.submit(new WorkStarter(id)));
    }
}
