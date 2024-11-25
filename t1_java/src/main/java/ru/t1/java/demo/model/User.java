package ru.t1.java.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.t1.java.demo.model.enums.Role;

import java.util.Collection;
import java.util.List;

/**
 * Сущность, представляющая пользователя в системе.
 * Реализует интерфейс UserDetails для интеграции с Spring Security.
 *
 * @author ivanogor
 * @version 1.0
 * @since 21.11.2024
 */
@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User implements UserDetails {

    /**
     * Уникальный идентификатор пользователя.
     */
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    /**
     * Имя пользователя (логин).
     */
    @Column(name = "username", unique = true, nullable = false)
    private String username;

    /**
     * Адрес электронной почты пользователя.
     */
    @Column(name = "email", unique = true, nullable = false)
    private String email;

    /**
     * Пароль пользователя.
     */
    @JsonIgnore
    @Column(name = "password", nullable = false)
    private String password;

    /**
     * Роль пользователя.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;

    /**
     * Возвращает список ролей пользователя в виде GrantedAuthority.
     *
     * @return Список ролей пользователя.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    /**
     * Проверяет, не истек ли срок действия аккаунта пользователя.
     *
     * @return true, если аккаунт не истек, иначе false.
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Проверяет, не заблокирован ли аккаунт пользователя.
     *
     * @return true, если аккаунт не заблокирован, иначе false.
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Проверяет, не истек ли срок действия учетных данных пользователя.
     *
     * @return true, если учетные данные не истекли, иначе false.
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Проверяет, включен ли аккаунт пользователя.
     *
     * @return true, если аккаунт включен, иначе false.
     */
    @Override
    public boolean isEnabled() {
        return true;
    }
}