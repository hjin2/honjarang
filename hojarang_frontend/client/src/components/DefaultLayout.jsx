import { Outlet } from 'react-router';
import Header from './Header/Header';

export default function DefaultLayout() {
  return (
    <>
      <div className="w-5/6 mx-auto">
        <Header />
        <Outlet />
      </div>
    </>
  );
}
