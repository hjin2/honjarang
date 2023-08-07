import { createSlice } from '@reduxjs/toolkit';

const initialState = {
  nickname : "",
  email : "'김현지@김현지.com'",
  profile_image : "",
  point : 0,
  address : "",
};

const UserinfoSlice = createSlice({
  name: 'Userinfo',
  initialState,
  reducers: {
    Userinfo: (state, action) => {
      const {nickname, email, profile_image, point, address} = action.payload;
      state.nickname = nickname;
      state.email = email;
      state.profile_image = profile_image;
      state.point = point;
      state.address = address;
    },
    setNickname: (state, action) => {
      state.nickname = action.payload
    },
    setAddress: (state, action) => {
      state.address = action.payload
    }
  },
});

export const { Userinfo, setNickname, setAddress } = UserinfoSlice.actions;
export default UserinfoSlice;
