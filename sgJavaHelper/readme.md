安装包:
mvn install:install-file -DgroupId=pf.java -DartifactId=pfHelperNotSpring -Dversion=0.0.1-SNAPSHOT -Dfile=pfHelperNotSpring-0.0.1-SNAPSHOT.jar -Dpackaging=jar -DgeneratePom=true

win10上要在git-bash上执行（否则不会自动更新maven-plugins导致报错）
mvn install:install-file -Dfile=sqljdbc42.jar -Dpackaging=jar -DgroupId=com.microsoft.sqlserver -DartifactId=sqljdbc42 -Dversion=6.0

发到私有maven(nexus)
mvn deploy

# pom引用
```		<pfHelperNotSpring.version>0.0.5</pfHelperNotSpring.version>		
        <dependency>
            <groupId>pf.java</groupId>
            <artifactId>pfHelperNotSpring</artifactId>
            <version>${pfHelperNotSpring.version}</version>
            <exclusions>
	        	<exclusion>
					<groupId>org.slf4j</groupId>
					<artifactId>slf4j-simple</artifactId>
		        </exclusion>
		    </exclusions> 
        </dependency>
        
      <sgHelperNotSpring.version>0.0.42</sgHelperNotSpring.version>
		<dependency>
			<groupId>com.sellgirl</groupId>
			<artifactId>sgJavaHelper</artifactId>
			<version>${sgHelperNotSpring.version}</version>
			<exclusions>
				<exclusion>
					<groupId>org.slf4j</groupId>
					<artifactId>slf4j-api</artifactId>
				</exclusion>
				<exclusion>
					<groupId>org.slf4j</groupId>
					<artifactId>slf4j-simple</artifactId>
				</exclusion>
			</exclusions>
		</dependency>
        
		<dependency>
            <groupId>jaxen</groupId>
            <artifactId>jaxen</artifactId>
            <version>1.1.1</version>
        </dependency>
        <dependency>
            <groupId>dom4j</groupId>
            <artifactId>dom4j</artifactId>
            <version>1.6.1</version>
        </dependency>
```

win10上要在git-bash上执行（否则不会自动更新maven-plugins导致报错）
mvn install:install-file -Dfile=sqljdbc42.jar -Dpackaging=jar -DgroupId=com.microsoft.sqlserver -DartifactId=sqljdbc4 -Dversion=6.0

异常处理:
1.sqlExecute里面
					try {
						transferItem.BeforeTransferAction.go(transferItem);
					}catch(Exception e) {
						this.SetError(new Exception(PFDataHelper.FormatString("任务{0}的BeforeTransferAction报错", transferItem.getHashId())));
						throw e;
					}
					
		
[sys_limit_id] 的作用:			
Transfer迁移数据时,如果sql提供了sys_limit_id属性,抛错误时会提供最大值
如:
1.脚本:
select
...,
a.id as sys_limit_id
from mall_center_member.member_info a
2.PFSqlExecuteBase.GetError()
里面的lastInsertedId就是通过sys_limit_id计算的

sys_limit_id通常用主键索引列,通常是不读到目标表的字段


说明:
1.原则上本项目不引用注入框架,所以提供了 IPFConfigMapper.getBeanInstance方法来对接注入框架的实例化方法

[lib]
sellgirlPayHelperNotSpring的lib文件夹内的文件用于测试和参考,不打包

# 混淆打包 proguard

## 本项目混淆打包
1. 在本项目的 proguard目录下执行 java -jar proguard.jar @proguard2.pro (只混淆不编译) 测试可用 
2. //proguard2.pro指定哪些类公开

## 三方项目引用sgJavaHelper时的混淆配置
ISGUnProGuard接口
推荐第三方引用sgJavaHelper时, 这样配置proguard:
1. 不混淆的类继承ISGUnProGuard
public enum SGCharacter implements ISGUnProGuard ...

2. proguard.pro配置
-keep class com.mygdx.game.** implements com.sellgirl.sgJavaHelper.ISGUnProGuard{*;}


[email]
因为本项目的email库是provided,所以
android发email要自行引用
        implementation 'com.sun.mail:android-mail:1.6.2'
        implementation 'com.sun.mail:android-activation:1.6.2'
pc发email要自行引用
        implementation 'com.sun.mail:javax.mail:1.6.2'
        
# 注入服务
对SGDataHelper注入各种服务提供者
## 注入日志服务
		SGDataHelper.sgLog=new SGLibGdxLog();
		
	