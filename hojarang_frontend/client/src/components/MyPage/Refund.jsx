import { useSelector } from "react-redux"
import { useState } from "react"

export default function Refund({modalState, setModalState}) {
  const onClickCloseButton = (()=>{
    setModalState(!modalState)
  })
  const point = useSelector((state) => state.userinfo.point)
  const [inputPoint, setInputPoint] = useState(0)
  const handleInputPoint = (event) =>{
    setInputPoint(event.target.value)
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
      <select name="pets" id="pet-select" className='border-2 rounded-lg h-8 w-full'>
        <option value="">--은행선택--</option>
        <option value="dog">대구은행</option>
        <option value="cat">기업은행</option>
        <option value="hamster">농협</option>
        <option value="parrot">국민은행</option>
        <option value="spider">신한은행</option>
        <option value="goldfish">우리은행</option>
    </select>
      <input type="text" placeholder='계좌 번호를 입력해주세요' className='h-8 text-xs w-full'/>
      <div className='text-xs'>계좌 번호 오기입으로 인한 미환급은 책임지지 않습니다. 계좌번호를 반드시 확인해 주세요.</div>
      <div className='flex justify-between'>
        <button type='button' className='main1-button w-24'>환급받기</button>
        <button type='button' className='main5-button w-24' onClick={onClickCloseButton}>취소하기</button>
      </div>
    </div>
  )
}
