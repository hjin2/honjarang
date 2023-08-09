import ChatList from "../../components/Chatting/ChatList"
import Chat from "../../components/Chatting/Chat"
import Chat2 from "../../components/Chatting/Chat2";
// import { firebaseApp } from "./firebase";
import { useState, useEffect } from "react";

export default function Chatting() {
  const [Lists, setLists] = useState([
    {id:1, title: 'test'}
  ])

  // useEffect(() => {
  //    fetch("")
  //      .then((res) => res.json())
  //      .then((data) => setLists(data));
  //  }, []);
 
//   const firebaseMessaging = firebaseApp.messaging();

// firebaseMessaging
//   .requestPermission()
//   .then(() => {
//     return firebaseMessaging.getToken(); //등록 토큰 받기
//   })
//   .then(function (token) {
//     console.log(token); //토큰 출력
//   })
//   .catch(function (error) {
//     console.log("FCM Error : ", error);
//   });

// firebaseMessaging.onMessage((payload) => {
//   console.log(payload.notification.title);
//   console.log(payload.notification.body);
// });


  return (
    <div className="flex">
    {Lists.map(list => (
        <ChatList list={list} key={list.id}/>
      ))}
      <Chat2 />
    </div>
  )
}
