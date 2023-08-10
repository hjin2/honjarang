import axios from 'axios'
import { useEffect, useState } from 'react'
import { useParams } from 'react-router'

export default function TransactionDetail() {
  const URL = import.meta.env.VITE_APP_API
  const [ detail, setDetail ] = useState({})
  const token = localStorage.getItem('access_token')
  const headers = {"Authorization" : `Bearer ${token}`}
  const {id} = useParams()

  useEffect(()=>{
    axios.get(`${URL}/api/v1/secondhand-transactions/${id}`,{headers})
      .then((res) => {
        console.log(res.data)
        setDetail(res.data)
      })
      .catch((err) => console.log(err))
  },[id])
  return (
    <div className="w-6/12 mx-auto mt-5 border rounded-lg p-5">
      <div className="flex justify-between">
        <div className="font-bold text-3xl">{detail.title}</div>
        <div>
          <div className="font-semibold text-right">{detail.seller_nickname}</div>
          <div>{detail.created_at?.slice(0,10)}</div>
        </div>
      </div>
      <div className="flex justify-between mt-5">
        <div className="text-main5 mt-3">{detail.price?.toLocaleString()}원</div>
        <button className="main1-full-button w-24">1:1 채팅</button>
      </div>
      <hr />
      <div className="mt-2">
        <img src={`https://honjarang-bucket.s3.ap-northeast-2.amazonaws.com/transactionImage/${detail.image}`} alt="상품이미지" />
        {detail.content}
      </div>
    </div>
  )
}
