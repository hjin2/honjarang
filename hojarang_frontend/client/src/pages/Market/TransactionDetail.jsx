import axios from 'axios'
import { useEffect, useState } from 'react'
import { useParams } from 'react-router'

export default function TransactionDetail() {
  const URL = import.meta.env.VITE_APP_API
  const [ detail, setDetail ] = useState({})
  const token = localStorage.getItem('access_token')
  const LoginId = localStorage.getItem("user_id")
  const headers = {"Authorization" : `Bearer ${token}`}
  const {id} = useParams()
  const [isWriter, setIsWriter] = useState(false)

  useEffect(()=>{
    axios.get(`${URL}/api/v1/secondhand-transactions/${id}`,{headers})
      .then((res) => {
        console.log(res.data)
        setDetail(res.data)
        if(Number(LoginId) === detail.seller_id){
          setIsWriter(true)
        }else{
          setIsWriter(false)
        }
      })
      .catch((err) => console.log(err))
  },[id])
  return (
    <div className="w-6/12 mx-auto mt-5 border rounded-lg p-5">
      <div className="flex justify-between">
        <div>
          <div className="font-bold text-3xl">{detail.title}</div>
          <div className="mt-3">{detail.price?.toLocaleString()}원</div>
        </div>
        <div>
          <div className="font-semibold text-right">{detail.seller_nickname}</div>
          <div>{detail.created_at?.slice(0,10)}</div>
        </div>
      </div>
      <div className="flex justify-between my-5">
        {detail.is_completed ? (
          <div className="text-main5 font-bold">판매완료</div>
        ):(
          <div className="text-main1 font-bold">판매중</div>
        )}
        <button className="main1-full-button w-24">1:1 채팅</button>
      </div>
      <hr />
      <div className="my-3">
        <img 
          src={`https://honjarang-bucket.s3.ap-northeast-2.amazonaws.com/transactionImage/${detail?.transaction_image}`} 
          alt="상품이미지"
          className="mx-auto"
          />
        <div className="mt-3 mb-5">
          {detail.content}
        </div>
        {!isWriter && detail.is_completed ? (
          <button className="main1-full-button w-full">수령확인</button>
        ):(
          <button className="main1-full-button w-full">구매하기</button>
        )}
      </div>
    </div>
  )
}
