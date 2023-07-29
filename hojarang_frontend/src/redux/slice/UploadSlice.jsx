import { createSlice } from "@reduxjs/toolkit";

const initialState = {
  image : "https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_1280.png",
}

const UploadSlice = createSlice({
  name : "imageUpload",
  initialState,
  reducers : {
    imageUpload : (state, action) =>{
      state.image = action.payload
      console.log(state.image)
    }
  }
})

export const { imageUpload } = UploadSlice.actions;
export default UploadSlice;