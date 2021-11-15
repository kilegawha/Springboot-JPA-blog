package com.cos.blog.test;

import java.util.List;
import java.util.function.Supplier;

import javax.persistence.Column;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.cos.blog.model.RoleType;
import com.cos.blog.model.User;
import com.cos.blog.repository.UserRepository;

//html파일이 아니라 data를 리턴해주는 Controller = RestController
@RestController
public class DummyControllerTest {
	
	@Autowired  //의존성주입
	private UserRepository userRepository;
	
	
	@DeleteMapping("/dummy/user/{id}")
	public String delete(@PathVariable int id) {
		try {
			userRepository.deleteById(id);
		}catch (EmptyResultDataAccessException e) {
			return "삭제에 실패했습니다. 해당 id는 DB에 없습니다";
		}
		return "삭제되었습니다"+ id;
	}
	
	
	
	//save함수는 id를 전달하지 않으면 insert를 해주고
	//save함수는 id를 전달하면 해당 id에 대한 데이터가 있으면 update를 해주고
	//save함수는 id를 전달하면 해당 id에 대한 데이터가 없으면 insert를 해준다.
	@Transactional //함수 종료시 자동 commit이된다.
	@PutMapping("dummy/user/{id}")
	public User updateUser(@PathVariable int id, @RequestBody User requestUser) { //json데이터를 요청 -> Java Object(MessageConverter의Jackson라이브러리기  변환해서 받아줘요.)로 변환해서 받아준다.
		System.out.println("id : " + id);
		System.out.println("password : " + requestUser.getPassword());
		System.out.println("Email : " + requestUser.getEmail());
		
		User user = userRepository.findById(id).orElseThrow(()->{
			return new  IllegalArgumentException("수정에 실패하였습니다."); 
		});
		user.setPassword(requestUser.getPassword());
		user.setEmail(requestUser.getEmail());
		
		//userRepository.save(user);
		
		// 더티 체킹
		
		return user;
	}
	
	
	@GetMapping("/dummy/users")
	public List<User>list(){
		return userRepository.findAll();
	}
	
	@GetMapping("/dummy/user")
	public Page<User>pageList(@PageableDefault(size=2, sort="id", direction=Sort.Direction.DESC)Pageable pageable){
		Page<User> pagingUsers = userRepository.findAll(pageable);
		
		List<User>users = pagingUsers.getContent();
		return pagingUsers;
	}
	////ss
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
