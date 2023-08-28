package ru.javaops.bootjava.config;

//@Configuration
//@EnableWebSecurity
//@Slf4j
//@AllArgsConstructor
public class SecurityConfiguration {

//    public static final PasswordEncoder PASSWORD_ENCODER = PasswordEncoderFactories.createDelegatingPasswordEncoder();
//
//    private final UserRepository userRepository;
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return PASSWORD_ENCODER;
//    }
//
//    @Bean
//    public UserDetailsService userDetailsService() {
//        return email -> {
//            log.debug("Authenticating '{}'", email);
//            Optional<User> optionalUser = userRepository.findByEmailIgnoreCase(email);
//            return new AuthUser(optionalUser.orElseThrow(
//                    () -> new UsernameNotFoundException("User '" + email + "' was not found")));
//        };
//    }
//
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http.authorizeRequests()
//                .antMatchers("/api/admin/**").hasRole(Role.ADMIN.name())
//                .antMatchers(HttpMethod.POST, "/api/profile").anonymous()
//                .antMatchers("/api/**").authenticated()
//                .and().httpBasic()
//                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                .and().csrf().disable();
//        return http.build();
//    }
}