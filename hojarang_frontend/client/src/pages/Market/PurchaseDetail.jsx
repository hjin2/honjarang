import {useEffect, useState} from 'react'
import { useParams } from 'react-router';
import PurchaseDetailProduct from '@/components/Market/Purchase/PurchaseDetailProduct';
import PurchaseDetailPlace from '@/components/Market/Purchase/PurchaseDetailPlace';
import axios from 'axios';
import PurchaseApply from '@/components/Market/Purchase/PurchaseApply';
import { useNavigate } from 'react-router-dom'
import Modal from '@/components/Common/Modal';
import PurchaserList from '@/components/Market/Purchase/PurchaserList';

export default function PurchaseDetail() {
  const navigate = useNavigate()
  const [currentTime, setCurrentTime] = useState(new Date());
  const URL = import.meta.env.VITE_APP_API
  const token = localStorage.getItem('access_token')
  const [point, setPoint] = useState(0)
  useEffect(() => {
    const interval = setInterval(() => {
      setCurrentTime(new Date());
    }, 1000); // 1초마다 업데이트

    return () => {
      clearInterval(interval);
    };
  }, []);
  const [modalState, setModalState] = useState(false)
  const id = useParams().id;
  // const[detail, setDetail] = useState([])
  const [detail, setDetail] = useState({});
  const [isWriter, setIsWriter] = useState(false)
  const [isPurchase, setIsPurchase] = useState(false)
  const loginId = localStorage.getItem("user_id")
  const headers = {"Authorization" : `Bearer ${token}`}
  const [purchasers, setPurchasers] = useState([]) 
  useEffect(() => {
    axios.get(`${URL}/api/v1/joint-purchases/${id}`, {headers})
    .then((res) => {
      console.log(res.data)
      setDetail(res.data)
      fetchUserInfo(loginId)
      if(Number(loginId) === Number(res.data.user_id)){
        setIsWriter(true)
      }else{
        setIsWriter(false)
      }
    })
    .catch((err) => {
      console.log(err)
    })
  axios.get(`${URL}/api/v1/joint-purchases/${id}/applicants`, {headers})
    .then((res) => {
      // console.log(res.data)
      if(res.data){
        setPurchasers(res.data)
        console.log(purchasers)
        for(let i=0;i<res.data.length;i++){
          if(Number(res.data[i].user_id) === Number(loginId)){
            setIsPurchase(true)
            break
          }
        }
      }else{
        setIsPurchase(false)
      }

    })
    .catch((err) => console.log(err))
  }, [id, isPurchase]);

  const fetchUserInfo = (userId)=>{
    axios.get(`${URL}/api/v1/users/info`,{
      params:{id:userId},
      headers
    })
    .then((res) => {
      console.log(res.data)
      setPoint(res.data.point)
    })
    .catch((err)=>{
      console.log(err)
    })
  }

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

  const deletePurchase = () =>{
    axios.delete(`${URL}/api/v1/joint-purchases/${id}`,{headers})
      .then((res)=>{
        console.log(res)
        navigate('/market')
      })
      .catch((err)=>{
        console.log(err)
      })
  }
  const CancelPurchase = () =>{
    axios.delete(`${URL}/api/v1/joint-purchases/${id}/applicants`, {headers})
      .then((res) => {
        console.log(res)
        setIsPurchase(false)
      })
      .catch((err)=>{
        console.log(err)
      })
  }

  const handleCheck = () =>{
    console.log(id)
    axios.put(`${URL}/api/v1/joint-purchases/${id}/receive`,{headers})
      .then((res) =>{
        console.log(res)
        window.alert("수령하셨습니다")
      })  
      .catch((err) => console.log(err))
  }

  const deadline = new Date(detail.deadline)
  const timeDiff = deadline - currentTime;
  const days = Math.floor(timeDiff / (1000 * 60 * 60 * 24));
  const hours = Math.floor((timeDiff % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60));
  const minutes = Math.floor((timeDiff % (1000 * 60 * 60)) / (1000 * 60));
  const seconds = Math.floor((timeDiff % (1000 * 60)) / 1000);

  return (
    <div className="w-6/12 mx-auto mt-5">
      <div className="border rounded-lg p-8 space-y-5 ">
        <div className="flex justify-between">
          <div className="flex flex-row space-x-4">
            <div className="font-bold text-3xl items-end">{detail.product_name}</div>
            <div className="text-main5 mt-3">{detail.price?.toLocaleString()}원</div>
          </div>
          <div>
            <div className="font-semibold text-right">{detail.nickname}</div>
            <div className="text-right ">{detail.created_at?.slice(0,10)}</div>
          </div>
        </div>
        <hr />
        <div className="space-y-2">
          <div className='flex justify-between'>
            <div className="space-x-2">
              <button onClick={() => handleTabClick('purchase_detail_product')} 
              className={`mr-2 ${activeTab === 'purchase_detail_product' ? 'font-semibold' : 'font-normal'}`} >상품 설명</button>
              <button onClick={() => handleTabClick('purchase_detail_place')}
              className={`${activeTab === 'purchase_detail_place' ? 'font-semibold' : 'font-normal'}`}>만남 장소</button>
            </div>
            <button onClick={(()=>{setModalState(!modalState)})}>참여자 목록</button>
            {modalState && (
              <Modal modalState={modalState} setModalState={setModalState}>
                <PurchaserList 
                  modalState={modalState} 
                  setModalState={setModalState}
                  purchasers = {purchasers}
                />
              </Modal>
            )}

          </div>
          <div>
          {activeTab === 'purchase_detail_product' && (
            <PurchaseDetailProduct content={detail.content} image={detail.image} />
            )}
          {activeTab === 'purchase_detail_place' && (
            <PurchaseDetailPlace
              latitude={detail.place_latitude}
              longitude={detail.place_longitude}
              placeName = {detail.place_name}
            />
            )}
          </div>
        </div>
      </div>
        <div className="border rounded-lg my-3 p-5 mx-auto items-center text-center">
          {timeDiff > 0 ? (
            <div>
              {(detail.current_person_count !== detail.target_person_count) ? (
                <div className='space-y-3 mb-3'>
                  <div className="text-main5">마감까지 남은 시간 : {days}일 {hours}시간 {minutes}분 {seconds}초</div>
                  <div className="text-main2">목표까지 {detail.target_person_count - detail.current_person_count}명</div>
                </div>
              ):(
                <div className="text-main5">모집 마감</div>
              )}
              {isPurchase ? (
                <button onClick={CancelPurchase} className="main5-full-button w-40">공동구매 취소</button>
                ):(
                  <button onClick={handleToggleAside} className="main1-full-button w-40">공동구매 신청</button>
                )
              }
            </div>
          ):
          (
            <div className='space-y-5'>
              <div className="text-main5">모집 마감</div>
              {isWriter ? (null):(
                <button onClick={handleCheck} className='main1-full-button w-40'>수령확인</button>
              )}
            </div>
          )}
          {isWriter&&timeDiff>0 ? (
            <button className="main5-full-button w-40 mt-3" onClick={deletePurchase}>모집 취소</button>
          ):null}
          <PurchaseApply 
            isAsideOpen={isAsideOpen}
            handleToggleAside={handleToggleAside}
            point = {point}
            setPoint = {setPoint}
            price = {detail.price}
            deliveryCharge={Math.floor(detail.delivery_charge/detail.target_person_count/10)*10}
            purchaseId = {id}
            setIsPurchase = {setIsPurchase}
          />
        </div>
    </div>
  )
}
