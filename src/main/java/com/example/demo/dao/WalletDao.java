package com.example.demo.dao;

import com.example.demo.model.Wallet;

import java.util.Optional;

public interface WalletDao {
    Wallet create(Wallet wallet);
    Optional<Wallet> findByUserId(Long userId);
    Wallet update(Wallet wallet);
    void deleteByUserId(Long userId);
}