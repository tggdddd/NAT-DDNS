# 内网穿透

## 描述

将本地服务映射到网络中，供其他网络端调用

## 使用说明

项目包含2个部分，分别是客户端与服务端
客户端由本地机器运行，服务器由服务器运行，没错，需要服务器

## 真的使用说明

### 1. 启动服务端

tip:webSocketServer下的server模块为socket调用的jar包，若有修改，需要将打包的jar包替换socket下的resource/icu/stopit/server.jar
启动webSocketServer下的socket模块

### 2. 启动客户端

将client下application.yaml修改为服务端地址和端口
本地机器启动client模块

### 3. 打开client配置页面

打开 client的启动页，设置好配置名（暂时无用），映射路径，服务器的映射端口
访问服务端地址：配置的端口 即访问映射路径

## 待完善

* [ ]  页面美化
* [ ]  服务端端口检测
* [ ]  支持反向代理
* [ ]  优化请求调用
* [ ]  测试

