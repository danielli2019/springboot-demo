package com.test.demo.springbootjpademo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

@Data
@Entity
@Table(name="book", schema = "books")
@EntityListeners(value = AuditingEntityListener.class)
public class Book {

    @Id
    private String bookId;

    private String bookName;

    @CreatedDate
    private Date createDate;

    @CreatedBy
    private String createBy;

}
