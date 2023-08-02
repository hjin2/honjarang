import { createSlice } from '@reduxjs/toolkit';

// 리덕스에서 관리할 상태 정의
const initialState = {
  isLogged: false,
  access_token: null,
  refresh_token: null,
};

const loginSlice = createSlice({
  name: 'login',
  initialState,
  reducers: {
    loginAccount: (state, action) => {
      state.isLogged = true;
      state.access_token = action.payload.access_token;
      state.refresh_token = action.payload.refresh_token;

    },
    logoutAccout: (state) => {
      state.isLogged = false;
      state.access_token = null;
      state.refresh_token = null;
    }
  },
});

// dispatch할 때 액션 전달 -> 어떻게 상태 변화시킬지 결정
export const { loginAccount } = loginSlice.actions;
// slice 내보냄
export default loginSlice;
