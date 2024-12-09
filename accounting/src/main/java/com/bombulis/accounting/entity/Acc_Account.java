package com.bombulis.accounting.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "account")
public class Acc_Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
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

    @OneToMany(mappedBy = "senderAccount")
    private List<Acc_Transaction> senderTransaction;

    @OneToMany(mappedBy = "recipientAccount")
    private List<Acc_Transaction> recipientTransaction;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", updatable=false, nullable = false)
    @Setter @Getter
    @JsonBackReference
    private Acc_User user;

    public Acc_Account(String name, Acc_User user) {
        this.name = name;
        this.create_at = new Timestamp(System.currentTimeMillis());
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Acc_Account)) return false;
        Acc_Account account = (Acc_Account) o;
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