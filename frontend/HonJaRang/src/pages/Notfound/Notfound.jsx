import NotfoundImage from "@/assets/404.jpg"
import { useNavigate } from "react-router-dom";

export default function Notfound() {
  const navigate = useNavigate()
  const onClick = () =>{
    navigate("/")
  }
  return (
  <div className="flex flex-col items-center mt-10">
    <img src={NotfoundImage} className="w-4/12 mx-auto" />
    <div className="mt-5 text-3xl font-semibold">404 ERROR</div>
    <div>존재하지 않은 주소를 입력하셨거나,</div>
    <div>요청하신 페이지의 주소가 변경, 삭제되어 찾을 수 없습니다.</div>
    <button className="text-main1 mt-5 font-bold" onClick={onClick}>홈으로</button>
    <div></div>
  </div>
  );
}
