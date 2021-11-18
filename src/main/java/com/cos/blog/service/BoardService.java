package com.cos.blog.service;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cos.blog.dto.ReplySaveRequestDto;
import com.cos.blog.model.Board;
import com.cos.blog.model.User;
import com.cos.blog.repository.BoardRepository;
import com.cos.blog.repository.ReplyRepository;

@Service
public class BoardService {

	@Autowired
	private BoardRepository boardRepository;

	@Autowired
	private ReplyRepository replyRepository;

	@Transactional
	public void writeAction(Board board, User user) { // title content
		board.setCount(0);
		board.setUser(user);
		boardRepository.save(board);
	}

	@Transactional(readOnly = true)
	public Page<Board> wirteList(Pageable pageable) {
		return boardRepository.findAll(pageable);
	}

	@Transactional(readOnly = true)
	public Board lookWrite(int id) {
		return boardRepository.findById(id).orElseThrow(() -> {
			return new IllegalArgumentException("글 상세보기 실패:아이디를 찾을 수 없습니다.");
		});
	}

	@Transactional
	public void deleteWrite(int id) {
		boardRepository.deleteById(id);
	}

	@Transactional
	public void update(int id, Board requestBoard) {
		Board board = boardRepository.findById(id).orElseThrow(() -> {
			return new IllegalArgumentException("글 찾기 실패:아이디를 찾을 수 없습니다.");
		});// 영속화 완료
		board.setTitle(requestBoard.getTitle());
		board.setContent(requestBoard.getContent());
		// 해당 함수로 종료시에 (Service가 종료될 때)트랜잭션이 종료돕니다. 이떄 더티체킹이 일어나면서 자동업데이트가 됨.db flush
	}

	@Transactional
	public void writeComment(ReplySaveRequestDto replySaveRequestDto) {
		
	/*  댓글작성시 영속화  하는 방법	
		User user = userRepository.findById(replySaveRequestDto.getUserId()).orElseThrow(() -> {
			return new IllegalArgumentException("댓글 쓰기 실패:유저 아이디를 찾을 수 없습니다.");
		});
		
		Board board = boardRepository.findById(replySaveRequestDto.getBoardId()).orElseThrow(() -> {
			return new IllegalArgumentException("댓글 쓰기 실패:게시글 아이디를 찾을 수 없습니다.");
		});
		//첫번째 방법 직접 빌드로 다 적어준다	
		Reply reply = Reply.builder()
				.user(user)
				.board(board)
				.content(replySaveRequestDto.getContent())
				.build();
	 		
		//두번째 방법 Reply클래스에 메서드를 만들어준다
		Reply reply = new Reply();
		reply.update(user, board, replySaveRequestDto.getContent());
		
		replyRepository.save(reply);
		*/				
		replyRepository.mSave(replySaveRequestDto.getUserId(), replySaveRequestDto.getBoardId(), replySaveRequestDto.getContent());		
	}
	@Transactional
	public void commentDelete(int replyId) {
		replyRepository.deleteById(replyId);
	}
}
