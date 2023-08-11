import { useState, useRef, useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import axios from 'axios';


export default function ArticleUpdate() {
  const { id } = useParams()
  const URL = import.meta.env.VITE_APP_API
  const token = localStorage.getItem("access_token")
  const headers = {
    "Authorization" : `Bearer ${token}`,
    "Content-Type" : "multipart/formed-data"
  }
  const navigate = useNavigate();
  const [category, setCategory] = useState("")
  const [title, setTitle] = useState("")
  const [content, setContent] = useState("")
  const fileInput = useRef(null)
  const [image, setImage] = useState()

  useEffect(()=>{
    axios.get(`${URL}/api/v1/posts/${id}`, {headers})
      .then((res) =>{
        console.log(res.data)
        setTitle(res.data.title)
        setCategory(res.data.category)
        setContent(res.data.content)
        setImage(res.data.image)
      })
      .catch((err) => {
        console.log(err)
      })
  },[])

  const handleImage = (e) =>{
    if (e.target.files[0]){
      setImage(e.target.files[0])
    }
  }
  const handleCategory = (e) =>{
    setCategory(e.target.value)
  }
  const handleTitle = (e) =>{
    setTitle(e.target.value)
  }
  const handleContent = (e) =>{
    setContent(e.target.value)
  }

  const editArticle = () => {
    const formData = new FormData()
    formData.append("post_image", image)
    formData.append("title", title)
    formData.append("content", content)
    formData.append("category", category)
    formData.append("isNotice", "False")
    console.log
    axios.put(`${URL}/api/v1/posts/${id}`,formData,{headers})
      .then((res) => {
        console.log(res)
        navigate(`/board/article/${id}`);
      })
      .catch((err)=>{
        console.log(err)
      })
  };
  
  

  return (
    <div className="border rounded-lg max-w-2xl mx-auto mt-10 pb-3 p-5">
      <div className="py-3">
        <p className="mb-2">카테고리 선택</p>
        <form action="" value="카테고리">
          <select value={category} onChange={handleCategory} className="border border-gray3 rounded-lg h-8 w-24">
            <option value="FREE">자유</option>
            <option value="TIP">꿀Tip</option>
          </select>
        </form>
      </div>
      <div className="py-3">
        <div className="flex flex-col">
          <label className="mb-2 text-base text-black">제목</label>
          <input
            className="bg-white py-2 px-3 border-gray3 outline-none rounded-lg"
            onChange={handleTitle}
            value={title}
          />
        </div>
      </div>
      <div className="py-3">
        <div className="flex flex-col">
          <label className="mb-2 text-base text-black">내용</label>
          <textarea
            className="bg-white py-2 px-3 border border-gray3 outline-none resize-none h-64 rounded-lg"
            onChange={handleContent}
            value={content}
          ></textarea>
        </div>
      </div>
      <input
        type="file"
        accept="image/jpg,image/png,image/jpeg"
        name="post_image"
        onChange={handleImage}
      />
      <div className="py-3">
        <button onClick={editArticle} className="main1-button w-24">수정하기</button>
      </div>
    </div>
  );
}
