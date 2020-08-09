package com.fh.shop.api.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JsonUtil {

	public static void outJson(HttpServletResponse response, Object object){
		response.setContentType("application/json;charset=utf-8");
		response.setCharacterEncoding("utf-8");
        ObjectMapper mapper=new ObjectMapper();
        try {
        	ServletOutputStream os = response.getOutputStream();
			String jsonStr = mapper.writeValueAsString(object);
			os.print(jsonStr);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
