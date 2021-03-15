# hydrogen-dns-server

基于 SpringBoot 和 Netty 的简单的 DNS 服务器，仅支持 A 和 AAAA 查询。

### 使用方法：

首先修改配置 `dns.local.config-folder-path` 到指定的文件夹，例如加上参数 `--dns.local.config-folder-path=./data`

然后再文件夹下创建 `A.csv` 或者 `AAAA.csv`，内容格式参考代码中的 `data/dns-data-file/A.csv` 文件内容

运行本项目。

### 接下来

1. 实现作为 DNS 中继，缓存并保存上游 DNS 结果
1. 实现可指定一些域名，主动从上游更新地址
1. 实现可指定一个上游 DNS 服务器的列表
1. 一个简单的增删改查界面