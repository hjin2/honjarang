import SideTab from '../../components/Common/SideTab'
import Content from '../../components/Common/Content';
import DeliveryRoom from '../../components/Market/DeliveryRoom';
import PurchaseRoom from '../../components/Market/PurchaseRoom';
import TransactionRoom from '../../components/Market/TransactionRoom';
import PurchaseList from '../../components/Market/PurchaseList';
import { useEffect, useState } from 'react';

import Rooms from '../../components/Market/Rooms';
import { Link } from 'react-router-dom';
import axios from 'axios';

export default function Market() {
  const [activeTabIndex, setActiveTabIndex] = useState(0)
  const [purchaseData, setPurchaseData] = useState([]);
  const URL = import.meta.env.VITE_APP_API
  const token = localStorage.getItem("access_token")
  // const [deliveryData, setDeliveryData] = useState([]);
  // const [transactionData, setTransactionData] = useState([]);

  // const purchaseData =[
  //   {id:'1', product_name: '젤리젤리1', image: "/src/assets/panda-bear.png", deadline:'2023-08-05', current_person_count:'3', target_person_count:'5', price:'12345000'},
  //   {id:'2', product_name: 'ㅁㅁ', image: "/src/assets/panda-bear.png", deadline:'마감기한', current_person_count:'현재 인원', target_person_count:'목표인원', price:'4000'},
  //   {id:'3', product_name: '상ㅇㄹ', image: "/src/assets/panda-bear.png", deadline:'마감기한', current_person_count:'현재 인원', target_person_count:'목표인원', price:'12333'},
  //   {id:'4', product_name: '상ㅇㄹㄴㅁ', image: "/src/assets/panda-bear.png", deadline:'마감기한', current_person_count:'현재 인원', target_person_count:'목표인원', price:'1222'},
  //   {id:'5', product_name: 'ㄷㄷ', image: "/src/assets/panda-bear.png", deadline:'마감기한', current_person_count:'현재 인원', target_person_count:'목표인원', price:'상품가격'},
  //   {id:'6', product_name: '빈츠', image: "/src/assets/panda-bear.png", deadline:'마감기한', current_person_count:'현재 인원', target_person_count:'목표인원', price:'상품가격'},
  //   {id:'7', product_name: '상품이름', image: "/src/assets/panda-bear.png", deadline:'마감기한', current_person_count:'현재 인원', target_person_count:'목표인원', price:'상품가격'},
  //   {id:'8', product_name: '김재욱바보', image: "/src/assets/panda-bear.png", deadline:'2088-07-18', current_person_count:'1', target_person_count:'2', price:'718000'},
  //   {id:'9', product_name: '상품이름', image: "/src/assets/panda-bear.png", deadline:'마감기한', current_person_count:'현재 인원', target_person_count:'목표인원', price:'상품가격'},
  //   {id:'10', product_name: '상품이름', image: "/src/assets/panda-bear.png", deadline:'마감기한', current_person_count:'현재 인원', target_person_count:'목표인원', price:'상품가격'},
  //   {id:'11', product_name: '상품이름', image: "/src/assets/panda-bear.png", deadline:'마감기한', current_person_count:'현재 인원', target_person_count:'목표인원', price:'상품가격'}, 
  //   {id:'12', product_name: '상품이름', image: "/src/assets/panda-bear.png", deadline:'마감기한', current_person_count:'현재 인원', target_person_count:'목표인원', price:'상품가격'}, 
  //   {id:'13', product_name: '상품이름', image: "/src/assets/panda-bear.png", deadline:'마감기한', current_person_count:'현재 인원', target_person_count:'목표인원', price:'상품가격'}, 
  //   {id:'14', product_name: '상품이름', image: "/src/assets/panda-bear.png", deadline:'마감기한', current_person_count:'현재 인원', target_person_count:'목표인원', price:'상품가격'}, 
  //   {id:'15', product_name: '상품이름', image: "/src/assets/panda-bear.png", deadline:'마감기한', current_person_count:'현재 인원', target_person_count:'목표인원', price:'상품가격'}, 
  // ]
  

  const deliveryData = [
    {id:'1', current_total_price:'현재금액', target_min_price:'최소주문금액', store_id:'1', store_name:'배달가게명', store_image:"/src/assets/panda-bear.png", user_id:'1', nickname:'ischar'},
    {id:'2', current_total_price:'1500', target_min_price:'30000', store_id:'3', store_name:'도미노피자', store_image:"/src/assets/panda-bear.png", user_id:'3', nickname:'ischar'},
    {id:'3', current_total_price:'330000', target_min_price:'330001', store_id:'1', store_name:'가게명', store_image:"/src/assets/panda-bear.png", user_id:'12', nickname:'ischar'},
    {id:'4', current_total_price:'현재금액', target_min_price:'최소주문금액', store_id:'1', store_name:'가게명', store_image:"/src/assets/panda-bear.png", user_id:'3', nickname:'dong'},
    {id:'5', current_total_price:'530000', target_min_price:'53000000', store_id:'1', store_name:'동혁카페', store_image:"/src/assets/panda-bear.png", user_id:'1', nickname:'ischar'},
    {id:'6', current_total_price:'현재금액', target_min_price:'최소주문금액', store_id:'1', store_name:'가게명', store_image:"/src/assets/panda-bear.png", user_id:'1', nickname:'ischar'},
    {id:'7', current_total_price:'현재금액', target_min_price:'최소주문금액', store_id:'1', store_name:'가게명', store_image:"/src/assets/panda-bear.png", user_id:'1', nickname:'ischar'},
  ]

  const transactionData = [
    {id:'1', file_url: "/src/assets/panda-bear.png", is_complete:'True', title:'게시글 제목', price:'15000', user_id:'1', buyer_id:'2', created_at:'2023-08-06'},
    {id:'2', file_url: "/src/assets/panda-bear.png", is_complete:'False', title:'이효진 천재', price:'126000', user_id:'1', buyer_id:'2', created_at:'2023-12-06'},
    {id:'3', file_url: "/src/assets/panda-bear.png", is_complete:'True', title:'게시글 제목', price:'15000', user_id:'1', buyer_id:'2', created_at:'2023-08-06'},
    {id:'4', file_url: "/src/assets/panda-bear.png", is_complete:'True', title:'게시글 제목', price:'15000', user_id:'1', buyer_id:'2', created_at:'2023-08-06'},
    {id:'5', file_url: "/src/assets/panda-bear.png", is_complete:'True', title:'게시글 제목', price:'15000', user_id:'1', buyer_id:'2', created_at:'2023-08-06'},
    {id:'6', file_url: "/src/assets/panda-bear.png", is_complete:'True', title:'게시글 제목', price:'15000', user_id:'1', buyer_id:'2', created_at:'2023-08-06'},
    {id:'7', file_url: "/src/assets/panda-bear.png", is_complete:'True', title:'게시글 제목', price:'15000', user_id:'1', buyer_id:'2', created_at:'2023-08-06'},
  ]

  // useEffect(() => {
  //   axios.get(`${URL}/api/v1/joint-purchases`,{params : {page : page, size : size},headers:{"Authorization" : `Bearer ${token}`}})
  //   .then((res) => {
  //     setPurchaseData(res.data)
  //   })
  //   .catch((err) => {
  //     console.log(err)
  //   })
  // })

  //   axios.get('http://honjarang.kro.kr:30000/api/v1/joint-deliveries')
  //   .then((res) => {
  //     setDeliveryData(res.data)
  //   })
  //   .catch((err) => {
  //     console.log(err)
  //   })    
    
  //   // 중고거래 url 고치기
  //   axios.get('http://honjarang.kro.kr:30000/api/v1/transactions')
  //   .then((res) => {
  //     setTransactionData(res.data)
  //   })
  //   .catch((err) => {
  //     console.log(err)
  //   })
    
  // }, []);

  const tabs = [
    {
      title: '공동구매',
      content: <PurchaseList/>,
      recruit: <Link to="/market/purchase/create">모집하기</Link>
    },
    {
      title: '공동배달',
      content: <Rooms roomsData={deliveryData} component={DeliveryRoom}/>,
      recruit: <Link to="/market/delivery/create">모집하기</Link>
    },
    {
      title: '중고거래',
      content:  <Rooms roomsData={transactionData} component={TransactionRoom}/>,
      recruit: <Link to="/market/transaction/create">모집하기</Link>
    },
  ]


  return (
    <div className="flex space-x-14 mx-auto">
      <div className="basis-1/6 text-center">
        <SideTab 
          tabs = {tabs}
          activeTabIndex = {activeTabIndex}
          setActiveTabIndex = {setActiveTabIndex}
          />
        <button type = "button" className="main3-full-button w-28" >
          {tabs[activeTabIndex].recruit}
        </button>
      </div>
      <div className="basis-5/6">
        <Content
          tabs = {tabs}
          activeTabIndex = {activeTabIndex}
          setActiveTabIndex = {setActiveTabIndex}
          />
      </div>
    </div>
  );
}
