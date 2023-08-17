import { useEffect } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import { API } from "@/apis/config";

export default function Refresh() {
  const navigate = useNavigate()

  useEffect(() => {
    const refreshAPI = axios.create({
      baseURL: "https://honjarnag.kro.kr/api/v1",
      headers: {"Content-Type" : "application/json"},
    })

    const interceptor = axios.interceptors.response.use(
      function (response) {
        return response
      },
      async function(err) {
        const originalConfig = err.config
        // const msg = err.response.data.message
        const status = err.response.status

        if (status === 403) {

          await axios.post(`${API.USER}/refresh`,{
            refresh_token: localStorage.getItem('refresh_token')
          })
          .then((res) => {
            localStorage.setItem('access_token', res.data.access_token)
            localStorage.setItem('refresh_token', res.data.refresh_token)
  
            originalConfig.headers["Autorization"] = "Bearer "+res.data.access_token
            window.location.reload()
            return refreshAPI(originalConfig)
          })
          .then((res) => {
            window.location.reload()
          })
          .catch((err) => {
            console.log(err)
          })
        }
        else {
          return Promise.reject(err)
        }
      }
    )
    return () => {
      axios.interceptors.response.eject(interceptor)
    }
  },[])
}
