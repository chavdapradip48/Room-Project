package com.pradip.roommanagementsystem.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pradip.roommanagementsystem.dto.ERoles;
import javax.persistence.*;

import lombok.*;

import java.util.Objects;

@Entity
@Table(name = "roles")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"user"})
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private ERoles name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonBackReference
    @JsonIgnore
    private User user;

    @Override
    public int hashCode() {
        // Only include the id in the hashCode calculation to avoid cyclic dependency
        return Objects.hash(id);
    }
}
