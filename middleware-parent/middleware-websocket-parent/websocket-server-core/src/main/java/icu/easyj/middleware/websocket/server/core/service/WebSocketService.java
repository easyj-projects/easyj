/*
 * Copyright 2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package icu.easyj.middleware.websocket.server.core.service;

import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * WebSocket服务
 *
 * @author wangliang181230
 */
@ServerEndpoint(value = "/websocket")
public class WebSocketService {

	private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketService.class);

	/**
	 * 单个Session最大订阅数
	 */
	private static final int MAX_SUBSCRIBE_ONE_SESSION = 10;


	/**
	 * 与某个客户端的连接会话，需要通过它来给客户端发送数据
	 */
	private Session session = null;


	/**
	 * 建立连接事件
	 *
	 * @param session WebSocket会话
	 */
	@OnOpen
	public void onOpen(Session session) {
		this.session = session;
	}

	/**
	 * 关闭连接事件
	 *
	 * @param session     WebSocket会话
	 * @param closeReason 关闭原因
	 */
	@OnClose
	public void onClose(Session session, CloseReason closeReason) {
	}

	/**
	 * 连接出现异常事件
	 *
	 * @param session WebSocket会话
	 * @param t       异常信息
	 */
	@OnError
	public void onError(Session session, Throwable t) {
	}

	/**
	 * 收到客户端消息事件
	 *
	 * @param session WebSocket会话
	 * @param message 客户端发送过来的消息
	 */
	@OnMessage
	public void onMessage(Session session, String message) {
	}
}
