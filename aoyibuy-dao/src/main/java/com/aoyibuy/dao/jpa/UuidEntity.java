package com.aoyibuy.dao.jpa;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.hibernate.annotations.GenericGenerator;

/**
 * 包含一个uuid的id字段的实体基类
 * 
 * @author wh
 * @since 0.0.1
 */
@SuppressWarnings("serial")
@Data
@EqualsAndHashCode(callSuper = false)
@MappedSuperclass
public abstract class UuidEntity extends BaseEntity {

    @Id
    @GeneratedValue(generator = "uuidGenerator")
    @GenericGenerator(name = "uuidGenerator", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(length = 36)
    private String id;
    
}
