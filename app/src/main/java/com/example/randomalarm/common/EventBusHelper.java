package com.example.randomalarm.common;

import org.greenrobot.eventbus.EventBus;

/**
 * EventBus辅助类
 * author : Mzz
 * date : 2019 2019/5/9 9:33
 * description :
 */
public class EventBusHelper {

    private static EventBus eventBus = EventBus.getDefault();

    /**
     * 订阅事件
     *
     * @param subscriber
     */
    public static void register(Object subscriber) {
        if (!eventBus.isRegistered(subscriber)) {
            eventBus.register(subscriber);
        }
    }

    /**
     * 取消订阅
     *
     * @param subscriber
     */
    public static void unregister(Object subscriber) {
        if (eventBus.isRegistered(subscriber)) {
            eventBus.unregister(subscriber);
        }
    }

    /**
     * 取消事件继续传递
     *
     * @param event
     */
    public static void cancelDelivery(Object event) {
        eventBus.cancelEventDelivery(event);
    }

    /**
     * 获取粘性事件
     *
     * @param classType
     * @param <T>
     * @return
     */
    public static <T> T getStickyEvent(Class <T> classType) {
        return eventBus.getStickyEvent(classType);
    }

    /**
     * 删除粘性事件
     *
     * @param event
     */
    public static void removeStickyEvent(Object event) {
        eventBus.removeStickyEvent(event);
    }

    /**
     * 发送事件
     *
     * @param event
     */
    public static void postEvent(Object event) {
        eventBus.post(event);
    }

    /**
     * 发送粘性事件
     *
     * @param event
     */
    public static void postStickyEvent(Object event) {
        eventBus.postSticky(event);
    }

}

