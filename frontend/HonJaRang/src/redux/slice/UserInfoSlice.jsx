import { createSlice } from '@reduxjs/toolkit';

const initialState = {
  nickname: '',
  email: '',
  profile_image: '',
  point: 0,
  address: '',
};

const UserinfoSlice = createSlice({
  name: 'Userinfo',
  initialState,
  reducers: {
    Userinfo: (state, action) => {
      const {
        nickname,
        email,
        profile_image,
        point,
        address,
        latitude,
        longitude,
      } = action.payload;
      state.nickname = nickname;
      state.email = email;
      state.profile_image = profile_image;
      state.point = point;
      state.address = address;
      state.latitude = latitude;
      state.longitude = longitude;
    },
    setUserInfo: (state, action) => {
      state.nickname = action.payload.nickname;
      state.address = action.payload.address;
      state.latitude = action.payload.latitude;
      state.longtitude = action.payload.longtitude;
    },
    charge: (state, action) => {
      state.point = state.point + action.payload;
    },
    refund: (state, action) => {
      state.point = state.point - action.payload;
    },
    imageUpload: (state, action) => {
      console.log(action.payload);
      const profile_image = action.payload;
      if (profile_image) {
        state.profile_image = profile_image;
      } else {
        state.profile_image =
          'https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_1280.png';
        console.log(1);
      }
    },
    setUserNickname: (state, action) => {
      state.nickname = action.payload;
      console.log(1);
    },
  },
});

export const {
  Userinfo,
  setUserInfo,
  charge,
  refund,
  imageUpload,
  setUserNickname,
} = UserinfoSlice.actions;
export default UserinfoSlice;
