package com.cs.common;




import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class PageBean implements Serializable {

    private List list;        //要返回的某一页的记录列表

    private long allRow;         //总记录数
    private long totalPage;        //总页数
    private long currentPage;    //当前页
    private long pageSize;        //每页记录数

    private boolean isFirstPage;    //是否为第一页
    private boolean isLastPage;        //是否为最后一页
    private boolean hasPreviousPage;    //是否有前一页
    private boolean hasNextPage;        //是否有下一页


    public List getList() {
        return list;
    }
    public void setList(List list) {
        this.list = list;
    }
    public long getAllRow() {
        return allRow;
    }
    public void setAllRow(long allRow) {
        this.allRow = allRow;
    }
    public long getTotalPage() {
        return totalPage;
    }
    public void setTotalPage(long totalPage) {
        this.totalPage = totalPage;
    }
    public long getCurrentPage() {
        return currentPage;
    }
    public void setCurrentPage(long currentPage) {
        this.currentPage = currentPage;
    }
    public long getPageSize() {
        return pageSize;
    }
    public void setPageSize(long pageSize) {
        this.pageSize = pageSize;
    }

    public static Map returnJSON(PageBean pageBean,List list){
        Map map= new HashMap<>();
        map.put("totalPage",pageBean.getTotalPage());
        map.put("hasNext",pageBean.isHasNextPage());
        map.put("firstPage",pageBean.isFirstPage());
        map.put("hasPre",pageBean.isHasPreviousPage());
        map.put("rows",pageBean.getAllRow());
        map.put("currentPage",pageBean.getCurrentPage());
        map.put("lastPage",pageBean.isLastPage());
        if (list==null){
            map.put("records", pageBean.getList());
        }else
            map.put("records",list);
        //map.put("datas", list==null?pageBean.getList():list);
        return map;
    }

    /** *//**
     * 以下判断页的信息,只需getter方法(is方法)即可
     * @return
     */

    public boolean isFirstPage() {
        return currentPage == 1;    // 如是当前页是第1页
    }
    public boolean isLastPage() {
        return currentPage == totalPage;    //如果当前页是最后一页
    }
    public boolean isHasPreviousPage() {
        return currentPage != 1;        //只要当前页不是第1页
    }
    public boolean isHasNextPage() {
        return currentPage != totalPage;    //只要当前页不是最后1页
    }


    /** *//**
     * 计算总页数,静态方法,供外部直接通过类名调用
     * @param pageSize 每页记录数
     * @param allRow 总记录数
     * @return 总页数
     */
    public static int countTotalPage(final int pageSize,final int allRow){
        int totalPage = (allRow % pageSize == 0 && allRow != 0) ? allRow/pageSize : allRow/pageSize+1;
        return totalPage;
    }

    /** *//**
     * 计算当前页开始记录
     * @param pageSize 每页记录数
     * @param currentPage 当前第几页
     * @return 当前页开始记录号
     */
    public static int countOffset(final int pageSize,final int currentPage){
        final int offset;
        if(currentPage == 0){
            offset = 0;
        }else{
            offset = pageSize*(currentPage-1);
        }
        return offset;
    }

    /** *//**
     * 计算当前页,若为0或者请求的URL中没有"?page=",则用1代替
     * @param page 传入的参数(可能为空,即0,则返回1)
     * @return 当前页
     */
    public static int countCurrentPage(int page){
        final int curPage = (page==0?1:page);
        return curPage;
    }

    public static String queryStr(Map<String, String> queryMap) {
        if(null!=queryMap){
            String queryUrl="";
            for(Map.Entry<String, String> qm : queryMap.entrySet()){
                if(qm.getValue()!=null && !qm.getValue().equals("") && qm.getValue().length()>0){
                    queryUrl += "&query." + qm.getKey()+"=" + qm.getValue();
                }
            }
            return queryUrl;
        }
        return "";
    }


}
