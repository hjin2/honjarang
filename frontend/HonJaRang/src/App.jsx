import { BrowserRouter, Route, Routes } from 'react-router-dom';

import Chat from '@/pages/Chatting/Chat';
import Chatting from '@/pages/Chatting/Chatting';
import Login from '@/pages/login/login';
import Signup from '@/pages/signup/signup';
import Notfound from '@/pages/Notfound/Notfound';
import Market from '@/pages/Market/Market';
import WebRTC from '@/pages/WebRTC/WebRTC';
import Board from '@/pages/Board/Board';
import Map1 from '@/pages/Map/Map';
import MyPage from '@/pages/MyPage/MyPage';
import FindPassword from '@/pages/FindPassword/FindPassword';
import ChangePassword from '@/pages/FindPassword/ChangePassword';
import ArticleCreate from '@/pages/Board/ArticleCreate';
import ArticleDetail from '@/pages/Board/ArticleDetail';
import ArticleUpdate from '@/pages/Board/ArticleUpdate';
import Checkout from '@/pages/Checkout/Checkout';
import Fail from '@/pages/Checkout/Fail';
import Success from '@/pages/Checkout/Success';
import DefaultLayout from '@/components/DefaultLayout';
import PurchaseDetail from '@/pages/Market/PurchaseDetail';
import DeliveryDetail from '@/pages/Market/DeliveryDetail';
import WebRTCCreate from '@/pages/WebRTC/WebRTCCreate';
import PurchaseCreate from '@/pages/Market/PurchaseCreate';
import DeliveryCreate from '@/pages/Market/DeliveryCreate';
import TransactionCreate from '@/pages/Market/TransactionCreate';
import TransactionDetail from '@/pages/Market/TransactionDetail';
import FreeChat from '@/pages/WebRTC/FreeChat';
import FirebaseMessaging from '@/pages/PushTest/PushTest';
import Main from './pages/Main/Main';
import SetNewPassword from './pages/FindPassword/SetNewPassword';
import { useSelector } from 'react-redux';
import AuthRoute from '@/components/AuthRoute';
import Refresh from './pages/Token/Refresh';
import { useEffect } from 'react';

function App() {
  useEffect(() =>{
    const currentURL = window.location.href
    if (currentURL === 'https://i9d202.p.ssafy.io/') {
    // 원하는 도메인으로 리다이렉트합니다.
    window.location.href = 'https://honjarang.kro.kr/';
    }
  }, []);
  // if (process.env.NODE_ENV === "production") {
  //   console.log = function no_console() {};
  //   console.warn = function no_console() {};
  //   console.warn = function () {};
  // }
  const isLogged = useSelector((state) => state.login.isLogged)
  return (
    <div className="app">
      <BrowserRouter>
        <Refresh />
        <Routes>
          <Route element={<DefaultLayout />}>
            <Route path="/webrtc" element={<AuthRoute component={<WebRTC/>} isLogged={isLogged} />} />
            <Route path="/webrtc/create" element={<AuthRoute component={<WebRTCCreate/>} isLogged={isLogged}/>}/>
            <Route path="/chatting" element={<AuthRoute component={<Chatting/>} isLogged={isLogged} />} />
            <Route path="/market" element={<AuthRoute component={<Market/>} isLogged={isLogged} />} />
            <Route path="/market/purchasedetail/:id" element={<AuthRoute component={<PurchaseDetail/>} isLogged={isLogged} />}></Route>
            <Route
              path="/market/deliverydetail/:id"
              element={<AuthRoute component={<DeliveryDetail/>} isLogged={isLogged} />}
            ></Route>
            <Route path="/market/purchase/create" element={<AuthRoute component={<PurchaseCreate/>} isLogged={isLogged}/>}></Route>
            <Route path="/market/delivery/create" element={<AuthRoute component={<DeliveryCreate/>} isLogged={isLogged}/>}></Route>
            <Route path="/market/transaction/create" element={<AuthRoute component={<TransactionCreate/>} isLogged={isLogged}/>}></Route>
            <Route path="/market/transactiondetail/:id" element={<AuthRoute component={<TransactionDetail/>} isLogged={isLogged}/>}></Route>
            <Route path="/board" element={<AuthRoute component={<Board/>} isLogged={isLogged} />} />
            <Route path="/map" element={<AuthRoute component={<Map1/>} isLogged={isLogged} />} />
            <Route path="/mypage/:id" element={<AuthRoute component={<MyPage/>} isLogged={isLogged} />} />
            <Route
              path="/board/articlecreate"
              element={<AuthRoute component={<ArticleCreate/>} isLogged={isLogged} />}
            ></Route>
            <Route path="/board/article/:id" element={<AuthRoute component={<ArticleDetail/>} isLogged={isLogged} />} />
            <Route path="/board/articleupdate/:id" element={<AuthRoute component={<ArticleUpdate/>} isLogged={isLogged}/>}></Route>
            <Route path="/*" element={<Notfound />} />
            <Route path="/chatting/:id" element={<AuthRoute component={<Chat/>} isLogged={isLogged} />} />
          </Route>
          <Route path="/webrtc/:sessionid" element={<AuthRoute component={<FreeChat/>} isLogged={isLogged}/>}/>
          <Route path="/checkout/:price" element={<AuthRoute component={<Checkout/>} isLogged={isLogged} />} />
          <Route path="/checkout/fail" element={<AuthRoute component={<Fail/>} isLogged={isLogged} />} />
          <Route path="/checkout/success" element={<AuthRoute component={<Success/>} isLogged={isLogged} />} />
          <Route path="/push" element={<AuthRoute component={<FirebaseMessaging/>} isLogged={isLogged} />} />
          <Route exact path="/" element={<Main />} />
          <Route path="/login" element={<Login />} />
          <Route path="/findpassword" element={<FindPassword />} />
          <Route path="/signup" element={<Signup />} />
          <Route path="/setnewpassword" element={<SetNewPassword/>}></Route>
          <Route
            path="/findpassword/changepassword"
            element={<AuthRoute component={<ChangePassword/>} isLogged={isLogged} />}
          ></Route>
        </Routes>
      </BrowserRouter>
    </div>
  );
}

export default App;
