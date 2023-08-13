import { BrowserRouter, Route, Routes } from 'react-router-dom';

import Chatting from '@/pages/Chatting/Chatting';
import Chat from '@/pages/Chatting/Chat';
import Login from '@/pages/login/login';
import Signup from '@/pages/signup/signup';
import Notfound from '@/pages/Notfound/Notfound';
import Market from '@/pages/Market/Market';
import WebRTC from '@/pages/WebRTC/WebRTC';
import Board from '@/pages/Board/Board';
import Map1 from '@/pages/Map/Map';
import Map2 from '@/pages/Map/Map2';
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


function App() {
  return (
    <div className="app">
      <BrowserRouter>
        <Routes>
          <Route element={<DefaultLayout />}>
            <Route exact path="/" element={<Login />} />
            <Route path="/login" element={<Login />} />
            <Route path="/signup" element={<Signup />} />
            <Route path="/webrtc" element={<WebRTC />} />
            <Route path="/webrtc/create" element={<WebRTCCreate/>}/>
            <Route path="/chatting" element={<Chatting />} />
            <Route path="/market" element={<Market />} />
            <Route path="/market/purchasedetail/:id" element={<PurchaseDetail />}></Route>
            <Route
              path="/market/deliverydetail/:id"
              element={<DeliveryDetail />}
            ></Route>
            <Route path="/market/purchase/create" element={<PurchaseCreate/>}></Route>
            <Route path="/market/delivery/create" element={<DeliveryCreate/>}></Route>
            <Route path="/market/transaction/create" element={<TransactionCreate/>}></Route>
            <Route path="/market/transactiondetail/:id" element={<TransactionDetail/>}></Route>
            <Route path="/board" element={<Board />} />
            <Route path="/map" element={<Map1 />} />
            <Route path="/chatting/:id" element={<Chat />} />
            <Route path="/mypage/:id" element={<MyPage />} />
            <Route path="/findpassword" element={<FindPassword />} />
            <Route
              path="/findpassword/changepassword"
              element={<ChangePassword />}
            ></Route>
            <Route
              path="/board/articlecreate"
              element={<ArticleCreate />}
            ></Route>
            <Route path="/board/article/:id" element={<ArticleDetail />} />
            <Route path="/board/articleupdate/:id" element={<ArticleUpdate/>}></Route>
            <Route path="/*" element={<Notfound />} />
          </Route>
          <Route path="/webrtc/:sessionid" element={<FreeChat/>}/>
          <Route path="/checkout" element={<Checkout />} />
          <Route path="/checkout/fail" element={<Fail />} />
          <Route path="/checkout/success" element={<Success />} />
          <Route path="/map2" element={<Map2 />} />
          <Route path="/push" element={<FirebaseMessaging />} />
        </Routes>
      </BrowserRouter>
    </div>
  );
}

export default App;
