package com.cos.blog.test;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

//사용작가 요청하면 HTML파일로 응답
//@Controller

//사용자가 요청하면 응답해준다(Data)
@RestController
public class HttpControllerTest {
	
	private static final String TAG = "HttpControllerTest:"; 
	//인터넷브라우저 요청은 무조건 get요청밖에 할 수 없다.
	//http://localhost:8080/http/get
	@GetMapping("/http/get")
	public String getTest(@RequestParam int id, @RequestParam String username) {
		return "get 요청";
	}
	
	//http://localhost:8080/http/post
	@PostMapping("http/post")
	public String postTest() {
		return "post 요청";
	}
	
	//http://localhost:8080/http/put
	@PutMapping("/http/put")
	public String putTest() {
		return "put요청";
	}
	//http://localhost:8080/http/delete
	@DeleteMapping("/http/delete")
	public String deleteTest() {
		return "delete 요청";
	}
}
