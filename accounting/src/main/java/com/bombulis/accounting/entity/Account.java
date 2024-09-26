package com.bombulis.accounting.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, columnDefinition = "serial")
    @Getter @Setter
    private Long id;
    @Getter @Setter
    private String name;
    @Getter @Setter
    private Timestamp create_at;
    @Getter @Setter
    private Timestamp lastUpdate_at;
    @Getter @Setter
    private boolean deleted = false;
    @Getter @Setter
    private boolean archive = false;
    @Getter @Setter
    private String type;
    @Getter @Setter
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", updatable=false, nullable = false)
    @Setter @Getter
    @JsonBackReference
    private User user;

    public Account(String name, User user) {
        this.name = name;
        this.create_at = new Timestamp(System.currentTimeMillis());
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Account)) return false;
        Account account = (Account) o;
        return id.equals(account.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @PreUpdate
    protected void onUpdate() {
        this.lastUpdate_at = new Timestamp(new Date().getTime());
    }
}