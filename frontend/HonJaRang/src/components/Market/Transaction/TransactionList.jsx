import { useEffect, useState } from 'react';
import Rooms from '../Rooms';
import Pagination from 'react-js-pagination';
import axios from 'axios';
import TransactionRoom from './TransactionRoom';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faMagnifyingGlass } from '@fortawesome/free-solid-svg-icons';
import { useCallback } from 'react';
import { API } from '@/apis/config';

export default function TransactionList() {
  const token = localStorage.getItem('access_token');
  const headers = { Authorization: `Bearer ${token}` };
  const [transactionData, setTransactionData] = useState([]);
  const [pageSize, setPageSize] = useState(1);
  const [currentPage, setCurrentPage] = useState(1);
  const [keyword, setKeyword] = useState('');

  const fetchTransacationData = useCallback(() => {
    axios
      .get(`${API.SECONDHAND}`, {
        params: { page: currentPage, size: 12, keyword: keyword },
        headers,
      })
      .then((res) => {
        console.log(res.data);
        setTransactionData(res.data);
      })
      .catch((err) => console.log(err));
  }, [currentPage, keyword]);
  const fetchPageSize = useCallback(() => {
    axios
      .get(`${API.SECONDHAND}/page`, {
        params: { size: 12, keyword: keyword },
        headers,
      })
      .then((res) => {
        console.log(res.data);
        setPageSize(res.data);
      })
      .catch((err) => console.log(err));
  }, [keyword]);

  useEffect(() => {
    fetchPageSize();
  }, []);

  useEffect(() => {
    fetchTransacationData();
  }, [currentPage]);

  useEffect(() => {
    if (keyword) {
      fetchTransacationData();
    }
  }, [keyword]);

  const setPage = useCallback((error) => {
    setCurrentPage(error);
  }, []);

  const search = (e) => {
    e.preventDefault();
    fetchTransacationData();
  };

  const handleKeyword = (e) => {
    setKeyword(e.target.value);
  };

  return (
    <div className="h-full">
      <div className="flex justify-end mb-5">
        <form action="" className="space-x-2" onSubmit={search}>
          <input
            className="border border-gray2 focus:outline-main2 h-10 p-2"
            type="text"
            placeholder="검색어"
            onChange={handleKeyword}
          />
          <button>
            <FontAwesomeIcon
              icon={faMagnifyingGlass}
              style={{ color: '#008b28' }}
            />
          </button>
        </form>
      </div>
      {transactionData.length > 0 ? (
        <>
          <div className="flex justify-center">
            <Rooms roomsData={transactionData} component={TransactionRoom} />
          </div>
          <div className="flex justify-center" style={{ height: '10%' }}>
            <Pagination
              activePage={currentPage}
              itemsCountPerPage={12}
              totalItemsCount={12 * pageSize}
              pageRangeDisplayed={10}
              prevPageText={'<'}
              nextPageText={'>'}
              onChange={setPage}
            />
          </div>
        </>
      ) : (
        <div className="text-center mt-10 font-bold text-xl">
          중고거래 목록이 없습니다
        </div>
      )}
    </div>
  );
}
