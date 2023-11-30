package com.eightbit.books.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.eightbit.books.entity.History;

@Repository
public interface HistoryRepository extends JpaRepository<History, Long> {

	List<History> findAllByOrderByCheckoutDateDesc();

	List<History> findByCheckoutDateBetweenOrderByCheckoutDateDesc(Date since, Date until);

	List<History> findByCheckoutDateBetweenAndBooksBookIdInOrderByCheckoutDateDesc(Date since, Date until,
			List<Integer> bookId);

	List<History> findByCheckoutDateBetweenAndUserUserIdInOrderByCheckoutDateDesc(Date since, Date until,
			List<Integer> userId);

	List<History> findByCheckoutDateBetweenAndBooksBookIdInAndUserUserIdInOrderByCheckoutDateDesc(Date since,
			Date until, List<Integer> bookId, List<Integer> userId);

	List<History> findByBooksBookId(int bookId);

	List<History> findByUserUserId(int userId);

	History findById(int historyId);
}
