export interface ApiResponse<T> {
  message: string;
  data: T;
}

export interface Wallet {
  id: number;
  userId: string;
  balance: number;
  currency: string;
  version: number;
}

export interface Transaction {
  id: number;
  fromWalletId: number;
  toWalletId: number;
  amount: number;
  status: string;
  referenceId: string;
}

export interface LoginRequest {
  username: string;
  password: string;
}

export interface CreateWalletRequest {
  userId: string;
  currency: string;
}

export interface TransferRequest {
  fromWalletId: number;
  toWalletId: number;
  amount: number;
}