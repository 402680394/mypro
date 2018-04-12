package com.ztdx.eams.domain.system.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

/**
 * Created by li on 2018/3/22.
 */
@Data
@Entity
@Table(name = "sys_user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    //姓名
    @Size(max = 10)
    @Column(name = "user_name")
    private String name;

    //工号
    @Size(max = 20)
    @Column(name = "user_workers")
    private String workers;

    //用户名
    @Size(max = 20)
    @Column(name = "user_username")
    private String username;

    //密码
    @Size(max = 30)
    @Column(name = "user_password")
    private String password;

    //所属机构
    @Min(value = 1)
    @Column(name = "organization_id")
    private int organizationId;

    //电话
    @Size(max = 20)
    @Column(name = "user_phone")
    private String phone;

    //邮箱
    @Size(max = 50)
    @Column(name = "user_email")
    private String email;

    //职位
    @Size(max = 20)
    @Column(name = "user_position")
    private String position;

    //备注
    @Size(max = 100)
    @Column(name = "user_remark")
    private String remark;

    //状态
    @Min(value = 0)
    @Max(value = 10)
    @Column(name = "user_status")
    private int status;
}
