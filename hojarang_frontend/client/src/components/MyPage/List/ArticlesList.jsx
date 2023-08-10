import { useState, useEffect } from "react";
import axios from "axios";
import Pagination from "react-js-pagination";

export default function ArticlesList() {
  const URL = import.meta.env.VITE_APP_API
  const token = localStorage.getItem("user_id")
  const headers = {"Authorization" : `Bearer ${token}`}
  const [pageSize, setPageSize] = useState(0)
  const [currentPage, setCurrentPage] = useState(1)
  const [articles, setArticles] = useState([])
  useEffect(() => {
    axios.get(`${URL}/api/v1/users/page-post`,{params:{size:10}, headers})
      .then((res)=>{
        console.log(res)
        setPageSize(res.data)
      })
      .catch((err)=>console.log(err))
  },[])

  useEffect(()=>{
    axios.get(`${URL}/api/v1/users/posts`,{params:{page:1,size:10}, headers})
      .then((res)=>{
        console.log(res)
        setArticles(res.data)
      })
      .catch((err)=>console.log(err))
  },[currentPage])

  const setPage = (error) =>{
    setCurrentPage(error)
  }

  return (
    <div className="p-6 h-full">
      <div>
        {articles.length === 0 ?(
          <div>작성한 글이 없습니다.</div>
        ):(
          <div>
            <div className="flex">
              <div className="w-4/6">제목</div>
              <div className="w-2/6">작성일</div>
            </div>
            <div>
              {articles.map((article) => {
                <div key={article.id} className="flex">
                  <div className="w-4/6">{article.title}</div>
                  <div className="w-2/6">{article.created_at?.slice(0,10)}</div>
                </div>
              })}
            </div>
            <div>
              <Pagination
                activePage={currentPage}
                itemsCountPerPage={12}
                totalItemsCount={12*pageSize}
                pageRangeDisplayed={10}
                prevPageText={"<"}
                nextPageText={">"}
                onChange={setPage}
                />
            </div>
          </div>
        )}
      </div>
    </div>
  );
}
