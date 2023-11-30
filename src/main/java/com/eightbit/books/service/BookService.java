package com.eightbit.books.service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eightbit.books.entity.Books;
import com.eightbit.books.entity.Genre;
import com.eightbit.books.entity.History;
import com.eightbit.books.entity.Status;
import com.eightbit.books.model.BookSearchQuery;
import com.eightbit.books.repository.BooksRepository;
import com.eightbit.books.repository.GenreRepository;
import com.eightbit.books.repository.HistoryRepository;
import com.eightbit.books.repository.StatusRepository;

@Service
public class BookService {

	@Autowired
	private BooksRepository booksRepo;

	@Autowired
	private HistoryRepository historyRepo;

	@Autowired
	private GenreRepository genreRepo;

	@Autowired
	private StatusRepository statusRepo;

	public List<Books> findAll(){
		return booksRepo.findAllByOrderByTitleAsc();
	}
	
	/**
	 * ★特定書籍検索
	 * @param bookId
	 * @return 特定書籍データ
	 */
	public Books findOne(int bookId) {
		return booksRepo.findByBookId(bookId);
	}
	
	/**
	 * ★特定書籍検索(書籍名、著者)
	 * @param searchQuery
	 * @return 特定書籍データ(複数)
	 */
	public List<Books> searchBook(BookSearchQuery searchQuery) {
		int radioValue = searchQuery.getRadioValue();
		String queryText = searchQuery.getQueryText();

		List<Books> bookList = null;
		
		//押したradioボタンが0=書籍名か、1=著者名かによってif文で処理を変える
		if(radioValue == 0) {
			//0が押されたら、queryTextの値をbookrepoの書籍名としてbookListに格納する
			bookList = booksRepo.findByTitleContaining(queryText);
		}else {
			bookList = booksRepo.findByAuthorContaining(queryText);
		}
		//格納された値を返す
		return bookList;
	}

	/**
	 * ★書籍検索(ジャンル)
	 * @param genreId
	 * @return 特定書籍データ(複数)
	 */
	public List<Books> searchBookGenre(int genreId) {

		return booksRepo.findByGenreGenreId(genreId);
	}

	/**
	 * ★特定の書籍データを削除する 該当書籍IDをもつHistoryテーブルのデータも全て削除する
	 * 
	 * @param bookId
	 */
	public void deleteBookAndHistoryData(int bookId) {
		Books del = booksRepo.findByBookId(bookId);
		List<History> delH = historyRepo.findByBooksBookId(bookId);
		booksRepo.delete(del);
		historyRepo.deleteAll(delH);
	}

	/**
	 * ★書籍特定データ在庫情報更新
	 * @param bookId
	 * @param stock
	 * @param status
	 */
	public void updateBookStock(int bookId, int stock, int status) {

		Books book = booksRepo.getReferenceById((long) bookId);
		book.setStock(stock);

		switch (status) {
		case 0:
//		    statusは更新せずstockのみ更新(何もしない)
			break;
		case 1:
//			Not Available → Available
			//getReferenceById...指定された識別子を持つentityへの参照を返す
			book.setStatus(statusRepo.getReferenceById((long)1));
			break;
		case 2:
//			Available → Not Available
			book.setStatus(statusRepo.getReferenceById((long)2));
			break;
		}
		booksRepo.save(book);
	}

	/**
	 * 登録時のジャンル一覧取得
	 * @return 全ジャンルデータ
	 */
	public List<Genre> getGenreAll() {
		List<Genre> genreList = genreRepo.findAll();
		return genreList;
	}

	/**
	 * ★書籍情報新規登録
	 * @param book
	 * @param genreId
	 */
	public void bookRegist(Books book, int genreId) {
		//ジャンルの値を取得する
		Genre genre = genreRepo.getReferenceById((long)genreId);
		//statusの値を取得する
		Status status = statusRepo.getReferenceById((long)1);

		book.setGenre(genre);
		book.setStatus(status);
		book.setRegistrationDate(new Date());
		
		booksRepo.save(book);
	}

}
