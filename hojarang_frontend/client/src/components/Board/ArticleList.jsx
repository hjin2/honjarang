import Article from './Article';
import { useNavigate } from 'react-router-dom';
import { Fragment, useEffect, useState } from 'react';
import axios from 'axios';
import Pagination from 'react-js-pagination';

export default function AricleList() {
  const URL = import.meta.env.VITE_APP_API
  const token = localStorage.getItem("access_token")
  const headers = {"Authorization" : `Bearer ${token}`}
  const [articles, setArticles] = useState([])
  const [currentPage, setCurrentPage] = useState(1)
  const [pageSize, setPageSize] = useState(0)
  useEffect(()=>{
    axios.get(`${URL}/api/v1/posts/page`,{params:{size:15}, headers})
      .then((res)=>{
        console.log(res.data)
        setPageSize(res.data)
      })
      .catch((err)=>{
        console.log(err)
      })
  },[])
	useEffect(()=>{
    axios.get(`${URL}/api/v1/posts`,{params:{page:currentPage}, headers})
      .then((res)=>{
        console.log(res.data)
        setArticles(res.data)
      })
      .catch((err)=>console.log(err))
  },[currentPage])
  const navigate = useNavigate()
  const handleButton = () =>{
    navigate("/board/articlecreate")
  }
  const setPage = (error)=>{
    setCurrentPage(error)
  }
  return (
    <div>
    <div className="w-4/5 mx-auto">
      <div className="flex justify-between font-bold">
        <div className="w-1/6">글번호</div>
        <div className="w-2/6 text-center">작성자</div>
        <div className="w-3/6 text-center">제목</div>
        <div className="w-2/6">작성일자</div>
      </div>
      <div className="space-y-2">
        {articles?.map((article)=>(
          <div 
            key={article.id} 
            onClick={()=>{navigate(`/board/article/${article.id}`)}}
            className="cursor-pointer"
          >
            <Article
              article={article}
              />
          </div>
        ))}
      </div>
      <footer>
        <Pagination
          activePage={currentPage}
          itemsCountPerPage={15}
          totalItemsCount={15*pageSize}
          pageRangeDisplayed={10}
          prevPageText={"<"}
          nextPageText={">"}
          onChange={setPage}
        />
      </footer>
    </div>
    <button className="main1-button w-24" onClick={handleButton}>작성하기</button>
    </div>
  );
}
