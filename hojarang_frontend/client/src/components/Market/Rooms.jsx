import React from 'react'
import Room from './Room'
import { useState } from 'react';
import { useSelector } from 'react-redux';

export default function Rooms() {

  const imgbox = useSelector((state) => state.upload.image);
  const [room, setRoom] = useState([
    {thumnail: <img src="{imgbox}" alt="" />, title: '상품이름', button:'참여하기' },
    {thumnail: <img src="{imgbox}" alt="" />, title: '제목', button:'입장하기' },
    {thumnail: <img src="{imgbox}" alt="" />, title: '상품이름', button:'참여하기' },
    {thumnail: <img src="{imgbox}" alt="" />, title: '상품이름', button:'참여하기' },
    {thumnail: <img src="{imgbox}" alt="" />, title: '상품이름', button:'참여하기' },
    {thumnail: <img src="{imgbox}" alt="" />, title: '상품이름', button:'참여하기' },
    {thumnail: <img src="{imgbox}" alt="" />, title: '상품이름', button:'참여하기' },
    {thumnail: <img src="{imgbox}" alt="" />, title: '상품이름', button:'참여하기' },
    {thumnail: <img src="{imgbox}" alt="" />, title: '상품이름', button:'참여하기' },
    {thumnail: <img src="{imgbox}" alt="" />, title: '상품이름', button:'참여하기' },
    {thumnail: <img src="{imgbox}" alt="" />, title: '상품이름', button:'참여하기' },
    {thumnail: <img src="{imgbox}" alt="" />, title: '상품이름', button:'참여하기' },
    {thumnail: <img src="{imgbox}" alt="" />, title: '상품이름', button:'참여하기' },
    {thumnail: <img src="{imgbox}" alt="" />, title: '상품이름', button:'참여하기' },
    {thumnail: <img src="{imgbox}" alt="" />, title: '상품이름', button:'참여하기' },
    {thumnail: <img src="{imgbox}" alt="" />, title: '상품이름', button:'참여하기' },
  ])

  return (
    <div className="grid md:grid-cols-5 md:grid-flow-3 grid-cols-2 gap-3">
      <div><Room /></div>
      <div><Room /></div>
      <div><Room /></div>
      <div><Room /></div>
      <div><Room /></div>
      <div><Room /></div>
      <div><Room /></div>
  
  
          



    </div>
  )
}
