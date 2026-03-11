package com.sellgirl.sellgirlPayWeb.projHelper;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.alibaba.fastjson.JSON;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class JwtHelper {

//    //private static final String SECRET = "9a96349e2345385785e804e0f4254dee";
//
//   // private static String ISSUER = "sys_user";
//
//    public static String CreateJWT(Map<String, String> claims,String secret){
////
//    	Calendar c=Calendar.getInstance();
//    	c.add(Calendar.MINUTE, +10);
//    	Date expireDatePoint=c.getTime();
//        try {
//            //使用HMAC256进行加密
//            Algorithm algorithm = Algorithm.HMAC256(secret);
//
//            //创建jwt
//            JWTCreator.Builder builder = JWT.create()
//                    //.withIssuer(ISSUER) //发行人
//                    //.thExpiresAt(expireDatePoint)//过期时间点
//                    ; 
//
//            //传入参数
//            claims.forEach((key,value)-> {
//                builder.withClaim(key, value);
//            });
//            
//            builder.withClaim("exp", expireDatePoint);
//
//            //签名加密
//            return builder.sign(algorithm);
//        } catch (IllegalArgumentException | UnsupportedEncodingException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    public static String CreateJWT2(Map<String, Object> claims,String secret){
//        String token = Jwts.builder()
//        		.setHeaderParam("typ", "JWT")
//                .setClaims(claims)
////                .setSubject(auth.getName())
//                .setExpiration(new Date(System.currentTimeMillis() + 60 * 60 * 24 * 1000))
////                .signWith(SignatureAlgorithm.HS512, secret).compact()
//                .signWith(SignatureAlgorithm.HS256, secret).compact()
//                ;
//        return token;
//    }
//    
//
//    public static String CreateJWT3(Map<String, Object> claims,String secret){
//        String token = Jwts.builder()
//        		.setHeaderParam("typ", "JWT")
//                .setClaims(claims)
////                .setSubject(auth.getName())
//                .setExpiration(new Date(System.currentTimeMillis() + 60 * 60 * 24 * 1000))
////                .signWith(SignatureAlgorithm.HS512, secret).compact()
//                .signWith(SignatureAlgorithm.HS256, secret).compact()
//                ;
//        return token;
//    }
    /**
     * 生成token
     * @param claims
     * @param expireDatePoint  过期时间点
     * @return
     */
    public static String genToken(Map<String, String> claims, Date expireDatePoint,String secret){

        try {
            //使用HMAC256进行加密
            Algorithm algorithm = Algorithm.HMAC256(secret);

            //创建jwt
            JWTCreator.Builder builder = JWT.create().
                    //withIssuer(ISSUER). //发行人
                    withExpiresAt(expireDatePoint); //过期时间点

            //传入参数
            claims.forEach((key,value)-> {
                builder.withClaim(key, value);
            });

            //签名加密
            return builder.sign(algorithm);
        } catch (IllegalArgumentException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
    
//    public static String genTokenForObject(Object claims, Date expireDatePoint,String secret){
//
//        try {
//            //使用HMAC256进行加密
//            Algorithm algorithm = Algorithm.HMAC256(secret);
//
//            //创建jwt
//            JWTCreator.Builder builder = JWT.create().
//                    //withIssuer(ISSUER). //发行人
//                    withExpiresAt(expireDatePoint); //过期时间点
//
//            //传入参数
//            builder.withClaim("claims", JSON.toJSONString(claims));
//            
////            claims.forEach((key,value)-> {            	
////            	if(value instanceof String) {
////                    builder.withClaim(key, value.toString());
//////            	}else if(value instanceof List<String>) {
//////                    ArrayList<String> aa=new ArrayList<String>().toArray();
//////                    builder.withArrayClaim(key, aa);
//////                    builder.withArrayClaim(key, ((Iterable)value).to);
////            	}else {
////                    builder.withClaim(key, JSON.toJSONString(value));            		
////            	}
////            });
//
//            //签名加密
//            return builder.sign(algorithm);
//        } catch (IllegalArgumentException | UnsupportedEncodingException e) {
//            throw new RuntimeException(e);
//        }
//    }

   /**
     * 解密jwt
     * @param token
     * @return
     * @throws RuntimeException
     */
    public static Map<String,String> verifyToken(String token, String secret) throws RuntimeException{
        Algorithm algorithm = null;
        try {
            //使用HMAC256进行加密
            algorithm = Algorithm.HMAC256(secret);
        } catch (IllegalArgumentException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

        //解密
        JWTVerifier verifier = JWT.require(algorithm)//.withIssuer(ISSUER)
        		.build();
        DecodedJWT jwt =  verifier.verify(token);
        Map<String, Claim> map = jwt.getClaims();
        Map<String, String> resultMap = new HashMap<>();
        map.forEach((k,v) -> resultMap.put(k, v.asString()));
        return resultMap;
    }
}
