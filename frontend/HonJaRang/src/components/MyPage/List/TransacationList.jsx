import Rooms from '@/components/Market/Rooms';
import Pagination from 'react-js-pagination';
import TransactionRoom from '@/components/Market/Transaction/TransactionRoom';

export default function TransactionList({
  transactionData,
  currentPage,
  pageSize,
  setCurrentPage,
}) {
  const setPage = (error) => {
    setCurrentPage(error);
  };
  return (
    <div className="h-full">
      {transactionData.length > 0 ? (
        <>
          <div className="h-full">
            <Rooms roomsData={transactionData} component={TransactionRoom} />
          </div>
          <div className="mt-10 flex justify-center">
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
        <div>중고거래가 없습니다.</div>
      )}
    </div>
  );
}
