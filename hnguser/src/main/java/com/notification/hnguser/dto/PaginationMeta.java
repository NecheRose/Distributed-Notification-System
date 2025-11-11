package com.notification.hnguser.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PaginationMeta {

    private int total;
    private int limit;
    private int page;

    @JsonProperty("total_pages")
    private int totalPages;

    @JsonProperty("has_next")
    private boolean hasNext;

    @JsonProperty("has_previous")
    private boolean hasPrevious;

    public PaginationMeta() {}

    public PaginationMeta(int total, int limit, int page, int totalPages, boolean hasNext, boolean hasPrevious) {
        this.total = total;
        this.limit = limit;
        this.page = page;
        this.totalPages = totalPages;
        this.hasNext = hasNext;
        this.hasPrevious = hasPrevious;
    }

    public static PaginationMeta defaultMeta() {
        return new PaginationMeta(1, 1, 1, 1, false, false);
    }

    // Getters and setters
    public int getTotal() { return total; }
    public void setTotal(int total) { this.total = total; }

    public int getLimit() { return limit; }
    public void setLimit(int limit) { this.limit = limit; }

    public int getPage() { return page; }
    public void setPage(int page) { this.page = page; }

    public int getTotalPages() { return totalPages; }
    public void setTotalPages(int totalPages) { this.totalPages = totalPages; }

    public boolean isHasNext() { return hasNext; }
    public void setHasNext(boolean hasNext) { this.hasNext = hasNext; }

    public boolean isHasPrevious() { return hasPrevious; }
    public void setHasPrevious(boolean hasPrevious) { this.hasPrevious = hasPrevious; }
}
