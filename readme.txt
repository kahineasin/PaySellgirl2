sellgirl上的运行地址:
https://pay.sellgirl.com:44303/pcdeskimg.jpg
https://pay.sellgirl.com:44303/pcdeskimgmanager

打包jar:
clean后install

项目结构:(->表示依赖)
api->无         //好舒服声明
sellgirlPayHelper->无	//帮助类
sellgirlPayDomain->无 //领域对象
sellgirlPayDao->Domain  //mybatis
sellgirlPayService->Dao  //服务和服务实现(可以引用其它子项目的biz)同时也实现了api的接口(unicomShop项目的mq也放这里)
                    ->api
sellgirlPayMq->  //mq任务
sellgirlPayWeb->Service  //是spring boot入口(在crm项目里,web反而叫service)

项目改名后需要更改后才能运行的几个地方:
1.App启动类@ComponentScan @MapperScan(basePackages = {"com.sellgirl.*"})
2.改App启动类所在的命名空间和controller的命名空间 (会导致程序能启动,但所有url无效)
3.Web\bom.xml下的启动类全名
4.application.properties里改mybatis的命名设置,DataSourceAspect改空间名,TestDAO.xml里面的命名空间,
  aop里@Pointcut("execution(* com.sellgirl.*.service.*.*(..))")(会导致Dao的Bean找不到)
5.可能是因为dataSource名命空间不对(提示dataSource没有url)

下面是一些测试的地址,这些地址如果正常访问,应该就能说明项目配置没问题:
1.http://localhost:28503/crm1/getusersmodify
2.http://localhost:28503/crm1/getsalesday
3.http://localhost:28503/webtest
4.http://localhost:28503/showerror
https://localhost:44303/sashamobiledesktopflip.jpg
https://localhost:44303/sashapcdesktop.jpg


下面是一些测试各类型mq的地址，需要根据情况配置对应消费者的地址才行
1.http://localhost:28503/sendmq
2.http://localhost:28503/sendmq2
3.http://localhost:28503/sendrocketmq
4.http://localhost:28503/sendhyzlupdatemq

mq的使用:
1.监听,在com.sellgirl.sellgirlPayMq 
    加继承PFConsumerTask的Component类
2.发送,在com.sellgirl.sellgirlPayMq.producer 
    加继承PFProdutTask的类XxProduct
  然后PFMqHelper.BuildProducer("message content",new XxProduct());调用
  
rest返回值:
	@GetMapping("/")
	public AbstractApiResult<?> index() {
        return AbstractApiResult.success();
        return AbstractApiResult.error("xx");

GitLab:
ssh://git@gitlab.sellgirl.com:23/wxj/receiveunicom.git
http://gitlab.sellgirl.com/wxj/receiveunicom.git

R
sellgirlPay 
Project ID: 4
0
对接联通核心数据,监听mq等

The repository for this project is empty
You can create files directly in GitLab using one of the following options.

Command line instructions
You can also upload existing files from your computer using the instructions below.


Git global setup
git config --global user.name "吴肖均"
git config --global user.email "wxj@sellgirl.com"

Create a new repository
git clone http://gitlab.sellgirl.com/wxj/receiveunicom.git
cd receiveunicom
touch README.md
git add README.md
git commit -m "add README"
git push -u origin master

Push an existing folder
cd existing_folder
git init
git remote add origin http://gitlab.sellgirl.com/wxj/receiveunicom.git
git add .
git commit -m "Initial commit"
git push -u origin master

Push an existing Git repository
cd existing_repo
git remote rename origin old-origin
git remote add origin http://gitlab.sellgirl.com/wxj/receiveunicom.git
git push -u origin --all
git push -u origin --tags

2020-02-18 20：35

[cmd提交到github的方式]
参考：https://www.jianshu.com/p/750a57374177
1.首先在eclipse里面commit
2.cmd进入项目目录如:D:\eclipse_workspace_sellgirlPay\sellgirlPay
  (此目录下有隐藏的.git文件夹)
3.cmd依次输入：
git add .
git commit     //这样需要:wq 退出
git commit -m 'your message'
git push --set-upstream https://github.com/kahineasin/PaySellgirl.git master

docker发布:
1.在服务器新建一个docker文件夹，将maven打包好的jar包和Dockerfile文件复制到服务器的docker文件夹下
2.docker build -t springbootdemo4docker .
3.docker run springbootdemo4docker .