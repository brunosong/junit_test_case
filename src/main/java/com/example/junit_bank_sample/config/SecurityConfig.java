package com.example.junit_bank_sample.config;

import com.example.junit_bank_sample.config.jwt.JwtAuthenticationFilter;
import com.example.junit_bank_sample.config.jwt.JwtAuthorizationFilter;
import com.example.junit_bank_sample.domain.user.UserEnum;
import com.example.junit_bank_sample.dto.ResponseDto;
import com.example.junit_bank_sample.util.CustomResponseUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class SecurityConfig {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        log.debug("디버그 : BCryptPasswordEncoder 빈 등록");
        return new BCryptPasswordEncoder();
    }


    // JWT 필터 등록이 필요함
    public class CustomSecurityFilterManager extends AbstractHttpConfigurer<CustomSecurityFilterManager, HttpSecurity> {
        @Override
        public void configure(HttpSecurity builder) throws Exception {
            AuthenticationManager authenticationManager = builder.getSharedObject(AuthenticationManager.class);
            builder.addFilter(new JwtAuthenticationFilter(authenticationManager));
            builder.addFilter(new JwtAuthorizationFilter(authenticationManager));
            super.configure(builder);
        }
    }

    
    // JWT 서버를 만들어서 세션을 사용하지 않음
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        log.debug("디버그 : filterChain 빈 등록");
        httpSecurity.headers().frameOptions().disable();   //아이프레임 허용안한다.
        httpSecurity.csrf().disable();      //enable 이면 post 맨 작동안함
        httpSecurity.cors().configurationSource(configurationSource()); //다른 서버에서 요청하는 것을 막는것을 풀겠다.

        //JSESSIONID 를 서버쪽에서 관리안하겠다는 뜻
        httpSecurity.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        //react , 앱으로 요청할 예정
        httpSecurity.formLogin().disable(); //폼로그인을 쓰지 않겠다라는것

        //httpBasic 은 브라우저가 팝업창을 이용해서 사용자 인증을 진행한다. 그것을 해제한다.
        httpSecurity.httpBasic().disable();


        // 필터 적용
        httpSecurity.apply(new CustomSecurityFilterManager());


        //Exception 가로채기 자세한정보는 : https://recordsoflife.tistory.com/839 이거 참고하는게 좋겠음
        //인증실패
        httpSecurity.exceptionHandling().authenticationEntryPoint((request,response,authenticationException) -> {
            CustomResponseUtil.unAuthentication(response, "로그인을 진행해주세요");
        });

        // 권한 실패
        httpSecurity.exceptionHandling().accessDeniedHandler((request, response, e) -> {
            CustomResponseUtil.fail(response, "권한이 없습니다", HttpStatus.FORBIDDEN);
        });

        httpSecurity.authorizeRequests()
                .antMatchers("/api/s/**").authenticated()
                .antMatchers("/api/admin/**").hasRole(""+ UserEnum.ADMIN) //최근 공식문서에서는 ROLE_ 안붙여도 가능
                .anyRequest().permitAll();

        return httpSecurity.build();

    }

    public CorsConfigurationSource configurationSource() {
        log.debug("디버그 : CorsConfigurationSource 설정 등록");
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.addAllowedMethod("*");  //모든 http 메소드를 허용하겠다.
        corsConfiguration.addAllowedOriginPattern("*");  //모든 IP 주소 허용 ( 나중에는 프론트 앤드 주소만 허용해 준다든지 하면 된다. )
        corsConfiguration.setAllowCredentials(true); //클라이언트 쿠키 요청 하용

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);   //모든요청 ( /** ) 에 저 컨피그를 설정하겠다는것

        return source;
    }

}
