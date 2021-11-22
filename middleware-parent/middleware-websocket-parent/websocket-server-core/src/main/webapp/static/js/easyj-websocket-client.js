/**
 * WebSocket的客户端插件
 *
 * @author wangliang181230
 */
(function (wsc) {
	if (wsc) return;

	if (typeof String.prototype.startsWith !== "function") {
		String.prototype.startsWith = function (prefix) {
			return this.slice(0, prefix.length) === prefix;
		};
	}

	// 工具方法
	window.wscUtils = {
		// 打印一些参数信息，初期开发时观察一些情况
		printArgs: function (funName, args) {
			console.debug("%s————————————————————————————————————————", funName);
			console.debug(args);
		},

		// 将json字符串转换为json对象
		toJson: function (jsonStr, callback) {
			if (typeof callback !== "function") {
				callback = function (json) {
					return json;
				};
			}

			if (!jsonStr) {
				return callback("");  // 为空
			} else if (typeof jsonStr === "object") { // 是对象
				if (jsonStr.toString() === "[object Blob]") { // 是二进制流，将其转换为字符串
					let reader = new FileReader();
					reader.onload = function (event) {
						jsonStr = reader.result;// 内容就在这里
						wscUtils.toJson(jsonStr, callback);
					};
					reader.readAsText(jsonStr, 'UTF-8');
					return jsonStr; // 这里返回的还是Blob，不建议调用toJson()拿到的结果直接使用，建议采用callback方式
				} else {
					return callback(jsonStr);
				}
			}

			// 不是字符串
			if (typeof jsonStr !== "string") {
				return callback(jsonStr);
			}

			// 尝试用jQuery的方法解析json字符串
			if (window["jQuery"] && jQuery.parseJSON) {
				try {
					return callback(jQuery.parseJSON(jsonStr));
				} catch (e) {
				}
			}
			// 尝试用JS的原生方法解析json字符串
			if (window["JSON"] && JSON.parse) {
				try {
					return callback(JSON.parse(jsonStr));
				} catch (e) {
				}
			}
			// 尝试使用eval解析json字符串
			try {
				let json = null;
				eval('json = ' + jsonStr);
				return callback(json);
			} catch (e) {
				console.error("json字符串转换为json对象失败，该json串为：%s\r\n错误信息：", jsonStr);
				console.error(e);
				throw e;
			}
		},

		// 生成整形随机数
		intRandom: function (i) {
			return Math.floor(i * Math.random());
		},

		// 随机生成UUID
		uuidRandom: function () {
			return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function (c) {
				let r = Math.random() * 16 | 0,
					v = c === 'x' ? r : (r & 0x3 | 0x8);
				return v.toString(16);
			});
		},

		// 属性继承
		extend: function (target, source) {
			for (let key in source) {
				if (source[key] != null) {
					target[key] = source[key];
				}
			}
			return target;
		},

		// 是路由服务地址
		isWSRouterUrl: function (url) {
			if (!url) return false;
			return url.startsWith("http://") || url.startsWith("https://");
		},

		// 是WS服务地址
		isWSUrl: function (url) {
			if (!url) return false;
			return url.startsWith("ws://") || url.startsWith("wss://");
		},

		// 通过路由服务获取WS地址的方法
		send: function (routerUrl, requestType, onSuccess, onError) {
			if (!window["$"] && !window["jQuery"]) {
				wscUtils.alert("WebSocketClient：通过WebSocket路由服务获取闲置服务地址，需使用jQuery.ajax()方法，请先引用jQuery.js");
				return this;
			}

			(window["jQuery"] || window["$"]).ajax({
				url: routerUrl,
				type: requestType,
				crossDomain: true,
				xhrFields: {withCredentials: true},
				success: function (json) {
					onSuccess(json);
				},
				error: function () {
					onError(arguments);
				}
			});
		},

		// 监听路由服务请求响应的方法
		getWSUrlFromJson: function (json) {
			let url;
			if (typeof json === "string") {
				if (wscUtils.isWSUrl(json)) url = json;
			} else {
				if (wscUtils.isWSUrl(json["url"])) url = json.url;
			}
			return url;
		},

		// 提示错误信息
		alert: function (msg) {
			alert(msg);
		}
	};

	function WebSocketClient(options) {
		// 判断当前浏览器是否支持WebSocket
		if (!('WebSocket' in window)) {
			wscUtils.alert('当前浏览器不支持WebSocket');
			return this;
		}

		if (!options) {
			wscUtils.alert("创建WebSocketClient对象时，参数不能为空");
			return;
		}
		if (typeof options == "string") {
			options = {url: options};
		}
		if (!options.url) {
			wscUtils.alert("创建WebSocketClient对象时，url不能为空");
			return;
		}

		// 将入参配置覆盖到默认配置中，并返回options
		this.url = options.url;
		if (options.routerMethodType) this.routerMethodType = options.routerMethodType;
		if (options.spareWSUrl) {
			this.spareWSUrl = options.spareWSUrl;
			if (!this.url) {
				this.url = this.getSpareWSUrl();
			}
		}
		if (typeof options.onopen === "function") this.onopen = options.onopen;
		if (typeof options.onmessage === "function") this.onmessage = options.onmessage;
		if (typeof options.onclose === "function") this.onclose = options.onclose;
		if (typeof options.onerror === "function") this.onerror = options.onerror;

		// 获取url的类型，可以是路由服务地址来间接获取WS服务地址，也可以直接是WS服务地址。
		if (wscUtils.isWSUrl(this.url)) {
			this.urlType = "WS";
		} else if (wscUtils.isWSRouterUrl(this.url)) {
			this.urlType = "WSR";
		} else {
			wscUtils.alert("WebSocket服务地址格式有误：" + this.url);
			return;
		}

		// 是否自动连接，默认情况下不自动连接
		if (options.autoConnect === true) {
			this.connect();
		}
	}

	wscUtils.extend(WebSocketClient.prototype, {
		// 配置参数
		url: "",
		urlType: "", // url的类型，WS=WebSocket服务地址，WSR=WebSocket路由服务(Router)地址
		routerMethodType: "GET", // 路由服务的请求方式
		spareWSUrl: "", // 备用WS服务地址，当urlType=MS，但由于路由服务故障导致获取WS服务地址失败时，会使用此备用WS服务地址，具有更好的容错性
		getSpareWSUrl: function () {
			if (this.spareWSUrl) {
				if (typeof this.spareWSUrl === "string") {
					return this.spareWSUrl;
				} else if (typeof this.spareWSUrl === "object" && this.spareWSUrl.length > 0) {
					if (this.spareWSUrl.length === 1) {
						return this.spareWSUrl[0];
					} else {
						this.spareWSUrl[wscUtils.intRandom(this.spareWSUrl.length)];
					}
				}
			}
			return null;
		},

		onopen: null,
		onmessage: null,
		onclose: null,
		onerror: null,

		ws: null, // 连接对象
		status: "none", // 当前状态，none=未连接，connecting=连接中，connected=已连接，error=连接出错，closed=连接已关闭
		lastError: null, // 连接错误信息

		connectTime: null, // 连接开始时间
		connectSuccessTime: null, // 连接成功时间

		subscribeMap: {}, // 已订阅的数据

		delayFuns: [], // 连接成功后才执行的延迟方法

		// 创建连接
		connect: function () {
			if (this.status === "connected" || this.status === "connecting") { // 已连接或连接中，无需再连接
				return this;
			} else {
				this.clear("connecting"); // 其他状态需重置属性后，再创建连接
			}

			// 创建ws连接
			console.info("开始创建WebSocket连接");
			this.connectTime = new Date();
			if (this.urlType === "WS") { // 是WS服务地址，直接创建WebSocket连接
				this.buildWebSocketConnect(this.url);
			} else { // 是WebSocket路由服务地址，先获取闲置WS服务，再连接
				this.getIdleWebSocketThenConnect();
			}

			return this;
		},

		// ajax获取闲置的WS服务地址，再连接
		getIdleWebSocketThenConnect: function () {
			let self = this;

			wscUtils.send(this.url, this.routerMethodType,
				function (json) {
					let url = wscUtils.getWSUrlFromJson(json);
					if (!url) {
						console.error("未获取到WebSocket服务地址，请求路由服务的响应内容如下：");
						console.error(json);
						url = this.getSpareWSUrl();
						if (!url) {
							console.error("通过路由服务未获取到WebSocket服务地址，也未配置备用服务地址，无法进行WebSocket连接");
							return;
						} else {
							console.warn("通过备用的WebSocket服务地址连接");
						}
					}
					self.buildWebSocketConnect(url);
				},
				function () {
					let spareWSUrl = self.getSpareWSUrl();
					if (spareWSUrl) {
						self.buildWebSocketConnect(spareWSUrl);
					} else {
						console.error("调用路由服务获取WebSocket服务地址失败，也未配置备用服务地址，无法进行WebSocket连接，错误信息如下：");
						console.error(arguments);
					}
				}
			);
		},

		// 创建WebSocket连接对象
		buildWebSocketConnect: function (wsUrl) {
			let websocket = new WebSocket(wsUrl);
			this.ws = websocket;

			let self = this;

			// 连接成功后的回调方法
			websocket.onopen = function (open) {
				wscUtils.printArgs("onopen", arguments);
				self.onOpen(open);
			};

			// 接收到消息的回调方法
			websocket.onmessage = function (event) {
				wscUtils.printArgs("onmessage", arguments);
				self.onMessage(event);
			};

			// 连接关闭的回调方法
			websocket.onclose = function (close) {
				wscUtils.printArgs("onclose", arguments);
				self.onClose(close);
			};

			// 连接发生错误的回调方法
			websocket.onerror = function (error) {
				wscUtils.printArgs("onerror", arguments);
				self.onError(error);
			};

			return this;
		},

		// 状态
		changeStatus: function (status) {
			this.status = status;
			return this;
		},

		// 添加延迟执行的方法，在连接打开后执行
		addDelayFuns: function (delayFun, autoConnect) {
			if (typeof delayFun === "function") {
				if (this.status === "connected") {
					delayFun(); // 连接已经打开，直接执行方法
				} else {
					this.delayFuns[this.delayFuns.length] = delayFun; // 将方法保存到延迟执行的队列中
					if (this.status !== "connecting" && autoConnect === true) {
						this.connect();
					}
				}
			}
			return this;
		},

		// 连接打开后，执行延迟的方法
		runDelayFuns: function () {
			for (let i = 0; i < this.delayFuns.length; i++) {
				try {
					this.delayFuns[i]();
				} catch (e) {
					console.error("执行第%d个延迟方法失败", i);
					console.error(e);
				}
			}
			this.delayFuns = [];
		},

		// 主动关闭连接
		close: function () {
			this.clear("closed");
		},

		// 清除数据，包括连接、状态、订阅等
		clear: function (status) {
			// 清理原连接
			if (this.ws != null) {
				try {
					this.ws.close();
				} catch (e) {
				}
			}
			this.ws = null;

			// 状态变更
			if (status) {
				this.changeStatus(status);
			}

			// 清理订阅数据
			this.subscribeMap = {};

			// 清理延迟执行队列
			if (status !== "connecting") {
				this.delayFuns = [];
			}
		},

		// 订阅消息
		subscribe: function (channel, id, listener) {
			if (!channel) {
				wscUtils.alert("channel参数不能为空");
				return this;
			}
			if (typeof channel !== "string") {
				wscUtils.alert("channel参数必须是字符串");
				return this;
			}
			if (typeof id === "function") {
				listener = id;
				id = null;
			} else if (!listener) {
				wscUtils.alert("receiveMsgFun参数不能为空");
				return this;
			}

			if (this.status !== "connected") {
				let self = this;
				this.addDelayFuns(function () {
					self.subscribe(channel, id, listener);
				}, true);
				return this;
			}

			if (id) {
				if (typeof id !== "string") id = id + ""; // 转换为字符串
			} else {
				id = "*";
			}

			// 判断是否已订阅
			let subscribeInfo = this.getSubscribeByChannelAndId(channel, id);
			if (subscribeInfo) {
				console.debug("已经订阅，不可重复订阅");
				return;
			}

			// 保存订阅
			subscribeInfo = this.addSubscribe(channel, id, listener);

			// 发送订阅消息
			this.send({
				type: "subscribe",
				key: subscribeInfo.key,
				channel: channel,
				id: id
			});

			console.debug("发送订阅请求：key=%s, channel=%s, id=%s, listener=%s", subscribeInfo.key, channel, id, listener);

			return this;
		},

		// 取消订阅
		unsubscribe: function (channel, id) {
			if (!channel) {
				wscUtils.alert("channel参数不能为空");
				return this;
			}
			if (typeof channel !== "string") {
				wscUtils.alert("channel参数必须是字符串");
				return this;
			}

			if (this.status !== "connected") { // 未连接
				// 如果是连接中，则连接完成后执行；否则，不执行。
				if (this.status === "connecting") {
					let self = this;
					this.addDelayFuns(function () {
						self.unsubscribe(channel, id);
					});
				}
				return this;
			}

			if (id) {
				if (typeof id !== "string") id = id + ""; // 转换为字符串
			} else {
				id = "*";
			}

			// 获取订阅信息
			let subscribeInfo = this.getSubscribeByChannelAndId(channel, id);
			if (!subscribeInfo) {
				console.debug("订阅信息不存在，无法发起取消请求");
				return this;
			}

			// 发送取消订阅消息
			this.send({
				type: "unsubscribe",
				key: subscribeInfo.key,
				channel: channel,
				id: id
			});

			// 移除订阅
			this.removeSubscribe(subscribeInfo.key);

			console.debug("发送取消订阅请求：key=%s, channel=%s, id=%s", subscribeInfo.key, channel, id);

			return this;
		},

		// 获取监听方法
		getListenerByChannelAndId: function (channel, id) {
			let subscribeInfo = this.getSubscribeByChannelAndId(channel, id);
			return subscribeInfo ? subscribeInfo.listener : null;
		},

		// 获取监听方法
		getListener: function (key) {
			let subscribeInfo = this.getSubscribe(key);
			return subscribeInfo ? subscribeInfo.listener : null;
		},

		// 执行监听了当前消息的监听方法
		runListener: function (json, event) {
			let listener = this.getListener(json.key);
			if (listener) {
				this.runOneListener(listener, json, event);
			} else {
				console.warn("监听方法不存在，接收到的数据为：%s", JSON.stringify(json));
			}
			return this;
		},

		// 执行单个监听器方法
		runOneListener: function (listenerFun, json, event) {
			try {
				listenerFun(json, event);
			} catch (e) {
				console.error("执行监听方法失败，接收到的数据为：%s\r\n错误信息：", JSON.stringify(json));
				console.error(e);
			}
			return this;
		},

		// 保存订阅数据
		addSubscribe: function (channel, id, listener) {
			// 创建订阅对象
			let subscribeInfo = {
				key: wscUtils.uuidRandom(),
				channel: channel,
				id: id,
				listener: listener
			};

			// 保存订阅信息
			while (this.subscribeMap[subscribeInfo.key]) {
				console.debug("出现了重复的uuid：" + subscribeInfo.key);
				subscribeInfo.key = wscUtils.uuidRandom(); // 防止重复的uuid处理
			}
			this.subscribeMap[subscribeInfo.key] = subscribeInfo;

			// 返回订阅信息
			return subscribeInfo;
		},

		// 根据频道名及ID获取订阅信息
		getSubscribeByChannelAndId: function (channel, id) {
			if (id) {
				if (typeof id !== "string") id = id + ""; // 转换为字符串
			} else {
				id = "*";
			}

			let subscribeInfo;
			for (let key in this.subscribeMap) {
				subscribeInfo = this.subscribeMap[key];
				if (subscribeInfo.channel === channel && subscribeInfo.id === id) {
					return subscribeInfo;
				}
			}

			return null;
		},

		// 根据唯一键获取订阅信息
		getSubscribe: function (key) {
			return this.subscribeMap[key];
		},

		// 删除订阅数据
		removeSubscribeByChannelAndId: function (channel, id) {
			let subscribeInfo = this.getSubscribeByChannelAndId(channel, id);
			if (subscribeInfo) {
				this.removeSubscribe(subscribeInfo.key);
			}
			return subscribeInfo;
		},

		// 根据唯一键删除订阅信息
		removeSubscribe: function (key) {
			try {
				delete this.subscribeMap[key];
			} catch (e) {
				this.subscribeMap[key] = null;
			}
		},

		// 连接成功后的回调方法
		onOpen: function (open) {
			this.connectSuccessTime = new Date();
			console.info("创建WebSocket连接成功，耗时：%dms", (this.connectSuccessTime.getTime() - this.connectTime.getTime()));
			this.changeStatus("connected"); // 标记为已连接

			// 执行配置的onopen方法
			if (typeof this.onopen == "function") {
				try {
					this.onopen(open);
				} catch (e) {
					console.error("执行自定义的this.onopen()方法失败");
					console.error(e);
				}
			}

			// 执行延迟方法
			this.runDelayFuns();

			return this;
		},

		// 接收到消息的回调方法
		onMessage: function (event) {
			// 执行配置的onmessage方法
			if (typeof this.onmessage == "function") {
				try {
					this.onmessage(event);
				} catch (e) {
					console.error("执行自定义的this.onmessage()方法失败");
					console.error(e);
				}
			}

			// 执行订阅的方法
			if (event && event.data) {
				let self = this;
				wscUtils.toJson(event.data, function (json) {
					// 判断是否为订阅的频道返回的消息
					if (typeof json === "object" && json.key) {
						self.runListener(json, event);
					}
				});
			}

			return this;
		},

		// 连接关闭的回调方法
		onClose: function (close) {
			// 执行配置的onclose方法
			if (typeof this.onclose == "function") {
				try {
					this.onclose(close);
				} catch (e) {
					console.error(e);
				}
			}

			// 变更状态，清理数据
			let status = this.status === "error" ? "" : "closed";
			if (this.ws) {
				this.clear(status);
			} else {
				if (this.status !== "error") {
					this.changeStatus(status);
				}
			}

			console.info("WebSocket连接已关闭");
			console.info(close);

			return this;
		},

		// 连接发生错误的回调方法
		onError: function (error) {
			// 执行配置的onerror方法
			if (typeof this.onerror == "function") {
				try {
					this.onerror(error);
				} catch (e) {
					console.error(e);
				}
			}

			// 变更状态，清理数据
			this.changeStatus("error");
			this.clear();
			this.lastError = error;

			console.error("WebSocket连接出错了");
			console.error(error);

			return this;
		},

		// 发送消息到服务端
		send: function (msg) {
			if (!msg) {
				wscUtils.alert("消息不能为空");
				return this;
			}
			if (!msg.key) {
				wscUtils.alert("消息的key不能为空");
				return this;
			}
			if (!msg.type) {
				wscUtils.alert("消息的类型不能为空");
				return this;
			}

			if (this.ws == null) {
				wscUtils.alert("WebSocket还未连接，无法发送消息");
				return this;
			}

			if (typeof msg != "string") {
				msg = JSON.stringify(msg);
			}
			this.ws.send(msg);

			return this;
		}
	});

	window.WebSocketClient = WebSocketClient;
})(window['WebSocketClient']);