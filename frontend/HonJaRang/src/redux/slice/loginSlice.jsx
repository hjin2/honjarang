import { createSlice } from '@reduxjs/toolkit';

// 리덕스에서 관리할 상태 정의
const initialState = {
  isLogged: true,
};

const loginSlice = createSlice({
  name: 'login',
  initialState,
  reducers: {
    setLoginStatus: (state, action) => {
      state.isLogged = action.payload;
    },
  },
});

// dispatch할 때 액션 전달 -> 어떻게 상태 변화시킬지 결정
export const { setLoginStatus } = loginSlice.actions;
// slice 내보냄
export default loginSlice.reducer;
