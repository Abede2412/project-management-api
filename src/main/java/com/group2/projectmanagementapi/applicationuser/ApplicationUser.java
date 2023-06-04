package com.group2.projectmanagementapi.applicationuser;

import java.util.UUID;

import org.hibernate.annotations.GenericGenerator;

import com.group2.projectmanagementapi.appusers.AppUser;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationUser {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @OneToOne(mappedBy = "applicationUser")
    private AppUser appUser;
 
    private String email;

    private String username;

    private String password;

}
