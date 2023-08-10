import { useNavigate } from "react-router";

export default function TransactionRoom(roomData) {
  const navigate = useNavigate()
  const {id, transaction_image, is_complete, title, price} = roomData;
  const onClick = () =>{
    navigate(`/market/transactiondetail/${id}`)
  }
  return (
    <div >
      <div className="border-2 border-gray1 w-52 h-60 p-2 rounded-lg">
        {/* 이미지와 그 위 텍스트 */}
        <div className="flex justify-center relative">
          <div className="w-32 h-32">
            <img src={`https://honjarang-bucket.s3.ap-northeast-2.amazonaws.com/transactionImage/${transaction_image}`} alt="상품 이미지" className="w-full h-full rounded-lg" />
          </div>
          <div className="absolute inset-0 flex items-end justify-end">
            <p className="text-sm font-bold">{is_complete}</p>
          </div>
        </div>
        {/* 상품 제목 */}
        <div className="flex justify-between my-4">
          <div>{title}</div>
          <div>{price} </div>
        </div>
        {/* 참여버튼 */}
        <div className="flex justify-end">
        <button type="button" className="main1-full-button w-20" onClick={onClick}>채팅하기</button>
        </div>
      </div>
    </div>
  )
}
