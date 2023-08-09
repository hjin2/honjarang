import { createSlice } from '@reduxjs/toolkit';

// 리덕스에서 관리할 상태 정의
const initialState = {
  session: undefined
};

const sessionSlice = createSlice({
  name: 'session',
  initialState,
  reducers: {
    handleSession: (state, action) => {
      state.session = action.payload;
      console.log(1,action.payload)
    }
  },
});

// dispatch할 때 액션 전달 -> 어떻게 상태 변화시킬지 결정
export const { handleSession } = sessionSlice.actions;
// slice 내보냄
export default sessionSlice;
