import { createSlice } from '@reduxjs/toolkit';

// 리덕스에서 관리할 상태 정의
const initialState = {
  isLogged: false,
  user_email: null,
  user_password: null,
};

const loginSlice = createSlice({
  name: 'login',
  initialState,
  reducers: {
    loginAccount: (state, action) => {
      state.isLogged = true;
      state.user_email = action.payload.user_email;
      state.user_id = action.payload.user_password;
    },
  },
});

// dispatch할 때 액션 전달 -> 어떻게 상태 변화시킬지 결정
export const { loginAccount } = loginSlice.actions;
// slice 내보냄
export default loginSlice;
