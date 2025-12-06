package com.example.demo.dao.impl;

import com.example.demo.dao.WalletDao;
import com.example.demo.model.Wallet;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.*;
import java.util.Optional;

public class WalletDaoImpl implements WalletDao {
    private DataSource dataSource;

    public WalletDaoImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Wallet create(Wallet wallet) {
        String sql = "INSERT INTO wallets (user_id, balance) VALUES (?, ?)";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setLong(1, wallet.getUserId());
            stmt.setBigDecimal(2, wallet.getBalance());
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("创建钱包失败，没有行受到影响。");
            }
            
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    wallet.setId(generatedKeys.getLong(1));
                } else {
                    throw new SQLException("创建钱包失败，没有获取到ID。");
                }
            }
            return wallet;
        } catch (SQLException e) {
            throw new RuntimeException("创建钱包时发生错误", e);
        }
    }

    @Override
    public Optional<Wallet> findByUserId(Long userId) {
        String sql = "SELECT id, user_id, balance FROM wallets WHERE user_id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Wallet wallet = new Wallet();
                    wallet.setId(rs.getLong("id"));
                    wallet.setUserId(rs.getLong("user_id"));
                    wallet.setBalance(rs.getBigDecimal("balance"));
                    return Optional.of(wallet);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("查询钱包时发生错误", e);
        }
        return Optional.empty();
    }

    @Override
    public Wallet update(Wallet wallet) {
        String sql = "UPDATE wallets SET balance = ? WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setBigDecimal(1, wallet.getBalance());
            stmt.setLong(2, wallet.getId());
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("更新钱包失败，钱包可能不存在。");
            }
            return wallet;
        } catch (SQLException e) {
            throw new RuntimeException("更新钱包时发生错误", e);
        }
    }

    @Override
    public void deleteByUserId(Long userId) {
        String sql = "DELETE FROM wallets WHERE user_id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, userId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("删除钱包时发生错误", e);
        }
    }
}