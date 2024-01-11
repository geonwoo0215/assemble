package com.geonwoo.assemble.global.auth.jwt;

//@Component
//@RequiredArgsConstructor
//@Slf4j
//public class JwtExceptionFilter extends OncePerRequestFilter {
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//        try {
//            filterChain.doFilter(request, response);
//        } catch (ExpiredJwtException e) {
//            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//            response.getWriter().write("token Expired");
//            response.getWriter().flush();
//        }
//    }
//}
