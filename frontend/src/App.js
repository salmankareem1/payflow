import React, { useEffect, useState } from "react";
import axios from "axios";

function App() {
  const [wallets, setWallets] = useState([]);
  const [userId, setUserId] = useState("");
  const [balance, setBalance] = useState("");
  const [currency, setCurrency] = useState("");

  const fetchWallets = () => {
    axios.get("http://localhost:8080/api/wallets")
      .then(res => {
        setWallets(res.data);
      })
      .catch(err => {
        console.error(err);
      });
  };

  useEffect(() => {
    fetchWallets();
  }, []);

  const handleCreateWallet = (e) => {
    e.preventDefault();

    const newWallet = {
      userId: userId,
      balance: parseFloat(balance),
      currency: currency
    };

    axios.post("http://localhost:8080/api/wallet", newWallet)
      .then(() => {
        setUserId("");
        setBalance("");
        setCurrency("");
        fetchWallets();
      })
      .catch(err => {
        console.error(err);
      });
  };

  return (
    <div>
      <h1>Payflow Wallets</h1>

      <h2>Create Wallet</h2>
      <form onSubmit={handleCreateWallet}>
        <div>
          <input
            type="text"
            placeholder="Enter user id"
            value={userId}
            onChange={(e) => setUserId(e.target.value)}
          />
        </div>

        <div>
          <input
            type="number"
            placeholder="Enter balance"
            value={balance}
            onChange={(e) => setBalance(e.target.value)}
          />
        </div>

        <div>
          <input
            type="text"
            placeholder="Enter currency"
            value={currency}
            onChange={(e) => setCurrency(e.target.value)}
          />
        </div>

        <button type="submit">Create Wallet</button>
      </form>

      <hr />

      <h2>Wallet List</h2>
      {wallets.map(wallet => (
        <div key={wallet.id}>
          <p>User: {wallet.userId}</p>
          <p>Balance: {wallet.balance}</p>
          <p>Currency: {wallet.currency}</p>
          <hr />
        </div>
      ))}
    </div>
  );
}

export default App;