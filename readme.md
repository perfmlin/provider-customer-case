#生产者/消费者场景模拟

##设计目标
- 解耦业务逻辑
- 利用多线程，提高业务吞吐量


##设计要点
- 利用第三者（即调度器）进行生产者及消费者的解耦 

> 生产者--只关心生产数据（消息），可能是任何来源（DB、EVENT NOTIFICATION、USER INPUT...） 

> 消费者（类REDUEX的REDUCER）---只需要根据数据（类REDUX的ACTION）做具体的业务逻辑 

- 调度器，即我们连接生产者和消费者的容器

> 缓冲生产者的数据 

> 生产者消费者管理【注册/注销/调度】 

