package zeus.producerCustomerCase;

import zeus.producerCustomerCase.exception.ReduceException;

/**
 * 调度服务接口
 */
public interface IDispatchService<TMessage> {
    //region 数据/消息队列接口
    /**
     * 插入数据进调度器的缓冲区
     * 这是一个线程阻塞方法
     * @param message
     */
    void pushMessage(TMessage message);

    /**
     * 从调度器取出数据/消息
     * 这是一个线程阻塞方法
     * @return not null
     */
    TMessage popMessage();
    //#endregion

    void notifyCustomer(TMessage message) throws ReduceException;

    //region 消费者、生产者注册接口
    /**
     * 注册生产者
     * @param producer
     * @return
     * @throws Exception
     */
    boolean registerProducer(IProducer producer);

    /**
     * 注册消费者
     * @param customer
     */
    boolean registerCustomer(ICustomer customer);


    //endregion

    //region 调度服务管理接口
    /**
     * 调度器是否可工作
     * @return true or false
     */
    boolean isRunning();

    /**
     * 调度器是否已结束
     * @return
     */
    boolean isStopped();

    /**
     * 开始调度任务
     * @return true 启动成功或正在运行中
     */
    boolean start();

    /**
     * 停止调度任务
     * 线程阻塞
     * @return true 停止成功
     */
    boolean stop();

    /**
     * 暂停调度任务
     */
    void pause();

    /**
     * 打印工作情况
     */
    void report();
    //#endregion
}
