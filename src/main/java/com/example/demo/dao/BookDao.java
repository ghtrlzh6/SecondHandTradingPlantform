package com.example.demo.dao;

import com.example.demo.model.Book;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

public interface BookDao {
    /**
     * 保存新书
     *
     * @param book 书籍对象
     * @return 保存后的书籍ID
     * @throws SQLException 数据库访问异常
     */
    Long save(Book book) throws SQLException;

    /**
     * 根据ID查找书籍
     *
     * @param id 书籍ID
     * @return 书籍对象的Optional包装
     * @throws SQLException 数据库访问异常
     */
    java.util.Optional<Book> findById(Long id) throws SQLException;

    /**
     * 获取所有书籍（分页）
     *
     * @param offset 偏移量
     * @param limit  限制数量
     * @return 书籍列表
     * @throws SQLException 数据库访问异常
     */
    List<Book> findAll(int offset, int limit) throws SQLException;

    /**
     * 根据关键词搜索书籍（标题或作者）
     *
     * @param keyword 关键词
     * @param offset  偏移量
     * @param limit   限制数量
     * @return 匹配的书籍列表
     * @throws SQLException 数据库访问异常
     */
    List<Book> findByKeyword(String keyword, int offset, int limit) throws SQLException;

    /**
     * 计算书籍总数
     *
     * @return 总数
     * @throws SQLException 数据库访问异常
     */
    int countAll() throws SQLException;

    /**
     * 根据关键词计算匹配的书籍数量
     *
     * @param keyword 关键词
     * @return 匹配的数量
     * @throws SQLException 数据库访问异常
     */
    int countByKeyword(String keyword) throws SQLException;

    /**
     * 根据卖家ID获取其发布的书籍
     *
     * @param sellerId 卖家ID
     * @return 书籍列表
     * @throws SQLException 数据库访问异常
     */
    List<Book> findBySellerId(Long sellerId) throws SQLException;
}