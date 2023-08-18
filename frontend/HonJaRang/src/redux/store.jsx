import { configureStore } from '@reduxjs/toolkit';
import loginSlice from '@/redux/slice/loginSlice';
import articleSlice from '@/redux/slice/articleSlice';
import UserinfoSlice from '@/redux/slice/UserInfoSlice';

// 스토어 생성
const store = configureStore({
  reducer: {
    login: loginSlice,
    articles: articleSlice.reducer,
    userinfo: UserinfoSlice.reducer,
  },
  middleware: (getDefaultMiddleware) =>
    getDefaultMiddleware({ serializableCheck: false }),
  // 기본 값이 true지만 배포할때 코드를 숨기기 위해서 false로 변환하기 쉽게 설정에 넣어놨다.
  devTools: true,
});

// // useSelector 사용시 타입으로 사용하기 위함
// export type RootState = ReturnType<typeof store.getState>

// // useDispatch를 좀 더 명확하게 사용하기 위함
// export type AppDispatch = typeof store.dispatch

export default store;
