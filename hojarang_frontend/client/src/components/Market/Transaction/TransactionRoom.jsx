import { useNavigate } from "react-router";
import defaultImage from "@/assets/noimage.png"

export default function TransactionRoom(roomData) {
  const navigate = useNavigate()
  const {id, transaction_image, is_complete, title, price} = roomData;
  const onClick = () =>{
    navigate(`/market/transactiondetail/${id}`)
  }
  return (
    <div >
      <div className="border-2 border-gray1 w-52 p-2 rounded-lg">
        {/* 이미지와 그 위 텍스트 */}
        <div className="flex justify-center">
          {transaction_image ? (
            <div className="w-32 h-32">
              <img src={`https://honjarang-bucket.s3.ap-northeast-2.amazonaws.com/transactionImage/${transaction_image}`} alt="상품 이미지" className="w-full h-full rounded-lg" />
            </div>
          ):(
            <div className="w-32 h-32">
              <img src={defaultImage} alt="기본이미지" />
            </div>
          )}

        </div>
        {/* 상품 제목 */}
        <div className="flex justify-between my-4">
          <div>{title}</div>
          <div>{price.toLocaleString()}원</div>
        </div>
        {/* 참여버튼 */}
        <div className="flex justify-between">
          {is_complete ? (
            <div className="text-main5 font-bold">판매완료</div>
          ):(
            <div className="text-main1 font-bold">판매중</div>
          )}
          <button type="button" className="main1-full-button w-20" onClick={onClick}>채팅하기</button>
        </div>
      </div>
    </div>
  )
}
