import React, { useState, useRef, useEffect } from 'react';
import { useNavigate } from 'react-router';



function ChatList({list}) {
  const navigate = useNavigate()
  const onClick = () => {
    navigate(`./${list.id}`)
  }
  
  return (
    <div  className="border border-solid border-black " onClick={onClick}>
      <img src="" alt="" />
      <span>{list.name}</span>
      <br />
      <span>{list.last_message}</span>
    </div>
  )
}

export default ChatList