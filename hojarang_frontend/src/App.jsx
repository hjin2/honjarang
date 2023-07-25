import { 
  BrowserRouter, 
  Route, 
  Routes 
} from 'react-router-dom';


import Login from './pages/Login/Login';
import Signup from './pages/Signup/Signup';
import Notfound from './pages/Notfound/Notfound';
import Header from './components/header';
import Market from './pages/Market/Market';
import WebRTC from './pages/WebRTC/WebRTC';
import Board from './pages/Board/Board';
import Chatting from './pages/Chatting/Chatting';
import Map from './pages/Map/Map';




function App() {
  return (
    <div className='App'>
      <BrowserRouter>
        <Header/>
        <Routes>
          <Route exact path="/" element={<Login />} />
          <Route path="/login" element={<Login />} />
          <Route path="/signup" element={<Signup />}/>
          <Route path="/webrtc" element={<WebRTC />}/>
          <Route path="/chatting" element={<Chatting />}/>
          <Route path="/market" element={<Market />}/>
          <Route path="/board" element={<Board />}/>
          <Route path="/map" element={<Map />}/>
          <Route path="/*" element={<Notfound />}/>
        </Routes>
      </BrowserRouter>
    </div>
  );
}

export default App;
