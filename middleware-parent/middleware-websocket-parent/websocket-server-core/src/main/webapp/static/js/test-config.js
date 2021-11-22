var profile = (window.location.search.substr(window.location.search.indexOf("=") + 1)) || window["profileActive"] || "dev"; // 通过maven打包命令生成

var config = {
	dev: {
		debug: true,
		wsUrl: "ws://localhost:" + window["serverPort"] + "/websocket",
	},
	devkt: {
		debug: true,
		wsUrl: "ws://localhost:" + window["serverPort"] + "/websocket",
	},
	test: {
		debug: true,
		wsrUrl: "http://192.168.168.15:9000/japi/ws/router",
		wsUrl: "ws://192.168.168.15:9001/websocket",
	},
	prod: {
		debug: false,
		wsrUrl: "http://wsmanage.nbwjw.gov.cn/japi/ws/router",
		wsUrl: "ws://ws.nbwjw.gov.cn/websocket",
	},
	prodkt: {
		debug: true,
		wsrUrl: "http://wsmanage.nbwjw.gov.cn/japi/ws/router",
		wsUrl: "ws://ws.nbwjw.gov.cn/websocket",
	}
};

if (!config[profile]) {
	alert("环境变量“" + profile + "”无效，转换为默认值：dev");
	profile = "dev";
}
console.info("当前环境变量：" + profile + "\r\n配置信息：" + JSON.stringify(config[profile]));

$.extend(config, config[profile]);
