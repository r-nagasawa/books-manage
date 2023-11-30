package com.eightbit.books.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.tomcat.util.http.fileupload.RequestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.eightbit.books.entity.History;
import com.eightbit.books.entity.User;
import com.eightbit.books.model.UserUpdateQuery;
import com.eightbit.books.repository.HistoryRepository;
import com.eightbit.books.repository.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private HistoryRepository historyRepo;

	public List<User> findAll(){
		return userRepo.findAll();
	}
	
	/**
	 * ★ユーザ名検索
	 * @param searchQuery
	 * @return 特定ユーザデータ(複数)
	 */
	public List<User> searchUser(String searchQuery) {
		
		List<User> result = new ArrayList<User>();
		result.addAll(userRepo.findByLastNameContaining(searchQuery));
		result.addAll(userRepo.findByFirstNameContaining(searchQuery));
		return result;
	}

	/**
	 * ★特定ユーザ検索
	 * @param userId
	 * @return 特定ユーザデータ
	 */
	public User searchUserById(int userId) {
		
		//ユーザーデータ表示
		return userRepo.findByUserId(userId);
		
	}

	/**
	 * ★特定のユーザデータを削除する 該当ユーザIDをもつHistoryテーブルのデータも全て削除する
	 * @param userId
	 */
	public void deleteUserAndHistoryData(int userId) {
		User del = userRepo.findByUserId(userId);
		List<History> delH = historyRepo.findByUserUserId(userId);
		userRepo.delete(del);
		historyRepo.deleteAll(delH);
	}

	/**
	 * 登録情報変更時パラメータ受け取り用
	 * @param user
	 * @return パラメータ格納モデル
	 */
	public UserUpdateQuery getUserDto(User user) {
		UserUpdateQuery uuq = new UserUpdateQuery();

		uuq.setUserId(user.getUserId());
		uuq.setLastName(user.getLastName());
		uuq.setFirstName(user.getFirstName());
		uuq.setTel(user.getTel());
		uuq.setMail(user.getMail());
		uuq.setAddress(user.getAddress());

		return uuq;
	}

	/**
	 * ★特定ユーザデータ情報更新
	 * @param uuq
	 */
	@Transactional
	public void updateUser(UserUpdateQuery uuq) {
		//User user = userRepo.findByUserId(userId);
		//idで受け取って更新をかける...getReferenceByIdでuuqに格納されたuserIDをゲッターで取得する
		User user = userRepo.getReferenceById((long)uuq.getUserId());
	
		user.setUserId(uuq.getUserId());
		user.setLastName(uuq.getLastName());
		user.setFirstName(uuq.getFirstName());
		user.setTel(uuq.getTel());
		user.setMail(uuq.getMail());
		user.setAddress(uuq.getAddress());
		
		userRepo.save(user);
		
	}

	/**
	 * ★ユーザ情報新規登録
	 * @param user
	 * @param birth
	 * @throws ParseException 
	 */
	public void userRegist(User user, String birth) {
		
		//entity(User)に誕生日(StringをparseDateで変換)と登録日時を値をセット
		user.setBirth(ServiceUtility.parseDate(birth));
		user.setUserRegistered(new Date());
		
		//疑問点…他のデータはどこからきたのか？→
		
		/*
		User newUser = new User();
		Date now = new Date();
		//String bDate = new String();
		//Date date1 = null;
	
		//String型で受け取ってDateにCastする
		newUser.setUserId(user.getUserId());
		newUser.setLastName(user.getLastName());
		newUser.setFirstName(user.getFirstName());
		//newUser.setBirth(user.getBirth());
		newUser.setBirth(ServiceUtility.parseDate(birth));
		newUser.setSex(user.getSex());
		newUser.setTel(user.getTel());
		newUser.setMail(user.getMail());
		newUser.setAddress(user.getAddress());
		newUser.setUserRegistered(now);
		*/
		
		userRepo.save(user);
	}
}
