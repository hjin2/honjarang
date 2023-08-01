import { React, useState } from 'react';
import { useNavigate } from 'react-router-dom';
// import axios from 'axios';
import { useDispatch } from 'react-redux';
import TextField from '../../components/Board/TextFields';
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
    <div className="mt-10 max-w-xl mx-auto">
      카테고리 선택
      <form action="" value="말머리">
        <select value={category} onChange={newCate}>
          <option value="자유">자유</option>
          <option value="Tip">꿀Tip</option>
        </select>
      </form>
      <div>
        <TextField
          label="제목"
          value={title}
          onChange={(e) => setFormData({ ...formData, title: e.target.value })}
        />
      </div>
      <br />
      <div>
        <TextField
          label="내용"
          value={content}
          onChange={(e) => setFormData({ ...formData, content: e.target.value })}
        />
      </div>
      <br />
      <div>
        <Link to={`/board/`}>
          <button onClick={saveArticle}>저장</button>
        </Link>
      </div>
    </div>
  );
};

export default ArticleCreate;
