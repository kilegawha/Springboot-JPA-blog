package com.cos.blog.test;

import java.util.List;
import java.util.function.Supplier;

import javax.persistence.Column;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cos.blog.model.RoleType;
import com.cos.blog.model.User;
import com.cos.blog.repository.UserRepository;

//html파일이 아니라 data를 리턴해주는 Controller = RestController
@RestController
public class DummyControllerTest {
	
	@Autowired  //의존성주입
	private UserRepository userRepository;
	
	@GetMapping("/dummy/users")
	public List<User>list(){
		return userRepository.findAll();
	}
	
	@GetMapping("/dummy/user")
	public List<User>pageList(@PageableDefault(size=2, sort="id", direction=Sort.Direction.DESC)Pageable pageable){
		Page<User> pagingUsers = userRepository.findAll(pageable);
		
		List<User>users = pagingUsers.getContent();
		return users;
	}
	//
/*	
	//{id}주소로 파라미터를 전달 받을 수 있다.
	//http://localhost:8000/blog/dummy/user/3
	@GetMapping("/dummy/user/{id}")
	public User detail(@PathVariable int id) {		
		User user = userRepository.findById(id).orElseGet(new Supplier<User>() {
			@Override
			public User get() {
				return new User();
			}			
		});
		return user;
		}
	*/
	@GetMapping("/dummy/user/{id}")
	public User detail(@PathVariable int id) {	
		User user = userRepository.findById(id).orElseThrow(new Supplier<IllegalArgumentException>() {
			@Override
			public IllegalArgumentException get() {				
				return new IllegalArgumentException("해당 유저는 없습니다. id : " + id);
			}					
		});
		//요청 : 웹브라우저
		//user객체 = 자바 오브젝트
		// 변환 (웹브라우저가 이해할 수 있는 데이터 ) -> json(Gson라이브러리)
		// 스프링부트 = MessageConverter 라는 애가 응답시 자동작동\
		// 만약 자바 오브젝트를 리턴하게 되면 MessageConverter가 Jackson라이브러리를 호출해서
		// user 오브젝틀을 json으로 변환해서 브라우저에게 던져준다.
		
		return user;
	}
	
	
	@PostMapping("/dummy/join")
	public String join(User user) {
		System.out.println("username:" + user.getUsername());
		System.out.println("password:" + user.getPassword());
		System.out.println("email:" + user.getEmail());
		user.setRole(RoleType.USER);
		userRepository.save(user);
		return "회원가입이 완료되었습니다.";
	}
}
