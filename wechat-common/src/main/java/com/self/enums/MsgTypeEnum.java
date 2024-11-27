package com.self.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;
import java.util.stream.Stream;

/**
 * 
 * @Description: 发送消息的类型/动作 枚举
 */
@AllArgsConstructor
@Getter
public enum MsgTypeEnum {
	
	CONNECT_INIT(0, "第一次(或重连)初始化连接"),
	WORDS(1, "文字表情消息"),
	IMAGE(2, "图片"),
	VOICE(3, "语音"),
	VIDEO(4, "视频"),



	SIGNED(8, "消息签收"),
	KEEPALIVE(9, "客户端保持心跳"),
	heart(10, "拉取好友");
	
	public  Integer type;
	public  String content;


//	public static MsgTypeEnum getMsgTypeEnum(Integer type){
//		return Stream.of(MsgTypeEnum.values()).filter(t -> Objects.equals(type ,t.type)).findFirst().orElseThrow(() -> {
//			throw new RuntimeException("没有该条消息的处理策略");
//		});
//	}

}
