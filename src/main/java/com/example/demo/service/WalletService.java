package com.example.demo.service;

import com.example.demo.dao.WalletDao;
import com.example.demo.dao.impl.WalletDaoImpl;
import com.example.demo.model.Wallet;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.util.Optional;

public class WalletService {
    private WalletDao walletDao;

    public WalletService(DataSource dataSource) {
        this.walletDao = new WalletDaoImpl(dataSource);
    }

    /**
     * 为用户创建钱包
     * @param userId 用户ID
     * @return 新创建的钱包
     */
    public Wallet createWallet(Long userId) {
        Wallet wallet = new Wallet(userId);
        return walletDao.create(wallet);
    }

    /**
     * 获取用户钱包
     * @param userId 用户ID
     * @return 钱包对象，如果不存在则返回null
     */
    public Wallet getWallet(Long userId) {
        Optional<Wallet> walletOptional = walletDao.findByUserId(userId);
        return walletOptional.orElse(null);
    }

    /**
     * 充值
     * @param userId 用户ID
     * @param amount 充值金额
     * @return 更新后的钱包
     */
    public Wallet deposit(Long userId, BigDecimal amount) {
        Wallet wallet = getWallet(userId);
        if (wallet == null) {
            wallet = createWallet(userId);
        }
        wallet.deposit(amount);
        return walletDao.update(wallet);
    }

    /**
     * 提现
     * @param userId 用户ID
     * @param amount 提现金额
     * @return 更新后的钱包，如果余额不足则返回null
     */
    public Wallet withdraw(Long userId, BigDecimal amount) {
        Wallet wallet = getWallet(userId);
        if (wallet == null) {
            return null;
        }
        
        if (wallet.withdraw(amount)) {
            return walletDao.update(wallet);
        }
        return null; // 余额不足
    }

    /**
     * 检查余额是否足够
     * @param userId 用户ID
     * @param amount 需要检查的金额
     * @return 是否足够
     */
    public boolean hasSufficientBalance(Long userId, BigDecimal amount) {
        Wallet wallet = getWallet(userId);
        if (wallet == null) {
            return false;
        }
        return wallet.getBalance().compareTo(amount) >= 0;
    }

    /**
     * 支付（从买家钱包扣款）
     * @param buyerId 买家ID
     * @param amount 支付金额
     * @return 支付是否成功
     */
    public boolean pay(Long buyerId, BigDecimal amount) {
        Wallet wallet = getWallet(buyerId);
        if (wallet == null) {
            return false;
        }
        
        if (wallet.withdraw(amount)) {
            walletDao.update(wallet);
            return true;
        }
        return false;
    }

    /**
     * 收款（向卖家钱包加款）
     * @param sellerId 卖家ID
     * @param amount 收款金额
     * @return 更新后的钱包
     */
    public Wallet receive(Long sellerId, BigDecimal amount) {
        Wallet wallet = getWallet(sellerId);
        if (wallet == null) {
            wallet = createWallet(sellerId);
        }
        wallet.deposit(amount);
        return walletDao.update(wallet);
    }
}