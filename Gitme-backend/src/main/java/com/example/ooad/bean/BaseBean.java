package com.example.ooad.bean;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

//用于保存相关实体
//即数据库的每一个表
//都应该基础于BaseBean


//用注释@Entity，表示它是一个 JPA Entity
//Hibernate automatically translates the entity into a table.
//我试过了！！！真的可以！！！

//@JoinColumn指定该实体类对应的表中引用的表的外键，name属性指定外键名称，referencedColumnName指定应用表中的字段名称
//@JoinColumn(name=”role_id”): 标注在连接的属性上(一般多对1的1)，指定了本类用1的外键名叫什么。
//@JoinTable(name="permission_role") ：标注在连接的属性上(一般多对多)，指定了多对多的中间表叫什么。

//主键@Id

@MappedSuperclass
public class BaseBean {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.DATE)
    @Column(name = "created_Date", nullable = true)
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createdDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdData) {
        this.createdDate = createdData;
    }
}
