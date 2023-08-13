import { useState, useEffect } from "react";
import axios from "axios";
import Pagination from "react-js-pagination";
import Article from "@/components/MyPage/List/Article";
import { useNavigate } from "react-router-dom";
import { activetabStyles } from "@/components/MyPage/MypageCss"

export default function ArticleList() {
  const navigate = useNavigate()
  const URL = import.meta.env.VITE_APP_API
  const token = localStorage.getItem("access_token")
  const headers = {"Authorization" : `Bearer ${token}`}
  const [pageSize, setPageSize] = useState(1)
  const [currentPage, setCurrentPage] = useState(1)
  const [articles, setArticles] = useState([])
  useEffect(() => {
    axios.get(`${URL}/api/v1/users/page-post`,{params:{size:10}, headers})
      .then((res)=>{
        console.log(res.data)
        setPageSize(res.data)
        setCurrentPage(1)
      })
      .catch((err)=>console.log(err))
  },[])

  useEffect(()=>{
    axios.get(`${URL}/api/v1/users/posts`,{params:{size:10, page:currentPage},headers})
      .then((res)=>{
        console.log(res.data)
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
          <>
            <div className="w-full">
              <div className="mb-5 w-24" style={activetabStyles}>작성한 글</div>
              {articles?.map((article)=>(
                <div 
                  key={article.id} 
                  onClick={()=>{navigate(`/board/article/${article.id}`)}}
                  className="cursor-pointer"
                >
                  <Article
                    article={article}
                    />
                  <hr />
                </div>
              ))}
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
          </>
        )}
      </div>
    </div>
  );
}
