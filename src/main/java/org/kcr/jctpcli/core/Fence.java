package org.kcr.jctpcli.core;

import java.util.concurrent.atomic.AtomicBoolean;

// 初始化成功标记
public class Fence {

	// 登录完成
	private AtomicBoolean bLogin;
	// 账户信息
	private AtomicBoolean bAccount;
	// 合约信息
	private AtomicBoolean bInstrument;
	// 手续费
	private AtomicBoolean bCommissionRate;

	// 持仓信息
	private AtomicBoolean bInvestorPosition;

	// 构造函数
	public Fence() {
		this.bLogin = new AtomicBoolean();
		this.bAccount = new AtomicBoolean();
		this.bInstrument = new AtomicBoolean();
		this.bCommissionRate = new AtomicBoolean();
		this.bInvestorPosition = new AtomicBoolean();
	}

	// 是否已经完成登录
	public boolean hasLogin() {
		return bLogin.get();
	}

	public void doneLogin() {
		bLogin.set(true);
	}

	public boolean hasAccount() {
		return bAccount.get();
	}

	public void doneAccount() {
		bAccount.set(true);
	}

	public boolean hasInstrument() {
		return bInstrument.get();
	}

	public void doneInstrument() {
		bInstrument.set(true);
	}

	public boolean hasCommissionRate() {
		return bCommissionRate.get();
	}

	public void doneCommissionRate() {
		bCommissionRate.set(true);
	}

	public boolean hasInvestorPosition() {
		return bInvestorPosition.get();
	}

	public void doneInvestorPosition() {
		this.bInvestorPosition.set(true);
	}

	public boolean ready() {
		return hasLogin() && hasAccount() && hasInstrument() && hasCommissionRate() && hasInvestorPosition();
	}
}
