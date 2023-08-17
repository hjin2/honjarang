import { Navigate } from 'react-router-dom';

export default function AuthRoute({ isLogged, component: Component }) {
  return isLogged ? (
    Component
  ) : (
    <Navigate to="/login" {...alert('로그인이 필요합니다.')}></Navigate>
  );
}
