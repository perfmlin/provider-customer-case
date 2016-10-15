package zeus.producerCustomerCase;

import zeus.producerCustomerCase.exception.ReduceException;

/**
 * 消费者接口,只管消费，线程和我无关 嗷嗷
 */
public interface ICustomer<TMessage,TOut>  {
    /**
     * 消费者，暴露出，消费数据的通用接口
     * @param message
     */
    TOut reduce(TMessage message) throws ReduceException;
}
