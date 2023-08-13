import { useState, useRef } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import TextField from '@/components/Board/TextFields';
import TextArea from '@/components/Board/TextArea';
import { Link } from 'react-router-dom';


const ArticleCreate = () => {
  const URL = import.meta.env.VITE_APP_API
  const token = localStorage.getItem("access_token")
  const headers = {
    "Authorization" : `Bearer ${token}`,
    "Content-Type" : "multipart/form-data"
  }
  const navigate = useNavigate();
  const [category, setCategory] = useState("")
  const [title, setTitle] = useState("")
  const [content, setContent] = useState("")
  const fileInput = useRef(null)
  const [image, setImage] = useState()
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

  const saveArticle = () => {
    console.log(image, title, content, category)
    const formData = new FormData()
    formData.append("post_image", image)
    formData.append("title", title)
    formData.append("content", content)
    formData.append("category", category)
    axios.post(`${URL}/api/v1/posts`,formData,{headers})
      .then((res) => {
        console.log(res)
        navigate(`/board/article/${res.data}`,{replace:true});
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
        <TextField
          label="제목"
          handleTitle={handleTitle}
        />
      </div>
      <div className="py-3">
        <TextArea
          label="내용"
          handleContent={handleContent}
        />
      </div>
      <input
        type="file"
        accept="image/jpg,image/png,image/jpeg"
        name="post_image"
        onChange={handleImage}
      />
      <div className="py-3">
        <button onClick={saveArticle} className="main1-button w-24">작성 완료</button>
      </div>
    </div>
  );
};

export default ArticleCreate;
