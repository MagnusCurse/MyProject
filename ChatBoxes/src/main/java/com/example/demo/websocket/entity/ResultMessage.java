package com.example.demo.websocket.entity;

import lombok.Data;

/**
 * @ClassName: ResultMessage
 * @Description: 用来封装服务端给浏览器发送的消息数据
 */
@Data
public class ResultMessage {

    private boolean isSystem;
    private String fromName;
    private Object message;//如果是系统消息是数组
}
