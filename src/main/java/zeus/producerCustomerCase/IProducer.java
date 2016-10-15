package zeus.producerCustomerCase;

import zeus.producerCustomerCase.exception.ProduceException;

/**
 * 生产者接口,只管从哪拉数据及如何组装数据，线程和我无关 嗷嗷
 */
public interface IProducer<TOutput>{
    /**
     * 生产者，暴露出生产行为
     * @return
     */
    TOutput produce() throws ProduceException;
}
