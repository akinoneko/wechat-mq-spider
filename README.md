### 爬虫配置项说明

##### 1. 代理服务器配置

使用Anyproxy作为代理服务器,需要安装Node环境,安装anyproxy

```shell
npm -g install anyproxy
```

修改anyproxy的规则文件,macOS下为`/usr/local/lib/node_modules/anyproxy/lib/`,修改rule_default.js中的replaceServerResDataAsync方法

```javascript
if (/mp\/getmasssendmsg/i.test(req.url)) { //当链接地址为公众号历史消息页面时(第一种页面形式)
    if (serverResData.toString() !== "") {
        try { //防止报错退出程序
            var reg = /msgList = (.*?);/; //定义历史消息正则匹配规则
            var ret = reg.exec(serverResData.toString()); //转换变量为string
            HttpPost(ret[1], req.url, "/spider/getWechatMsgJson.do"); //这个函数是后文定义的，将匹配到的历史消息json发送到自己的服务器
            var http = require('http');
            console.log(123);
            http.get('http://127.0.0.1:8080/spider/getWechatHistory.do',
            function(res) { //这个地址是自己服务器上的一个程序，目的是为了获取到下一个链接地址，将地址放在一个js脚本中，将页面自动跳转到下一页。后文将介绍getWxHis.php的原理。
                console.log(res);
                res.on('data',
                function(chunk) {
                    callback(chunk + serverResData); //将返回的代码插入到历史消息页面中，并返回显示出来
                })
            });
        } catch(e) { //如果上面的正则没有匹配到，那么这个页面内容可能是公众号历史消息页面向下翻动的第二页，因为历史消息第一页是html格式的，第二页就是json格式的。
            try {
                var json = JSON.parse(serverResData.toString());
                if (json.general_msg_list != []) {
                    HttpPost(json.general_msg_list, req.url, "/spider/getWechatMsgJson.do"); //这个函数和上面的一样是后文定义的，将第二页历史消息的json发送到自己的服务器
                }
            } catch(e) {
                console.log(e); //错误捕捉
            }
            callback(serverResData); //直接返回第二页json内容
        }
    } else {
        callback(serverResData); //直接返回第二页json内容
    }
} else if (/mp\/profile_ext\?action=home/i.test(req.url)) { //当链接地址为公众号历史消息页面时(第二种页面形式)
    try {
        console.log("enter profile_ext!!!!!!!");
        var reg = /var msgList = \'(.*?)\';/; //定义历史消息正则匹配规则（和第一种页面形式的正则不同）
        var ret = reg.exec(serverResData.toString()); //转换变量为string
        HttpPost(ret[1], req.url, "/spider/getWechatMsgJson.do"); //这个函数是后文定义的，将匹配到的历史消息json发送到自己的服务器
        var http = require('http');
        http.get('http://127.0.0.1:8080/spider/getWechatHistory.do',
        function(res) { //这个地址是自己服务器上的一个程序，目的是为了获取到下一个链接地址，将地址放在一个js脚本中，将页面自动跳转到下一页。后文将介绍getWxHis.php的原理。
            console.log("statusCode: " + res.statusCode);
            res.on('data',
            function(chunk) {
                console.log("chunk ->" + chunk);
                //console.log("chunk_serda ->" +(serverResData+chunk));
                callback(serverResData + chunk); //将返回的代码插入到历史消息页面中，并返回显示出来
            })
        });
    } catch(e) {
        callback(serverResData);
    }
} else if (/mp\/profile_ext\?action=getmsg/i.test(req.url)) { //第二种页面表现形式的向下翻页后的json
    try {
        var json = JSON.parse(serverResData.toString());
        if (json.general_msg_list != []) {
            HttpPost(json.general_msg_list, req.url, "/spider/getWechatMsgJson.do"); //这个函数和上面的一样是后文定义的，将第二页历史消息的json发送到自己的服务器
        }
    } catch(e) {
        console.log(e);
    }
    callback(serverResData);
} else if (/mp\/getappmsgext/i.test(req.url)) { //当链接地址为公众号文章阅读量和点赞量时
    try {
        HttpPost(serverResData, req.url, "/spider/getWechatMsgExt.do"); //函数是后文定义的，功能是将文章阅读量点赞量的json发送到服务器
    } catch(e) {

}
    callback(serverResData);
} else if (/s\?__biz/i.test(req.url) || /mp\/rumor/i.test(req.url)) { //当链接地址为公众号文章时（rumor这个地址是公众号文章被辟谣了）
    try {
        var http = require('http');
        HttpPost(serverResData.toString(), req.url, "/spider/saveWechatArticle.do"); //将文章的html页发送给服务器处理
        http.get('http://127.0.0.1:8080/spider/getWechatPost.do',
        function(res) { //这个地址是自己服务器上的另一个程序，目的是为了获取到下一个链接地址，将地址放在一个js脚本中，将页面自动跳转到下一页。后文将介绍getWxPost.php的原理。
            res.on('data',
            function(chunk) {
                console.log(serverResData + chunk);
                callback(serverResData + chunk);
            })
        });
    } catch(e) {
        callback(serverResData);
    }
} else {
    callback(serverResData);
}
```

在rule_default.js文件的底部加上

```javascript
function HttpPost(str, url, path) { //将json发送到服务器，str为json内容，url为历史消息页面地址，path是接收程序的路径和文件名
    var http = require('http');
    var data = {
        str: encodeURIComponent(str),
        url: encodeURIComponent(url)
    };
    content = require('querystring').stringify(data);
    var options = {
        method: "POST",
        host: "www.domain.com",
        //注意没有http://，这是服务器的域名。
        port: 80,
        path: path,
        //接收程序的路径和文件名
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8',
            "Content-Length": content.length
        }
    };
    var req = http.request(options,
    function(res) {
        res.setEncoding('utf8');
        res.on('data',
        function(chunk) {
            console.log('BODY: ' + chunk);
        });
    });
    req.on('error',
    function(e) {
        console.log('problem with request: ' + e.message);
    });
    req.write(content);
    req.end();
}
```

`sudo anyproxy --root`生成证书后,使用`sudo anyproxy -i`启动程序,在终端上设置WiFi的代理地址为代理服务器的地址,端口8001,终端访问[http://localhost:8002/fetchCrtFile]([http://localhost:8002/fetchCrtFile)安装证书





