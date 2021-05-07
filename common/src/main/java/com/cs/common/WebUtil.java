package com.cs.common;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.servlet.http.HttpServletRequest;
import javax.xml.XMLConstants;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Chris on 2015/11/3 0003.
 */
public class WebUtil {


    private final static char[] digits = { '0', '1', '2', '3', '4', '5', '6', '7', '8',
            '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l',
            'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y',
            'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L',
            'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y',
            'Z' };

    /**
     * 用户初始化密码默认长度
     */
    private static final int PWD_LENGTH = 6;

    private static int CONNECTTIMEOUT =  5000;
    private static int READTIME = 10000;


   /* public static DocumentBuilder newDocumentBuilder() throws ParserConfigurationException {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        documentBuilderFactory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
        documentBuilderFactory.setFeature("http://xml.org/sax/features/external-general-entities", false);
        documentBuilderFactory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
        documentBuilderFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
        documentBuilderFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
        documentBuilderFactory.setXIncludeAware(false);
        documentBuilderFactory.setExpandEntityReferences(false);
        return documentBuilderFactory.newDocumentBuilder();
    }

    public static org.w3c.dom.Document newDocument() throws ParserConfigurationException {
        return newDocumentBuilder().newDocument();
    }
*/

    /**
     * 随机生成用户初始化密码
     * @return
     */
    public static String createUserPwd(){
        Random random = new Random();
        StringBuilder pwd = new StringBuilder();
        for(int i = 0; i < PWD_LENGTH; i ++){
            pwd.append(random.nextInt(10))  ;
        }
        return pwd.toString();
    }

    /**
     * 生成邀请码（6位）
     * 3位随机数 + 当前时间的毫秒数（不足三位的补齐3位） + 1位随机数
     * @return
     */
    public static String createInviteCode(){
        Random random = new Random();
        StringBuilder inviteCode = new StringBuilder();
        for(int i = 0; i < 2; i ++){
            inviteCode.append(random.nextInt(10));
        }
        Calendar nowtime = new GregorianCalendar();
        String millSecond = nowtime.get(Calendar.MILLISECOND) + "";
        while(millSecond.length() < 3){
            millSecond += random.nextInt(10);
            if(millSecond.length() >= 3){
                break;
            }
        }
        inviteCode.append(millSecond);
        inviteCode.append(random.nextInt(10));
        return inviteCode.toString();
    }

    /**
     * 检测字符是否为空
     * @param strs
     * @return true为空，false不为空
     */
    public static boolean checkEmpty(String...strs){
        if(null != strs && strs.length > 0){
            for(String s : strs){
                if(null == s || s.trim().equals("")){
                    return true;
                }
            }
        }
        return false;
    }


    /**
     * 发送Http请求到url对应的地址
     * @param urlStr 请求地址
     * @return 服务器的响应字符串（如果是json，需要做json处理）
     */
    public static String sendURL(String urlStr){
        if(!WebUtil.checkEmpty(urlStr)){
            try {
                URL url = new URL(urlStr);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                //设置连接的请求超时时间，单位为毫秒
                connection.setConnectTimeout(CONNECTTIMEOUT);
                //设置连接后读取response信息的超时时间，单位为毫秒
                connection.setReadTimeout(READTIME);
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(),"UTF-8"));
                StringBuffer stringBuffer = new StringBuffer();
                String temp = "";
                while((temp = reader.readLine()) != null){
                    stringBuffer.append(temp);
                }
                String result = stringBuffer.toString();
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static String getDayBegin(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DATE);
        return "" + year + "-" + (month + 1) + "-" + day + " 00:00:00";
    }



    public static String getDayEnd(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DATE);
        return "" + year + "-" + (month + 1) + "-" + day + " 23:59:59";
    }

    /**
     * 生成UUID
     * @return
     */
    public static String getUUID(){
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }

    public static String getRandomString(int length){
        StringBuffer s = new StringBuffer();
        //获取当前毫秒值
        Calendar nowTime = new GregorianCalendar();
        String millSecond = nowTime.get(Calendar.MILLISECOND) + "";
        s.append(millSecond);
        Random random = new Random();
        do{
            s.append(digits[random.nextInt(digits.length)]);
        }while(s.length() < length);
        return s.toString();
    }



    public static String genNonceStr() {
        Random random = new Random();
        return MD5Util.getMessageDigest(String.valueOf(random.nextInt(10000)).getBytes());
    }

    public static String formatTime(Date date){
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return f.format(date);
    }

    public static String changeTimeStyle(String timeStr, String style, String targetStyle){
        SimpleDateFormat f = new SimpleDateFormat(style);
        try {
            Date date = f.parse(timeStr);
            f = null;
            f = new SimpleDateFormat(targetStyle);
            return f.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }



    public static String formatTime(Date date, String style){
        SimpleDateFormat f = new SimpleDateFormat(style);
        return f.format(date);
    }


    public static Date formatTime(String dateStr, String style){
        SimpleDateFormat f = new SimpleDateFormat(style);
        try {
            return f.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String handlerPhone(String phone){
        if(!WebUtil.checkEmpty(phone) && phone.length() == 11){
            String str = "****";
            String str1 = phone.substring(0,3);
            String str2 = phone.substring(7);
            return str1 + str + str2;
        }
        return "";
    }


    public static String getBasePath(HttpServletRequest request){
        String path = request.getContextPath();
        String basePath = request.getScheme()+"://"+request.getServerName()
                +":"+request.getServerPort()+path+"/";
        return basePath;
    }

    public static String getRootRealPath(HttpServletRequest request){
        return request.getSession().getServletContext().getRealPath("/");
    }


    // 加密
    public static String getBase64(String str) {
        byte[] b = null;
        String s = null;
        try {
            b = str.getBytes("utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (b != null) {
            s = new BASE64Encoder().encode(b);
        }
        return s;
    }

    // 解密
    public static String getFromBase64(String s) {
        byte[] b = null;
        String result = null;
        if (s != null) {
            BASE64Decoder decoder = new BASE64Decoder();
            try {
                b = decoder.decodeBuffer(s);
                result = new String(b, "utf-8");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }


    public static Map xmlHandler(String xmlStr, String...elementNames){
        SAXReader reader = new SAXReader();
        Map map = new HashMap();
        try {
            Document doc = DocumentHelper.parseText(xmlStr);
            //获取根目录元素
            Element root = doc.getRootElement();
            for(Iterator it = root.elementIterator(); it.hasNext();){
                Element element = (Element) it.next();
                String eName = element.getName();
                for(String s : elementNames){
                    if(eName.equals(s)){
                        map.put(s, element.getTextTrim());
                    }
                }
            }
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return map;
    }

    public static void main(String[] args) {
        String msg = "<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg><appid>" +
                "<![CDATA[wxa1261e22bf0e9094]]></appid><mch_id><![CDATA[1490693182]]></mch_id><nonce_str><![CDATA[MG5ynq35ULuLT1RV]]>" +
                "</nonce_str><sign><![CDATA[4D14CEF41F6AFD83C21051B1162E765B]]></sign><result_code><![CDATA[SUCCESS]]></result_code><prepay_id>" +
                "<![CDATA[wx09151815977306e7142a604d2114954223]]></prepay_id><trade_type><![CDATA[APP]]></trade_type></xml>";

        Element root = WebUtil.getXmlRoot(msg);
        String return_code = root.element("return_code").getTextTrim();
        System.out.println(return_code);
    }

    public static Element getXmlRoot(String xmlStr){
        SAXReader reader = new SAXReader();
        try{
            reader.setFeature("http://apache.org/xml/features/disallow-doctype-decl",true);

            reader.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            reader.setFeature("http://xml.org/sax/features/external-general-entities", false);
            reader.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
            reader.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
            reader.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
           /* reader.setXIncludeAware(false);
            reader.setExpandEntityReferences(false);
*/
            Document document = reader.read(new ByteArrayInputStream(xmlStr.getBytes("utf-8")));
            //读取xml字符串，注意这里要转成输入流
            Element root = document.getRootElement();//获取根元素
            return root;

        }catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


   /* public static org.w3c.dom.Document getXmlRootV2(String xmlStr){
        //SAXReader reader = new SAXReader();

        try {
            //DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

            org.w3c.dom.Document document = WebUtil.newDocument();
            org.w3c.dom.Document root = (org.w3c.dom.Document) document.createElement(xmlStr);
            return root;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public   static   Document   parse(org.w3c.dom.Document   doc)   throws   Exception   {
        if   (doc   ==   null)   {
            return   (null);
        }
        org.dom4j.io.DOMReader   xmlReader   =   new   org.dom4j.io.DOMReader();
        return   (xmlReader.read(doc));
    }

    *//**
     *   org.dom4j.Document   ->   org.w3c.dom.Document
     *   @param   doc   Document(org.dom4j.Document)
     *   @throws   Exception
     *   @return   Document
     *//*
    public   static   org.w3c.dom.Document   parse(Document   doc)   throws   Exception   {
        if   (doc   ==   null)   {
            return   (null);
        }
        java.io.StringReader   reader   =   new   java.io.StringReader(doc.asXML());
        org.xml.sax.InputSource   source   =   new   org.xml.sax.InputSource(reader);
        javax.xml.parsers.DocumentBuilderFactory   documentBuilderFactory   =
                javax.xml.parsers.DocumentBuilderFactory.newInstance();
        javax.xml.parsers.DocumentBuilder   documentBuilder   =   documentBuilderFactory.
                newDocumentBuilder();
        return   (documentBuilder.parse(source));
    }*/

    public static String createlFileName(){
        Calendar currentTime = Calendar.getInstance();
        int year = currentTime.get(Calendar.YEAR);
        int month = currentTime.get(Calendar.MONTH) + 1;
        int date = currentTime.get(Calendar.DAY_OF_MONTH);
        int hour = currentTime.get(Calendar.HOUR_OF_DAY);
        int minuter = currentTime.get(Calendar.MINUTE);
        int second = currentTime.get(Calendar.SECOND);
        Random random = new Random();
        return hour + "-" + minuter + "-" + second + "_r-" + random.nextInt(10) + "" + random.nextInt(10) + "" + random.nextInt(10);
    }

    /**
     * 创建年月日形式的文件夹
     * @param rootPath
     * @return
     */
    public static File createDir(String rootPath){
        File dir = new File(rootPath);
        if(!dir.exists()){
            dir.mkdir();
        }
        Calendar currentTime = Calendar.getInstance();
        int year = currentTime.get(Calendar.YEAR);
        File yearDir = new File(dir, String.valueOf(year));
        if(!yearDir.exists()){
            yearDir.mkdirs();
        }
        int month = currentTime.get(Calendar.MONTH) + 1;
        File monthDir = new File(yearDir, String.valueOf(month));
        if(!monthDir.exists()){
            monthDir.mkdir();
        }
        int date = currentTime.get(Calendar.DAY_OF_MONTH);
        File dateDir = new File(monthDir, String.valueOf(date));
        if(!dateDir.exists()){
            dateDir.mkdir();
        }
        return dateDir;
    }


    /**
     * 将时间转换成显示的时间格式，参考微信
     * @param addTime
     * @return
     */
    public static String getShowTime(Date addTime) {
        //当前日历时间
        Calendar currentCalendar = Calendar.getInstance();
        //发布日历时间
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTime(addTime);
        //当前时间单位
        int cYear = currentCalendar.get(Calendar.YEAR);
        int cMonth = currentCalendar.get(Calendar.MONTH);
        int cDate = currentCalendar.get(Calendar.DAY_OF_MONTH);
        int cHour = currentCalendar.get(Calendar.HOUR_OF_DAY);
        int cMinuter = currentCalendar.get(Calendar.MINUTE);
        //发布时间单位
        int sYear = startCalendar.get(Calendar.YEAR);
        int sMonth = startCalendar.get(Calendar.MONTH);
        int sDate = startCalendar.get(Calendar.DAY_OF_MONTH);
        int sHour = startCalendar.get(Calendar.HOUR_OF_DAY);
        int sMinuter = startCalendar.get(Calendar.MINUTE);
        if(cYear >sYear){
            return (cYear - sYear) + "年前";
        }
        else if(cMonth > sMonth){
            return (cMonth - sMonth) + "个月前";
        }
        else if(cDate > sDate){
            return (cDate - sDate) > 1 ?  (cDate - sDate) + "天前" : "昨天";
        }
        else if(cHour > sHour){
            return (cHour - sHour) + "小时前";
        }
        else if(cMinuter >= sMinuter){
            return (cMinuter - sMinuter) < 1 ? "刚刚" : (cMinuter - sMinuter) + "分钟前";
        }
        return "";
    }


    /**
     * 获取字符的编码
     * @param str
     * @return
     */
    public static String getEncoding(String str) {
        String encode = "GB2312";
        try {
            if (str.equals(new String(str.getBytes(encode), encode))) {
                String s = encode;
                return s;
            }
        } catch (Exception exception) {
        }
        encode = "ISO-8859-1";
        try {
            if (str.equals(new String(str.getBytes(encode), encode))) {
                String s1 = encode;
                return s1;
            }
        } catch (Exception exception1) {
        }
        encode = "UTF-8";
        try {
            if (str.equals(new String(str.getBytes(encode), encode))) {
                String s2 = encode;
                return s2;
            }
        } catch (Exception exception2) {
        }
        encode = "GBK";
        try {
            if (str.equals(new String(str.getBytes(encode), encode))) {
                String s3 = encode;
                return s3;
            }
        } catch (Exception exception3) {
        }
        return "";
    }


    public static String generateOrderNo(){
        Date date = new Date();
        String dateStr = formatTime(date,"yyyyMMddHHmmssSS");
        Random random = new Random();
        return dateStr + "521" + random.nextInt(10) + "" + random.nextInt(10) + "" + random.nextInt(10);
    }


    public static String generateOrderNo(String type,int step){
        Date date = new Date();
        String dateStr =type+ date.getTime();
        Random random = new Random();
        return dateStr + random.nextInt(step);
    }

    public static String DateformatTime(Date date) {
        //SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //转换为日期
        long time = date.getTime();
        if (isThisYear(date)) {//今年
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
            if (isToday(date)) { //今天
                int minute = minutesAgo(time);
                if (minute < 60) {//1小时之内
                    if (minute <= 1) {//一分钟之内，显示刚刚
                        return "刚刚";
                    } else {
                        return minute + "分钟前";
                    }
                } else {
                    return simpleDateFormat.format(date);
                }
            } else {
                if (isYestYesterday(date)) {//昨天，显示昨天
                    return "昨天 " + simpleDateFormat.format(date);
                } else if (isThisWeek(date)) {//本周,显示周几
                    String weekday = null;
                    if (date.getDay() == 1) {
                        weekday = "周一";
                    }
                    if (date.getDay() == 2) {
                        weekday = "周二";
                    }
                    if (date.getDay() == 3) {
                        weekday = "周三";
                    }
                    if (date.getDay() == 4) {
                        weekday = "周四";
                    }
                    if (date.getDay() == 5) {
                        weekday = "周五";
                    }
                    if (date.getDay() == 6) {
                        weekday = "周六";
                    }
                    if (date.getDay() == 0) {
                        weekday = "周日";
                    }
                    return weekday + " " + simpleDateFormat.format(date);
                } else {
                    SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm");
                    return sdf.format(date);
                }
            }
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            return sdf.format(date);
        }
    }

    /**
     * String型时间戳格式化
     *
     * @return
     */
    public static String LongFormatTime(String time) {
        //SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //转换为日期
        Date date = new Date();
        date.setTime(Long.parseLong(time));
        if (isThisYear(date)) {//今年
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
            if (isToday(date)) { //今天
                int minute = minutesAgo(Long.parseLong(time));
                if (minute < 60) {//1小时之内
                    if (minute <= 1) {//一分钟之内，显示刚刚
                        return "刚刚";
                    } else {
                        return minute + "分钟前";
                    }
                } else {
                    return simpleDateFormat.format(date);
                }
            } else {
                if (isYestYesterday(date)) {//昨天，显示昨天
                    return "昨天 " + simpleDateFormat.format(date);
                } else if (isThisWeek(date)) {//本周,显示周几
                    String weekday = null;
                    if (date.getDay() == 1) {
                        weekday = "周一";
                    }
                    if (date.getDay() == 2) {
                        weekday = "周二";
                    }
                    if (date.getDay() == 3) {
                        weekday = "周三";
                    }
                    if (date.getDay() == 4) {
                        weekday = "周四";
                    }
                    if (date.getDay() == 5) {
                        weekday = "周五";
                    }
                    if (date.getDay() == 6) {
                        weekday = "周六";
                    }
                    if (date.getDay() == 0) {
                        weekday = "周日";
                    }
                    return weekday + " " + simpleDateFormat.format(date);
                } else {
                    SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm");
                    return sdf.format(date);
                }
            }
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            return sdf.format(date);
        }
    }

    private static int minutesAgo(long time) {
        return (int) ((System.currentTimeMillis() - time) / (60000));
    }

    private static boolean isToday(Date date) {
        Date now = new Date();
        return (date.getYear() == now.getYear()) && (date.getMonth() == now.getMonth()) && (date.getDate() == now.getDate());
    }

    private static boolean isYestYesterday(Date date) {
        Date now = new Date();
        return (date.getYear() == now.getYear()) && (date.getMonth() == now.getMonth()) && (date.getDate() + 1 == now.getDate());
    }

    private static boolean isThisWeek(Date date) {
        Date now = new Date();
        if ((date.getYear() == now.getYear()) && (date.getMonth() == now.getMonth())) {
            if (now.getDay() - date.getDay() < now.getDay() && now.getDate() - date.getDate() > 0 && now.getDate() - date.getDate() < 7) {
                return true;
            }
        }
        return false;
    }

    private static boolean isThisYear(Date date) {
        Date now = new Date();
        return date.getYear() == now.getYear();
    }

}
