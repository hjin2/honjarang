import { useSelector, useDispatch } from "react-redux"
import { useState } from "react"
import axios from "axios"
import { refund } from "@/redux/slice/UserInfoSlice"
import { API } from "@/apis/config"

export default function Refund({modalState, setModalState}) {
  const onClickCloseButton = (()=>{
    setModalState(!modalState)
  })
  const dispatch = useDispatch()
  const point = useSelector((state) => state.userinfo.point)
  const [inputPoint, setInputPoint] = useState(0)
  const [accountHolder, setAccoutHolder] = useState('')
  const [accountNumber, setAccountNumber] = useState('')
  const [bank, setBank] = useState('')
  const URL = import.meta.env.VITE_APP_API
  const token = localStorage.getItem("access_token")

  const handleInputPoint = (event) =>{
    setInputPoint(event.target.value)
  }
  const handleAccountHolder = (event) =>{
    setAccoutHolder(event.target.value)
  }
  const handleAccountNumber = (event) =>{
    setAccountNumber(event.target.value)
  }
  const handleBank = (event) =>{
    setBank(event.target.value)
  }

  const handleRefund = () =>{
    const headers = {
      "Authorization" : `Bearer ${token}`
    }
    const data = {
      point : inputPoint,
      accountHolder : accountHolder,
      accountNumber : accountNumber,
      bank : bank,
    }
    axios.put(`${API.USER}/withdraw`,data, {headers})
      .then(function(response){
        dispatch(refund(inputPoint))
        console.log(response)
      })
      .catch(function(error){
        console.log(error)
      })
  }
  return (
    <div className='space-y-4'>
      <div>환급 가능한 포인트</div>
      <div className='h-8 border rounded-lg'>{point}P</div>
      <div>환급할 포인트</div>
      <input type="number" placeholder='환급할 포인트를 입력해주세요.' className='h-8 text-xs w-full' onChange={handleInputPoint}/>
      <div>환급 후 포인트</div>
      <div className='h-8 border rounded-lg'>{Number(point) - Number(inputPoint)}P</div>
      <hr />
      <div>환급 계좌 선택</div>
      <select name="banks" id="bank-select" className='border-2 rounded-lg h-8 w-full' onChange={handleBank}>
        <option value="">--은행선택--</option>
        <option value="dague">대구은행</option>
        <option value="giup">기업은행</option>
        <option value="nonghub">농협</option>
        <option value="gugmin">국민은행</option>
        <option value="shinhan">신한은행</option>
        <option value="wori">우리은행</option>
    </select>
      <input type="text" placeholder="예금주" className='h-8 text-xs w-full' onChange={handleAccountHolder}/>
      <input type="text" placeholder='계좌 번호를 입력해주세요' className='h-8 text-xs w-full' onChange={handleAccountNumber}/>
      <div className='text-xs'>계좌 번호 오기입으로 인한 미환급은 책임지지 않습니다. 계좌번호를 반드시 확인해 주세요.</div>
      <div className='flex justify-between'>
        <button type='button' className='main1-button w-24' onClick={handleRefund}>환급받기</button>
        <button type='button' className='main5-button w-24' onClick={onClickCloseButton}>취소하기</button>
      </div>
    </div>
  )
}
