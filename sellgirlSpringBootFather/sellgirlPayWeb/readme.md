# run
## 运行jar包
1. 本web项目当前pom是jdk1.8，在jdk17下运行报错：
Could not initialize class org.codehaus.groovy.vmplugin.v7.Java7
可以如下方式运行：
"D:\Program Files\Java\jdk1.8.0_201\bin\java" -jar ./target/sellgirlPayWeb-0.0.4.jar
或
"D:\Program Files\Java\jre-1.8\bin\java" -jar ./sellgirlPayWeb-0.0.4.jar
2. 可能需要复制文件夹lib XmlConfig SystemLocalData LocalData 

# 视图位置
src\main\resources\templates\...
1. 之前gamepad的做法是先从“SystemLocalData/Txt/gamepad2.html”读取正文内容，在程序中替换生成字符串，
2. 然后再把用springView的内置方法把字符串用utext属性放到src\main\resources\templates\的模版内

# 商城
## 商城功能
### 产品模块(不需要登陆的主页)
com.sellgirl.sellgirlPayWeb.product
### 用户模块(管理登陆)
com.sellgirl.sellgirlPayWeb.user
### 其它类
com.sellgirl.sellgirlPayWeb.shop

## 开发待办
1. book.html等页面需要改为template/product2里面的需要积分才能看的版本
2. 考虑要不要使用@Transactional事务回滚(加积分,改订单状态的过程)
3. 找回密码的功能(修改密码的功能也没有?)
5. cloudflare加速
6. resource-search.html 页面搜索中文后, 如果因为登录失效跳到登录页, 之后回来url变为xxx?q=??
   看看怎么处理好
7. webp图片生成缩略图报错,计算不了width
   sgJavaHelper unitTest org.sellgirlPayHelperNotSpring.UncheckImg
8. cli测试支付接口
9. 做aiAgent,语音操作cli,识别内鬼,操作图片...之类
10. 要不要统计resource-detail?id=x的访问次数,记录喜好

## 测试地址

# 开发指南
## 打包
1. mvn clean package -Pprod-build  
  //这样可以排除yml,之后把yml放在jar的同级目录即可
2. 排除的文件在sellgirlPayWeb/pom.xml中设置, 比如有:
application-prod.yml, static/resourceImg/** ...
注意这些排除项有可能和父项目的include(eclipse开发环境运行时需要)少许冲突
//排除的这些文件, 视情况, 需要另外复制到jar运行时的同级目录(比如static/resourceImg/...)

## 前端post

```
    <script src="/sg/js/jq/jquery-2.1.4.js"></script>
    
		  $.post("/PostSign",
		  {
		    name:"Donald Duck",
		    city:"Duckburg"
		  },
		  function(data,status){
		    //alert("Data: " + data + "\nStatus: " + status);
			callback({success:data.success,message:data.msg,signDays:data.data},status);
		  })
		  .success(function() { /*alert("second success");*/ })//grid进入
		  .error(function() { alert("error"); alert(JSON.stringify(arguments))})//tree进入
		  .complete(function(XMLHttpRequest, textStatus){})
		  ;
```

## 确定弹窗
1.见 templates/Product/index.html 升级模态框

	```
	<button class="btn btn-sm btn-outline-warning me-2" data-bs-toggle="modal" data-bs-target="#upgradeModal">⭐ 升级资源站</button>
	```
2.

	```
	<!-- 升级模态框 -->
	<div class="modal fade" id="upgradeModal" tabindex="-1" aria-labelledby="upgradeModalLabel" aria-hidden="true">
	    <div class="modal-dialog">
	        <div class="modal-content">
	            <div class="modal-header">
	                <h5 class="modal-title" id="upgradeModalLabel">🔓 确定消耗1积分解锁</h5>
	                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
	            </div>
	            <div class="modal-body">
	                <p>建议先查看网盘连接是否过期</p>
	                <!--<div class="mb-3">
	                    <label class="form-label">邀请码</label>
	                    <input type="text" class="form-control" id="modalInviteCode" placeholder="请输入邀请码">
	                </div>-->
	                <div id="modalUpgradeMessage"></div>
	            </div>
	            <div class="modal-footer">
	                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">取消</button>
	                <button type="button" class="btn btn-primary" id="modalUpgradeBtn">立即升级</button>
	            </div>
	        </div>
	    </div>
	</div>
	```
3.关闭弹窗 

	```
    setTimeout(() => {
	    location.reload(); //暂不知道怎么关闭模态窗,直接刷新
	    //upgradeModal.style.display='none'; //好像是这样关;显示是'block'
	}, 1500);
	```

	

## 商品图片
src/main/resources/static/bookImg/cover/ 这个文件夹的图片不上传到git, 里面的图片名对应mysql的book_id，所以mysql数据不能随便清理，否则图片名对应不上。UncheckImportBook.testImportBook()方法可以把数据导入到book表，同时处理cover图

## 前端页面
1. templates/Product 正在用的版本
2. templates/Product/bak, templates/Product2 是各个版本的历史备份
3. static/assets,static目录下的 announcement.js,common.js,data-,books.js,data-resources.js 是样式和本地json测试数据
4. 对接百度网盘api(先确定隐私问题,再决定做不做)

## swagger接口文档
http://127.0.0.1:8080/v2/api-docs	这个是json格式,
是供单元测试org.sellgirl.sellgirlPayWeb.controller.Doc类使用的

# 运营指南
电脑需jre1.8环境,
mysql(我装的5.1.46-community), 服务器改用mysql  8.0.45-0ubuntu0.22.04.1

## 发布
建议服务器环境：Ubtuntu 22.04 LTS jdk1.8

### jar发布方式
1. 安装mysql后用脚本 sql/sgshop.sql 初始化数据库
2. 把网站图片复制到jar包同级目录下的 static/bookImg/**.jpg
3. cmd命令行运行 "D:\Program Files\Java\jre-1.8\bin\java" -jar ./sellgirlPayWeb-0.0.4.jar
4. 浏览器访问http://localhost:28303/

### 云ubuntu
1. scp -r D:\cache\html1\shop root@你的服务器IP:~/myapp/
    更新时,通常只上传jar就行了
2. ssh root@你的服务器IP
3. cd ~/myapp/shop
    可能需要修改 sudo nano application.yml
4. screen -S myapp
5. java -jar sellgirlPayWeb-0.0.4.jar
6. 访问 http://www.bdhome.xyz/

## 系统参数
1. 邀请码在 SystemLocalData/Txt/shop/inviteCode.txt
2. 公告 static/announcement.js
3. 防爬设置 application.yml 的 "antispider"部分, 修改后用 sudo systemctl restart myweb 重启网站
antispider:
    ip:
        window-seconds: 5	#5秒内
        max-count: 10	#最多10次
        block-minutes: 60	#封60秒
        
## 测试账号
1. normal aaa:aaaaaa1
2. resource fff:ffffff1

## xmpp
1. 9090页面管理员账号 admin:2557667040@qq.com:axDvgYjIlk35!