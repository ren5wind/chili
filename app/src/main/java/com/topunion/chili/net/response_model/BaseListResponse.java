package com.topunion.chili.net.response_model;

/**
 * Created by Shawn on 7/19/17.
 */

public class BaseListResponse {

    public int state;

    public int pageNo;
    public int pageSize;
    public String order;
    public boolean autoCount;
    public int totalCount;
    public boolean orderBySetted;
    public boolean hasNext;
    public int nextPage;
    public boolean hasPrev;
    public int prevPage;
    public int totalPages;
    public int first;
}
