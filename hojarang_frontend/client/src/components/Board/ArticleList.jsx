import Article from '@/components/Board/Article';
import { useNavigate } from 'react-router-dom';
import React, { Fragment, useEffect, useState } from 'react';
import axios from 'axios';
import Pagination from 'react-js-pagination';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faMagnifyingGlass } from '@fortawesome/free-solid-svg-icons';
import { useCallback } from 'react';
import { activetabStyles } from '@/components/MyPage/MypageCss';


export default function AricleList() {
  const URL = import.meta.env.VITE_APP_API
  const token = localStorage.getItem("access_token")
  const headers = {"Authorization" : `Bearer ${token}`}
  const [articles, setArticles] = useState([])
  const [currentPage, setCurrentPage] = useState(1)
  const [pageSize, setPageSize] = useState(1)
  const [keyword, setKeyworkd] = useState('')
  const handleKeyword = (e) =>{
    setKeyworkd(e.target.value)

  }
  const fetchArticles = useCallback(() =>{
    axios.get(`${URL}/api/v1/posts`,{params:{page:currentPage, keyword:keyword}, headers})
    .then((res)=>{
      console.log(res.data)
      setArticles(res.data)
    })
    .catch((err)=>console.log(err))
  },[currentPage, keyword])

  const search = (e) =>{
    e.preventDefault()
    if(keyword){
      fetchArticles()
    }
  }
  useEffect(()=>{
    axios.get(`${URL}/api/v1/posts/page`,{params:{size:15, keyword:keyword}, headers})
      .then((res)=>{
        setPageSize(res.data)
      })
      .catch((err)=>{
        console.log(err)
      })
    },[keyword])

  useEffect(()=>{
      fetchArticles()
    },[currentPage])
  
  const navigate = useNavigate()
  const handleButton = () =>{
    navigate("/board/articlecreate")
  }
  const setPage = (error)=>{
    setCurrentPage(error)
  }
  const MemorizedArticle = React.memo(Article)

  return (
    <div>
      <div className="w-3/5 mx-auto">
        <div className='text-lg mb-5 font-bold w-28' style={activetabStyles}>자유게시판</div>
        <div className="flex mb-5">
          <div className="w-3/6 font-bold text-center">제목</div>
          <div className='w-1/6 font-bold'>작성자</div>
          <div className='w-1/6 font-bold'>작성일</div>
          <div className='w-1/6 font-bold'>조회수</div>
        </div>
        <hr />
        <div className="space-y-4 mt-2">
          {articles?.map((article)=>(
            <div 
              key={article.id} 
              onClick={()=>{navigate(`/board/article/${article.id}`)}}
              className="cursor-pointer"
            >
              <MemorizedArticle
                article={article}
                />
              <hr />
            </div>
          ))}
        </div>
        <footer>
        <div className='flex justify-between mt-5'>
          <form action="" className="space-x-2" onSubmit={search}>
            <input type="text" placeholder="검색어" onChange={handleKeyword}/>
            <button>
              <FontAwesomeIcon icon={faMagnifyingGlass} style={{color: "#008b28",}} />
            </button>
          </form>
          <button className="main1-button w-24" onClick={handleButton}>작성하기</button>
        </div>
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
    </div>
  );
}
