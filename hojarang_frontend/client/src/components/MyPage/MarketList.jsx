import axios from "axios";
import { useEffect } from "react";

export default function MarketList() {
  const URL = import.meta.env.VITE_APP_API
  const token = localStorage.getItem("access_token")
  useEffect(()=>{
    axios.get(`${URL}/api/v1/users/joint-deliveries-participating`)
  })

  return <div>MarketList</div>;
}
