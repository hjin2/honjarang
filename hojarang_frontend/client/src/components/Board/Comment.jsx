import axios from "axios"
import { useEffect, useState } from "react"

export default function Comment({comment, id, updateComments}) {
  const token = localStorage.getItem("access_token")
  const URL = import.meta.env.VITE_APP_API
  const headers = {"Authorization" : `Bearer ${token}`}
  const [user, setUser] = useState({})
  const LoginId = localStorage.getItem("user_id")
  const [isWriter, setIsWriter] = useState(false)
  const deleteComment = () =>{
    axios.delete(`${URL}/api/v1/posts/${id}/comments/${comment.id}`,{headers})
      .then((res) =>{
        updateComments(comment.id)
      })
      .catch((err)=>{console.log(err)})
  }
  useEffect(()=>{
    if(comment.userId == LoginId){
      setIsWriter(true)
    }else{
      setIsWriter(false)
    }
  },[])
  return (
    <div className="flex space-x-3">
      <div>
        <div className='text-sm font-semibold'>{comment.nickname}</div>
        <div className='text-sm'>{comment.content}</div>
        <div className="flex space-x-5">
          <div className='text-xs text-gray3'>{comment.createdAt?.slice(0,10)}</div>
          {isWriter ?(
              <button className="text-xs text-main5" onClick={deleteComment}>댓글삭제</button>
            ):(null)
          }
        </div>
      </div>
    </div>
  )
}
