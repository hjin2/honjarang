import { useState, useEffect } from 'react';
import PurchaseList from '@/components/MyPage/List/PurchaseList';
import axios from 'axios';
import { activetabStyles, tabStyles } from '@/components/MyPage/MypageCss';
import { API } from '@/apis/config';

export default function Purchase({ id, isMe }) {
  const token = localStorage.getItem('access_token');
  const headers = { Authorization: `Bearer ${token}` };
  const [activeTab, setActiveTab] = useState('write-purchase');
  const [joinPageSize, setJoinPageSize] = useState(1);
  const [writePageSize, setWritePageSize] = useState(1);
  const [joinCurrentPage, setJoinCurrentPage] = useState(1);
  const [writeCurrentPage, setWriteCurrentPage] = useState(1);
  const [joinPurchaseData, setJoinPurchaseData] = useState([]);
  const [writePurchaseData, setWritePurchaseData] = useState([]);
  const handleTabClick = (tab) => {
    setActiveTab(tab);
  };
  useEffect(() => {
    axios
      .get(`${API.USER}/page-joined-purchase`, { params: { size: 4 }, headers })
      .then((res) => {
        console.log(res.data);
        setJoinPageSize(res.data);
      })
      .catch((err) => console.log(err));
  }, []);

  useEffect(() => {
    axios
      .get(`${API.USER}/jointpurchase-participating`, {
        params: { page: joinCurrentPage, size: 4 },
        headers,
      })
      .then((res) => {
        console.log(res);
        setJoinPurchaseData(res.data);
      })
      .catch((err) => console.log(err));
  }, [joinCurrentPage]);

  useEffect(() => {
    axios
      .get(`${API.USER}/page-joint-purchase/${id}`, {
        params: { size: 4 },
        headers,
      })
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
      .get(`${API.USER}/jointpurchase-writer/${id}`, {
        params: { size: 4, page: writeCurrentPage },
        headers,
      })
      .then((res) => {
        console.log(res.data);
        setWritePurchaseData(res.data);
      })
      .catch((err) => {
        console.log(err);
      });
  }, [writeCurrentPage]);

  return (
    <div className="p-6">
      <div className="space-x-5 mb-5">
        <button
          onClick={() => handleTabClick('write-purchase')}
          className={`${
            activeTab === 'write-purchase' ? 'font-semibold' : 'font-normal'
          }`}
          style={activeTab === 'write-purchase' ? activetabStyles : tabStyles}
        >
          작성 공구
        </button>
        {isMe ? (
          <button
            onClick={() => handleTabClick('join-purchase')}
            className={`${
              activeTab === 'join-purchase' ? 'font-semibold' : 'font-normal'
            }`}
            style={activeTab === 'join-purchase' ? activetabStyles : tabStyles}
          >
            참여 공구
          </button>
        ) : null}
      </div>
      <div>
        {activeTab === `join-purchase` && (
          <PurchaseList
            pageSize={joinPageSize}
            purchaseData={joinPurchaseData}
            setCurrentPage={setJoinCurrentPage}
            currentPage={joinCurrentPage}
          />
        )}
        {activeTab === 'write-purchase' && (
          <PurchaseList
            pageSize={writePageSize}
            purchaseData={writePurchaseData}
            setCurrentPage={setWriteCurrentPage}
            currentPage={writeCurrentPage}
          />
        )}
      </div>
    </div>
  );
}
