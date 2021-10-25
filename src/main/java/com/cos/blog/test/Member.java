package com.cos.blog.test;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data				//getter와 setter
@AllArgsConstructor	//모든생성자
@NoArgsConstructor	//빈생성자
public class Member {
	private int id;
	private String username;
	private String pssword;
	private String email;
}
