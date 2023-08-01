// import React from 'react';
import { useState } from 'react';

export const Dropdown = () => {
  const [cate, setCate] = useState('말머리 선택');
  const newCate = (e) => {
    setCate(e.target.value);
    console.log(e.target.value);
  };

  return (
    <div>
      <form action="" value="말머리">
        <select value={cate} onChange={newCate}>
          <option value="자유">자유</option>
          <option value="Tip">꿀Tip</option>
        </select>
      </form>
    </div>
  );
};
