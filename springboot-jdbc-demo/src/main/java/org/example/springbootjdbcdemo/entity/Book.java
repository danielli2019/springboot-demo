package org.example.springbootjdbcdemo.entity;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Date;

public class Book {

    private String bookId;

    private String bookName;

    private ZonedDateTime createDate;

    private String createBy;

    private String recordStatus;

    private Object origin;

    private ZonedDateTime modifyDate;

    private String modifyBy;

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public ZonedDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(ZonedDateTime createDate) {
        this.createDate = createDate;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getRecordStatus() {
        return recordStatus;
    }

    public void setRecordStatus(String recordStatus) {
        this.recordStatus = recordStatus;
    }

    public Object getOrigin() {
        return origin;
    }

    public void setOrigin(Object origin) {
        this.origin = origin;
    }

    public ZonedDateTime getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(ZonedDateTime modifyDate) {
        this.modifyDate = modifyDate;
    }

    public String getModifyBy() {
        return modifyBy;
    }

    public void setModifyBy(String modifyBy) {
        this.modifyBy = modifyBy;
    }
}
