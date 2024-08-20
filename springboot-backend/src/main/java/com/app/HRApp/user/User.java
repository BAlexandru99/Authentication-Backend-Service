package com.app.HRApp.user;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users")
/*
 * 'user' is a reserved keyword in SQL, so we name our table users. If you name it user, you will get a org.h2.jdbc.JdbcSQLSyntaxErrorException. 
 *  See https://docs.microsoft.com/en-us/sql/t-sql/language-elements/reserved-keywords-transact-sql?view=sql-server-ver16 for a list of reserved keywords.
 */ 
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@NotBlank(message =  "username cannot be blank")
	@NonNull
	@Column(nullable = false, unique = true)
	private String username;

	@NotBlank(message =  "password cannot be blank")
    @NonNull
	@Column(nullable = false)
	private String password;

	@NotBlank(message = "first name cannot be blank")
	@NonNull
	@Column(nullable = false)
	private String firstName;

	@NotBlank(message = "last name cannot be blank")
	@NonNull
	@Column(nullable = false)
	private String lastName;

	@NotNull(message = "phone number cannot be blank")
	@Column(nullable = false)
	private Long phoneNumber;

	@Column(name = "data", columnDefinition = "TIMESTAMP(0)")
    private LocalDateTime timestamp;

	@Column(unique = true)
	private String token;

	@Column
	private boolean isEnabled = false;

}
