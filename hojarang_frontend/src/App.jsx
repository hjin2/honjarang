import './App.css';
import { BrowserRouter, Route, Routes } from 'react-router-dom';
import Login from './pages/login/login';
import Signup from './pages/signup/signup';

function App() {
  return (
    <BrowserRouter>
      <div className='App'>
        <Routes>
          <Route exact path="/" element={<Login />} />
          <Route path="/login" element={<Login />} />
          <Route path="/signup" element={<Signup />}/>
        </Routes>
      </div>
    </BrowserRouter>
  );
}

export default App;
