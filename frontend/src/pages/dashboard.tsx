import React, { useEffect, useState } from "react";
import { useAuth } from "../context/authContext";
import { useNavigate } from "react-router-dom";
import apiClient from "../api/apiClient";
import { ApiResponse, Wallet, Transaction } from "../types";

function Dashboard() {
  const { logout } = useAuth();
  const navigate = useNavigate();

  const [wallets, setWallets] = useState<Wallet[]>([]);
  const [transactions, setTransactions] = useState<Transaction[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  // Transfer form state
  const [fromWalletId, setFromWalletId] = useState("");
  const [toWalletId, setToWalletId] = useState("");
  const [amount, setAmount] = useState("");
  const [transferMessage, setTransferMessage] = useState<string | null>(null);
  const [transferError, setTransferError] = useState<string | null>(null);

  // Create wallet form state
  const [userId, setUserId] = useState("");
  const [currency, setCurrency] = useState("");
  const [createMessage, setCreateMessage] = useState<string | null>(null);

  const fetchData = async () => {
    try {
      const [walletsRes, transactionsRes] = await Promise.all([
        apiClient.get<ApiResponse<Wallet[]>>("/api/wallets"),
        apiClient.get<ApiResponse<Transaction[]>>("/api/transactions"),
      ]);
      setWallets(walletsRes.data.data);
      setTransactions(transactionsRes.data.data);
    } catch (err) {
      setError("Failed to load data");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchData();
  }, []);

  const handleLogout = () => {
    logout();
    navigate("/login");
  };

  const handleCreateWallet = async (e: React.SyntheticEvent<HTMLFormElement>) => {
    e.preventDefault();
    try {
      await apiClient.post("/api/wallet", { userId, currency });
      setCreateMessage("Wallet created successfully");
      setUserId("");
      setCurrency("");
      fetchData();
    } catch (err: any) {
      setCreateMessage(err.response?.data?.message || "Failed to create wallet");
    }
  };

  const handleTransfer = async (e: React.SyntheticEvent<HTMLFormElement>) => {
    e.preventDefault();
    setTransferMessage(null);
    setTransferError(null);
    try {
      await apiClient.post("/api/transaction", {
        fromWalletId: parseInt(fromWalletId),
        toWalletId: parseInt(toWalletId),
        amount: parseFloat(amount),
      });
      setTransferMessage("Transfer completed successfully");
      setFromWalletId("");
      setToWalletId("");
      setAmount("");
      fetchData();
    } catch (err: any) {
      setTransferError(err.response?.data?.message || "Transfer failed");
    }
  };

  if (loading) {
    return (
      <div className="min-h-screen bg-slate-50 flex items-center justify-center">
        <p className="text-slate-500">Loading...</p>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-slate-50">

      {/* Header */}
      <header className="bg-white border-b border-slate-200 px-6 py-4 flex items-center justify-between">
        <h1 className="text-xl font-bold text-slate-900">PayFlow</h1>
        <button
          onClick={handleLogout}
          className="text-sm text-slate-500 hover:text-slate-900 transition-colors"
        >
          Sign out
        </button>
      </header>

      <main className="max-w-6xl mx-auto px-6 py-8 space-y-8">

        {error && (
          <div className="px-4 py-3 bg-red-50 border border-red-200 rounded-lg text-red-700 text-sm">
            {error}
          </div>
        )}

        {/* Wallets Section */}
        <section>
          <h2 className="text-lg font-semibold text-slate-900 mb-4">Wallets</h2>
          {wallets.length === 0 ? (
            <p className="text-slate-500 text-sm">No wallets found.</p>
          ) : (
            <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-4">
              {wallets.map((wallet) => (
                <div key={wallet.id} className="bg-white rounded-xl border border-slate-200 p-5">
                  <p className="text-xs text-slate-400 mb-1">Wallet #{wallet.id}</p>
                  <p className="text-2xl font-bold text-slate-900">
                    {wallet.balance} <span className="text-base font-medium text-slate-500">{wallet.currency}</span>
                  </p>
                  <p className="text-sm text-slate-600 mt-1">{wallet.userId}</p>
                </div>
              ))}
            </div>
          )}
        </section>

        {/* Two column layout for forms */}
        <div className="grid grid-cols-1 lg:grid-cols-2 gap-8">

          {/* Create Wallet */}
          <section className="bg-white rounded-xl border border-slate-200 p-6">
            <h2 className="text-lg font-semibold text-slate-900 mb-4">Create Wallet</h2>

            {createMessage && (
              <div className="mb-4 px-4 py-3 bg-green-50 border border-green-200 rounded-lg text-green-700 text-sm">
                {createMessage}
              </div>
            )}

            <form onSubmit={handleCreateWallet} className="space-y-4">
              <div>
                <label className="block text-sm font-medium text-slate-700 mb-1">User ID</label>
                <input
                  type="text"
                  value={userId}
                  onChange={(e) => setUserId(e.target.value)}
                  placeholder="e.g. user123"
                  required
                  className="w-full px-3 py-2 border border-slate-200 rounded-lg text-sm focus:outline-none focus:ring-2 focus:ring-slate-900 transition"
                />
              </div>
              <div>
                <label className="block text-sm font-medium text-slate-700 mb-1">Currency</label>
                <input
                  type="text"
                  value={currency}
                  onChange={(e) => setCurrency(e.target.value)}
                  placeholder="e.g. EUR"
                  required
                  className="w-full px-3 py-2 border border-slate-200 rounded-lg text-sm focus:outline-none focus:ring-2 focus:ring-slate-900 transition"
                />
              </div>
              <button
                type="submit"
                className="w-full py-2 px-4 bg-slate-900 hover:bg-slate-700 text-white text-sm font-semibold rounded-lg transition-colors duration-200"
              >
                Create Wallet
              </button>
            </form>
          </section>

          {/* Transfer Funds */}
          <section className="bg-white rounded-xl border border-slate-200 p-6">
            <h2 className="text-lg font-semibold text-slate-900 mb-4">Transfer Funds</h2>

            {transferMessage && (
              <div className="mb-4 px-4 py-3 bg-green-50 border border-green-200 rounded-lg text-green-700 text-sm">
                {transferMessage}
              </div>
            )}
            {transferError && (
              <div className="mb-4 px-4 py-3 bg-red-50 border border-red-200 rounded-lg text-red-700 text-sm">
                {transferError}
              </div>
            )}

            <form onSubmit={handleTransfer} className="space-y-4">
              <div>
                <label className="block text-sm font-medium text-slate-700 mb-1">From Wallet ID</label>
                <input
                  type="number"
                  value={fromWalletId}
                  onChange={(e) => setFromWalletId(e.target.value)}
                  placeholder="e.g. 1"
                  required
                  className="w-full px-3 py-2 border border-slate-200 rounded-lg text-sm focus:outline-none focus:ring-2 focus:ring-slate-900 transition"
                />
              </div>
              <div>
                <label className="block text-sm font-medium text-slate-700 mb-1">To Wallet ID</label>
                <input
                  type="number"
                  value={toWalletId}
                  onChange={(e) => setToWalletId(e.target.value)}
                  placeholder="e.g. 2"
                  required
                  className="w-full px-3 py-2 border border-slate-200 rounded-lg text-sm focus:outline-none focus:ring-2 focus:ring-slate-900 transition"
                />
              </div>
              <div>
                <label className="block text-sm font-medium text-slate-700 mb-1">Amount</label>
                <input
                  type="number"
                  value={amount}
                  onChange={(e) => setAmount(e.target.value)}
                  placeholder="e.g. 100"
                  required
                  className="w-full px-3 py-2 border border-slate-200 rounded-lg text-sm focus:outline-none focus:ring-2 focus:ring-slate-900 transition"
                />
              </div>
              <button
                type="submit"
                className="w-full py-2 px-4 bg-slate-900 hover:bg-slate-700 text-white text-sm font-semibold rounded-lg transition-colors duration-200"
              >
                Transfer
              </button>
            </form>
          </section>
        </div>

        {/* Recent Transactions */}
        <section>
          <h2 className="text-lg font-semibold text-slate-900 mb-4">Recent Transactions</h2>
          {transactions.length === 0 ? (
            <p className="text-slate-500 text-sm">No transactions yet.</p>
          ) : (
            <div className="bg-white rounded-xl border border-slate-200 overflow-hidden">
              <table className="w-full text-sm">
                <thead className="bg-slate-50 border-b border-slate-200">
                  <tr>
                    <th className="px-4 py-3 text-left text-xs font-medium text-slate-500 uppercase">Ref</th>
                    <th className="px-4 py-3 text-left text-xs font-medium text-slate-500 uppercase">From</th>
                    <th className="px-4 py-3 text-left text-xs font-medium text-slate-500 uppercase">To</th>
                    <th className="px-4 py-3 text-left text-xs font-medium text-slate-500 uppercase">Amount</th>
                    <th className="px-4 py-3 text-left text-xs font-medium text-slate-500 uppercase">Status</th>
                  </tr>
                </thead>
                <tbody className="divide-y divide-slate-100">
                  {transactions.map((tx) => (
                    <tr key={tx.id} className="hover:bg-slate-50 transition-colors">
                      <td className="px-4 py-3 text-slate-500 font-mono text-xs">{tx.referenceId?.slice(0, 16)}...</td>
                      <td className="px-4 py-3 text-slate-700">Wallet {tx.fromWalletId}</td>
                      <td className="px-4 py-3 text-slate-700">Wallet {tx.toWalletId}</td>
                      <td className="px-4 py-3 font-medium text-slate-900">{tx.amount}</td>
                      <td className="px-4 py-3">
                        <span className="px-2 py-1 bg-green-50 text-green-700 rounded-full text-xs font-medium">
                          {tx.status}
                        </span>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          )}
        </section>

      </main>
    </div>
  );
}

export default Dashboard;