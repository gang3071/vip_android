package com.timego.calculcator.api;

import java.util.List;

/**
 * **********************
 *
 * @Author bug machine
 * 创建时间： 2025/1/16 14:45
 * 用途
 * **********************
 */
public class DomainBean {

    private int id;
    private String name;
    private String domain;
    private String client_version;
    private List<DomainBean> domain_ext;

    public DomainBean() {
    }

    public DomainBean(String domain) {
        this.domain = domain;
    }

    public String getClient_version() {
        return client_version;
    }

    public void setClient_version(String client_version) {
        this.client_version = client_version;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public List<DomainBean> getDomain_ext() {
        return domain_ext;
    }

    public void setDomain_ext(List<DomainBean> domain_ext) {
        this.domain_ext = domain_ext;
    }
}
