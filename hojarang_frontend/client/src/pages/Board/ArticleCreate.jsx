import { React, useState } from 'react';
import { useNavigate } from 'react-router-dom';
// import axios from 'axios';
import { useDispatch } from 'react-redux';
import TextField from '../../components/Board/TextFields';
import TextArea from '../../components/Board/TextArea';
import { createArticle } from '../../redux/slice/articleSlice';
import { Link } from 'react-router-dom';
import { combineReducers } from '@reduxjs/toolkit';


const ArticleCreate = () => {
  const navigate = useNavigate();
  const dispatch = useDispatch();

  const [formData, setFormData] = useState({
    category: '자유',
    title: '',
    content: '',
  })

  const newCate =(e) => {
    setFormData({...formData, category:e.target.value});
  }

  const {category, title, content} = formData;  // 비구조화 할당

  const saveArticle = () => {
    const combinedData = {
      ...formData,
    };
    console.log(combinedData)

    // const { value, name } = event.target; //event.target에서 name과 value만 가져오기
    setFormData({...formData, title:'', content:''});
    dispatch(createArticle(formData));
    navigate('/board');
  };
  
  
  // const saveBoard = async () => {
  //   await axios.post(`//localhost:3000/board`, board).then((res) => {
  //     alert('등록되었습니다.');
  //     navigate('/board');
  //   });
  // };

  return (
    <div className="border rounded-lg max-w-2xl mx-auto mt-10 pb-3 p-5">
      <div className="py-3">
        <p className="mb-2">카테고리 선택</p>
        <form action="" value="카테고리">
          <select value={category} onChange={newCate} className="border border-gray3 rounded-lg h-8 w-24">
            <option value="자유">자유</option>
            <option value="Tip">꿀Tip</option>
          </select>
        </form>
      </div>
      <div className="py-3">
        <TextField
          label="제목"
          value={title}
          onChange={(e) => setFormData({ ...formData, title: e.target.value })}
        />
      </div>
      <div className="py-3">
        <TextArea
          label="내용"
          value={content}
          onChange={(e) => setFormData({ ...formData, content: e.target.value })}
        />
      </div>
      <br />
      <div className="py-3">
        <Link to={`/board/`}>
          <button onClick={saveArticle} className="main1-button w-24">작성 완료</button>
        </Link>
      </div>
    </div>
  );
};

export default ArticleCreate;
