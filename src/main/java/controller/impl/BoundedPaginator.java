package controller.impl;

import controller.utils.IPaginatorController;

import java.util.function.Supplier;

public class BoundedPaginator implements IPaginatorController {
    private final Supplier<Integer> boundService;
    private final IdMapped<PageControlCallback> callbacks = new IdMapped<>();
    private int pageSize;
    private int start;
    private int end;
    private int tt;

    public BoundedPaginator(Supplier<Integer> boundService, int pageSize) {
        this.boundService = boundService;
        tt = boundService.get();
        setPageSize(pageSize);
    }

    @Override
    public int addCallback(PageControlCallback callback) {
        callback.render(start, end, tt);
        return callbacks.addObj(callback);
    }

    @Override
    public void removeCallback(int id) {
        callbacks.removeByKey(id);
    }

    @Override
    public void setPageSize(int size) {
        if(size <= 0) throw new IllegalArgumentException("Must be positive");
        pageSize = size;
        firstPage();
    }

    @Override
    public void invokeAllCallback() {
        callbacks.foreach(callback -> callback.render(start, Math.min(end,tt), tt));
    }
    @Override
    public void nextPage() {
        tt = boundService.get();
        if(end < tt) {
            start += pageSize;
            end += pageSize;
            end = Math.min(end, tt);
        }
        invokeAllCallback();
    }

    @Override
    public void prevPage() {
        if(start > 0) {
            start -=  pageSize;
            start = Math.max(0, start);
            end -= pageSize;
        }
        invokeAllCallback();
    }

    @Override
    public void lastPage() {
        tt = boundService.get();
        while(end < tt) {
            start += pageSize;
            end += pageSize;
            end = Math.min(end, tt);
        }
        invokeAllCallback();
    }

    @Override
    public void firstPage() {
        start = 0;
        end = pageSize;
        invokeAllCallback();
    }
}
