import { useState, useRef } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import TextField from '@/components/Board/TextFields';
import TextArea from '@/components/Board/TextArea';
import { API } from '@/apis/config';

const ArticleCreate = () => {
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
    axios.post(`${API.POST}`,formData,{headers})
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
        <p className="mb-2 text-lg">카테고리 선택</p>
        <form action="" value="카테고리">
          <select value={category} onChange={handleCategory} 
          className="border border-gray2 focus:outline-main2 rounded-lg h-8 w-24">
            <option value="FREE">자유</option>
            <option value="TIP">꿀Tip</option>
          </select>
        </form>
      </div>
      <div className="py-3">
        <div className="text-lg mb-1">제목</div>
        <TextField
          handleTitle={handleTitle}
        />
      </div>
      <div className="py-3">
        <div className="text-lg mb-1">내용</div>
        <TextArea
          handleContent={handleContent}
        />
      </div>
      <input
        type="file"
        accept="image/jpg,image/png,image/jpeg"
        name="post_image"
        onChange={handleImage}
        className="border border-gray2 focus:outline-main2 h-8 p-1 w-60"
      />
      <div className="py-3 flex justify-between">
        <button onClick={saveArticle} className="main1-full-button w-32">작성 완료</button>
        <button onClick={()=>{navigate("/board")}} className="main5-full-button w-32">목록</button>
      </div>
    </div>
  );
};

export default ArticleCreate;
