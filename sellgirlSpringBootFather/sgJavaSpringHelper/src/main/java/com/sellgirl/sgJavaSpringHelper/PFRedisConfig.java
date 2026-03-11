package com.sellgirl.sgJavaSpringHelper;

import com.sellgirl.sgJavaSpringHelper.config.SGDataHelper;

import redis.clients.jedis.Jedis;

public class PFRedisConfig {
	/**
	 * 含端口
	 */
	protected String ip;
	protected String password;
	// protected static Jedis jedis;//为了不建立多个连接
	protected Jedis jedis;// 不要用static,多线程时,redis会有奇怪问题的

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Jedis open() {
		try {
			SGDataHelper.ReTry((a, b, c) -> {
				doOpen();
			});
		} catch (Exception e) {
			SGDataHelper.WriteError(new Throwable(), e);
		}
		return jedis;
	}

	// 这种留着旧连接的方式可能报错:redis.clients.jedis.exceptions.JedisConnectionException:
	// java.net.SocketException: Socket closed
	// cnblogs.com/kissazi2/archive/2012/09/04/2669830.html
//	public Jedis doOpen() {		
//		if(jedis==null) {
//			//Jedis jedis=null;
//	        //Jedis jedis = new Jedis("cloud.perfect99.com",10133);
//			if(ip.indexOf(':')>-1) {
//				String[] ips=ip.split(":");
//		         jedis = new Jedis(ips[0],Integer.valueOf(ips[1]));
//			}else {
//				 jedis = new Jedis(ip);
//			}
//	        // 如果 Redis 服务设置了密码，需要下面这行，没有就不需要
//	         jedis.auth(password); 
//	         System.out.println("连接成功");
//		}else if(!jedis.isConnected()) {
//			jedis.connect();
//	         System.out.println("重新连接成功");
//		}		
//        //查看服务是否运行
//        System.out.println("redis服务正在运行: "+jedis.ping());
//        return jedis;
//	}
	public static void close(Jedis jedis) {
		if (jedis == null) {
			return;
		}
		try {
			try {
				// System.out.println("close:1");
				jedis.disconnect();
			} catch (Exception e) {
				SGDataHelper.WriteError(new Throwable(), e);
			}
			try {
				// System.out.println("2");
				jedis.close();
			} catch (Exception e) {
				SGDataHelper.WriteError(new Throwable(), e);
			}
			// System.out.println("3");
			jedis = null;
			// System.out.println("4");
			System.out.println("redis服务头闭");
		} catch (Exception e) {
			SGDataHelper.WriteError(new Throwable(), e);
		}
	}

	public void close() {
		close(jedis);
	}

	public Jedis doOpen() {
		if (jedis != null) {
			close(jedis);
		}

		// Jedis jedis=null;
		// Jedis jedis = new Jedis("cloud.perfect99.com",10133);
		if (ip.indexOf(':') > -1) {
			String[] ips = ip.split(":");
			jedis = new Jedis(ips[0], Integer.valueOf(ips[1]));
		} else {
			jedis = new Jedis(ip);
		}
		// 如果 Redis 服务设置了密码，需要下面这行，没有就不需要
		if (!SGDataHelper.StringIsNullOrWhiteSpace(password)) {
			jedis.auth(password);
		}
		// System.out.println("连接成功");

		// 查看服务是否运行
		System.out.println("redis服务连接成功,正在运行: " + jedis.ping());
		return jedis;
	}
}
