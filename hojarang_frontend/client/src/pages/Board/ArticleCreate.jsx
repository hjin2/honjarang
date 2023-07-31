import { React, useState, Fragment } from 'react';
import { useNavigate } from 'react-router-dom';
// import axios from 'axios';
import { useDispatch } from 'react-redux';
import TextField from '../../components/Board/TextFields';
import { createArticle } from '../../redux/slice/articleSlice';
import { v4 as uuidv4 } from 'uuid';
import { Dropdown } from '../../components/Board/Dropdown';
import { Link } from 'react-router-dom';

const ArticleCreate = () => {
  const navigate = useNavigate();
  const dispatch = useDispatch();

  // const [cate, setCate] = useState("말머리 선택");
  // const newCate =(e) => {
  //   setCate(e.target.value)
  // }

  const [values, setValues] = useState({
    cate: '',
    category: '',
    title: '',
    content: '',
  });

  const { cate, category, title, content } = values; //비구조화 할당

  const saveArticle = () => {
    console.log(values);
    // const { value, name } = event.target; //event.target에서 name과 value만 가져오기
    setValues({ cate: '', category: '', title: '', content: '' });
    dispatch(
      createArticle({
        id: uuidv4(),
        ...values,
      }),
    );
    navigate('/board');
  };

  // const saveBoard = async () => {
  //   await axios.post(`//localhost:3000/board`, board).then((res) => {
  //     alert('등록되었습니다.');
  //     navigate('/board');
  //   });
  // };

  // const backToList = () => {
  //   navigate('/board');
  // };

  return (
    <div className="mt-10 max-w-xl mx-auto">
      말머리선택 <Dropdown />
      {/* <form action="" value="말머리">
        <select value={cate} onChange={newCate}>
          <option value="자유">자유</option>
          <option value="Tip">꿀Tip</option>
        </select>
      </form> */}
      <div>
        <TextField
          label="말머리"
          value={values.category}
          onChange={(e) => setValues({ ...values, category: e.target.value })}
        />
      </div>
      <br />
      <div>
        <TextField
          label="제목"
          value={values.title}
          onChange={(e) => setValues({ ...values, title: e.target.value })}
        />
      </div>
      <br />
      <div>
        <TextField
          label="내용"
          value={values.content}
          onChange={(e) => setValues({ ...values, content: e.target.value })}
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
