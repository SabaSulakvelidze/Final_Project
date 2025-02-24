package com.example.final_project.model.customImpl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

public class CustomPage<T> implements Page<T> {
    private final List<T> content;
    private final int pageNumber;
    private final int totalPages;
    private final int pageSize;
    private final long totalElements;

    public CustomPage(List<T> content, Pageable pageable, long totalElements) {
        this.content = content;
        this.pageNumber = pageable.getPageNumber();
        this.totalPages = (int) Math.ceil((double) totalElements / pageable.getPageSize());
        this.pageSize = content.size();
        this.totalElements = totalElements;
    }

    @Override
    public List<T> getContent() {
        return content;
    }

    @Override
    public int getNumber() {
        return pageNumber;
    }

    @Override
    public int getSize() {
        return (int) totalElements;
    }

    @Override
    public int getTotalPages() {
        return totalPages;
    }

    @Override
    public int getNumberOfElements() {
        return pageSize;
    }

    @Override
    public long getTotalElements() {
        return totalElements;
    }

    @Override
    public boolean hasContent() {
        return !content.isEmpty();
    }

    @Override
    public Sort getSort() {
        return null;
    }

    @Override
    public Pageable getPageable() {
        return Pageable.unpaged(); // If needed, modify to return an actual pageable object
    }

    @Override
    public Pageable nextPageable() {
        return null;
    }

    @Override
    public Pageable previousPageable() {
        return null;
    }

    @Override
    public boolean isFirst() {
        return pageNumber == 0;
    }

    @Override
    public boolean isLast() {
        return pageNumber + 1 >= totalPages;
    }

    @Override
    public boolean hasNext() {
        return pageNumber + 1 < totalPages;
    }

    @Override
    public boolean hasPrevious() {
        return pageNumber > 0;
    }

    @Override
    public <U> Page<U> map(Function<? super T, ? extends U> converter) {
        List<U> convertedContent = (List<U>) content.stream().map(converter).toList();
        return new CustomPage<>(convertedContent, Pageable.unpaged(), totalElements);
    }

    @Override
    public Iterator<T> iterator() {
        return content.iterator();
    }
}
