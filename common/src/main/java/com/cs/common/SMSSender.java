package com.cs.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * @ClassName: SMSSender
 * @Description: TODO  发送 短信
 * @author henryfang1@163.com
 * @date 2012-9-9 下午03:51:34
 *
 */


public class SMSSender {
    private static Logger logger = LoggerFactory.getLogger(SMSSender.class);



    //发送传递的参数
    //http://www.ztsms.cn:8800/sendXSms.do?username=用户名&password=密码&mobile=手机号码&content=内容&dstime=&productid=产品ID&xh=留空
    private static String username;
    private static String password;
    private static String mobile;
    private static String content;
    private static String dstime;
    private static Integer productid;
    private static String xh;
    private static String sendurl;

    /**
     * 创建一个新的实例 SMSSender.
     *  助通科技提供的用户名与地址
     */
    /****
     * username	用户名（必填）
     * 	password	密码（必填）
     * 	mobile	手机号，多个手机号为用半角 , 分开，如13899999999,13688888888(最多100个，必填)
     * 	content	发送内容（必填）
     * 	dstime	定时时间，为空时表示立即发送（选填）
     * 	productid	产品id(必填)
     * 	xh	扩展号,留空
     * ***/


    public SMSSender() {
        username = "ssdk666"; //您的用户名
        password = "Ssdk1234"; //密码
        sendurl = "http://www.ztsms.cn/"; //助通提供的发送地址
        productid = 676767; //产品ID号
        xh = "东康"; //扩展号留空
    }

    public String sendSms(String mobile, String content) {
        String sendUrl = null;
        try {// 否则发到手机乱码
            sendUrl = sendurl + "sendXSms.do?username=" + username + "&password="+ password
                    + "&mobile=" + mobile + "&content="+URLEncoder.encode(content, "UTF-8")
                    + "&productid=" + productid + "&xh="+xh;
        } catch (UnsupportedEncodingException uee) {
            uee.printStackTrace();
            //logger.error(uee.toString());
        }

        //logger.info("短信内容为------------->:" + content);
        return getUrl(sendUrl);
    }

    public String sendSms(String mobile, String content,HttpServletRequest request) {
        String sendUrl = null;
        try {// 否则发到手机乱码
            sendUrl = sendurl + "sendXSms.do?username=" + username + "&password="+ password
                    + "&mobile=" + mobile + "&content="+URLEncoder.encode(content, "UTF-8")
                    + "&productid=" + productid + "&xh="+xh;
        } catch (UnsupportedEncodingException uee) {
            uee.printStackTrace();
            //logger.error(uee.toString());
        }

        logger.info("短信客户ip------------->:" + getIpAddress(request));
        return getUrl(sendUrl);
    }

    public static String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    /**
     * @Title: getUrl
     * @Description: 获取地址
     * @param urlString
     * @return
     */

    public String getUrl(String urlString) {
        StringBuffer sb = new StringBuffer();
        try {
            URL url = new URL(urlString);
            URLConnection conn = url.openConnection();
            conn.setReadTimeout(15000);
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            for (String line = null; (line = reader.readLine()) != null;) {
                sb.append(line + "\n");
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        String result = "";
        try {
            result = URLDecoder.decode(sb.toString(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return result;
    }

    /***
     * 测试地址
     *
     * ***/
    public static void main(String[] args) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        String message = "您好，为了让您更好的接收短信"+dateFormat.format(new Date())+"【东康】";
//		String phone = "13088888888,15288888888,13888888888,13788888888,15088888888";
        String phone = "1111";
        String r = new SMSSender().sendSms(phone, message);
        System.out.println(r);


    }

}
