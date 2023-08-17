import { useState, useEffect } from 'react';
import DeliveryList from '@/components/MyPage/List/DeliveryList';
import axios from 'axios';
import { activetabStyles, tabStyles } from '@/components/MyPage/MypageCss';
import { API } from '@/apis/config';

export default function Delivery({ id, isMe }) {
  const token = localStorage.getItem('access_token');
  const headers = { Authorization: `Bearer ${token}` };
  const [activeTab, setActiveTab] = useState('write-delivery');
  const [joinPageSize, setJoinPageSize] = useState(1);
  const [writePageSize, setWritePageSize] = useState(1);
  const [joinCurrentPage, setJoinCurrentPage] = useState(1);
  const [writeCurrentPage, setWriteCurrentPage] = useState(1);
  const [joinDeliveryData, setJoinDeliveryData] = useState([]);
  const [writeDeliveryData, setWriteDeliveryData] = useState([]);
  const handleTabClick = (tab) => {
    setActiveTab(tab);
  };
  useEffect(() => {
    axios
      .get(`${API.USER}/page-join`, { params: { size: 4 }, headers })
      .then((res) => {
        console.log(res.data);
        setJoinPageSize(res.data);
      })
      .catch((err) => console.log(err));
  }, []);

  useEffect(() => {
    axios
      .get(`${API.USER}/joint-deliveries-participating`, {
        params: { page: joinCurrentPage, size: 4 },
        headers,
      })
      .then((res) => {
        console.log(res);
        setJoinDeliveryData(res.data);
      })
      .catch((err) => console.log(err));
  }, [joinCurrentPage]);

  useEffect(() => {
    axios
      .get(`${API.USER}/page-writing/${id}`, { params: { size: 4 }, headers })
      .then((res) => {
        console.log(res.data);
        setWritePageSize(res.data);
      })
      .catch((err) => {
        console.log(err);
      });
  }, []);

  useEffect(() => {
    axios
      .get(`${API.USER}/joint-deliveries-writer/${id}`, {
        params: { size: 4, page: writeCurrentPage },
        headers,
      })
      .then((res) => {
        console.log(res.data);
        setWriteDeliveryData(res.data);
      })
      .catch((err) => {
        console.log(err);
      });
  }, [writeCurrentPage]);

  return (
    <div className="p-6">
      <div className="space-x-5 mb-5">
        <button
          onClick={() => handleTabClick('write-delivery')}
          className={`${
            activeTab === 'write-delivery' ? 'font-semibold' : 'font-normal'
          }`}
          style={activeTab === 'write-delivery' ? activetabStyles : tabStyles}
        >
          작성 배달
        </button>
        {isMe ? (
          <button
            onClick={() => handleTabClick('join-delivery')}
            className={`${
              activeTab === 'join-delivery' ? 'font-semibold' : 'font-normal'
            }`}
            style={activeTab === 'join-delivery' ? activetabStyles : tabStyles}
          >
            참여 배달
          </button>
        ) : null}
      </div>
      <div>
        {isMe ? (
          <>
            {activeTab === `join-delivery` && (
              <DeliveryList
                pageSize={joinPageSize}
                deliveryData={joinDeliveryData}
                setCurrentPage={setJoinCurrentPage}
                currentPage={joinCurrentPage}
              />
            )}
          </>
        ) : null}
        {activeTab === 'write-delivery' && (
          <DeliveryList
            pageSize={writePageSize}
            deliveryData={writeDeliveryData}
            setCurrentPage={setWriteCurrentPage}
            currentPage={writeCurrentPage}
          />
        )}
      </div>
    </div>
  );
}
