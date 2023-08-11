import Rooms from "@/components/Market/Rooms";
import DeliveryRoom from "@/components/Market/DeliveryRoom"
import Pagination from "react-js-pagination";


export default function DeliveryList({pageSize, deliveryData, setCurrentPage, currentPage}) {

  const setPage = (error) =>{
    setCurrentPage(error)
  }

  return(
    <div className="h-full p-6">
      {deliveryData.length > 0 ?(
        <>
        <div className="h-full">
          <Rooms roomsData={deliveryData} component={DeliveryRoom}/>
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
