import { Outlet } from 'react-router';
import Header from './Header/Header';

export default function DefaultLayout() {
  return (
    <>
      <div className="w-5/6 mx-auto">
        <div style={{height : '10%'}}>
          <Header />
        </div>
        <div style={{height : "90%"}}>
          <Outlet />
        </div>
      </div>
    </>
  );
}
