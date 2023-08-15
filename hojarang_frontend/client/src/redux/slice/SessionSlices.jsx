import { createSlice } from '@reduxjs/toolkit';

const initialState = {
    session : undefined
}


export const SessionSlice = createSlice({
  name: 'session',
  initialState,
  reducers: {
    deleteSession: (state, action) => {
        if(state.session){
            state.session = undefined
        }
    },
    updateSession: (state, action) => {
        console.log(2,action.payload)
        state.session = action.payload
    },
  },
});
export const { deleteSession, updateSession } = SessionSlice.actions;
export default SessionSlice;
