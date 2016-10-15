package zeus.producerCustomerCase;

/**
 * 调度员接口，流水线上苦逼的人
 * 调度员必须是一个runnable的实现者,用于执行调度任务
 */
public interface IDispatcher extends Runnable {
    /**
     * 暴露一个接口给IDispatcherService用于告知服务，我在调度....
     * @param dispatcherService
     */
    void bind(IDispatchService dispatcherService);

    /**
     * 好的调度员，一定要报告工作情况
     */
    void report();

    boolean isRunning();

}
