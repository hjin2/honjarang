import Rooms from "@/components/Market/Rooms";
import PurchaseRoom from "@/components/Market/Purchase/PurchaseRoom"
import Pagination from "react-js-pagination";


export default function PurchaseList({pageSize, purchaseData, setCurrentPage, currentPage}) {

  const setPage = (error) =>{
    setCurrentPage(error)
  }

  return(
    <div className="h-full">
      {purchaseData.length > 0 ?(
        <>
        <div className="h-full">
          <Rooms roomsData={purchaseData} component={PurchaseRoom}/>
        </div>
        <div className="flex justify-center">
          <Pagination
            activePage={currentPage}
            itemsCountPerPage={8}
            totalItemsCount={8*pageSize}
            pageRangeDisplayed={10}
            prevPageText={"<"}
            nextPageText={">"}
            onChange={setPage}
            />
        </div>
      </>
      ):(
        <div>참여한 공동구매가 없습니다.</div>
      )}
    </div>
  )
}
