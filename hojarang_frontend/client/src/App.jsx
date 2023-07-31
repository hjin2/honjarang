import { 
  BrowserRouter, 
  Route, 
  Routes 
} from 'react-router-dom';

import Chatting from './pages/Chatting/Chatting';
import Login from './pages/Login/Login';
import Signup from './pages/Signup/Signup';
import Notfound from './pages/Notfound/Notfound';
import Market from './pages/Market/Market';
import WebRTC from './pages/WebRTC/WebRTC';
import Board from './pages/Board/Board';
import Map from './pages/Map/Map';
import MyPage from './pages/MyPage/MyPage';
import FindPassword from './pages/FindPassword/FindPassword';
import ChangePassword from './pages/FindPassword/ChangePassword';
import ArticleCreate from './pages/Board/ArticleCreate';
import { ArticleDetail } from './pages/Board/ArticleDetail';
import Checkout from './pages/Checkout/Checkout';
import Fail from './pages/Checkout/Fail';
import Success from './pages/Checkout/Success';
import DefaultLayout from './components/DefaultLayout';
import DeliveryDetail from './components/Market/DeliveryDetail';


function App() {
  return (
    <div className='app'>
      <BrowserRouter>
        <Routes>
          <Route element={ <DefaultLayout/> }>
            <Route exact path="/" element={<Login />} />
            <Route path="/login" element={<Login />} />
            <Route path="/signup" element={<Signup />}/>
            <Route path="/webrtc" element={<WebRTC />}/>
            <Route path="/chatting" element={<Chatting />}/>
            <Route path="/market" element={<Market />}/>
            <Route path="/market/deliverydetail/:id" element={<DeliveryDetail/>}></Route>
            <Route path="/board" element={<Board />}/>
            <Route path="/map" element={<Map />}/>
            <Route path="/mypage/:nickname" element={<MyPage />}/>
            <Route path="/findpassword" element={<FindPassword/>}/>
            <Route path="/findpassword/changepassword" element={<ChangePassword/>}></Route>
            <Route path="/board/articlecreate" element={<ArticleCreate/>}></Route>
            <Route path="/board/article/:id" element={<ArticleDetail />} />
            <Route path="/*" element={<Notfound />}/>
          </Route>
          <Route path="/checkout" element={ <Checkout/> }/>
          <Route path="/checkout/fail" element={ <Fail/> }/>
          <Route path="/checkout/success" element={ <Success/> }/>
        </Routes>
      </BrowserRouter>
    </div>
  );
}

export default App;
