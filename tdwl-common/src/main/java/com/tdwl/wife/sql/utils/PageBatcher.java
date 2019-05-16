package com.tdwl.wife.sql.utils;

import java.io.Serializable;

public class PageBatcher implements Serializable {

    /**
     * serialVersionUID:序列号.
     * 
     * @since JDK 1.8
     */
    private static final long serialVersionUID = -3884195264802652285L;

    private int currPage = 1;// 页码，默认是第一页
    private int pageSize = 15;// 每页显示的记录数，默认是15
    private int totalCount;// 总记录数
    private int totalPage;// 总页数

    private int recordForm;
    private int recordTo;

    public PageBatcher(int totalCount, int pageSize) {
        this.pageSize = pageSize;
        setTotalCount(totalCount);
    }

    public boolean hasNext() {
        this.recordForm = (currPage - 1) * pageSize;// 从0开始
        this.recordTo = currPage * pageSize > totalCount ? totalCount : currPage * pageSize;
        return currPage++ <= totalPage;
    }

    public int getRecordForm() {
        return recordForm;
    }

    public int getRecordTo() {
        return recordTo;
    }

    public int getPageSize() {
        return pageSize;
    }

    public int getTotalCount() {
        return totalCount;
    }

    private void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
        // 在设置总记录数的时候计算出对应的页数
        int totalPage = totalCount % pageSize == 0 ? totalCount / pageSize : totalCount / pageSize + 1;
        this.totalPage = totalPage;
    }

    public int getTotalPage() {
        return totalPage;
    }

    // public static void main(String[] args) {
    // String[] s = {"1","2","3","4","5","6","7","8","9","10","11","12"};
    // List<String> list = Arrays.asList(s);

    // PageInfo page = new PageInfo(12,5);
    // while(page.hasNext()){
    // System.out.println(list.subList(page.getRecordForm(), page.getRecordTo()));
    // }
    // }

}
