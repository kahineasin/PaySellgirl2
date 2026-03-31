# 项目简介
本项目为sellgirl java工具库(Java tool library),一切应用方法应有尽有,如 jdbc多态, 定时作业, 动态值类型转换, 日期运算和显示, 图像对齐
1. pfHelperNotSpring ,这是工具库 
2. sellgirlPayWeb 这是一些前端页面(测试和展示图型结果) 

# pfHelperNotSpring
1. com.sellgirl.pfHelperNotSpring.config.PFDataHelper , 这里有大部分的工具常用方法
2. com.sellgirl.pfHelperNotSpring.sql , jdbc多态处理的方法 

# 测试地址

下面是一些测试的地址,这些地址如果正常访问,应该就能说明项目配置没问题:
0. http://localhost:28303 
1. http://localhost:28303/crm1/getusersmodify 
2. http://localhost:28303/crm1/getsalesday 
3. http://localhost:28303/webtest 
4. http://localhost:28303/showerror 
5. https://localhost:44303/sashamobiledesktopflip.jpg 

##sellgirl上的运行地址
### 正式
1. http://pay.sellgirl.com:28303/pcdeskimgmanager
3. http://pay.sellgirl.com:28303/mobiledeskimg.jpg
3. http://pay.sellgirl.com:28303/sashacrossmobiledesktop.jpg
4. http://pay.sellgirl.com:28303/sashapcdesktop.jpg

### https已没用
4. https://pay.sellgirl.com:44303/pcdeskimgmanager
7. https://pay.sellgirl.local:44303/pcdeskimgmanager
2. https://sellgirl.com:44303/sashaliethumbnail?w=800&h=0&t=Anticlockwise90 
2. https://pay.sellgirl.com:44303/pcdeskimg.jpg
3. https://pay.sellgirl.com:44303/mobiledeskimg.jpg
5. https://pay.sellgirl.com:44303/sashaliethumbnail?w=800&h=0&t=Anticlockwise90
6. https://pay.sellgirl.local:44303/mobiledeskimg.jpg
8. https://pay.sellgirl.local:44303/getpcdeskimgname

### a开发环境
9. http://localhost:28303/mobiledeskimg.jpg
10. http://localhost:28303/getpcdeskimgname
11. http://localhost:28303/pcdeskimgmanager
12. http://localhost:28303/sashacrossmobiledesktop.jpg
13. http://localhost:28303/sample/eval?code=1%2B2
15. http://localhost:28303/swagger-ui.html
16. http://localhost:28303/story/sasha
17. http://localhost:28303/sashapcdesktop.jpg
18. http://localhost:28303/story/gamePad


# 打包
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

# 如果要改项目名
项目改名后需要更改后才能运行的几个地方:
1.App启动类@ComponentScan @MapperScan(basePackages = {"com.sellgirl.*"})
2.改App启动类所在的命名空间和controller的命名空间 (会导致程序能启动,但所有url无效)
3.Web\bom.xml下的启动类全名
4.application.properties里改mybatis的命名设置,DataSourceAspect改空间名,TestDAO.xml里面的命名空间,
  aop里@Pointcut("execution(* com.sellgirl.*.service.*.*(..))")(会导致Dao的Bean找不到)
5.可能是因为dataSource名命空间不对(提示dataSource没有url)

# Helper帮助类的说明
## 常用方法
` PFDataHelper.ObjectToInt(); `
## 表达式解析
` namespace express `
## 定时任务
` namespace task `
## sql使用
```
		if(null==q.getUserId()&&null==q.getUserName()) {
			return null;
		}
		ISGJdbc dstJdbc=JdbcHelper.GetShop();
		try (ISqlExecute myResource = SGSqlExecute.Init(dstJdbc)) {
		  	SGSqlWhereCollection query =myResource.getWhereCollection();
			//query.setIgnoreNullValue(false);
            query.Add("user_id",q.getUserId());
            query.Add("user_name",q.getUserName());
          	String SqlString = SGDataHelper.FormatString( 
          		"select * from user " +
          		"{0} " 
          		, 
          		        query.ToSql()
          		    );
          	SGDataTable dt= myResource.GetDataTable(SqlString,null);
          	if(null!=dt&&!dt.IsEmpty()) {
	          	List<User> list= dt.ToList(User.class, (obj,row,c)->{
	          		obj.setUserName(row.getStringColumn(SqlString));
	          		obj.setInvitationCode(SqlString);
	          	});
          		return list.get(0);
          	}else {
          		return null;
          	}
		} catch (Throwable e) {
			throw new RuntimeException(e);
		} 
```
### 获得刚插入行的id
```
			bookCreate model=new bookCreate();
			//...
			model.setCreate_date(SGDate.Now());			
			SGSqlInsertCollection insert=dstExec.getInsertCollection();			
			insert.InitItemByModel(model);
			SGSqlCommandString sql=new SGSqlCommandString(
					SGDataHelper.FormatString(
"insert into sg_book ({0}) values ({1})",insert.ToKeysSql(),insert.ToValuesSql())
					);
			int r=dstExec.ExecuteSqlInt(sql, null, true);
			System.out.println("id2:"+dstExec.GetLastInsertedId());
```
### 复制表，备份表(支持大数据，很推荐)
```
//例子 UncheckImportBook.testBulkCopy
ResultSet srcDr = srcExec.GetHugeDataReader("select * from sg_book");
dstExec.HugeBulkReader(null, srcDr,"sg_book", null, null, null);
```
### 修改表
```
		ISGJdbc jdbc=JdbcHelper.GetShop();
		try (ISqlExecute dstExec = SGSqlExecute.Init(jdbc)) {
			dstExec.AutoCloseConn(false);
            List<String> srcFieldNames = new ArrayList<String>();
            srcFieldNames.add("vip_order_id");
            srcFieldNames.add("status");
            ResultSetMetaData dstMd = dstExec.GetMetaDataNotClose("sg_vip_order", srcFieldNames);

            PFSqlUpdateCollection update = dstExec.getUpdateCollection(dstMd);

            
            String[] mArray = new String[] {"status"};
            String[] primaryKeys = new String[] {"vip_order_id"};
            update.UpdateFields(mArray);
            update.PrimaryKeyFields(false, primaryKeys);

            update.Set("vip_order_id", id);
            update.Set("status", com.sellgirl.sellgirlPayWeb.pay.model.OrderStatus.已支付.ordinal());
            
			
			SGSqlCommandString sql=new SGSqlCommandString(
					SGDataHelper.FormatString(
							" update sg_vip_order set  {0} {1} limit 1",
							update.ToSetSql(),
			                update.ToWhereSql()
					));
			dstExec.close();
			int r=dstExec.ExecuteSqlInt(sql, null, true);
			dstExec.close();
			if(0<r) {
				return dstExec.GetLastInsertedId();
			}
			//System.out.println("id2:"+dstExec.GetLastInsertedId());
		} catch (Exception e) {
			e.printStackTrace();
		}
```
### 批量更新
```
	public void testMySqlDriverUpdateSpeed() throws Exception {
		initPFHelper();
		ISGJdbc dstJdbc = JdbcHelperTest.GetMySqlTest2Jdbc();//ok
		SGSpeedCounter[] speed=new SGSpeedCounter[]{null};
		int[] startCnt=new int[]{0};
		try (ISqlExecute myResource = SGSqlExecute.Init(dstJdbc)) {
			myResource.GetConn().setAutoCommit(false);
			myResource.SetInsertOption(a->a.setProcessBatch(50000));
			int insertCnt=1000000;//1000000;
			int[] idx= new int[]{0};
			boolean b =
					myResource.doUpdateList(
							Arrays.asList(new String[]{"id","col1","col2"}),
							"test_tb_04",
							new String[]{"id"},
							(a, b2, c) -> a < insertCnt,
							(a) -> {
								Map<String,Object> map=new HashMap<>();
								map.put("id", idx[0]);
								map.put("col1", String.valueOf(idx[0]));
								map.put("col2", String.valueOf(idx[0]*100));
								idx[0]++;
								return map;
							},
							null,
							a -> {
								// 测试速度
								if(null==speed[0]){
									speed[0]=new SGSpeedCounter(com.sellgirl.sgJavaHelper.SGDate.Now());
									startCnt[0]=a;
								}
								System.out.println(speed[0].getSpeed(a-startCnt[0],com.sellgirl.sgJavaHelper.SGDate.Now()));
								System.out.println("ProcessBatch:"+myResource.GetInsertOption().getProcessBatch());
							},
							null);
		} catch (Exception e) {
			throw e;
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
		Thread.sleep(2000);//写日志的时间
	}
```


## 写日志
1. 实现ISGLog 

```
	public class SGLibGdxLog implements ISGLog {...}
	SGDataHelper.sgLog=new SGLibGdxLog();
```
2. print

```
    	SGDataHelper.getLog().print(
		SGDataHelper.FormatString("xx{0}",1)
    	);
```

## 发邮件
1. 配置 SGEmailSend 的public字段
2. SGEmailSend.SendMail()

## 上传文件到ubuntu(linux)
见sellgirlPayWeb项目的单元测试的:
org.sellgirl.sellgirlPayWeb.controller.model.ConcurrentSftpUpload
此方法未移到helper,因为有额外的小型库引用

下面是一些测试各类型mq的地址，需要根据情况配置对应消费者的地址才行
1.http://localhost:28303/sendmq
2.http://localhost:28303/sendmq2
3.http://localhost:28303/sendrocketmq
4.http://localhost:28303/sendhyzlupdatemq

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

## docker
打包地址:192.168.0.10/home/maxwell_api/docker
运行地址:10.20.3.19 (在任意目录上docker run其实是一样的)
注意下面每一步都要改当日的版本号
1. (192.168.0.10)上传bocker文件夹到192.168.0.10/home/maxwell_api/docker
   docker build -t uat-registry.perfect99.com/pf-maxwell-api:0.0.8-20220727 .
2. (这步可省略)docker tag pf.perfect99.com/pf-maxwell-consumer-0.0.1-20220704.jar uat-registry.perfect99.com/pf-maxwell-consumer:0.0.1
   (注意tag前面的地址一定要是仓库地址,因为push时是按tag名字来的,push的目标一定是tag前面的地址)
3. docker push uat-registry.perfect99.com/pf-maxwell-api:0.0.8-20220727
4. (10.20.3.19)docker run --name mesos_uploader --restart=always -v /data/nginx/fs/html/mesos_upload_file:/usr/local/app/upload_file -p 28303:28303 -d uat-registry.perfect99.com/pf-maxwell-api:0.0.8-20220727

# ssl
自己发证书的方法
https://my.oschina.net/u/4398701/blog/3583822

1. 生成证书


``` 
keytool -genkey -alias pay_sellgirl_com -storetype PKCS12 -keyalg RSA -keysize 2048 -keystore D:\download\pay_sellgirl_com_keystore.p12 -validity 36500 -dname "cn=pay.sellgirl.com,ou=sellgirl,o=sellgirl,l=ZhongShan,st=GuangDong,c=CN" -storepass sellgirl
```
``` 
keytool -genkey -alias sellgirl_com -storetype PKCS12 -keyalg RSA -keysize 2048 -keystore D:\download\sellgirl_com_keystore.p12 -validity 36500 -dname "cn=sellgirl.com,ou=sellgirl,o=sellgirl,l=ZhongShan,st=GuangDong,c=CN" -storepass sellgirl
```
 
2. 导出证书

``` 
keytool -export -alias pay_sellgirl_com -keystore pay_sellgirl_com_keystore.p12 -file pay_sellgirl_com_keystore.cer
```
``` 
keytool -export -alias sellgirl_com -keystore sellgirl_com_keystore.p12 -file sellgirl_com_keystore.cer
```

3. 安装证书
双击.cer文件安装，安装时要选择安装位置为"受信任的根机构"。

似乎也可以双击.p12文件来安装，但标准似乎是用.cer装。生成的证书服务器也装一下吧，浏览器的设置里面也可以装（但应该不必要）

4. 显示证书 （非必要）
https://jingyan.baidu.com/article/6079ad0eb284ad28ff86db18.html
keytool -list -v -keystore pay_sellgirl_com_keystore.p12 -storepass sellgirl


## 参数说明
https://blog.csdn.net/sayyy/article/details/78351512
keytool -genkeypair \
        -alias pay_sellgirl_com \
        -keyalg RSA \
        -keysize 4096 \
        -keypass sellgirl \
        -sigalg SHA256withRSA \
        -dname "cn=pay.sellgirl.com,ou=sellgirl,o=sellgirl,l=ZhongShan,st=GuangDong,c=CN" \
        -validity 3650 \
        -keystore pay_sellgirl_com_keystore.jks \
        -storetype JKS \
        -storepass sellgirl
keytool -genkeypair -alias pay_sellgirl_com -keyalg RSA -keysize 4096 -keypass sellgirl -sigalg SHA256withRSA -dname "cn=pay.sellgirl.com,ou=sellgirl,o=sellgirl,l=ZhongShan,st=GuangDong,c=CN" -validity 3650 -keystore pay_sellgirl_com_keystore.jks -storetype JKS -storepass sellgirl
## 似乎还需要这样
要发证书给签名机构?
https://blog.csdn.net/weixin_39171105/article/details/109054348
https://blog.csdn.net/sayyy/article/details/78351512
