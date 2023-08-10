import axios from "axios";
import { useEffect } from "react";

export default function MarketList() {
  const URL = import.meta.env.VITE_APP_API
  const token = localStorage.getItem("access_token")
  const headers = {"Authorization" : `Bearer ${token}`}
  useEffect(()=>{
    axios.get(`${URL}/api/v1/users/joint-deliveries-participating`, {params:{page:1, size:10},headers})
      .then((res) => console.log(res))
      .catch((err) => console.log(err))
  },[])

  return <div>MarketList</div>;
}
