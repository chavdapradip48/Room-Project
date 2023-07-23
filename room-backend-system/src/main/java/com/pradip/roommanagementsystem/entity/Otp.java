package com.pradip.roommanagementsystem.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Objects;

@Entity
@Data
@NoArgsConstructor
@ToString(exclude = {"user"})
public class Otp {
    @Id
    @GeneratedValue
    private Long id;

    private String code;

    private boolean verified;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Timestamp createdAt;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonBackReference
    @JsonIgnore
    private User user;

    public Otp(String code,boolean verified,User user){
        this.code=code;
        this.verified=verified;
        this.user=user;
    }

    @Override
    public int hashCode() {
        // Only include the id in the hashCode calculation to avoid cyclic dependency
        return Objects.hash(id);
    }
}
