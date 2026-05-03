package com.aimealplan.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * パスワードのハッシュ化に使用する BCryptPasswordEncoder を Bean として登録します。
     *
     * @return PasswordEncoder
     */
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * セキュリティフィルターチェーンの設定。
     *
     * <p><b>現在の設定は開発用の暫定設定です。本番リリース前に必ず以下の TODO を対応してください。</b></p>
     *
     * <ul>
     *   <li>TODO [SEC-01] CSRF 保護の有効化:
     *       現在 {@code csrf.disable()} により CSRF 保護が無効化されています。
     *       セッションベース認証を採用する場合は CSRF 保護を有効化してください。
     *       REST API のみで Cookie/Session を使用しない場合は無効化のままで問題ありませんが、
     *       その場合はステートレス認証（JWT 等）を採用し、セッション管理を無効化してください。
     *       参考: {@code http.sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))}</li>
     *
     *   <li>TODO [SEC-02] エンドポイントごとのアクセス制御:
     *       現在 {@code anyRequest().permitAll()} により全エンドポイントが認証なしでアクセス可能です。
     *       認証機能の実装後は以下のように制限を追加してください。
     *       例:
     *       <pre>
     *       .authorizeHttpRequests(auth -> auth
     *           .requestMatchers("/api/auth/**").permitAll()
     *           .requestMatchers(HttpMethod.GET, "/api/recipes/**").permitAll()
     *           .anyRequest().authenticated()
     *       )
     *       </pre>
     *   </li>
     * </ul>
     */
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // TODO [SEC-01] 本番前に CSRF 保護の要否を再検討すること
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                // TODO [SEC-02] 本番前にエンドポイントごとの認証・認可ルールを設定すること
                .anyRequest().permitAll()
            );
        return http.build();
    }
}
