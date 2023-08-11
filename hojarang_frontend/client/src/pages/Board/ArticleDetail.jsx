import { useState, useEffect, useCallback } from 'react';
import { useParams, Link, useNavigate } from 'react-router-dom';
import axios from 'axios';
import Comment from '../../components/Board/Comment';

export const ArticleDetail = () => {
  const navigate = useNavigate()
  const { id } = useParams();
  const URL = import.meta.env.VITE_APP_API
  const token = localStorage.getItem("access_token")
  const headers = {"Authorization" : `Bearer ${token}`}
  const [detail, setDetail] = useState({})
  const [comments, setComments] = useState([])
  const [isClick, setIsClick] = useState(false)
  const [comment, setComment] = useState('')
  const [user, setUser] = useState({})
  const [isWriter, setIsWriter] = useState(false)
  const handleClick = () =>{
    setIsClick(!isClick)
  }


  const addComment = () =>{
    const data = {
      "content" : comment
    }
    axios.post(`${URL}/api/v1/posts/${id}/comments`,data,{headers})
      .then((res)=>{
        console.log(res)
        fetchComments()
      })
      .catch((err)=>console.log(err))
  }

  const writeComment = (e) =>{
    e.preventDefault()
    if(comment.trim() !== ''){
      console.log(comment)
      addComment()
      setComment('')
    }
  }

  const fetchUser = (userId) =>{
    axios.get(`${URL}/api/v1/users/info`,{params:{id:userId}, headers})
      .then((res)=>{
        console.log(res.data)
        setUser(res.data)
      })
      .catch((err)=>{
        console.log(err)
      })
  }

  useEffect(()=>{
    axios.get(`${URL}/api/v1/posts/${id}`, {headers})
      .then((res) =>{
        setDetail(res.data)
        console.log(res.data)
        if(Number(res.data.user_id) === Number(localStorage.getItem("user_id"))){
          setIsWriter(true)
        }else{
          setIsWriter(false)
        }
        fetchUser(res.data.user_id)
      })
      .catch((err) => {
        console.log(err)
      })
      fetchComments()
  },[id])
  
  const fetchComments = useCallback(() =>{
    axios.get(`${URL}/api/v1/posts/${id}/comments`, {headers})
    .then((res)=>{
      console.log(res.data)
      setComments(res.data)
    })
    .catch((err)=>{
      console.log(err)
    })
  },[id])
  
  const handelArticleDelete = () => {
    axios.delete(`${URL}/api/v1/posts/${id}`,{headers})
      .then((res)=>{
        console.log(res)
        navigate("/board", {replace:true})
      })
      .catch((err)=>console.log(err))
  }
  const editArticle = () => {
    navigate(`/board/articleupdate/${id}`)
  }

  return (
    <div className="border rounded-lg max-w-2xl mx-auto mt-10 p-5">
      {/* 제목 */}
      <div>
        <div className='text-xl font-semibold mb-5'>
          [{detail.category}] {detail.title}
        </div>
        <button>좋아요</button>
      </div>
      <div className="flex justify-between px-2 my-3">
        <div className='flex'>
          <img src={user.profile_image} alt="pofile_image" className="w-10 h-10 rounded-full mr-3"/>
          <div>
            <div className='text-xs font-semibold'>{detail.nickname}</div>
            <div className='text-xs'>{detail.created_at?.slice(0,16)}</div>
          </div>
        </div>
        <div className="flex space-x-2">
          <div className="text-xs mt-3">댓글 개수 : {comments.length}</div>
          <button onClick={handleClick}>
            <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" strokeWidth={1.5} stroke="currentColor" className="w-6 h-6">
              <path strokeLinecap="round" strokeLinejoin="round" d="M12 6.75a.75.75 0 110-1.5.75.75 0 010 1.5zM12 12.75a.75.75 0 110-1.5.75.75 0 010 1.5zM12 18.75a.75.75 0 110-1.5.75.75 0 010 1.5z" />
            </svg>
          </button>
          {isClick&&isWriter ? (
            <div className='absolute border-2 rounded-lg bg-white text-center space-y-2 p-2'>
              <div type="button" className="cursor-pointer" onClick={editArticle}>수정</div>
              <div type="button" className="coursor-pointer" onClick={handelArticleDelete}>삭제</div>
            </div>
          ):null}
        </div>
      </div>
      <hr />
      {/* 본문 내용 */}
      <div className="whitespace-pre-line p-2 my-2">
        <img src={`https://honjarang-bucket.s3.ap-northeast-2.amazonaws.com/postImage/${detail.post_image}`} alt="" />
        <div>
          {detail.content}
        </div>
      </div>
      <hr />
      {/* 댓글 */}
      <form className='flex justify-between mt-5 mb-3' onSubmit={writeComment}>
        <input 
          type="text"
          className="w-9/12 border-none focus:outline-none" 
          placeholder="댓글..."
          value={comment}
          onChange={(e) => setComment(e.target.value)}
        />
        <button type="submit" className="text-gray3">댓글 작성</button>
      </form>
      {comments.length>0 ? (
        <div className="space-y-2 px-2">
          {comments.map((comment)=>(
            <Comment
              key={comment.id}
              comment={comment}
              id = {detail.id}
              fetchComments={fetchComments}
            />
          ))}
        </div>
      ):(
        <div>댓글이 없습니다.</div>
      )}
    </div>
  );
};
export default ArticleDetail;