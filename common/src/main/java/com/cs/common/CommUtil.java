package com.cs.common;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.*;
import java.math.BigDecimal;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.binary.Base64;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class CommUtil
{
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat(
            "yyyy-MM-dd");

    static int totalFolder;
    static int totalFile;

    public static String first2low(String str)
    {
        String s = "";
        s = str.substring(0, 1).toLowerCase() + str.substring(1);
        return s;
    }

    public static String first2upper(String str) {
        String s = "";
        s = str.substring(0, 1).toUpperCase() + str.substring(1);
        return s;
    }

    public static List<String> str2list(String s)
            throws IOException
    {
        List list = new ArrayList();
        if ((s != null) && (!s.equals(""))) {
            StringReader fr = new StringReader(s);
            BufferedReader br = new BufferedReader(fr);
            String aline = "";
            while ((aline = br.readLine()) != null) {
                list.add(aline);
            }
        }
        return list;
    }

    public static Date formatDate(String s) {
        Date d = null;
        try {
            d = dateFormat.parse(s);
        } catch (Exception localException) {
        }
        return d;
    }

    public static Date formatDate(String s, String format) {
        Date d = null;
        try {
            SimpleDateFormat dFormat = new SimpleDateFormat(format);
            d = dFormat.parse(s);
        } catch (Exception localException) {
        }
        return d;
    }

    public static String formatTime(String format, Object v) {
        if (v == null)
            return null;
        if (v.equals(""))
            return "";
        SimpleDateFormat df = new SimpleDateFormat(format);
        return df.format(v);
    }

    public static String formatLongDate(Object v) {
        if ((v == null) || (v.equals("")))
            return "";
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return df.format(v);
    }

    public static String formatShortDate(Object v) {
        if (v == null)
            return null;
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return df.format(v);
    }

    public static String decode(String s) {
        String ret = s;
        try {
            ret = URLDecoder.decode(s.trim(), "UTF-8");
        } catch (Exception localException) {
        }
        return ret;
    }

    public static String encode(String s) {
        String ret = s;
        try {
            ret = URLEncoder.encode(s.trim(), "UTF-8");
        } catch (Exception localException) {
        }
        return ret;
    }

    public static String convert(String str, String coding) {
        String newStr = "";
        if (str != null)
            try {
                newStr = new String(str.getBytes("ISO-8859-1"), coding);
            } catch (Exception e) {
                return newStr;
            }
        return newStr;
    }

    public static boolean isImg(String extend) {
        boolean ret = false;
        List<String> list = new ArrayList<String>();
        list.add("jpg");
        list.add("jpeg");
        list.add("bmp");
        list.add("gif");
        list.add("png");
        list.add("tif");
        for (String s : list) {
            if (s.equals(extend))
                ret = true;
        }
        return ret;
    }



    public static boolean createFolder(String folderPath) {
        boolean ret = true;
        try {
            File myFilePath = new File(folderPath);
            if ((!myFilePath.exists()) && (!myFilePath.isDirectory())) {
                ret = myFilePath.mkdirs();
                if (!ret)
                    System.out.println("创建文件夹出错");
            }
        }
        catch (Exception e) {
            System.out.println("创建文件夹出错");
            ret = false;
        }
        return ret;
    }

    public static List toRowChildList(List list, int perNum)
    {
        List l = new ArrayList();
        if (list == null) {
            return l;
        }

        for (int i = 0; i < list.size(); i += perNum) {
            List cList = new ArrayList();
            for (int j = 0; j < perNum; j++)
                if (i + j < list.size())
                    cList.add(list.get(i + j));
            l.add(cList);
        }
        return l;
    }

    public static List copyList(List list, int begin, int end) {
        List l = new ArrayList();
        if (list == null)
            return l;
        if (end > list.size())
            end = list.size();
        for (int i = begin; i < end; i++) {
            l.add(list.get(i));
        }
        return l;
    }

    public static boolean isNotNull(Object obj)
    {
        return (obj != null) && (!obj.toString().equals(""));
    }



    public static boolean deleteFolder(String path)
    {
        boolean flag = false;
        File file = new File(path);

        if (!file.exists()) {
            return flag;
        }

        if (file.isFile()) {
            return deleteFile(path);
        }
        return deleteDirectory(path);
    }

    public static boolean deleteFile(String path)
    {
        boolean flag = false;
        File file = new File(path);

        if ((file.isFile()) && (file.exists())) {
            file.delete();
            flag = true;
        }
        return flag;
    }

    public static boolean deleteDirectory(String path)
    {
        if (!path.endsWith(File.separator)) {
            path = path + File.separator;
        }
        File dirFile = new File(path);

        if ((!dirFile.exists()) || (!dirFile.isDirectory())) {
            return false;
        }
        boolean flag = true;

        File[] files = dirFile.listFiles();
        for (int i = 0; i < files.length; i++)
        {
            if (files[i].isFile()) {
                flag = deleteFile(files[i].getAbsolutePath());
                if (!flag)
                    break;
            }
            else {
                flag = deleteDirectory(files[i].getAbsolutePath());
                if (!flag)
                    break;
            }
        }
        if (!flag) {
            return false;
        }

        return dirFile.delete();
    }

    public static String showPageStaticHtml(String url, int currentPage, int pages)
    {
        String s = "";
        if (pages > 0) {
            if (currentPage >= 1) {
                s = s + "<a href='" + url + "_1.htm'>首页</a> ";
                if (currentPage > 1) {
                    s = s + "<a href='" + url + "_" + (currentPage - 1) +
                            ".htm'>上一页</a> ";
                }
            }
            int beginPage = currentPage - 3 < 1 ? 1 : currentPage - 3;
            if (beginPage <= pages) {
                s = s + "第　";
                int i = beginPage; for (int j = 0; (i <= pages) && (j < 6); j++) {
                    if (i == currentPage)
                        s = s + "<a class='this' href='" + url + "_" + i +
                                ".htm'>" + i + "</a> ";
                    else
                        s = s + "<a href='" + url + "_" + i + ".htm'>" + i +
                                "</a> ";
                    i++;
                }

                s = s + "页　";
            }
            if (currentPage <= pages) {
                if (currentPage < pages) {
                    s = s + "<a href='" + url + "_" + (currentPage + 1) +
                            ".htm'>下一页</a> ";
                }
                s = s + "<a href='" + url + "_" + pages + ".htm'>末页</a> ";
            }
        }
        return s;
    }

    public static String showPageHtml(String url, String params, int currentPage, int pages)
    {
        String s = "";
        if (pages > 0) {
            if (currentPage >= 1) {
                s = s + "<a href='" + url + "?currentPage=1" + params +
                        "'>首页</a> ";
                if (currentPage > 1) {
                    s = s + "<a href='" + url + "?currentPage=" + (
                            currentPage - 1) + params + "'>上一页</a> ";
                }
            }
            int beginPage = currentPage - 3 < 1 ? 1 : currentPage - 3;
            if (beginPage <= pages) {
                s = s + "第　";
                int i = beginPage; for (int j = 0; (i <= pages) && (j < 6); j++) {
                    if (i == currentPage)
                        s = s + "<a class='this' href='" + url + "?currentPage=" +
                                i + params + "'>" + i + "</a> ";
                    else
                        s = s + "<a href='" + url + "?currentPage=" + i + params +
                                "'>" + i + "</a> ";
                    i++;
                }

                s = s + "页　";
            }
            if (currentPage <= pages) {
                if (currentPage < pages) {
                    s = s + "<a href='" + url + "?currentPage=" + (
                            currentPage + 1) + params + "'>下一页</a> ";
                }
                s = s + "<a href='" + url + "?currentPage=" + pages + params +
                        "'>末页</a> ";
            }
        }

        return s;
    }

    public static String showPageFormHtml(int currentPage, int pages)
    {
        String s = "";
        if (pages > 0) {
            if (currentPage >= 1) {
                s = s + "<a href='javascript:void(0);' onclick='return gotoPage(1)'>首页</a> ";
                if (currentPage > 1) {
                    s = s + "<a href='javascript:void(0);' onclick='return gotoPage(" + (
                            currentPage - 1) + ")'>上一页</a> ";
                }
            }
            int beginPage = currentPage - 3 < 1 ? 1 : currentPage - 3;
            if (beginPage <= pages) {
                s = s + "第　";
                int i = beginPage; for (int j = 0; (i <= pages) && (j < 6); j++) {
                    if (i == currentPage)
                        s = s + "<a class='this' href='javascript:void(0);' onclick='return gotoPage(" +
                                i + ")'>" + i + "</a> ";
                    else
                        s = s + "<a href='javascript:void(0);' onclick='return gotoPage(" +
                                i +
                                ")'>" + i + "</a> ";
                    i++;
                }

                s = s + "页　";
            }
            if (currentPage <= pages) {
                if (currentPage < pages) {
                    s = s + "<a href='javascript:void(0);' onclick='return gotoPage(" + (
                            currentPage + 1) + ")'>下一页</a> ";
                }
                s = s + "<a href='javascript:void(0);' onclick='return gotoPage(" +
                        pages + ")'>末页</a> ";
            }
        }

        return s;
    }

    public static String showPageAjaxHtml(String url, String params, int currentPage, int pages)
    {
        String s = "";
        if (pages > 0) {
            String address = url + "?1=1" + params;
            if (currentPage >= 1) {
                s = s + "<a href='javascript:void(0);' onclick='return ajaxPage(\"" +
                        address + "\",1,this)'>首页</a> ";
                s = s + "<a href='javascript:void(0);' onclick='return ajaxPage(\"" +
                        address +
                        "\"," + (
                        currentPage - 1) +
                        ",this)'>上一页</a> ";
            }

            int beginPage = currentPage - 3 < 1 ? 1 : currentPage - 3;
            if (beginPage <= pages) {
                s = s + "第　";
                int i = beginPage; for (int j = 0; (i <= pages) && (j < 6); j++) {
                    if (i == currentPage)
                        s = s + "<a class='this' href='javascript:void(0);' onclick='return ajaxPage(\"" +
                                address +
                                "\"," +
                                i +
                                ",this)'>" +
                                i +
                                "</a> ";
                    else
                        s = s + "<a href='javascript:void(0);' onclick='return ajaxPage(\"" +
                                address + "\"," + i +
                                ",this)'>" + i + "</a> ";
                    i++;
                }

                s = s + "页　";
            }
            if (currentPage <= pages) {
                s = s + "<a href='javascript:void(0);' onclick='return ajaxPage(\"" +
                        address +
                        "\"," + (
                        currentPage + 1) +
                        ",this)'>下一页</a> ";
                s = s + "<a href='javascript:void(0);' onclick='return ajaxPage(\"" +
                        address + "\"," + pages + ",this)'>末页</a> ";
            }
        }

        return s;
    }


    public static char randomChar() {
        char[] chars = { 'a', 'A', 'b', 'B', 'c', 'C', 'd', 'D',
                'e', 'E', 'f', 'F', 'g', 'G', 'h', 'H', 'i', 'I', 'j', 'J',
                'k', 'K', 'l', 'L', 'm', 'M', 'n', 'N', 'o', 'O', 'p', 'P',
                'q', 'Q', 'r', 'R', 's', 'S', 't', 'T', 'u', 'U', 'v', 'V',
                'w', 'W', 'x', 'X', 'y', 'Y', 'z', 'Z' };
        int index = (int)(Math.random() * 52.0D) - 1;
        if (index < 0) {
            index = 0;
        }
        return chars[index];
    }

    public static String[] splitByChar(String s, String c) {
        String[] list = s.split(c);
        return list;
    }

    public static Object requestByParam(HttpServletRequest request, String param) {
        if (!request.getParameter(param).equals("")) {
            return request.getParameter(param);
        }
        return null;
    }

    public static String substringfrom(String s, String from) {
        if (s.indexOf(from) < 0)
            return "";
        return s.substring(s.indexOf(from) + from.length());
    }

    public static int null2Int(Object s) {
        int v = 0;
        if (s != null)
            try {
                v = Integer.parseInt(s.toString());
            } catch (Exception localException) {
            }
        return v;
    }

    public static float null2Float(Object s) {
        float v = 0.0F;
        if (s != null)
            try {
                v = Float.parseFloat(s.toString());
            } catch (Exception localException) {
            }
        return v;
    }

    public static double null2Double(Object s) {
        double v = 0.0D;
        if (s != null)
            try {
                v = Double.parseDouble(null2String(s));
            } catch (Exception localException) {
            }
        return v;
    }

    public static boolean null2Boolean(Object s) {
        boolean v = false;
        if (s != null)
            try {
                v = Boolean.parseBoolean(s.toString());
            } catch (Exception localException) {
            }
        return v;
    }

    public static String null2String(Object s) {
        return s == null ? "" : s.toString().trim();
    }

    public static Long null2Long(Object s) {
        Long v = Long.valueOf(0L);
        if (s != null)
            try {
                v = Long.valueOf(Long.parseLong(s.toString()));
            } catch (Exception localException) {
            }
        return v;
    }

    public static String getTimeInfo(long time) {
        int hour = (int)time / 3600000;
        long balance = time - hour * 1000 * 60 * 60;
        int minute = (int)balance / 60000;
        balance -= minute * 1000 * 60;
        int seconds = (int)balance / 1000;
        String ret = "";
        if (hour > 0)
            ret = ret + hour + "小时";
        if (minute > 0)
            ret = ret + minute + "分";
        else if ((minute <= 0) && (seconds > 0))
            ret = ret + "零";
        if (seconds > 0)
            ret = ret + seconds + "秒";
        return ret;
    }

    public static String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if ((ip == null) || (ip.length() == 0) || ("unknown".equalsIgnoreCase(ip))) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if ((ip == null) || (ip.length() == 0) || ("unknown".equalsIgnoreCase(ip))) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if ((ip == null) || (ip.length() == 0) || ("unknown".equalsIgnoreCase(ip))) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    public static int indexOf(String s, String sub) {
        return s.trim().indexOf(sub.trim());
    }

    public static Map cal_time_space(Date begin, Date end) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long l = end.getTime() - begin.getTime();
        long day = l / 86400000L;
        long hour = l / 3600000L - day * 24L;
        long min = l / 60000L - day * 24L * 60L - hour * 60L;
        long second = l / 1000L - day * 24L * 60L * 60L - hour * 60L * 60L - min * 60L;
        Map map = new HashMap();
        map.put("day", Long.valueOf(day));
        map.put("hour", Long.valueOf(hour));
        map.put("min", Long.valueOf(min));
        map.put("second", Long.valueOf(second));
        return map;
    }

    public static final String randomString(int length) {
        char[] numbersAndLetters = "0123456789abcdefghijklmnopqrstuvwxyz0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                .toCharArray();
        if (length < 1) {
            return "";
        }
        Random randGen = new Random();
        char[] randBuffer = new char[length];
        for (int i = 0; i < randBuffer.length; i++) {
            randBuffer[i] = numbersAndLetters[randGen.nextInt(71)];
        }
        return new String(randBuffer);
    }

    public static final String randomInt(int length) {
        if (length < 1) {
            return null;
        }
        Random randGen = new Random();
        char[] numbersAndLetters = "0123456789".toCharArray();

        char[] randBuffer = new char[length];
        for (int i = 0; i < randBuffer.length; i++) {
            randBuffer[i] = numbersAndLetters[randGen.nextInt(10)];
        }
        return new String(randBuffer);
    }

    public static long getDateDistance(String time1, String time2)
    {
        long quot = 0L;
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date1 = ft.parse(time1);
            Date date2 = ft.parse(time2);
            quot = date1.getTime() - date2.getTime();
            quot = quot / 1000L / 60L / 60L / 24L;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return quot;
    }

    public static double div(Object a, Object b)
    {
        double ret = 0.0D;
        if ((!null2String(a).equals("")) && (!null2String(b).equals(""))) {
            BigDecimal e = new BigDecimal(null2String(a));
            BigDecimal f = new BigDecimal(null2String(b));
            if (null2Double(f) > 0.0D)
                ret = e.divide(f, 3, 1).doubleValue();
        }
        DecimalFormat df = new DecimalFormat("0.00");
        return Double.valueOf(df.format(ret)).doubleValue();
    }

    public static double subtract(Object a, Object b)
    {
        double ret = 0.0D;
        BigDecimal e = new BigDecimal(null2Double(a));
        BigDecimal f = new BigDecimal(null2Double(b));
        ret = e.subtract(f).doubleValue();
        DecimalFormat df = new DecimalFormat("0.00");
        return Double.valueOf(df.format(ret)).doubleValue();
    }

    public static double add(Object a, Object b)
    {
        double ret = 0.0D;
        BigDecimal e = new BigDecimal(null2Double(a));
        BigDecimal f = new BigDecimal(null2Double(b));
        ret = e.add(f).doubleValue();
        DecimalFormat df = new DecimalFormat("0.00");
        return Double.valueOf(df.format(ret)).doubleValue();
    }

    public static double mul(Object a, Object b)
    {
        BigDecimal e = new BigDecimal(null2Double(a));
        BigDecimal f = new BigDecimal(null2Double(b));
        double ret = e.multiply(f).doubleValue();
        DecimalFormat df = new DecimalFormat("0.00");
        return Double.valueOf(df.format(ret)).doubleValue();
    }

    public static double formatMoney(Object money) {
        DecimalFormat df = new DecimalFormat("0.00");
        return Double.valueOf(df.format(money)).doubleValue();
    }

    public static int M2byte(float m) {
        float a = m * 1024.0F * 1024.0F;
        return (int)a;
    }

    public static boolean convertIntToBoolean(int intValue) {
        return intValue != 0;
    }

    public static String getURL(HttpServletRequest request) {
        String contextPath = request.getContextPath().equals("/") ? "" :
                request.getContextPath();
        String url = "http://" + request.getServerName();
        if (null2Int(Integer.valueOf(request.getServerPort())) != 80)
            url = url + ":" + null2Int(Integer.valueOf(request.getServerPort())) + contextPath;
        else {
            url = url + contextPath;
        }
        return url;
    }



    public static int parseDate(String type, Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        if (type.equals("y")) {
            return cal.get(1);
        }
        if (type.equals("M")) {
            return cal.get(2) + 1;
        }
        if (type.equals("d")) {
            return cal.get(5);
        }
        if (type.equals("H")) {
            return cal.get(11);
        }
        if (type.equals("m")) {
            return cal.get(12);
        }
        if (type.equals("s")) {
            return cal.get(13);
        }
        return 0;
    }

    public static int[] readImgWH(String imgurl)
    {
        boolean b = false;
        try
        {
            URL url = new URL(imgurl);

            BufferedInputStream bis = new BufferedInputStream(
                    url.openStream());

            byte[] bytes = new byte[100];

            OutputStream bos = new FileOutputStream(
                    new File("C:\\thetempimg.gif"));
            int len;
            while ((len = bis.read(bytes)) > 0)
            {
                bos.write(bytes, 0, len);
            }
            bis.close();
            bos.flush();
            bos.close();

            b = true;
        }
        catch (Exception e) {
            b = false;
        }
        int[] a = new int[2];
        if (b)
        {
            File file = new File("C:\\thetempimg.gif");
            BufferedImage bi = null;
            boolean imgwrong = false;
            try
            {
                bi = ImageIO.read(file);
                try
                {
                    int i = bi.getType();
                    imgwrong = true;
                } catch (Exception e) {
                    imgwrong = false;
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            if (imgwrong) {
                a[0] = bi.getWidth();
                a[1] = bi.getHeight();
            } else {
                a = null;
            }

            file.delete();
        } else {
            a = null;
        }
        return a;
    }

    public static boolean fileExist(String path)
    {
        File file = new File(path);
        return file.exists();
    }

    public static int splitLength(String s, String c)
    {
        int v = 0;
        if (!s.trim().equals("")) {
            v = s.split(c).length;
        }
        return v;
    }

    public static double fileSize(File folder)
    {
        totalFolder += 1;

        long foldersize = 0L;
        File[] filelist = folder.listFiles();
        for (int i = 0; i < filelist.length; i++) {
            if (filelist[i].isDirectory()) {
                foldersize = (long)(foldersize + fileSize(filelist[i]));
            } else {
                totalFile += 1;
                foldersize += filelist[i].length();
            }
        }
        return div(Long.valueOf(foldersize), Integer.valueOf(1024));
    }

    public static int fileCount(File file)
    {
        if (file == null) {
            return 0;
        }
        if (!file.isDirectory()) {
            return 1;
        }
        int fileCount = 0;
        File[] files = file.listFiles();
        for (File f : files) {
            if (f.isFile()) {
                fileCount++;
            } else if (f.isDirectory()) {
                fileCount++;
                fileCount += fileCount(file);
            }
        }
        return fileCount;
    }

    public static String get_all_url(HttpServletRequest request)
    {
        String query_url = request.getRequestURI();
        if ((request.getQueryString() != null) &&
                (!request.getQueryString().equals(""))) {
            query_url = query_url + "?" + request.getQueryString();
        }
        return query_url;
    }

    public static Color getColor(String color)
    {
        if (color.charAt(0) == '#') {
            color = color.substring(1);
        }
        if (color.length() != 6)
            return null;
        try
        {
            int r = Integer.parseInt(color.substring(0, 2), 16);
            int g = Integer.parseInt(color.substring(2, 4), 16);
            int b = Integer.parseInt(color.substring(4), 16);
            return new Color(r, g, b); } catch (NumberFormatException nfe) {
        }
        return null;
    }

    public static Set<Integer> randomInt(int a, int length)
    {
        Set list = new TreeSet();
        int size = length;
        if (length > a) {
            size = a;
        }
        while (list.size() < size) {
            Random random = new Random();
            int b = random.nextInt(a);
            list.add(Integer.valueOf(b));
        }
        return list;
    }

    public static Double formatDouble(Object obj, int len)
    {
        Double ret = Double.valueOf(0.0D);
        String format = "0.0";
        for (int i = 1; i < len; i++) {
            format = format + "0";
        }
        DecimalFormat df = new DecimalFormat(format);
        return Double.valueOf(df.format(obj));
    }

    public static boolean isChinese(char c)
    {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);

        return (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS) ||
                (ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS) ||
                (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A) ||
                (ub == Character.UnicodeBlock.GENERAL_PUNCTUATION) ||
                (ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION) ||
                (ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS);
    }

    public static boolean isMessyCode(String strName)
    {
        Pattern p = Pattern.compile("\\s*|\t*|\r*|\n*");
        Matcher m = p.matcher(strName);
        String after = m.replaceAll("");
        String temp = after.replaceAll("\\p{P}", "");
        char[] ch = temp.trim().toCharArray();
        float chLength = ch.length;
        float count = 0.0F;
        for (int i = 0; i < ch.length; i++) {
            char c = ch[i];
            if (Character.isLetterOrDigit(c))
                continue;
            if (!isChinese(c)) {
                count += 1.0F;
                System.out.print(c);
            }
        }

        float result = count / chLength;

        return result > 0.4D;
    }

    public static String trimSpaces(String IP)
    {
        while (IP.startsWith(" ")) {
            IP = IP.substring(1, IP.length()).trim();
        }
        while (IP.endsWith(" ")) {
            IP = IP.substring(0, IP.length() - 1).trim();
        }
        return IP;
    }

    public static boolean isIp(String IP)
    {
        boolean b = false;
        IP = trimSpaces(IP);
        if (IP.matches("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}")) {
            String[] s = IP.split("\\.");
            if ((Integer.parseInt(s[0]) < 255) &&
                    (Integer.parseInt(s[1]) < 255) &&
                    (Integer.parseInt(s[2]) < 255) &&
                    (Integer.parseInt(s[3]) < 255))
                b = true;
        }
        return b;
    }

    public static String generic_domain(HttpServletRequest request)
    {
        String system_domain = "localhost";
        String serverName = request.getServerName();
        if (isIp(serverName))
            system_domain = serverName;
        else {
            system_domain = serverName.substring(serverName.indexOf(".") + 1);
        }

        return system_domain;
    }

    public static long getTime(Date date){
        return date.getTime();
    }

    public static long getNowTime(){
        return new Date().getTime();
    }

    public static String testcode(String s){
        String _s = "";
        try {
            _s += "1"+new String(s.getBytes("GBK"),"ISO8859_1")+"\n";
            _s += "2"+new String(s.getBytes("GBK"),"UTF-8")+"\n";
            _s += "3"+new String(s.getBytes("GBK"))+"\n";
            _s += "4"+new String(s.getBytes("ISO8859_1"),"GBK")+"\n";
            _s += "5"+new String(s.getBytes("ISO8859_1"),"UTF-8")+"\n";
            _s += "6"+new String(s.getBytes("ISO8859_1"))+"\n";
            _s += "7"+new String(s.getBytes("UTF-8"),"GBK")+"\n";
            _s += "8"+new String(s.getBytes("UTF-8"),"ISO8859_1")+"\n";
            _s += "9"+new String(s.getBytes("UTF-8"))+"\n";
            _s += "10"+new String(s.getBytes(),"GBK")+"\n";
            _s += "11"+new String(s.getBytes(),"ISO8859_1")+"\n";
            _s += "12"+new String(s.getBytes(),"UTF-8")+"\n";
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return _s;
    }

    public static String byteToStr(byte[] bytes){

        try {
            String a = new String(bytes,"utf-8");
            return a;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }


    public static String getRandomUUID_18() {
     /*Date date = new Date();
     String time = date.getTime()+"";
     int n = 18-time.length();*/
        int num = (int)((Math.random()*9+1)*10);
        return num+"";
    }


    public static String getWeekStr(String str){
        if("1".equals(str)){
            str = "星期日";
        }else if("2".equals(str)){
            str = "星期一";
        }else if("3".equals(str)){
            str = "星期二";
        }else if("4".equals(str)){
            str = "星期三";
        }else if("5".equals(str)){
            str = "星期四";
        }else if("6".equals(str)){
            str = "星期五";
        }else if("7".equals(str)){
            str = "星期六";
        }
        return str;
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
     * 将requst中 xml 信息转成map
     * @param request
     * @return
     * @throws IOException
     * @throws DocumentException
     */

    public static Map<String, String> xmlToMap(HttpServletRequest request) throws IOException, DocumentException {
        Map<String, String> map = new HashMap<String, String>();
        SAXReader reader = new SAXReader();
        InputStream in = request.getInputStream();
        Document document = reader.read(in);
        Element root = document.getRootElement();
        List<Element> list = root.elements();
        for (Element e : list) {
            map.put(e.getName(), e.getText());
        }
        in.close();
        return map;
    }


    public static String phoneHidden(String phone){
        if (phone==null || phone.equals("")){
            return null;
        }
        return phone.replaceAll("(\\d{3})\\d{4}(\\d{4})","$1****$2");
    }

    public static Integer dateDiff(Date startTime, Date endTime)
    {
        try {
            Float nd = new Float(1000 * 24 * 60 * 60);// 一天的毫秒数
            Float diff = new Float(endTime.getTime() - startTime.getTime());//计算相差多少毫秒
            Float day = diff / nd;// 计算差多少天

            return day.intValue();//返回相差的天数
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static String encodeBase64(String str){
        byte[] b = str.getBytes();
        Base64 base64 = new Base64();
        b = base64.encode(b);
        String s  =new String(b);
        return s;
    }

    public static String dncodeBase64(String str){
        byte[] b = str.getBytes();
        Base64 base64 = new Base64();
        b = base64.decode(b);
        String s  =new String(b);
        return s;
    }

}
