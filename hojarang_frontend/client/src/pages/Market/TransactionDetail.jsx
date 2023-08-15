import axios from 'axios'
import { useEffect, useState } from 'react'
import { useParams } from 'react-router'
import { useNavigate } from 'react-router-dom'

export default function TransactionDetail() {
  const navigate = useNavigate()
  const URL = import.meta.env.VITE_APP_API
  const [ detail, setDetail ] = useState({})
  const token = localStorage.getItem('access_token')
  const LoginId = localStorage.getItem("user_id")
  const headers = {"Authorization" : `Bearer ${token}`}
  const {id} = useParams()
  const [isWriter, setIsWriter] = useState(false)
  const [isSell, setIsSell] = useState(false)
  const [isFinish, setIsFinish] = useState(false)
  const [isClick, setIsClick] = useState(false)
  const [isBuyer, setIsBuyer] = useState(false)

  const startChat = () => {
    const data = {target_id : Number(detail.seller_id)}
    axios.post(`${URL}/api/v1/chats/one-to-one`,data,{ headers:{'Authorization': `Bearer ${token}`, "Content-Type":"Application/json"}})
      .then((res) => {
        console.log(res)
        navigate(`/chatting/${res.data}`)
      })
      .catch((err) => console.log(err))
  }

  const handleClick = () => {
    setIsClick(!isClick)
  }
  const editTransaction = () =>{
    navigate(`/market/transactionupdate/${id}`)
  }
  const handelTransaction = () =>{
    axios.delete(`${URL}/api/v1/secondhand-transactions/${id}`,{headers})
      .then((res) => {
        console.log(res)
        navigate('/market', {replace : true})
      })
      .catch((err) => {
        console.log(err)
      })
  }
  const buy = () => {
    axios.put(`${URL}/api/v1/secondhand-transactions/${id}/buy`,[],{headers})
      .then((res) => {
        console.log(res)
        setIsSell(true)
      })
      .catch((err) => {
        console.log(err)
        window.alert("포인트가 부족합니다.")
      })
  }

  const check = () => {
    axios.put(`${URL}/api/v1/secondhand-transactions/${id}/check`, [], {headers})
      .then((res) => {
        console.log(res)
        setIsFinish(true)
      })
      .catch((err) => {
        console.log(err)
      })
  }

  useEffect(()=>{
    axios.get(`${URL}/api/v1/secondhand-transactions/${id}`,{headers})
      .then((res) => {
        console.log(res.data)
        setDetail(res.data)
        if(Number(LoginId) === res.data.seller_id){
          setIsWriter(true)
        }else{
          setIsWriter(false)
        }
        if(res.data.is_completed){
          setIsSell(true)
        }else{
          setIsSell(false)
        }
        if(res.data.buyer_id == LoginId){
          setIsBuyer(true)
        }else{
          setIsBuyer(false)
        }
        if(res.data.is_received){
          setIsFinish(true)
        }else{
          setIsFinish(false)
        }
      })
      .catch((err) => console.log(err))
  },[])
  return (
    <div className="w-6/12 mx-auto mt-5 border rounded-lg p-5">
      <div className="flex justify-between">
        <div>
          <div className="font-bold text-3xl">{detail.title}</div>
          <div className="mt-3">{detail.price?.toLocaleString()}원</div>
        </div>
        <div className="flex">
          <div>
            <div className="font-semibold text-right">{detail.seller_nickname}</div>
            <div>{detail.created_at?.slice(0,10)}</div>
          </div>
          {isWriter ? (
            <button onClick={handleClick}>
              <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" strokeWidth={1.5} stroke="currentColor" className="w-6 h-6">
                <path strokeLinecap="round" strokeLinejoin="round" d="M12 6.75a.75.75 0 110-1.5.75.75 0 010 1.5zM12 12.75a.75.75 0 110-1.5.75.75 0 010 1.5zM12 18.75a.75.75 0 110-1.5.75.75 0 010 1.5z" />
              </svg>
            </button>
          ):(null)}
          {isClick&&isWriter ? (
            <div className='absolute border-2 rounded-lg bg-white text-center space-y-2 p-2'>
              <div className="cursor-pointer" onClick={editTransaction}>수정</div>
              <div className="cursor-pointer" onClick={handelTransaction}>삭제</div>
            </div>
          ):null}
        </div>
      </div>
      <div className="flex justify-between my-5">
        {isFinish ? (
          <div className="text-main1 font-bold">수령완료</div>
        ):(
          <>
            {isSell ? (
              <div className="text-main5 font-bold">판매완료</div>
            ):(
              <div className="text-main1 font-bold">판매중</div>
            )}
          </>
        )}
        <button className="main1-full-button w-24" onClick={startChat}>1:1 채팅</button>
      </div>
      <hr />
      <div className="my-3">
        {detail.transaction_image ? (
          <img 
            src={`${detail.transaction_image}`} 
            alt="상품이미지"
            className="mx-auto"
          />
        ):(null)}
        <div className="my-3">
          {detail.content}
        </div>
        {!isWriter ? (
          <div>
            {isSell ? (
              <>
              {isBuyer ? (
                <>
                  {isFinish ? (null):(
                    <button className="main1-full-button w-full" onClick={check}>수령확인</button>
                  )}
                </>
                ):(null)}
              </>
            ):(
              <button className="main1-full-button w-full" onClick={buy}>구매</button>
            )}
          </div>
        ):(
          null
        )}
      </div>
    </div>
  )
}
