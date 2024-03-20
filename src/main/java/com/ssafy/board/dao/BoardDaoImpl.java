package com.ssafy.board.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.ssafy.board.model.BoardDto;
import com.ssafy.util.DBUtil;

public class BoardDaoImpl implements BoardDao {
	//싱글톤 
	private BoardDaoImpl() {
	}
	private static BoardDao instance = new BoardDaoImpl();
	public static BoardDao getInstance() {
		return instance;
	}
	/**
	* DB에서 글 목록을 조회하여 리턴
	*/
	
	
	@Override
	public int writeArticle(BoardDto boardDto) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		int success = 0;
		try {
			conn = DBUtil.getConnection();
			pstmt = conn.prepareStatement("insert into board (user_id, subject, content) values (?,?,?)");
			pstmt.setString(1, boardDto.getUserId());
			pstmt.setString(2, boardDto.getSubject());
			pstmt.setString(3, boardDto.getContent());
			success = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(pstmt, conn);
		}
		return success;
	}

	@Override
	public List<BoardDto> listArticle() {
		List<BoardDto> list = new ArrayList<>();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = DBUtil.getConnection();
			StringBuilder sb = new StringBuilder();
			sb.append("select * from board");
			
			pstmt = conn.prepareStatement(sb.toString());
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				BoardDto board = new BoardDto();
				board.setArticleNo(rs.getInt("article_no"));
				board.setSubject(rs.getString("subject"));
				board.setContent(rs.getString("content"));
				board.setUserId(rs.getString("user_id"));
				board.setHit(rs.getInt("hit"));
				board.setRegisterTime(rs.getString("register_time"));
				list.add(board);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			DBUtil.close(rs,pstmt, conn);
		}
		return list;
	}

	@Override
	public BoardDto viewArticle(int articleNo) {
		BoardDto boardDto = null;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = DBUtil.getConnection();
			pstmt = conn.prepareStatement("select * from board where article_no=?");
			pstmt.setInt(1, articleNo);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				boardDto = new BoardDto();
				boardDto.setArticleNo(rs.getInt("article_no"));
				boardDto.setUserId(rs.getString("user_id"));
				boardDto.setSubject(rs.getString("subject"));
				boardDto.setContent(rs.getString("content"));
				boardDto.setRegisterTime(rs.getString("register_time"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			DBUtil.close(rs,pstmt, conn);
		}
		return boardDto;
	}

	@Override
	public void updateHit(int articleNo) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = DBUtil.getConnection();
			pstmt = conn.prepareStatement("update board set hit = hit + 1 where article_no=?");
			pstmt.setInt(1, articleNo);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			DBUtil.close(pstmt, conn);
		}
		
	}

}
