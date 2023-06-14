package com.example.junit_bank_sample.util;

import com.example.junit_bank_sample.dto.ResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;


public class CustomResponseUtil {

    private static final Logger logger = LoggerFactory.getLogger(CustomResponseUtil.class);

    public static void unAuthentication(HttpServletResponse response , String msg)  {
        try {
            ObjectMapper om = new ObjectMapper();
            ResponseDto<?> responseDto = new ResponseDto<>(-1, msg , null);  //로그인없이 페이지를 요청했기때문에
            String responseBody = om.writeValueAsString(responseDto);

            response.setContentType("application/json; charset=utf-8");
            response.setStatus(401);
            response.getWriter().println(responseBody);
        } catch ( Exception e ) {
            logger.error("서버 파싱 에러");
        }
    }
}
