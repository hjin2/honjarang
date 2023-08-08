import React, {useEffect, useState} from 'react'
import { useParams } from 'react-router';
import PurchaseDetailProduct from '../../components/Market/PurchaseDetailProduct';
import PurchaseDetailPlace from '../../components/Market/PurchaseDetailPlace';
import axios from 'axios';
import PurchaseApply from '../../components/Market/PurchaseApply';

export default function PurchaseDetail() {
  const [currentTime, setCurrentTime] = useState(new Date());
  const URL = import.meta.env.VITE_APP_API
  const token = localStorage.getItem('access_token')

  useEffect(() => {
    const interval = setInterval(() => {
      setCurrentTime(new Date());
    }, 1000); // 1초마다 업데이트

    return () => {
      clearInterval(interval);
    };
  }, []);

  const id = useParams().id;
  // const[detail, setDetail] = useState([])
  const [detail, setDetail] = useState({
    id: 1, 
    product_name: '젤리젤리1', 
    image: "/src/assets/panda-bear.png", 
    content: '하리보의 성공은 독일 시장에서 그치지 않았습니다. 하리보는 과일젤리와 감초젤리 부문의 세계 시장 리더로서 120개국 이상에서 만나볼 수 있습니다! 하리보는 10개국에 분포된 16개 지사에서 생산되고 있으며, 7,000명 이상의 직원들이 소비자들이 좋아하는 제품을 변함없는 최고의 품질로 즐길 수 있도록 최선을 다하고 있습니다.하리보는 인정받은 것에만 집중하지 않습니다. 꾸준한 제품 개발과 국내외 고품질 브랜드 인수를 통해 제품군을 지속적으로 확장하고 있습니다. 판매 및 생산망을 긴밀하게 구축하여 전 세계 어디서나 제품을 신속하게 받을 수 있습니다.뿐만아니라 다양한 국가에 따라 선호하는 맛을 정확히 살린 특별한 제품 개발로 세계시장의 리더로 더욱 확고히 자리매김 하고 있습니다.',
    deadline:'2023-08-05', 
    current_person_count:'3', 
    target_person_count:'5', 
    price:'12345000',
    delivery_charge: '6000',
    place_name: '어디어디',
    place_latitude: '만남 장소 위도',
    place_longitude: '만남 장소 경도',
    created_at: '생성일시',
    user_id: '작성자 ID',
    nickname:'작성자 닉네임'
  });

  
  useEffect(() => {
    axios.get(`${URL}/api/v1/joint-purchases/${id}`, {headers: {"Authorization" : `Bearer ${token}`}})
    .then((res) => {
      console.log(res.data)
      setDetail(res.data)
    })
    .catch((err) => {
      console.log(err)
    })
  }, [id]);

  // if (!detail) {
  //   return <div>Loading</div>
  // }

  // 상품설명, 만남장소 탭
  const [activeTab, setActiveTab] = useState('purchase_detail_product');
  const handleTabClick = (tab) => {
    setActiveTab(tab);
  };

  // 구매 탭
  const [isAsideOpen, setIsAsideOpen] = useState(false);
  const handleToggleAside = () => {
    setIsAsideOpen(!isAsideOpen);
  }

  // 구매 갯수
  const [quantity, setQuantity] = useState(0);
  const handleIncrement = () => {
    setQuantity(prevQuantity => prevQuantity + 1);
    // setQuantity(prevQuantity => (prevQuantity === '' ? 1 : parseInt(prevQuantity) + 1));
  };
  const handleDecrement = () => {
    setQuantity(prevQuantity => (prevQuantity > 0 ? prevQuantity - 1 : 0));
    // setQuantity(prevQuantity => (prevQuantity === '' ? 0 : Math.max(0, parseInt(prevQuantity) - 1)));
  };
  
  const handleChange = (e) => {
    const inputQuantity = parseInt(e.target.value);
    setQuantity(isNaN(inputQuantity) ? 0 : Math.max(0, inputQuantity));
    // setQuantity(inputQuantity === '' ? '' : Math.max(0, parseInt(inputQuantity)));
  };
  const handleCheck = () =>{

  }
  const deadline = new Date(detail.deadline)
  const timeDiff = deadline - currentTime;
  const days = Math.floor(timeDiff / (1000 * 60 * 60 * 24));
  const hours = Math.floor((timeDiff % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60));
  const minutes = Math.floor((timeDiff % (1000 * 60 * 60)) / (1000 * 60));
  const seconds = Math.floor((timeDiff % (1000 * 60)) / 1000);

  return (
    <div>
      <div className="border rounded-lg max-w-2xl mx-auto mt-10 pb-3 p-5 space-y-5 ">
        <div className="flex justify-between">
          <div className="flex flex-row">
            <div className="font-bold text-3xl flex items-end">{detail.product_name}</div>
            <div className="flex items-end">
              {detail.price}
            </div>
          </div>
          <div>
            <div className="font-semibold text-right">{detail.nickname}</div>
            <div className="text-right ">{detail.created_at}</div>
          </div>
        </div>
        <hr />
        <div className="space-y-2">
          <div className="space-x-2">
            <button onClick={() => handleTabClick('purchase_detail_product')} 
            className={`mr-2 ${activeTab === 'purchase_detail_product' ? 'font-semibold' : 'font-normal'}`} >상품 설명</button>
            <button onClick={() => handleTabClick('purchase_detail_place')}
            className={`${activeTab === 'purchase_detail_place' ? 'font-semibold' : 'font-normal'}`}>만남 장소</button>
          </div>
          <div>
          {activeTab === 'purchase_detail_product' && (
            <PurchaseDetailProduct content={detail.content} image={detail.image} />
            )}
          {activeTab === 'purchase_detail_place' && (
            <PurchaseDetailPlace
              latitude={detail.place_latitude}
              longitude={detail.place_longitude}
            />
            )}
          </div>
        </div>
      </div>
        <div className="border rounded-lg max-w-2xl mx-auto mt-5 mb-10 pb-3 p-5 space-y-5 flex flex-col items-center">
          {timeDiff > 0 ? (
            <div>
              <div>{detail.latitude}</div>
              <div className="text-main5">마감까지 남은 시간: {days}일 {hours}시간 {minutes}분 {seconds}초</div>
              <div className="text-main2">{detail.current_person_count}/{detail.target_person_count}(현재인원 / 목표인원)</div>
            </div>
          ):(
            <div className="text-main5">모집 마감</div>
          )}
          {timeDiff > 0 ? (
            <button onClick={handleToggleAside} className="main1-full-button w-40">공동구매 신청</button>
          ):(
            <button onClick={handleCheck} className='main1-full-button w-40'>수령확인</button>
          )}
          <PurchaseApply 
            isAsideOpen={isAsideOpen}
            handleToggleAside={handleToggleAside}
            quantity={quantity}
            handleIncrement={handleIncrement}
            handleDecrement={handleDecrement}
            handleChange={handleChange}
            detail={detail}
            user_id={detail.user_id}
            price = {detail.price}
            delivery_charge={detail.delivery_charge}
          />
        </div>
    </div>
  )
}
