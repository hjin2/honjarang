import { configureStore } from "@reduxjs/toolkit";
import loginSlice from "./slice/loginSlice";
import articleSlice from "./slice/articleSlice";

// 스토어 생성
const store = configureStore({
    reducer: {
        login:loginSlice.reducer,
        articles:articleSlice.reducer,
    }
})

// // useSelector 사용시 타입으로 사용하기 위함
// export type RootState = ReturnType<typeof store.getState>

// // useDispatch를 좀 더 명확하게 사용하기 위함
// export type AppDispatch = typeof store.dispatch

export default store;